#!/usr/bin/env python3
"""
Custom, business-rule-based mutation operators for the SQE project.

CM1  Age-limit shift  (BR1: birth date must be within the last 50 years and not in the future)
CM2  Guard removal    (BR2: only OWNER_ADMIN may act, and only on a pet that belongs to the owner)

For each mutant: patch the source, run the team unit tests, record KILLED or SURVIVED,
then restore the original file. Run from the repo root:  python3 mutation/custom_mutants.py
"""
import subprocess, pathlib, datetime

ROOT = pathlib.Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/org/springframework/samples/petclinic"
VALIDATOR = SRC / "rest/validation/PetAgeValidator.java"
CONTROLLER = SRC / "rest/controller/v1/OwnerRestControllerV1.java"

MUTANTS = [
    # id,     operator,            file,       find,                                    replace,                                   description
    ("CM1-a", "Age-limit shift",   VALIDATOR,  "minusYears(50)",                        "minusYears(49)",                          "Upper age limit 50 -> 49 years"),
    ("CM1-b", "Age-limit shift",   VALIDATOR,  "minusYears(50)",                        "minusYears(51)",                          "Upper age limit 50 -> 51 years"),
    ("CM1-c", "Age-limit shift",   VALIDATOR,  "birthDate.isAfter(today)",              "birthDate.isAfter(today.plusDays(1))",    "Future-date limit shifted by one day"),
    ("CM2-a", "Guard removal",     CONTROLLER, '@PreAuthorize("hasRole(@roles.OWNER_ADMIN)")', "",                                 "Role check removed from every owner endpoint"),
    ("CM2-b", "Guard removal",     CONTROLLER, "Pet pet = owner.getPet(petId);",        "Pet pet = this.clinicService.findPetById(petId);", "Ownership check replaced by global pet lookup"),
]

TEST_CMD = ["./mvnw", "-q", "test",
            "-Dtest=org.springframework.samples.petclinic.team.**",
            "-Dsurefire.failIfNoSpecifiedTests=false", "-Djacoco.skip=true"]

def run_tests():
    r = subprocess.run(TEST_CMD, cwd=ROOT, capture_output=True, text=True)
    return r.returncode == 0

def main():
    results = []
    print("Sanity check: original code must pass ...", flush=True)
    if not run_tests():
        raise SystemExit("Tests fail on unmutated code. Fix that first.")
    for mid, op, path, find, repl, desc in MUTANTS:
        original = path.read_text()
        if find not in original:
            results.append((mid, op, desc, "NOT APPLIED (pattern not found)"))
            continue
        path.write_text(original.replace(find, repl))
        try:
            print(f"{mid}: {desc} ...", end=" ", flush=True)
            status = "SURVIVED" if run_tests() else "KILLED"
            print(status)
        finally:
            path.write_text(original)          # always restore
        results.append((mid, op, desc, status))

    out = ROOT / "docs/mutation/custom-mutants.md"
    out.parent.mkdir(parents=True, exist_ok=True)
    lines = [f"# Custom mutation operators ({datetime.date.today()})", "",
             "| ID | Operator | Mutation | Result |", "|---|---|---|---|"]
    lines += [f"| {m} | {o} | {d} | {s} |" for m, o, d, s in results]
    killed = sum(1 for r in results if r[3] == "KILLED")
    lines += ["", f"Killed {killed} of {len(results)}"]
    out.write_text("\n".join(lines) + "\n")
    print("\n" + "\n".join(lines))

if __name__ == "__main__":
    main()

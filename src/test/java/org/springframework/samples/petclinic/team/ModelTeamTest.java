package org.springframework.samples.petclinic.team;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Covers the remaining Owner / Pet / Visit model code used by W1 and W2:
 * accessors, sorting of pets and visits, and the lazy-init branch in Pet.getVisitsInternal.
 */
class ModelTeamTest {

    private static Pet pet(int id, String name) {
        Pet p = new Pet();
        p.setId(id);
        p.setName(name);
        return p;
    }

    private static Visit visit(LocalDate date, String desc) {
        Visit v = new Visit();
        v.setDate(date);
        v.setDescription(desc);
        return v;
    }

    // ---------------- Owner ----------------

    @Test
    @DisplayName("Owner accessors store and return values")
    void owner_accessors() {
        Owner o = new Owner();
        o.setFirstName("George");
        o.setLastName("Franklin");
        o.setAddress("110 W. Liberty St.");
        o.setCity("Madison");
        o.setTelephone("6085551023");

        assertThat(o.getFirstName()).isEqualTo("George");
        assertThat(o.getLastName()).isEqualTo("Franklin");
        assertThat(o.getAddress()).isEqualTo("110 W. Liberty St.");
        assertThat(o.getCity()).isEqualTo("Madison");
        assertThat(o.getTelephone()).isEqualTo("6085551023");
    }

    @Test
    @DisplayName("Owner.getPets returns pets sorted by name, case-insensitive, and read-only")
    void owner_getPets_sortedAndUnmodifiable() {
        Owner o = new Owner();
        o.setPets(List.of(pet(1, "max"), pet(2, "Basil"), pet(3, "Leo")));

        assertThat(o.getPets()).extracting(Pet::getName).containsExactly("Basil", "Leo", "max");
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> o.getPets().add(pet(4, "Tom")))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Owner.setPetsInternal replaces the pet set")
    void owner_setPetsInternal() {
        Owner o = new Owner();
        Set<Pet> pets = new HashSet<>(Set.of(pet(1, "Leo")));
        ReflectionTestUtils.invokeMethod(o, "setPetsInternal", pets);
        assertThat(o.getPets()).hasSize(1);
    }

    @Test
    @DisplayName("Owner.toString includes the last name")
    void owner_toString() {
        Owner o = new Owner();
        o.setLastName("Franklin");
        assertThat(o.toString()).contains("Franklin");
    }

    // ---------------- Pet ----------------

    @Test
    @DisplayName("Pet accessors store and return values")
    void pet_accessors() {
        Pet p = pet(1, "Leo");
        PetType cat = new PetType();
        cat.setName("cat");
        Owner o = new Owner();
        p.setBirthDate(LocalDate.of(2020, 1, 1));
        p.setType(cat);
        p.setOwner(o);

        assertThat(p.getBirthDate()).isEqualTo(LocalDate.of(2020, 1, 1));
        assertThat(p.getType()).isSameAs(cat);
        assertThat(p.getOwner()).isSameAs(o);
    }

    @Test
    @DisplayName("Pet.getVisits on a new pet: lazy-init branch creates an empty set")
    void pet_getVisits_lazyInit() {
        assertThat(new Pet().getVisits()).isEmpty();
    }

    @Test
    @DisplayName("Pet.addVisit links the visit and getVisits sorts newest first")
    void pet_addVisit_sortedNewestFirst() {
        Pet p = pet(1, "Leo");
        Visit old = visit(LocalDate.of(2024, 1, 1), "rabies shot");
        Visit recent = visit(LocalDate.of(2025, 6, 1), "checkup");
        p.addVisit(old);
        p.addVisit(recent);

        assertThat(p.getVisits()).containsExactly(recent, old);
        assertThat(old.getPet()).isSameAs(p);
    }

    @Test
    @DisplayName("Pet.setVisits and setVisitsInternal replace the visit set")
    void pet_setVisits() {
        Pet p = pet(1, "Leo");
        p.setVisits(List.of(visit(LocalDate.of(2024, 1, 1), "a")));
        assertThat(p.getVisits()).hasSize(1);

        ReflectionTestUtils.invokeMethod(p, "setVisitsInternal", new HashSet<Visit>());
        assertThat(p.getVisits()).isEmpty();
    }

    // ---------------- Visit ----------------

    @Test
    @DisplayName("Visit accessors store and return values")
    void visit_accessors() {
        Visit v = visit(LocalDate.of(2025, 1, 2), "neutered");
        Pet p = pet(1, "Leo");
        v.setPet(p);

        assertThat(v.getDate()).isEqualTo(LocalDate.of(2025, 1, 2));
        assertThat(v.getDescription()).isEqualTo("neutered");
        assertThat(v.getPet()).isSameAs(p);
    }
}

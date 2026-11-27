package org.springframework.samples.petclinic.team;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;

/**
 * Critical module: Owner.getPet(String name, boolean ignoreNew).
 * Decision under test:  if (!ignoreNew || !pet.isNew())
 *   A = !ignoreNew      B = !pet.isNew()   (pet.isNew() means id == null)
 *
 * MCC table (all 4 combinations of A and B):
 *   T1  A=T B=T  -> T   ignoreNew=false, saved pet   -> pet returned
 *   T2  A=T B=F  -> T   ignoreNew=false, new pet     -> pet returned
 *   T3  A=F B=T  -> T   ignoreNew=true,  saved pet   -> pet returned
 *   T4  A=F B=F  -> F   ignoreNew=true,  new pet     -> null (skipped)
 *
 * MC/DC pairs taken from the MCC table:
 *   A independently changes the outcome:  T2 vs T4  (B fixed = F)
 *   B independently changes the outcome:  T3 vs T4  (A fixed = F)
 *   Minimum MC/DC set = {T2, T3, T4}
 */
class OwnerGetPetTeamTest {

    private Owner owner;
    private Pet savedPet;
    private Pet newPet;

    @BeforeEach
    void setUp() {
        owner = new Owner();
        savedPet = new Pet();
        savedPet.setId(1);
        savedPet.setName("Leo");
        newPet = new Pet();
        newPet.setName("Tom");
    }

    @Test
    @DisplayName("MCC T1 (A=T, B=T): ignoreNew=false, saved pet is found")
    void t1_notIgnoringNew_savedPet_found() {
        owner.addPet(savedPet);
        assertThat(owner.getPet("Leo", false)).isSameAs(savedPet);
    }

    @Test
    @DisplayName("MCC T2 (A=T, B=F): ignoreNew=false, new pet is still found")
    void t2_notIgnoringNew_newPet_found() {
        owner.addPet(newPet);
        assertThat(owner.getPet("Tom", false)).isSameAs(newPet);
    }

    @Test
    @DisplayName("MCC T3 (A=F, B=T): ignoreNew=true, saved pet is found")
    void t3_ignoringNew_savedPet_found() {
        owner.addPet(savedPet);
        assertThat(owner.getPet("Leo", true)).isSameAs(savedPet);
    }

    @Test
    @DisplayName("MCC T4 (A=F, B=F): ignoreNew=true, new pet is skipped")
    void t4_ignoringNew_newPet_skipped() {
        owner.addPet(newPet);
        assertThat(owner.getPet("Tom", true)).isNull();
    }

    @Test
    @DisplayName("Name match is case-insensitive")
    void nameMatch_isCaseInsensitive() {
        owner.addPet(savedPet);
        assertThat(owner.getPet("lEO")).isSameAs(savedPet);
    }

    @Test
    @DisplayName("Name not found returns null (loop finishes without match)")
    void nameNotFound_returnsNull() {
        owner.addPet(savedPet);
        assertThat(owner.getPet("Max", false)).isNull();
    }

    @Test
    @DisplayName("getPet(id): pet belonging to owner is found")
    void getPetById_found() {
        owner.addPet(savedPet);
        assertThat(owner.getPet(1)).isSameAs(savedPet);
    }

    @Test
    @DisplayName("getPet(id): id of another owner's pet returns null")
    void getPetById_notOwned_returnsNull() {
        owner.addPet(savedPet);
        assertThat(owner.getPet(99)).isNull();
    }
}

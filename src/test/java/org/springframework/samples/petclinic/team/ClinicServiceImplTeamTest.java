package org.springframework.samples.petclinic.team;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.*;
import org.springframework.samples.petclinic.service.ClinicServiceImpl;

/**
 * Unit tests for the ClinicServiceImpl methods on the W1/W2 path (owners, pets, visits).
 * Repositories are mocked, so only service logic runs.
 */
class ClinicServiceImplTeamTest {

    private PetRepository petRepo;
    private OwnerRepository ownerRepo;
    private VisitRepository visitRepo;
    private PetTypeRepository petTypeRepo;
    private ClinicServiceImpl service;

    @BeforeEach
    void setUp() {
        petRepo = mock(PetRepository.class);
        ownerRepo = mock(OwnerRepository.class);
        visitRepo = mock(VisitRepository.class);
        petTypeRepo = mock(PetTypeRepository.class);
        service = new ClinicServiceImpl(petRepo, mock(VetRepository.class), ownerRepo, visitRepo,
            mock(SpecialtyRepository.class), petTypeRepo);
    }

    // ---- findEntityById: try / catch (2 exception types) ----

    @Test
    @DisplayName("findOwnerById: found -> owner returned")
    void findOwner_found() {
        Owner o = new Owner();
        when(ownerRepo.findById(1)).thenReturn(o);
        assertThat(service.findOwnerById(1)).isSameAs(o);
    }

    @Test
    @DisplayName("findOwnerById: ObjectRetrievalFailureException (JPA not found) -> null")
    void findOwner_objectRetrievalFailure_null() {
        when(ownerRepo.findById(99)).thenThrow(new ObjectRetrievalFailureException(Owner.class, 99));
        assertThat(service.findOwnerById(99)).isNull();
    }

    @Test
    @DisplayName("findPetById: EmptyResultDataAccessException (JDBC not found) -> null")
    void findPet_emptyResult_null() {
        when(petRepo.findById(99)).thenThrow(new EmptyResultDataAccessException(1));
        assertThat(service.findPetById(99)).isNull();
    }

    // ---- findOwners: if (lastName != null) ----

    @Test
    @DisplayName("findOwners with lastName -> search by last name")
    void findOwners_byLastName() {
        Pageable page = PageRequest.of(0, 10);
        Page<Owner> result = new PageImpl<>(List.of(new Owner()));
        when(ownerRepo.findByLastName("Davis", page)).thenReturn(result);
        assertThat(service.findOwners("Davis", page)).isSameAs(result);
        verify(ownerRepo, never()).findAll(page);
    }

    @Test
    @DisplayName("findOwners without lastName -> all owners")
    void findOwners_all() {
        Pageable page = PageRequest.of(0, 10);
        Page<Owner> result = new PageImpl<>(List.of());
        when(ownerRepo.findAll(page)).thenReturn(result);
        assertThat(service.findOwners(null, page)).isSameAs(result);
    }

    // ---- W1 / W2 writes ----

    @Test
    @DisplayName("savePet: pet type is re-loaded from the repository before saving")
    void savePet_reloadsType() {
        PetType incoming = new PetType();
        incoming.setId(1);
        PetType stored = new PetType();
        stored.setId(1);
        stored.setName("cat");
        when(petTypeRepo.findById(1)).thenReturn(stored);
        Pet pet = new Pet();
        pet.setType(incoming);

        service.savePet(pet);

        assertThat(pet.getType()).isSameAs(stored);
        verify(petRepo).save(pet);
    }

    @Test
    @DisplayName("saveVisit delegates to the visit repository")
    void saveVisit() {
        Visit v = new Visit();
        service.saveVisit(v);
        verify(visitRepo).save(v);
    }

    @Test
    @DisplayName("saveOwner / deleteOwner delegate to the owner repository")
    void saveAndDeleteOwner() {
        Owner o = new Owner();
        service.saveOwner(o);
        service.deleteOwner(o);
        verify(ownerRepo).save(o);
        verify(ownerRepo).delete(o);
    }

    @Test
    @DisplayName("findAllOwners / findOwnerByLastName delegate to the owner repository")
    void ownerQueries() {
        when(ownerRepo.findAll()).thenReturn(List.of());
        when(ownerRepo.findByLastName("Davis")).thenReturn(List.of());
        assertThat(service.findAllOwners()).isEmpty();
        assertThat(service.findOwnerByLastName("Davis")).isEmpty();
    }

    @Test
    @DisplayName("findVisitsByPetId delegates to the visit repository")
    void visitsByPet() {
        when(visitRepo.findByPetId(14)).thenReturn(List.of());
        assertThat(service.findVisitsByPetId(14)).isEmpty();
    }
}

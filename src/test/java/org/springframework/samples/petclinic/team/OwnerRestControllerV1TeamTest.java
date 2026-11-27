package org.springframework.samples.petclinic.team;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.OwnerMapper;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.mapper.VisitMapper;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.rest.controller.v1.OwnerRestControllerV1;
import org.springframework.samples.petclinic.rest.dto.OwnerDto;
import org.springframework.samples.petclinic.rest.dto.OwnerFieldsDto;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import org.springframework.samples.petclinic.rest.dto.PetFieldsDto;
import org.springframework.samples.petclinic.rest.dto.VisitDto;
import org.springframework.samples.petclinic.rest.dto.VisitFieldsDto;
import org.springframework.samples.petclinic.service.ClinicService;

/**
 * White-box unit tests for the W1/W2 business logic in OwnerRestControllerV1.
 * Plain Mockito (no Spring context), so only controller logic is exercised.
 */
class OwnerRestControllerV1TeamTest {

    private ClinicService clinicService;
    private OwnerMapper ownerMapper;
    private PetMapper petMapper;
    private VisitMapper visitMapper;
    private OwnerRestControllerV1 controller;

    private Owner owner;
    private Pet pet;

    @BeforeEach
    void setUp() {
        clinicService = mock(ClinicService.class);
        petMapper = mock(PetMapper.class);
        visitMapper = mock(VisitMapper.class);
        ownerMapper = mock(OwnerMapper.class);
        controller = new OwnerRestControllerV1(clinicService, ownerMapper, petMapper, visitMapper);

        owner = new Owner();
        owner.setId(1);
        pet = new Pet();
        pet.setId(14);
        pet.setName("Tom");
        PetType cat = new PetType();
        cat.setId(1);
        cat.setName("cat");
        pet.setType(cat);
    }

    // ---------------- W1: addPetToOwner ----------------

    @Test
    @DisplayName("addPetToOwner: unknown owner -> 404 and nothing saved")
    void addPet_ownerMissing_404() {
        when(clinicService.findOwnerById(99)).thenReturn(null);

        ResponseEntity<PetDto> res = controller.addPetToOwner(99, new PetFieldsDto());

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(clinicService, never()).savePet(any());
    }

    @Test
    @DisplayName("addPetToOwner: known owner -> 201, pet linked to owner, Location header set")
    void addPet_ownerExists_201() {
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        when(petMapper.toPet(any(PetFieldsDto.class))).thenReturn(pet);
        when(petMapper.toPetDto(pet)).thenReturn(new PetDto());

        ResponseEntity<PetDto> res = controller.addPetToOwner(1, new PetFieldsDto());

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getHeaders().getLocation()).hasToString("/api/pets/14");
        ArgumentCaptor<Pet> saved = ArgumentCaptor.forClass(Pet.class);
        verify(clinicService).savePet(saved.capture());
        assertThat(saved.getValue().getOwner()).isSameAs(owner);
        assertThat(saved.getValue().getType().getName()).isNull();
    }

    // ------- updateOwnersPet: nested if (owner != null) { if (pet != null) } -------
    // MCC for (O = owner found, P = pet found):
    //   U1 O=T P=T -> 204   U2 O=T P=F -> 404   U3 O=F P=(not evaluated) -> 404
    // MC/DC: O shown by U1 vs U3, P shown by U1 vs U2.

    @Test
    @DisplayName("updateOwnersPet U1 (O=T, P=T): 204 and pet updated")
    void updatePet_ownerAndPetFound_204() {
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        when(clinicService.findPetById(14)).thenReturn(pet);
        PetFieldsDto dto = new PetFieldsDto();
        dto.setName("Tommy");
        dto.setBirthDate(LocalDate.of(2023, 5, 1));

        ResponseEntity<Void> res = controller.updateOwnersPet(1, 14, dto);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(clinicService).savePet(pet);
        assertThat(pet.getName()).isEqualTo("Tommy");
        assertThat(pet.getBirthDate()).isEqualTo(LocalDate.of(2023, 5, 1));
    }

    @Test
    @DisplayName("updateOwnersPet U2 (O=T, P=F): 404 and nothing saved")
    void updatePet_petMissing_404() {
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        when(clinicService.findPetById(99)).thenReturn(null);

        ResponseEntity<Void> res = controller.updateOwnersPet(1, 99, new PetFieldsDto());

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(clinicService, never()).savePet(any());
    }

    @Test
    @DisplayName("updateOwnersPet U3 (O=F): 404 and pet lookup never happens")
    void updatePet_ownerMissing_404() {
        when(clinicService.findOwnerById(99)).thenReturn(null);

        ResponseEntity<Void> res = controller.updateOwnersPet(99, 14, new PetFieldsDto());

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(clinicService, never()).findPetById(anyInt());
        verify(clinicService, never()).savePet(any());
    }

    // ---------------- getOwnersPet ----------------

    @Test
    @DisplayName("getOwnersPet: owner owns the pet -> 200")
    void getPet_owned_200() {
        owner.addPet(pet);
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        when(petMapper.toPetDto(pet)).thenReturn(new PetDto());

        assertThat(controller.getOwnersPet(1, 14).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("getOwnersPet: pet not owned by this owner -> 404")
    void getPet_notOwned_404() {
        when(clinicService.findOwnerById(1)).thenReturn(owner);

        assertThat(controller.getOwnersPet(1, 14).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("getOwnersPet: unknown owner -> 404")
    void getPet_ownerMissing_404() {
        when(clinicService.findOwnerById(99)).thenReturn(null);

        assertThat(controller.getOwnersPet(99, 14).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // ---------------- W2: addVisitToOwner ----------------

    @Test
    @DisplayName("addVisitToOwner: 201, visit linked to the given pet id, Location header set")
    void addVisit_201() {
        Visit visit = new Visit();
        visit.setId(7);
        when(visitMapper.toVisit(any(VisitFieldsDto.class))).thenReturn(visit);
        when(visitMapper.toVisitDto(visit)).thenReturn(new VisitDto());

        ResponseEntity<VisitDto> res = controller.addVisitToOwner(1, 14, new VisitFieldsDto());

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getHeaders().getLocation()).hasToString("/api/visits/7");
        verify(clinicService).saveVisit(visit);
        assertThat(visit.getPet().getId()).isEqualTo(14);
    }

    // ---------------- Owner CRUD (rest of the class) ----------------

    @Test
    @DisplayName("listOwners: lastName given and owners found -> 200, search by last name used")
    void listOwners_byLastName_200() {
        when(clinicService.findOwnerByLastName("Davis")).thenReturn(List.of(owner));
        assertThat(controller.listOwners("Davis").getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(clinicService, never()).findAllOwners();
    }

    @Test
    @DisplayName("listOwners: no lastName -> all owners returned, 200")
    void listOwners_all_200() {
        when(clinicService.findAllOwners()).thenReturn(List.of(owner));
        assertThat(controller.listOwners(null).getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(clinicService, never()).findOwnerByLastName(any());
    }

    @Test
    @DisplayName("listOwners: nothing found -> 404")
    void listOwners_empty_404() {
        when(clinicService.findOwnerByLastName("Nobody")).thenReturn(List.of());
        assertThat(controller.listOwners("Nobody").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("getOwner: found -> 200")
    void getOwner_found_200() {
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        when(ownerMapper.toOwnerDto(owner)).thenReturn(new OwnerDto());
        assertThat(controller.getOwner(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("getOwner: unknown -> 404")
    void getOwner_missing_404() {
        assertThat(controller.getOwner(99).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("addOwner: 201 with Location /api/owners/{id}")
    void addOwner_201() {
        when(ownerMapper.toOwner(any(OwnerFieldsDto.class))).thenReturn(owner);
        ResponseEntity<OwnerDto> res = controller.addOwner(new OwnerFieldsDto());
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getHeaders().getLocation()).hasToString("/api/owners/1");
        verify(clinicService).saveOwner(owner);
    }

    @Test
    @DisplayName("updateOwner: found -> 204 and every field copied")
    void updateOwner_found_204() {
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        OwnerFieldsDto dto = new OwnerFieldsDto();
        dto.setFirstName("Eiman");
        dto.setLastName("Wasim");
        dto.setAddress("1 Main St");
        dto.setCity("Islamabad");
        dto.setTelephone("0512345678");

        assertThat(controller.updateOwner(1, dto).getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(clinicService).saveOwner(owner);
        assertThat(owner.getFirstName()).isEqualTo("Eiman");
        assertThat(owner.getLastName()).isEqualTo("Wasim");
        assertThat(owner.getAddress()).isEqualTo("1 Main St");
        assertThat(owner.getCity()).isEqualTo("Islamabad");
        assertThat(owner.getTelephone()).isEqualTo("0512345678");
    }

    @Test
    @DisplayName("updateOwner: unknown -> 404 and nothing saved")
    void updateOwner_missing_404() {
        assertThat(controller.updateOwner(99, new OwnerFieldsDto()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(clinicService, never()).saveOwner(any());
    }

    @Test
    @DisplayName("deleteOwner: found -> 204 and deleted")
    void deleteOwner_found_204() {
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        assertThat(controller.deleteOwner(1).getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(clinicService).deleteOwner(owner);
    }

    @Test
    @DisplayName("deleteOwner: unknown -> 404 and nothing deleted")
    void deleteOwner_missing_404() {
        assertThat(controller.deleteOwner(99).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(clinicService, never()).deleteOwner(any());
    }
}

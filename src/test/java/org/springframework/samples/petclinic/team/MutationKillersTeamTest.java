package org.springframework.samples.petclinic.team;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.OwnerMapper;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.mapper.VisitMapper;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.rest.controller.v1.OwnerRestControllerV1;
import org.springframework.samples.petclinic.rest.dto.*;
import org.springframework.samples.petclinic.service.ClinicService;

/**
 * Tests added after PIT (ALL mutators) to kill surviving mutants.
 * Each test names the survivor numbers (S1..S25) it kills, see docs/mutation-analysis.
 */
class MutationKillersTeamTest {

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
        ownerMapper = mock(OwnerMapper.class);
        petMapper = mock(PetMapper.class);
        visitMapper = mock(VisitMapper.class);
        controller = new OwnerRestControllerV1(clinicService, ownerMapper, petMapper, visitMapper);
        owner = new Owner();
        owner.setId(1);
        pet = new Pet();
        pet.setId(14);
        pet.setName("Tom");
        PetType cat = new PetType();
        cat.setId(1);
        pet.setType(cat);
    }

    // ---------- Weak assertions: response body was never checked (S1, S2, S4-S8) ----------

    @Test
    @DisplayName("S1: addOwner returns the mapped DTO in the body")
    void s1_addOwner_body() {
        OwnerDto dto = new OwnerDto();
        when(ownerMapper.toOwner(any(OwnerFieldsDto.class))).thenReturn(owner);
        when(ownerMapper.toOwnerDto(owner)).thenReturn(dto);
        assertThat(controller.addOwner(new OwnerFieldsDto()).getBody()).isSameAs(dto);
    }

    @Test
    @DisplayName("S2: addPetToOwner returns the mapped DTO in the body")
    void s2_addPet_body() {
        PetDto dto = new PetDto();
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        when(petMapper.toPet(any(PetFieldsDto.class))).thenReturn(pet);
        when(petMapper.toPetDto(pet)).thenReturn(dto);
        assertThat(controller.addPetToOwner(1, new PetFieldsDto()).getBody()).isSameAs(dto);
    }

    @Test
    @DisplayName("S4: addVisitToOwner returns the mapped DTO in the body")
    void s4_addVisit_body() {
        Visit visit = new Visit();
        visit.setId(7);
        VisitDto dto = new VisitDto();
        when(visitMapper.toVisit(any(VisitFieldsDto.class))).thenReturn(visit);
        when(visitMapper.toVisitDto(visit)).thenReturn(dto);
        assertThat(controller.addVisitToOwner(1, 14, new VisitFieldsDto()).getBody()).isSameAs(dto);
    }

    @Test
    @DisplayName("S5: getOwner returns the mapped DTO in the body")
    void s5_getOwner_body() {
        OwnerDto dto = new OwnerDto();
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        when(ownerMapper.toOwnerDto(owner)).thenReturn(dto);
        assertThat(controller.getOwner(1).getBody()).isSameAs(dto);
    }

    @Test
    @DisplayName("S6: getOwnersPet returns the mapped DTO in the body")
    void s6_getOwnersPet_body() {
        PetDto dto = new PetDto();
        owner.addPet(pet);
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        when(petMapper.toPetDto(pet)).thenReturn(dto);
        assertThat(controller.getOwnersPet(1, 14).getBody()).isSameAs(dto);
    }

    @Test
    @DisplayName("S7: listOwners returns the mapped DTO list in the body")
    void s7_listOwners_body() {
        List<OwnerDto> dtos = new ArrayList<>(List.of(new OwnerDto()));
        when(clinicService.findAllOwners()).thenReturn(List.of(owner));
        when(ownerMapper.toOwnerDtoCollection(any())).thenReturn(dtos);
        ResponseEntity<List<OwnerDto>> res = controller.listOwners(null);
        assertThat(res.getBody()).isSameAs(dtos);
    }

    @Test
    @DisplayName("S8: updateOwner returns the mapped DTO in the body")
    void s8_updateOwner_body() {
        OwnerDto dto = new OwnerDto();
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        when(ownerMapper.toOwnerDto(owner)).thenReturn(dto);
        assertThat(controller.updateOwner(1, new OwnerFieldsDto()).getBody()).isSameAs(dto);
    }

    // ---------- Weak assertion: pet type update never checked (S9, S10, S11) ----------

    @Test
    @DisplayName("S9-S11: updateOwnersPet maps the incoming type DTO and stores it on the pet")
    void s9to11_updatePet_type() {
        PetTypeDto typeDto = new PetTypeDto();
        PetType dog = new PetType();
        dog.setId(2);
        dog.setName("dog");
        PetFieldsDto dto = new PetFieldsDto();
        dto.setName("Tom");
        dto.setBirthDate(LocalDate.of(2023, 1, 1));
        dto.setType(typeDto);
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        when(clinicService.findPetById(14)).thenReturn(pet);
        when(petMapper.toPetType(typeDto)).thenReturn(dog);   // exact argument, not any()

        controller.updateOwnersPet(1, 14, dto);

        assertThat(pet.getType()).isSameAs(dog);
    }

    // ---------- Missing tests on the model (S12, S13, S14, S24, S25) ----------

    @Test
    @DisplayName("S12: addPet links the pet back to its owner")
    void s12_addPet_setsOwner() {
        Owner o = new Owner();
        Pet p = new Pet();
        p.setName("Leo");
        o.addPet(p);
        assertThat(p.getOwner()).isSameAs(o);
    }

    @Test
    @DisplayName("S13: getPet(name) does NOT ignore new (unsaved) pets")
    void s13_getPetByName_includesNewPets() {
        Owner o = new Owner();
        Pet newPet = new Pet();          // id == null, so isNew() is true
        newPet.setName("Tom");
        o.addPet(newPet);
        assertThat(o.getPet("Tom")).isSameAs(newPet);
    }

    @Test
    @DisplayName("S14: getPets is sorted by name (8 pets, so a lucky HashSet order cannot pass)")
    void s14_getPets_sorted() {
        Owner o = new Owner();
        int id = 1;
        for (String n : List.of("Hugo", "bella", "Eve", "alfie", "Gus", "cleo", "Finn", "Dino")) {
            Pet p = new Pet();
            p.setId(id++);
            p.setName(n);
            o.addPet(p);
        }
        assertThat(o.getPets()).extracting(Pet::getName)
            .containsExactly("alfie", "bella", "cleo", "Dino", "Eve", "Finn", "Gus", "Hugo");
    }

    @Test
    @DisplayName("S24: getVisits is sorted newest first (8 visits)")
    void s24_getVisits_sorted() {
        Pet p = new Pet();
        int[] days = {5, 1, 8, 3, 7, 2, 6, 4};
        for (int d : days) {
            Visit v = new Visit();
            v.setDate(LocalDate.of(2025, 1, d));
            p.addVisit(v);
        }
        assertThat(p.getVisits()).extracting(Visit::getDate).isSortedAccordingTo((a, b) -> b.compareTo(a));
    }

    @Test
    @DisplayName("S25: getVisits returns a read-only list")
    void s25_getVisits_unmodifiable() {
        Pet p = new Pet();
        assertThatThrownBy(() -> p.getVisits().add(new Visit()))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    // ---------- Weak assertion on toString (S15-S23) ----------

    @Test
    @DisplayName("S15, S17-S23: toString includes every field of a saved owner")
    void s15to23_toString_savedOwner() {
        Owner o = new Owner();
        o.setId(7);
        o.setFirstName("George");
        o.setLastName("Franklin");
        o.setAddress("110 W. Liberty St.");
        o.setCity("Madison");
        o.setTelephone("6085551023");
        assertThat(o.toString())
            .contains("id = 7", "new = false", "lastName = 'Franklin'", "firstName = 'George'",
                "address = '110 W. Liberty St.'", "city = 'Madison'", "telephone = '6085551023'");
    }

    @Test
    @DisplayName("S16: toString reports new = true for an unsaved owner")
    void s16_toString_newOwner() {
        assertThat(new Owner().toString()).contains("new = true");
    }
}

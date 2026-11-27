package org.springframework.samples.petclinic.team;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.rest.advice.ExceptionControllerAdvice;
import org.springframework.samples.petclinic.rest.controller.v1.OwnerRestControllerV1;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.clinicService.ApplicationTestConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * BR2 role rule: only OWNER_ADMIN may add a pet (W1).
 * Runs the real Spring proxy so @PreAuthorize is enforced. Kills custom mutant CM2-a.
 */
@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
class OwnerSecurityTeamTest {

    private static final String PET_JSON =
        "{\"name\":\"Tom\",\"birthDate\":\"2024-01-01\",\"type\":{\"id\":1,\"name\":\"cat\"}}";

    @Autowired
    private OwnerRestControllerV1 controller;   // Spring proxy, method security active

    @MockitoBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new ExceptionControllerAdvice())
            .build();
        Owner owner = new Owner();
        owner.setId(1);
        when(clinicService.findOwnerById(1)).thenReturn(owner);
        PetType cat = new PetType();
        cat.setId(1);
        cat.setName("cat");
        when(clinicService.findPetTypeById(1)).thenReturn(cat);
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    @DisplayName("Allowed: OWNER_ADMIN can add a pet (201) and it is saved")
    void ownerAdmin_canAddPet() throws Exception {
        mockMvc.perform(post("/api/owners/1/pets").contentType(MediaType.APPLICATION_JSON).content(PET_JSON))
            .andExpect(status().isCreated());
        verify(clinicService).savePet(any());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    @DisplayName("Denied: VET_ADMIN cannot add a pet, nothing is saved")
    void vetAdmin_cannotAddPet() throws Exception {
        mockMvc.perform(post("/api/owners/1/pets").contentType(MediaType.APPLICATION_JSON).content(PET_JSON))
            .andExpect(r -> assertThat(r.getResponse().getStatus()).isGreaterThanOrEqualTo(400));
        verify(clinicService, never()).savePet(any());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    @DisplayName("Denied: VET_ADMIN cannot read an owner, service never called")
    void vetAdmin_cannotReadOwner() throws Exception {
        mockMvc.perform(get("/api/owners/1"))
            .andExpect(r -> assertThat(r.getResponse().getStatus()).isGreaterThanOrEqualTo(400));
        verify(clinicService, never()).findOwnerById(anyInt());
    }
}

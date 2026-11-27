package org.springframework.samples.petclinic.team;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.rest.validation.PetAgeValidator;

/**
 * White-box unit tests for PetAgeValidator.isValid (business rule BR1: pet birth date boundary).
 * One test per basis path from the CFG (V(G) = 4), plus 3-point BVA at both limits.
 */
class PetAgeValidatorTeamTest {

    private PetAgeValidator validator;
    private ConstraintValidatorContext context;
    private ConstraintViolationBuilder builder;
    private final LocalDate today = LocalDate.now();

    @BeforeEach
    void setUp() {
        validator = new PetAgeValidator();
        context = mock(ConstraintValidatorContext.class);
        builder = mock(ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    }

    // ---------- Basis paths (CFG nodes) ----------

    @Test
    @DisplayName("P1 (1-2-8): null birth date is accepted, no violation built")
    void p1_nullBirthDate_isValid() {
        assertThat(validator.isValid(null, context)).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    @DisplayName("P2 (1-3-4-8): future birth date is rejected with 'future' message")
    void p2_futureBirthDate_isRejected() {
        assertThat(validator.isValid(today.plusDays(1), context)).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Birth date cannot be in the future");
        verify(builder).addConstraintViolation();
    }

    @Test
    @DisplayName("P3 (1-3-5-6-8): birth date older than 50 years is rejected")
    void p3_tooOldBirthDate_isRejected() {
        assertThat(validator.isValid(today.minusYears(50).minusDays(1), context)).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Birth date cannot be older than 50 years");
        verify(builder).addConstraintViolation();
    }

    @Test
    @DisplayName("P4 (1-3-5-7-8): normal birth date is accepted, no violation built")
    void p4_validBirthDate_isValid() {
        assertThat(validator.isValid(today.minusYears(2), context)).isTrue();
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }

    // ---------- 3-point BVA: upper limit (today) ----------

    @Test
    @DisplayName("BVA upper: today (on the limit) is valid")
    void bva_today_isValid() {
        assertThat(validator.isValid(today, context)).isTrue();
    }

    @Test
    @DisplayName("BVA upper: yesterday (just inside) is valid")
    void bva_yesterday_isValid() {
        assertThat(validator.isValid(today.minusDays(1), context)).isTrue();
    }

    // tomorrow (just outside) is covered by P2

    // ---------- 3-point BVA: lower limit (today - 50 years) ----------

    @Test
    @DisplayName("BVA lower: exactly 50 years ago (on the limit) is valid")
    void bva_exactly50Years_isValid() {
        assertThat(validator.isValid(today.minusYears(50), context)).isTrue();
    }

    @Test
    @DisplayName("BVA lower: 50 years minus 1 day ago (just inside) is valid")
    void bva_justUnder50Years_isValid() {
        assertThat(validator.isValid(today.minusYears(50).plusDays(1), context)).isTrue();
    }

    // 50 years + 1 day (just outside) is covered by P3
}

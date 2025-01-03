package com.store.validation.validators

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class NotBooleanValidatorTest {

    private val validator = NotBooleanValidator()

    @Test
    fun `when value is null should return true`() {
        assertTrue(validator.isValid(null, null))
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `when value is boolean string should return false`(value: String) {
        assertFalse(validator.isValid(value, null))
    }

    @ParameterizedTest
    @ValueSource(strings = ["test", "123", "hello", "not-a-boolean"])
    fun `when value is non-boolean string should return true`(value: String) {
        assertTrue(validator.isValid(value, null))
    }
}
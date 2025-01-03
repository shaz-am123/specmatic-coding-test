package com.store.validation.validators

import com.store.validation.annotations.NotBoolean
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class NotBooleanValidator : ConstraintValidator<NotBoolean, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return value != "true" && value != "false"
    }
}
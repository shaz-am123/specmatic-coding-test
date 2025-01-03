package com.store.validation.annotations

import com.store.validation.validators.NotBooleanValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NotBooleanValidator::class])
annotation class NotBoolean(
    val message: String = "Value cannot be boolean",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
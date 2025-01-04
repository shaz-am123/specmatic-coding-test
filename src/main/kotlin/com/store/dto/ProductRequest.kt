package com.store.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.store.validation.annotations.NotBoolean
import jakarta.validation.constraints.*
import java.math.BigDecimal

data class ProductRequest(
    @field:NotBlank(message = "Name must not be blank.")
    @field:NotBoolean(message = "Name cannot be a boolean value")
    @field:Pattern(
        regexp = "^[A-Za-z '-]+$",
        message = "Name must only contain alphabets (A-Z, a-z), spaces, hyphens, or apostrophes."
    )
    @JsonProperty("name")
    val name: String,

    @field:NotBlank(message = "Type must not be blank.")
    @field:Pattern(
        regexp = "^(gadget|food|book|other)$",
        message = "Type must be one of the following: gadget, food, book, other."
    )
    @JsonProperty("type")
    val type: String,

    @field:Min(value = 1, message = "Inventory must be at least 1.")
    @field:Max(value = 9999, message = "Inventory must not exceed 9999.")
    @JsonProperty("inventory")
    val inventory: Int,

    @field:DecimalMin(value = "0", message = "Cost of product can not be less than 0")
    @field:Digits(
        integer = Int.MAX_VALUE,
        fraction = 2,
        message = "Cost of product can not have more than 2 decimal digits"
    )
    @JsonProperty("cost")
    val cost: BigDecimal
)

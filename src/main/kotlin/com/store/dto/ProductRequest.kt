package com.store.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductRequest(
    @JsonProperty("name")
    val name: String,

    @JsonProperty("type")
    val type: String,

    @JsonProperty("inventory")
    val inventory: Int
) {
    init {
        validateName()
        validateType()
        validateInventory()
    }

    private fun validateName() {
        require(name.isNotBlank()) { "Name must not be blank." }
        require(name != "true" && name != "false") { "Name cannot be a boolean value." }
        require(NAME_REGEX.matches(name)) { "Name must only contain alphabets (A-Z, a-z), spaces, hyphens, or apostrophes." }
    }

    private fun validateType() {
        require(type.isNotBlank()) { "Type must not be blank." }
        val validTypes = setOf("gadget", "food", "book", "other")
        require(validTypes.contains(type)) { "Type must be one of the following: gadget, food, book, other." }
    }

    private fun validateInventory() {
        require(inventory in 1..9999) { "Inventory must be between 1 and 9999." }
    }

    companion object {
        private val NAME_REGEX = "^[A-Za-z '-]+$".toRegex()
    }
}
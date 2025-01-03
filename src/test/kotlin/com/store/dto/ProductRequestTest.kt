package com.store.dto

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProductRequestTest {

    @Test
    fun `should create ProductRequest with valid inputs`() {
        val productRequest = ProductRequest(name = "Valid Name", type = "gadget", inventory = 100)
        assertNotNull(productRequest)
    }

    @Test
    fun `should throw IllegalArgumentException when name is blank`() {
        val exception = assertThrows<IllegalArgumentException> {
            ProductRequest(name = "  ", type = "gadget", inventory = 100)
        }
        assertEquals("Name must not be blank.", exception.message)
    }

    @Test
    fun `should throw IllegalArgumentException when name is a boolean`() {
        val exception = assertThrows<IllegalArgumentException> {
            ProductRequest(name = "true", type = "gadget", inventory = 100)
        }
        assertEquals("Name cannot be a boolean value.", exception.message)
    }

    @Test
    fun `should throw IllegalArgumentException when name contains invalid characters`() {
        val exception = assertThrows<IllegalArgumentException> {
            ProductRequest(name = "Invalid@Name", type = "gadget", inventory = 100)
        }
        assertEquals("Name must only contain alphabets (A-Z, a-z), spaces, hyphens, or apostrophes.", exception.message)
    }

    @Test
    fun `should throw IllegalArgumentException when type is blank`() {
        val exception = assertThrows<IllegalArgumentException> {
            ProductRequest(name = "Valid Name", type = "  ", inventory = 100)
        }
        assertEquals("Type must not be blank.", exception.message)
    }

    @Test
    fun `should throw IllegalArgumentException when type is invalid`() {
        val exception = assertThrows<IllegalArgumentException> {
            ProductRequest(name = "Valid Name", type = "invalidType", inventory = 100)
        }
        assertEquals("Type must be one of the following: gadget, food, book, other.", exception.message)
    }

    @Test
    fun `should throw IllegalArgumentException when inventory is below range`() {
        val exception = assertThrows<IllegalArgumentException> {
            ProductRequest(name = "Valid Name", type = "gadget", inventory = 0)
        }
        assertEquals("Inventory must be between 1 and 9999.", exception.message)
    }

    @Test
    fun `should throw IllegalArgumentException when inventory is above range`() {
        val exception = assertThrows<IllegalArgumentException> {
            ProductRequest(name = "Valid Name", type = "gadget", inventory = 10000)
        }
        assertEquals("Inventory must be between 1 and 9999.", exception.message)
    }
}

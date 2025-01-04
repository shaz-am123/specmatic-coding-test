package com.store.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.store.builders.ProductDefaultBuilder
import com.store.builders.ProductRequestDefaultBuilder
import com.store.domain.ProductId
import com.store.enums.ProductType
import com.store.services.ProductService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
class ProductControllerTest {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var productService: ProductService

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ProductController(productService)).build()
    }

    @Test
    fun `should return list of products when getProducts is called`() {
        val product1 = ProductDefaultBuilder().build()
        val product2 = ProductDefaultBuilder().id(2).name("bread").type(ProductType.food).build()

        `when`(productService.getProducts(null)).thenReturn(listOf(product1, product2))

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(listOf(product1, product2))))
    }

    @Test
    fun `should return list of products when getProducts is called with type`() {
        val product1 = ProductDefaultBuilder().build()

        `when`(productService.getProducts("gadget")).thenReturn(listOf(product1))

        mockMvc.perform(MockMvcRequestBuilders.get("/products?type=gadget"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(listOf(product1))))
    }

    @Test
    fun `should return created product ID when createProduct is called`() {
        val productRequest = ProductRequestDefaultBuilder().build()
        val createdProductId = ProductId(3)
        `when`(productService.createProduct(productRequest)).thenReturn(createdProductId)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/products")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(productRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(createdProductId)))
    }

    @Test
    fun `should return bad request when createProduct is called with invalid data (missing name)`() {
        val invalidProductRequest = """
        {
            "name": "",
            "type": "gadget",
            "inventory": 10000
        }
    """

        mockMvc.perform(
            MockMvcRequestBuilders.post("/products")
                .contentType("application/json")
                .content(invalidProductRequest)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return bad request when createProduct is called with invalid inventory `() {
        val invalidProductRequest = """
        {
            "name": "Iphone",
            "type": "gadget",
            "inventory": 10000
        }
    """

        mockMvc.perform(
            MockMvcRequestBuilders.post("/products")
                .contentType("application/json")
                .content(invalidProductRequest)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return bad request when createProduct is called with invalid type`() {
        val invalidProductRequest = """
    {
        "name": "iPhone",
        "type": "invalid_type",
        "inventory": 100
    }
    """

        mockMvc.perform(
            MockMvcRequestBuilders.post("/products")
                .contentType("application/json")
                .content(invalidProductRequest)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return bad request when createProduct is called with negative inventory`() {
        val invalidProductRequest = """
    {
        "name": "iPhone",
        "type": "gadget",
        "inventory": -1
    }
    """

        mockMvc.perform(
            MockMvcRequestBuilders.post("/products")
                .contentType("application/json")
                .content(invalidProductRequest)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return empty list when no products match the type filter`() {
        `when`(productService.getProducts("non_existent_type")).thenReturn(emptyList())

        mockMvc.perform(MockMvcRequestBuilders.get("/products?type=non_existent_type"))
            .andExpect(status().isOk)
            .andExpect(content().json("[]"))
    }

    @Test
    fun `should return bad request when createProduct is called with malformed JSON`() {
        val malformedJson = "{"

        mockMvc.perform(
            MockMvcRequestBuilders.post("/products")
                .contentType("application/json")
                .content(malformedJson)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should verify content type header is application-json for createProduct`() {
        val productRequest = ProductRequestDefaultBuilder().build()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/products")
                .content(objectMapper.writeValueAsString(productRequest))
        )
            .andExpect(status().isUnsupportedMediaType)
    }

    @Test
    fun `should return bad request when createProduct is called with empty request body`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/products")
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }
}

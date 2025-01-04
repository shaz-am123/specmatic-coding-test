package com.store.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.store.domain.Product
import com.store.domain.ProductId
import com.store.dto.ProductRequest
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
import java.math.BigDecimal

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
        val product1 = Product(ProductId(1), ProductRequest("Iphone", "gadget", 100, BigDecimal("1000")))
        val product2 = Product(ProductId(2), ProductRequest("Bread", "food", 200, BigDecimal("20")))

        `when`(productService.getProducts(null)).thenReturn(listOf(product1, product2))

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(listOf(product1, product2))))
    }

    @Test
    fun `should return list of products when getProducts is called with type`() {
        val product1 = Product(ProductId(1), ProductRequest("Iphone", "gadget", 100, BigDecimal("1000")))

        `when`(productService.getProducts("gadget")).thenReturn(listOf(product1))

        mockMvc.perform(MockMvcRequestBuilders.get("/products?type=gadget"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(listOf(product1))))
    }

    @Test
    fun `should return created product ID when createProduct is called`() {
        val productRequest = ProductRequest("Iphone", "gadget", 100, BigDecimal("1000"))
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
}

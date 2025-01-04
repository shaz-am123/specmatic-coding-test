package com.store.services

import com.store.builders.ProductDefaultBuilder
import com.store.builders.ProductRequestDefaultBuilder
import com.store.domain.Product
import com.store.domain.ProductId
import com.store.enums.ProductType
import com.store.repositories.ProductRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {

    @Mock
    private lateinit var productRepository: ProductRepository

    @InjectMocks
    private lateinit var productService: ProductService

    @BeforeEach
    fun setUp() {
        reset(productRepository)
    }

    @Test
    fun `createProduct should save product and return productId`() {
        val productRequest = ProductRequestDefaultBuilder().build()
        val productId = ProductId(1)
        val newProduct = Product(productId, productRequest)
        doNothing().`when`(productRepository).save(newProduct)

        val result = productService.createProduct(productRequest)

        assertThat(result).isEqualTo(ProductId(1))
        verify(productRepository).save(newProduct)
    }

    @Test
    fun `getProducts should return all products when no type is specified`() {
        val product1 = ProductDefaultBuilder().build()
        val product2 = ProductDefaultBuilder().id(2).name("bread").type(ProductType.food).build()
        `when`(productRepository.findAll()).thenReturn(listOf(product1, product2))

        val result = productService.getProducts(null)

        assertThat(result).hasSize(2)
        assertThat(result).containsExactlyInAnyOrder(product1, product2)
        verify(productRepository).findAll()
    }

    @Test
    fun `getProducts should return filtered products by type`() {
        val product1 = ProductDefaultBuilder().build()
        val product2 = ProductDefaultBuilder().id(2).name("Macbook").build()
        val product3 = ProductDefaultBuilder().id(3).name("bread").type(ProductType.food).build()
        `when`(productRepository.findByType(ProductType.gadget)).thenReturn(listOf(product1, product2))
        `when`(productRepository.findByType(ProductType.food)).thenReturn(listOf(product3))

        val result1 = productService.getProducts("food")
        val result2 = productService.getProducts("gadget")


        assertThat(result1).hasSize(1)
        assertThat(result1).containsExactlyInAnyOrder(product3)

        assertThat(result2).hasSize(2)
        assertThat(result2).containsExactlyInAnyOrder(product1, product2)

        verify(productRepository, times(1)).findByType(ProductType.gadget)
        verify(productRepository, times(1)).findByType(ProductType.food)

    }

    @Test
    fun `getProducts should return empty list when no products match the given type`() {
        `when`(productRepository.findByType(ProductType.food)).thenReturn(emptyList())

        val result = productService.getProducts("food")

        assertThat(result).isEmpty()
    }

    @Test
    fun `getProducts should throw an error if the type is invalid`() {
        assertThrows<IllegalArgumentException> { productService.getProducts("invalid") }
    }
}

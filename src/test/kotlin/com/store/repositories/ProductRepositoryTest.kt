package com.store.repositories

import com.store.builders.ProductDefaultBuilder
import com.store.domain.Product
import com.store.enums.ProductType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class ProductRepositoryTest {

    private lateinit var productRepository: ProductRepository

    @BeforeEach
    fun setUp() {
        productRepository = ProductRepository()
    }

    @Test
    fun `test save and findAll`() {
        val product1 = ProductDefaultBuilder().build()
        val product2 = ProductDefaultBuilder().id(2).name("Macbook").build()
        val product3 = ProductDefaultBuilder().id(3).name("Bread").type(ProductType.food).build()
        productRepository.save(product1)
        productRepository.save(product2)
        productRepository.save(product3)


        val products = productRepository.findAll()

        assertThat(products).hasSize(3)
        assertThat(products).containsExactlyInAnyOrder(product1, product2, product3)
    }

    @Test
    fun `test findByType returns products of the given type`() {
        val product1 = ProductDefaultBuilder().build()
        val product2 = ProductDefaultBuilder().id(2).name("Macbook").build()
        val product3 = ProductDefaultBuilder().id(3).name("Bread").type(ProductType.food).build()
        productRepository.save(product1)
        productRepository.save(product2)
        productRepository.save(product3)

        val products = productRepository.findByType(ProductType.gadget)

        assertThat(products).hasSize(2)
        assertThat(products).containsExactlyInAnyOrder(product1, product2)
    }

    @Test
    fun `test findByType returns empty list if no matching type found`() {
        val product1 = ProductDefaultBuilder().build()
        val product2 = ProductDefaultBuilder().id(2).name("Macbook").build()
        val product3 = ProductDefaultBuilder().id(3).name("Bread").type(ProductType.food).build()
        productRepository.save(product1)
        productRepository.save(product2)
        productRepository.save(product3)

        val products = productRepository.findByType(ProductType.book)

        assertThat(products).isEmpty()
    }

    @Test
    fun `test concurrent access to repository`() {
        val threads = (1..100).map { id ->
            Thread {
                productRepository.save(Product(id, "Product$id", ProductType.gadget, 150, BigDecimal("1000")))
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        val products = productRepository.findAll()

        assertThat(products).hasSize(100)
        assertEquals(products.map { it.id }.sorted(), (1..100).toList())
    }

    @Test
    fun `test save overwrites product with same ID`() {
        val product1 = ProductDefaultBuilder().build()
        val product2 = ProductDefaultBuilder().name("Iphone pro").build()

        productRepository.save(product1)
        productRepository.save(product2)

        val products = productRepository.findAll()

        assertThat(products).hasSize(1)
        assertThat(products[0]).isEqualTo(product2)
    }
}

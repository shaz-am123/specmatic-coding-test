package com.store.repositories

import com.store.domain.Product
import com.store.enums.ProductType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.assertEquals

class ProductRepositoryTest {

    private lateinit var productRepository: ProductRepository

    @BeforeEach
    fun setUp() {
        productRepository = ProductRepository()
    }

    @Test
    fun `test save and findAll`() {
        val product1 = Product(1, "Iphone", ProductType.gadget, 100)
        val product2 = Product(2, "Macbook", ProductType.gadget, 150)
        val product3 = Product(3, "Bread", ProductType.food, 150)
        productRepository.save(product1)
        productRepository.save(product2)
        productRepository.save(product3)


        val products = productRepository.findAll()

        assertThat(products).hasSize(3)
        assertThat(products).containsExactlyInAnyOrder(product1, product2, product3)
    }

    @Test
    fun `test findByType returns products of the given type`() {
        val product1 = Product(1, "Iphone", ProductType.gadget, 100)
        val product2 = Product(2, "Macbook", ProductType.gadget, 150)
        val product3 = Product(3, "Bread", ProductType.food, 150)
        productRepository.save(product1)
        productRepository.save(product2)
        productRepository.save(product3)

        val products = productRepository.findByType(ProductType.gadget)

        assertThat(products).hasSize(2)
        assertThat(products).containsExactlyInAnyOrder(product1, product2)
    }

    @Test
    fun `test findByType returns empty list if no matching type found`() {
        val product1 = Product(1, "Iphone", ProductType.gadget, 100)
        val product2 = Product(2, "Macbook", ProductType.gadget, 150)
        val product3 = Product(3, "Bread", ProductType.food, 150)
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
                productRepository.save(Product(id, "Product$id", ProductType.gadget, id * 10))
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
        val product1 = Product(1, "Iphone", ProductType.gadget, 100)
        val product2 = Product(1, "Iphone Pro", ProductType.gadget, 150)

        productRepository.save(product1)
        productRepository.save(product2)

        val products = productRepository.findAll()

        assertThat(products).hasSize(1)
        assertThat(products[0]).isEqualTo(product2)
    }
}

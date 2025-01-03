package com.store.services

import com.store.domain.Product
import com.store.domain.ProductId
import com.store.dto.ProductRequest
import com.store.enums.ProductType
import com.store.repositories.ProductRepository
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicInteger

@Service
class ProductService(private val productRepository: ProductRepository) {
    private val idCounter = AtomicInteger(0)

    fun createProduct(productRequest: ProductRequest): ProductId {
        val newProductId = idCounter.incrementAndGet()
        val newProduct = Product(ProductId(newProductId), productRequest)
        productRepository.save(newProduct)
        return ProductId(newProductId)
    }

    fun getProducts(type: String?): List<Product> {
        return if (type != null) {
            getProductsByType(type)
        } else {
            getAllProducts()
        }
    }

    private fun getAllProducts(): List<Product> = productRepository.findAll()
    private fun getProductsByType(type: String): List<Product> {
        val productType = ProductType.valueOf(type)
        return productRepository.findByType(productType)
    }
}

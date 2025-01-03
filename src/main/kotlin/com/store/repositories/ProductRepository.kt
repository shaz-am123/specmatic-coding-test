package com.store.repositories

import com.store.domain.Product
import com.store.enums.ProductType
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class ProductRepository {
    private val products = ConcurrentHashMap<Int, Product>()

    fun findAll(): List<Product> = products.values.toList()

    fun save(product: Product) {
        products[product.id] = product
    }

    fun findByType(type: ProductType): List<Product> {
        return products.values.filter { it.type == type }
    }
}


package com.store.builders

import com.store.domain.Product
import com.store.domain.ProductId
import com.store.dto.ProductRequest
import com.store.enums.ProductType
import java.math.BigDecimal

class ProductDefaultBuilder {
    private var id: ProductId = ProductId(1)
    private var name: String = "Iphone"
    private var type: ProductType = ProductType.gadget
    private var inventory: Int = 100
    private var cost: BigDecimal = BigDecimal("1000")

    fun id(id: Int) = apply { this.id = ProductId(id) }
    fun name(name: String) = apply { this.name = name }
    fun type(type: ProductType) = apply { this.type = type }

    fun build(): Product {
        val productRequest = ProductRequest(name, type.toString(), inventory, cost)
        return Product(id, productRequest)
    }
}
package com.store.builders

import com.store.dto.ProductRequest
import com.store.enums.ProductType
import java.math.BigDecimal

class ProductRequestDefaultBuilder {
    private var name: String = "Iphone"
    private var type: ProductType = ProductType.gadget
    private var inventory: Int = 100
    private var cost: BigDecimal = BigDecimal("1000")

    fun build(): ProductRequest {
        return ProductRequest(
            name = this.name,
            type = this.type,
            inventory = this.inventory,
            cost = this.cost
        )
    }
}
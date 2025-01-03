package com.store.domain

import com.store.dto.ProductRequest
import com.store.enums.ProductType

data class Product(
    val id: Int,
    val name: String,
    val type: ProductType,
    val inventory: Int,
    val cost: Int
) {
    constructor(productId: ProductId, productRequest: ProductRequest) : this(
        id = productId.id,
        name = productRequest.name,
        type = ProductType.valueOf(productRequest.type),
        inventory = productRequest.inventory,
        cost = productRequest.cost
    )
}

data class ProductId(
    val id: Int
)


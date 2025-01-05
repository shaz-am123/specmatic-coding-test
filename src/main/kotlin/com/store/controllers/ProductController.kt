package com.store.controllers

import com.store.domain.Product
import com.store.domain.ProductId
import com.store.dto.ProductRequest
import com.store.services.ProductService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
open class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getProducts(
        @RequestParam(required = false) type: String?
    ): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(productService.getProducts(type))
    }

    @PostMapping
    fun createProduct(
        @Valid @RequestBody productRequest: ProductRequest
    ): ResponseEntity<ProductId> {
        val productId = productService.createProduct(productRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(productId)
    }
}

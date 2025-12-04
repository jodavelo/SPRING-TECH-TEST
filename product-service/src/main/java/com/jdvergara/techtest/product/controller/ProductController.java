package com.jdvergara.techtest.product.controller;

import com.jdvergara.techtest.product.dto.ProductRequest;
import com.jdvergara.techtest.product.dto.ProductResponse;
import com.jdvergara.techtest.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Productos", description = "Operaciones sobre el catálogo de productos")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Crear un nuevo producto")
    @ApiResponse(
            responseCode = "201",
            description = "Producto creado correctamente",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
    )
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener un producto por ID")
    @ApiResponse(
            responseCode = "200",
            description = "Producto encontrado",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @Operation(summary = "Listar todos los productos")
    @ApiResponse(
            responseCode = "200",
            description = "Listado de productos",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
    )
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }
}

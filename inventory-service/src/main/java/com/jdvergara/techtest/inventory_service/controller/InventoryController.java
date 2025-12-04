package com.jdvergara.techtest.inventory_service.controller;

import com.jdvergara.techtest.inventory_service.dto.InventoryItemRequest;
import com.jdvergara.techtest.inventory_service.dto.InventoryItemResponse;
import com.jdvergara.techtest.inventory_service.dto.PurchaseRequest;
import com.jdvergara.techtest.inventory_service.dto.PurchaseResponse;
import com.jdvergara.techtest.inventory_service.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Inventario", description = "Gestión de stock y flujo de compras")
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @Operation(
            summary = "Crear o actualizar stock de un producto",
            description = "Permite registrar el stock inicial o actualizar la cantidad disponible"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Stock actualizado",
            content = @Content(schema = @Schema(implementation = InventoryItemResponse.class))
    )
    @PostMapping("/stock")
    public ResponseEntity<InventoryItemResponse> createOrUpdateStock(
            @Valid @RequestBody InventoryItemRequest request) {
        return ResponseEntity.ok(inventoryService.createOrUpdateStock(request));
    }

    @Operation(
            summary = "Consultar el stock disponible de un producto",
            description = "Devuelve la cantidad disponible y la última fecha de actualización"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Stock encontrado",
            content = @Content(schema = @Schema(implementation = InventoryItemResponse.class))
    )
    @GetMapping("/stock/{productId}")
    public ResponseEntity<InventoryItemResponse> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getStock(productId));
    }

    @Operation(
            summary = "Registrar una compra",
            description = "Valida stock, descuenta inventario, consulta información del producto en el Product Service y persiste la compra"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Compra realizada",
            content = @Content(schema = @Schema(implementation = PurchaseResponse.class))
    )
    @PostMapping("/purchases")
    public ResponseEntity<PurchaseResponse> createPurchase(
            @Valid @RequestBody PurchaseRequest request) {
        return ResponseEntity.ok(inventoryService.purchase(request));
    }
}

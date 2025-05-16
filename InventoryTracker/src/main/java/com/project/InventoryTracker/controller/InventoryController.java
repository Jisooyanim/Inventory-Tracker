package com.project.InventoryTracker.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.InventoryTracker.model.Inventory;
import com.project.InventoryTracker.service.InventoryService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("api/v1/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<Inventory> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    @GetMapping("{id}")
    public Inventory getInventoryById(@PathVariable Integer id) {
        return inventoryService.getInventoryById(id);
    }

    @PostMapping
    public void addInventory(@RequestBody Inventory inventory) {
        inventoryService.insertInventory(inventory);
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateInventory(@PathVariable Integer id, @RequestBody Inventory inventory) {
        boolean updated = inventoryService.updateInventory(id, inventory);

        if (updated) {
            return ResponseEntity.ok("Inventory updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inventory not found.");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteInventroy(@RequestBody Map<String, String> request) {
        String name = request.get("name");

        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Name must be provided in the request body.");
        }

        boolean deleted = inventoryService.deleteInventory(name);

        if (deleted) {
            return ResponseEntity.ok("Inventory '" + name + "' deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inventory '" + name + "' not found.");
        }
    }
}

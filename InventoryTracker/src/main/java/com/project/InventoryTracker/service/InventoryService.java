package com.project.InventoryTracker.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.InventoryTracker.model.Inventory;
import com.project.InventoryTracker.repository.InventoryRepository;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventoryById(Integer id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Inventory item not found."));
    }

    public void insertInventory(Inventory inventory) {
        inventoryRepository.saveAndFlush(inventory);
    }

    public boolean updateInventory(Integer id, Inventory updatedInventory) {
        Optional<Inventory> existing = inventoryRepository.findById(id);
        if(existing.isPresent()) {
            Inventory inventory = existing.get();
            inventory.setName(updatedInventory.getName());
            inventory.setDescription(updatedInventory.getDescription());
            inventory.setQuantity(updatedInventory.getQuantity());
            inventory.setCategory(updatedInventory.getCategory());
            inventoryRepository.saveAndFlush(inventory);
            
            return true;
        }

        return false;
    }

    public boolean deleteInventory(String name) {
        List<Inventory> inventories = inventoryRepository.findByName(name);
        if (!inventories.isEmpty()) {
            inventoryRepository.deleteAll(inventories);
            return true;
        }
        return false;
    }
}
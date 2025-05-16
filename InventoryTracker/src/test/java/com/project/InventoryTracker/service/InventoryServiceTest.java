package com.project.InventoryTracker.service;

import com.project.InventoryTracker.model.Inventory;
import com.project.InventoryTracker.repository.InventoryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    public void testInsertInventory() {
        Inventory inventory = new Inventory();
        inventory.setName("Test Item");
        inventory.setDescription("Test Description");
        inventory.setQuantity(10);
        inventory.setCategory("Test Category");

        inventoryService.insertInventory(inventory);
        verify(inventoryRepository, times(1)).saveAndFlush(any(Inventory.class));
    }
}
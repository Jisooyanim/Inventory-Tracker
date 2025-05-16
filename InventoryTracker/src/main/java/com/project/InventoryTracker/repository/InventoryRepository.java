package com.project.InventoryTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.InventoryTracker.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer>{
    List<Inventory> findByName(String name);
}
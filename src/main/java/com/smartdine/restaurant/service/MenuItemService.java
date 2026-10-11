package com.smartdine.restaurant.service;

import com.smartdine.restaurant.entity.MenuItem;
import com.smartdine.restaurant.repository.MenuItemRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    // Get all menu items
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    // Get one menu item
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id).orElse(null);
    }

    // Add menu item
    public MenuItem addMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    // Update menu item
    public MenuItem updateMenuItem(Long id, MenuItem updatedItem) {

        MenuItem existingItem =
                menuItemRepository.findById(id).orElse(null);

        if (existingItem == null) {
            return null;
        }

        existingItem.setName(updatedItem.getName());
        existingItem.setCategory(updatedItem.getCategory());
        existingItem.setDescription(updatedItem.getDescription());
        existingItem.setPrice(updatedItem.getPrice());
        existingItem.setImage(updatedItem.getImage());
        existingItem.setAvailable(updatedItem.isAvailable());

        return menuItemRepository.save(existingItem);
    }

    // Delete menu item
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }
}
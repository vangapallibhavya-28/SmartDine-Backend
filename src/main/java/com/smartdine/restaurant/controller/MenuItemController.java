package com.smartdine.restaurant.controller;

import com.smartdine.restaurant.entity.MenuItem;
import com.smartdine.restaurant.service.MenuItemService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    // GET - Get all menu items
    @GetMapping
    public List<MenuItem> getAllMenuItems() {
        return menuItemService.getAllMenuItems();
    }

    // GET - Get one menu item
    @GetMapping("/{id}")
    public MenuItem getMenuItem(@PathVariable Long id) {
        return menuItemService.getMenuItemById(id);
    }

    // POST - Add menu item
    @PostMapping
    public MenuItem addMenuItem(@RequestBody MenuItem menuItem) {
        return menuItemService.addMenuItem(menuItem);
    }

    // PUT - Update menu item
    @PutMapping("/{id}")
    public MenuItem updateMenuItem(
            @PathVariable Long id,
            @RequestBody MenuItem menuItem) {

        return menuItemService.updateMenuItem(id, menuItem);
    }

    // DELETE - Delete menu item
    @DeleteMapping("/{id}")
    public String deleteMenuItem(@PathVariable Long id) {

        menuItemService.deleteMenuItem(id);

        return "Menu item deleted successfully";
    }
}
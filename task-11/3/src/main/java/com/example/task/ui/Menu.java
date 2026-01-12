package com.example.task.ui;

import java.util.Map;

public class Menu {
    private String name;
    private Map<Integer, MenuItem> menuItems;

    public Menu(String name, Map<Integer, MenuItem> menuItems) {
        this.name = name;
        this.menuItems = menuItems;
    }

    public void displayMenu() {
        for (var entry : menuItems.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue().getTitle());
        }
        System.out.print("Select a menu item: ");
    }

    public boolean containsKey(Integer key) {
        return menuItems.containsKey(key);
    }

    public MenuItem getMenuItem(Integer key) {
        return menuItems.get(key);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Map<Integer, MenuItem> getMenuItems() { return menuItems; }
    public void setMenuItems(Map<Integer, MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
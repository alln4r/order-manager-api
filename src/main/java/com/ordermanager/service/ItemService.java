package com.ordermanager.service;

import com.ordermanager.model.Item;
import com.ordermanager.repository.ItemRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class ItemService {

    final private ItemRepository itemRepository;

    public ItemService() {
        // Injeção manual
        this.itemRepository = new ItemRepository();
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }


    public Item find(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + id));
    }

    public void save(Item item) {
        // Validações de negócio
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }

        itemRepository.save(item);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item with ID " + id + " not found")
                );

        itemRepository.delete(item);
    }
}

package com.ordermanager.service;

import com.ordermanager.model.Item;
import com.ordermanager.model.Order;
import com.ordermanager.model.StockMovement;
import com.ordermanager.repository.OrderRepository;
import com.ordermanager.repository.StockMovementRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private ItemService itemService;

    public StockMovementService() {
        this.stockMovementRepository = new StockMovementRepository();
        this.orderRepository = new OrderRepository();
        this.orderService = new OrderService();
        this.itemService =  new ItemService();
    }

    public List<StockMovement> findAll() {
        return stockMovementRepository.findAll();
    }

    public StockMovement findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }


        return stockMovementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StockMovement not found with ID: " + id));
    }

    public void create(Long itemId,int quantity) {

        Item item = itemService.find(itemId);

        StockMovement movement = new StockMovement();
        movement.setItem(item);
        movement.setCreationDate(LocalDateTime.now());
        movement.setQuantity(quantity);

        validateOrder(movement);
        stockMovementRepository.save(movement);

        // Tenta completar encomendas pendentes
        List<Order> pendingOrders = orderRepository.findByItemAndNotCompleted(movement.getItem());
        for (Order order : pendingOrders) {
            orderService.tryToFulfillOrder(order);
        }
    }

    private void validateOrder(StockMovement movement) {
        if (movement == null) {
            throw new IllegalArgumentException("movement cannot be null");
        }
        if (movement.getItem() == null) {
            throw new IllegalArgumentException("movement item cannot be null");
        }

        if (movement.getQuantity() <= 0) {
            throw new IllegalArgumentException("Order quantity must be positive");
        }
    }


    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        StockMovement movement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StockMovement with ID " + id + " not found"));

        if (movement.getQuantity() == 0) {
            throw new IllegalStateException("Cannot delete stock movement that has already been consumed.");
        }

        stockMovementRepository.delete(movement);
    }

}

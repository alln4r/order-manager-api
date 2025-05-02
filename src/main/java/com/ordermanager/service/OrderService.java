package com.ordermanager.service;

import com.ordermanager.model.Item;
import com.ordermanager.model.Order;
import com.ordermanager.model.StockMovement;
import com.ordermanager.model.User;
import com.ordermanager.repository.OrderRepository;
import com.ordermanager.repository.StockMovementRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository;
    private final StockMovementRepository stockMovementRepository;
    private final EmailService emailService;
    private ItemService itemService;
    private UserService userService;

    public OrderService() {
        this.orderRepository = new OrderRepository();
        this.stockMovementRepository = new StockMovementRepository();
        this.emailService = new EmailService();

        this.itemService =  new ItemService();
        this.userService =  new UserService();
    }


    public void save(Long userId, int quantity, Long itemId) {
        Item item = itemService.find(itemId);
        User user = userService.find(userId);

        Order order = new Order();
        order.setItem(item);
        order.setUser(user);
        order.setQuantity(quantity);
        order.setCreationDate(LocalDateTime.now());
        validateOrder(order);

        List<StockMovement> stockMovements = stockMovementRepository.findByItemId(itemId);
        int totalStock = stockMovements.stream().mapToInt(StockMovement::getQuantity).sum();

        if (totalStock < quantity) {
            // Sem stock suficiente → ordem pendente
            order.setCompleted(false);
            orderRepository.save(order);

            // Notifica o utilizador que ficou pendente
            emailService.sendOrderNotification(user.getEmail(), user.getName(), order.getId(), "Pendente");

            System.out.println("Ordem criada como pendente. Será completada automaticamente assim que houver stock.");
            throw new IllegalArgumentException("Ordem criada como pendente. Será completada automaticamente assim que houver stock do item: " + item.getName());


        }
        List<StockMovement> usedMovements = new ArrayList<>();
        // Subtrai do stock
        int remaining = quantity;
        for (StockMovement sm : stockMovements) {
            int available = sm.getQuantity();
            if (available <= 0) continue;

            if (available <= remaining) {
                remaining -= available;
                sm.setQuantity(0);
            } else {
                sm.setQuantity(available - remaining);
                remaining = 0;
            }

            stockMovementRepository.save(sm);
            usedMovements.add(sm);
            if (remaining == 0) break;
        }

        order.setStockMovements(usedMovements);
        order.setCompleted(true);
        orderRepository.save(order);

        emailService.sendOrderNotification(user.getEmail(), user.getName(), order.getId(), "completion");
    }



    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (order.getItem() == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }
        if (order.getUser() == null) {
            throw new IllegalArgumentException("Order user cannot be null");
        }
        if (order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Order quantity must be positive");
        }
    }

    public void tryToFulfillOrder(Order order) {
        List<StockMovement> availableStock = stockMovementRepository.findByItemId(order.getItem().getId());
        int totalAvailable = availableStock.stream().mapToInt(StockMovement::getQuantity).sum();

        if (totalAvailable >= order.getQuantity()) {
            order.setCompleted(true);
            order.setStockMovements(availableStock);
            orderRepository.save(order);
            emailService.sendOrderNotification(
                    order.getUser().getEmail(),
                    order.getUser().getName(),
                    order.getId(),
                    "completion"
            );
        }
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order find(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
    }

    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));

        // Repor stock ao eliminar uma encomenda
        if (order.isCompleted()) {
            StockMovement movement = new StockMovement();
            movement.setItem(order.getItem());
            movement.setCreationDate(LocalDateTime.now());
            movement.setQuantity(order.getQuantity());

            stockMovementRepository.save(movement);
        }

        orderRepository.delete(order);
    }

}
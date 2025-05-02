package com.ordermanager.repository;

import com.ordermanager.model.Item;
import com.ordermanager.model.Order;
import com.ordermanager.utils.BaseRepository;

import java.util.List;

public class OrderRepository extends BaseRepository<Order> {

    public OrderRepository() {
        super(Order.class);

    }
    public List<Order> findByItemAndNotCompleted(Item item) {
        return em.createQuery("SELECT o FROM Order o WHERE o.item = :item AND o.completed = false", Order.class)
                .setParameter("item", item)
                .getResultList();
    }

}
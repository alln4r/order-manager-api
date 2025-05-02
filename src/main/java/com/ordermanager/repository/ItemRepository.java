package com.ordermanager.repository;

import com.ordermanager.model.Item;
import com.ordermanager.utils.BaseRepository;


public class ItemRepository extends BaseRepository<Item> {

    public ItemRepository() {
        super(Item.class);

    }
}
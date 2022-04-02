package com.flowersAndGifts.dao;

import com.flowersAndGifts.model.Item;

import java.util.List;

public interface ItemDao {
    Item selectItemById(Long id);

    List<Item> selectAllItems();

    Item insertItem(Item item);

    Item updateItem(Item item);

    void deleteItem(Item item);
}

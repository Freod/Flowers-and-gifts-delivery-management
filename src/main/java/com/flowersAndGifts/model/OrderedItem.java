package com.flowersAndGifts.model;

import java.util.Objects;

public class OrderedItem {
    private Long id;
    private Item item;
    private Long count;

    public OrderedItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderedItem that = (OrderedItem) o;
        return Objects.equals(id, that.id) && Objects.equals(item, that.item) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item, count);
    }

    @Override
    public String toString() {
        return "OrderedItem{" +
                "id=" + id +
                ", item=" + item +
                ", count=" + count +
                '}';
    }
}

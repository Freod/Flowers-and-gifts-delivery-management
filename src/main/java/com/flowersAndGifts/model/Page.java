package com.flowersAndGifts.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Page<T> {
    private int pageNumber;
    private int pageSize;
    private String sortBy;
    private String direction;
    private List<T> elements = new ArrayList<>();
    private T filter;

    public Page(int pageNumber, int pageSize, T filter) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        sortBy = "id";
        direction = "ASC";
        this.filter = filter;
    }

    public Page(int pageNumber, int pageSize, String sortBy, String direction, T filter) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.direction = direction;
        this.filter = filter;
    }

    public Page(int pageNumber, int pageSize, String sortBy, String direction, List<T> elements, T filter) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.direction = direction;
        this.elements = elements;
        this.filter = filter;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

    public T getFilter() {
        return filter;
    }

    public void setFilter(T filter) {
        this.filter = filter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page<?> page = (Page<?>) o;
        return pageNumber == page.pageNumber && pageSize == page.pageSize && Objects.equals(sortBy, page.sortBy) && Objects.equals(direction, page.direction) && Objects.equals(elements, page.elements) && Objects.equals(filter, page.filter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNumber, pageSize, sortBy, direction, elements, filter);
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", sortBy='" + sortBy + '\'' +
                ", direction='" + direction + '\'' +
                ", elements=" + elements +
                ", filter=" + filter +
                '}';
    }

    public int getOffset() {
        return (pageNumber - 1) * pageSize;
    }
}

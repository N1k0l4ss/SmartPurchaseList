package models;

import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private double price;
    private boolean selected;
    private int listId;
    private int amount;

    public Product(int id, String name, double price, boolean selected, int listId, int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.selected = selected;
        this.listId = listId;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Double.compare(product.price, price) == 0 && selected == product.selected && listId == product.listId && amount == product.amount && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, selected, listId, amount);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSelected() {
        return selected;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }
}


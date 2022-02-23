package models;

import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private double price;
    private boolean selected;
    private int listId;

    public Product(int id, String name, double price, boolean selected, int listId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.selected = selected;
        this.listId = listId;;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 && selected == product.selected && listId == product.listId && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, selected, listId);
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

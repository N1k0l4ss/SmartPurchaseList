package models;

import java.util.Objects;

public class ShoppingList {
    private int id;
    private String name;
    private double selectedPrice;
    private double listPrice;


    public ShoppingList(int id, String name, double selectedPrice, double listPrice) {
        this.id = id;
        this.name = name;
        this.selectedPrice = selectedPrice;
        this.listPrice = listPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingList that = (ShoppingList) o;
        return id == that.id && Double.compare(that.selectedPrice, selectedPrice) == 0 && Double.compare(that.listPrice, listPrice) == 0 && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, selectedPrice, listPrice);
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

    public double getSelectedPrice() {
        return selectedPrice;
    }

    public void setSelectedPrice(double selectedPrice) {
        this.selectedPrice = selectedPrice;
    }

    public double getListPrice() {
        return listPrice;
    }

    public void setListPrice(double listPrice) {
        this.listPrice = listPrice;
    }
}
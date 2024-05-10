package neko.stationery.utils;

public class Product {
    String id, name, category, supplier, stock;

    public Product(String id, String name, String category, String supplier, String stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.supplier = supplier;
        this.stock = stock;
    }

    public Product(String name, String category, String supplier, String stock) {
        this.name = name;
        this.category = category;
        this.supplier = supplier;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}

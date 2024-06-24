package com.ganbro.shopmaster.models;

public class Product {
    private int id;
    private String name;
    private double price;
    private String imageUrl;
    private int quantity;
    private String category;
    private String description;
    private boolean isRecommended;
    private boolean isSelected;

    public Product() {
    }

    public Product(int id, String name, double price, String imageUrl, int quantity, String category, String description, boolean isRecommended) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.category = category;
        this.description = description;
        this.isRecommended = isRecommended;
        this.isSelected = false;
    }

    public Product(int id, String name, double price, String imageUrl, int quantity, String category) {
        this(id, name, price, imageUrl, quantity, category, "", false);
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", isRecommended=" + isRecommended +
                ", isSelected=" + isSelected +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        if (Double.compare(product.price, price) != 0) return false;
        if (quantity != product.quantity) return false;
        if (isRecommended != product.isRecommended) return false;
        if (isSelected != product.isSelected) return false;
        if (!name.equals(product.name)) return false;
        if (!imageUrl.equals(product.imageUrl)) return false;
        if (!category.equals(product.category)) return false;
        return description.equals(product.description);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + name.hashCode();
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + imageUrl.hashCode();
        result = 31 * result + quantity;
        result = 31 * result + category.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + (isRecommended ? 1 : 0);
        result = 31 * result + (isSelected ? 1 : 0);
        return result;
    }
}

// Encapsulation - Ingredient Class
public class Ingredient {
    private String name;
    private String quantity;
    private double price;

    public Ingredient(String name, String quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return quantity + " " + name + " - ₱" + String.format("%.2f", price);
    }
}
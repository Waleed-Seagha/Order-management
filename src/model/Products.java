package model;

/**Products table entity. The fields of this class are the same as in the table. Provides getters and setters for all fields.
 * Even if the getters or setters may give unused warnings, they may actually be used through reflection.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

public class Products {

    private int id;
    private String name;
    private int quantity;
    private double price;

    public Products(int id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    /**Empty constructor is needed when using reflection techniques.
     */

    @SuppressWarnings("unused")
    public Products() {}

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}

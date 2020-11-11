package model;

/**Order_Details table entity. The fields of this class are the same as in the table. Provides getters and setters for all fields.
 * Even if the getters or setters may give unused warnings, they may actually be used through reflection.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

public class Order_Details {

    private int order_id;
    private double price;
    private String date;

    public Order_Details(int orderId, double price, String date) {
        this.order_id = orderId;
        this.price = price;
        this.date = date;
    }

    /**Empty constructor is needed when using reflection techniques.
     */

    @SuppressWarnings("unused")
    public Order_Details() {
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int orderId) {
        this.order_id = orderId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

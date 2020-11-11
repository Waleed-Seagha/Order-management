package presentation.reportEntities;

/**Contains all the fields the Orders report must have: id, clientName, productName, quantity, totalPrice, date.
 * Instead of the client_id and product_id of the orders table, it provides the client name and product name.
 * It also holds the information from the order_details table, namely the price (totalPrice) and the date.
 * Provides getters for all fields.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

public class OrderReportEntity {

    private int id;
    private String clientName;
    private String productName;
    private int quantity;
    private double totalPrice;
    private String date;

    public OrderReportEntity(int id, String clientName, String productName, int quantity, double totalPrice, String date) {
        this.id = id;
        this.clientName = clientName;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getDate() {
        return date;
    }
}

package businessLayer;

import model.Clients;
import model.Order_Details;
import model.Orders;
import model.Products;
import presentation.PDFCreator;
import presentation.reportEntities.OrderReportEntity;

import java.util.ArrayList;

/**Order Business logic class.
 * Uses the DAO objects created using the BLL parent class.
 * Provides easy modifications to each table in the database.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

public class OrderBLL extends BLL{

    /**Adds a new order to the orders table with the data got from: the clients table
     * (for client_id), the products table (for product_id) - according to the names
     * given as parameters -, the parameters (for quantity). It also adds a new element
     * in the order_details table with the price as the product of the quantity and the
     * price for each product (got from the products table) and the date from the method parameters.
     *
     * Method also generates a bill as a pdf file in case the order is successful (the quantity requested
     * is less than the quantity in stock) or an understocked notice as a pdf file otherwise, telling
     * the user that a client attempted to order an amount of products which was more than the product
     * quantity available.
     */

    public void createOrder(String clientName, String product, int quantity, String date) {
        int orderID = ordersDAO.getLastID() + 1;
        ArrayList<Clients> findClient = clientsDAO.find("name", clientName);
        ArrayList<Products> findProduct = productsDAO.find("name", product);
        if(findClient != null) {
            if(findProduct != null) {
                if(findProduct.get(0).getQuantity() >= quantity) {
                    int newQuantity = findProduct.get(0).getQuantity() - quantity;
                    double price = findProduct.get(0).getPrice() * quantity;
                    Orders order = new Orders(orderID, findClient.get(0).getId(), findProduct.get(0).getId(), quantity);
                    Order_Details orderDetails = new Order_Details(orderID, price, date);
                    productsDAO.update("id", findProduct.get(0).getId(), "quantity", newQuantity);
                    ordersDAO.insert(order);
                    orderDetailsDAO.insert(orderDetails);
                    PDFCreator.generateBill(orderID, clientName, product, quantity, findProduct.get(0).getPrice(), date);
                } else {
                    PDFCreator.generateUnderstockedNotice(product, findProduct.get(0).getQuantity(), clientName, quantity, date);
                }
            } else {
                System.out.println("ERROR: Product " + product + " not found.");
            }
        } else {
            System.out.println("ERROR: Client " + clientName + " not found.");
        }
    }

    /**Creates a pdf report containing information from all 4 tables, according to the
     * fields of the OrderReportEntity: the client and product name are got from
     * their specific tables according to the id specified in the orders table, the price and
     * date fields being got from order_details table according to the order id.
     */

    public void report() {
        ArrayList<OrderReportEntity> allOrders = ordersDAO.getAllOrderFields();
        PDFCreator.generateOrdersReport(allOrders);
    }

}

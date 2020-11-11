package businessLayer;

import dao.AbstractDAO;
import dao.OrdersDAO;
import model.Clients;
import model.Order_Details;
import model.Orders;
import model.Products;

/**Abstract class creating a DAO for each table.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

public abstract class BLL {

    protected final OrdersDAO ordersDAO;
    protected final AbstractDAO<Clients> clientsDAO;
    protected final AbstractDAO<Products> productsDAO;
    protected final AbstractDAO<Order_Details> orderDetailsDAO;

    /**Creates a DAO for each table (clients, products, orders, order_details).
     */

    public BLL() {
        ordersDAO = new OrdersDAO(Orders.class);
        clientsDAO = new AbstractDAO<>(Clients.class);
        productsDAO = new AbstractDAO<>(Products.class);
        orderDetailsDAO = new AbstractDAO<>(Order_Details.class);
    }

}

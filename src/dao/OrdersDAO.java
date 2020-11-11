package dao;

import dataAccessLayer.ConnectionFactory;
import model.Orders;
import presentation.reportEntities.OrderReportEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**Handles the queries needed to operate on the database for orders table,
 * adding 2 extra methods needed specially for orders.
 * It extends AbstractDAO for Orders, keeping all its features.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

public class OrdersDAO extends AbstractDAO<Orders> {

    /**
     * @param type must always be Orders.class
     */
    public OrdersDAO(Class<Orders> type) {
        super(type);
    }

    /**Deletes all elements having the id of type IdOf ("something_id") equal to id parameter.
     */

    public void deleteByIdOf(String IdOf, int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        deleteFromOrderDetails(IdOf, id);
        String query = "DELETE FROM " + type.getSimpleName() + " WHERE " + IdOf + " = ?";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) ConnectionFactory.close(connection);
            if(statement != null) ConnectionFactory.close(statement);
        }
    }

    /**Creates a list of OrderReportEntity objects: the client and product name are got from
     * their specific tables according to the id specified in the orders table, the price and
     * date fields being got from order_details table according to the order id.
     * @return the list of OrderReportEntity objects.
     */

    public ArrayList<OrderReportEntity> getAllOrderFields() {
        ArrayList<OrderReportEntity> allOrderFields = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet[] resultSet = {null, null, null};
        ArrayList<Orders> allOrders = getAllFields();
        for(Orders order : allOrders) {
            String[] query = {"SELECT `name` FROM clients WHERE id = " + order.getClient_id(),
                    "SELECT `name` FROM products WHERE id = " + order.getProduct_id(),
                    "SELECT price, `date` FROM order_details WHERE order_id = " + order.getId()};
            try {
                connection = ConnectionFactory.getConnection();
                for(int i = 0; i < 3; i++) {
                    statement = connection.prepareStatement(query[i]);
                    resultSet[i] = statement.executeQuery();
                    resultSet[i].next();
                }
                allOrderFields.add(
                        new OrderReportEntity(order.getId(), resultSet[0].getString(1),
                                resultSet[1].getString(1), order.getQuantity(),
                                resultSet[2].getDouble(1), resultSet[2].getString(2))
                );
            } catch(SQLException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) ConnectionFactory.close(connection);
                if (statement != null) ConnectionFactory.close(statement);
                for(int i = 0; i < 3; i++) {
                    if (resultSet[i] != null) ConnectionFactory.close(resultSet[i]);
                }
            }
        }
        return allOrderFields;
    }

    /**Deletes from order_details table the elements having the order_id equal to the id from the orders table
     * where the "IdOf" field is equal to the id parameter.
     */

    private void deleteFromOrderDetails(String IdOf, int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT id FROM " + type.getSimpleName() + " WHERE " + IdOf + " = " + id;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                query = "DELETE FROM order_details WHERE order_id = " + resultSet.getInt(1);
                statement = connection.prepareStatement(query);
                statement.executeUpdate();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) ConnectionFactory.close(connection);
            if (statement != null) ConnectionFactory.close(statement);
            if (resultSet != null) ConnectionFactory.close(resultSet);
        }
    }
}

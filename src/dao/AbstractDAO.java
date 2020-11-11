package dao;

import dataAccessLayer.ConnectionFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**Handles the queries needed to operate on the database.
 * It can be used on any type of class from the model package through reflection.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

public class AbstractDAO<T> {

    /**Holds the type of the class T.
     */
    protected final Class<T> type;

    /**Sets the type variable based on the parameter.
     * @param type Must specify the class type that will be used (theClassType.class).
     *             For example, if T is Clients, the parameter given should be Clients.class.
     * */

    public AbstractDAO(Class<T> type) {
        this.type = type;
    }

    /**Inserts the element given as parameter in the according table (the one with the same name).
     * @param element can be of any type of the model package
     */

    public void insert(T element) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createInsertQuery(element.getClass().getDeclaredFields().length);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            int i = 1;
            for(Field field : element.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try{
                    statement.setObject(i, field.get(element));
                    i++;
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) ConnectionFactory.close(connection);
            if(statement != null) ConnectionFactory.close(statement);
        }
    }

    /**Deletes the element given as parameter from the according table (the one with the same name).
     * @param name the name of the element to be deleted
     */

    public void delete(String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createDeleteQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) ConnectionFactory.close(connection);
            if(statement != null) ConnectionFactory.close(statement);
        }
    }

    /**Private method that searches the table with the same name as T for fields that are equal to a specific value.
     * @param isString the value may be also an integer; in that case, it will be converted from string using Integer.parseInt
     * @return a list of all the found elements satisfying the given condition.
     */

    private ArrayList<T> find(String field, String value, boolean isString) {
        String query;
        if(isString) {
            query = "SELECT * FROM " + type.getSimpleName() + " WHERE `" +
                    field + "` = '" + value + "'";
        } else {
            query = "SELECT * FROM " + type.getSimpleName() + " WHERE `" +
                    field + "` = " + value;
        }
        return getTs(query);
    }

    /**Executes the query given as argument.
     * @return a list of the found elements after executing the query
     */

    private ArrayList<T> getTs(String query) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<T> list = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            list = createObjects(resultSet);
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) ConnectionFactory.close(connection);
            if (statement != null) ConnectionFactory.close(statement);
            if (resultSet != null) ConnectionFactory.close(resultSet);
        }
        if((list != null) && (list.isEmpty()))
            return null;
        return list;
    }

    /**Executes the private find method, setting the isString value of that method according to the type of the value parameter.
     * @return a list of all the found elements satisfying the given condition.
     */

    public ArrayList<T> find(String field, String value) {
        return find(field, value, true);
    }

    /**Executes the private find method, setting the isString value of that method according to the type of the value parameter.
     * @return a list of all the found elements satisfying the given condition.
     */

    public ArrayList<T> find(String field, int value) {
        return find(field, String.valueOf(value), false);
    }

    /**Private method running the code for the public update methods based on the isString type.
     * @param isString is true if value parameter is a string or false otherwise.
     */

    private void update(String idName, int id, String field, String value, boolean isString) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createUpdateQuery(idName, field);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(2, id);
            if(isString) statement.setString(1, value);
            else statement.setInt(1, Integer.parseInt(value));
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) ConnectionFactory.close(connection);
            if (statement != null) ConnectionFactory.close(statement);
        }
    }

    /**Updates the element given as parameter from the according table (the one with the same name),
     * searching it by id (which in the table may not be the "id" field necessarily, but "something_id",
     * that's why it is specified through idName variable. Finally, the field and value variables specify
     * which field should be updated and with what new value.
     */

    public void update(String idName, int id, String field, String value) {
        update(idName, id, field, value, true);
    }

    /**Updates the element given as parameter from the according table (the one with the same name),
     * searching it by id (which in the table may not be the "id" field necessarily, but "something_id",
     * that's why it is specified through idName variable. Finally, the field and value variables specify
     * which field should be updated and with what new value.
     */

    public void update(String idName, int id, String field, int value) {
        update(idName, id, field, String.valueOf(value), false);
    }

    /**
     * @return a list of all fields of the table with the same name as T
     */

    public ArrayList<T> getAllFields() {
        String query;
        query = "SELECT * FROM " + type.getSimpleName();
        return getTs(query);
    }

    /**
     * @return the highest ID value of the table with the same name as T
     */

    public int getLastID() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int id = -1;
        String query = "SELECT * FROM " + type.getSimpleName() + " ORDER BY id DESC LIMIT 1";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            if(resultSet.next()) {
                id = resultSet.getInt(1);
            } else {
                id = 0;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    /**Private method creating the query for inserting elements.
     * @param numberOfElements since our tables may have only 3 or 4 elements, this variable can be only 3 or 4.
     * @return the query to be executed by the insert method.
     */

    private String createInsertQuery(int numberOfElements) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(type.getSimpleName());
        switch(numberOfElements) {
            case 3:
                sb.append(" VALUES (?, ?, ?)");
                break;
            case 4:
                sb.append(" VALUES (?, ?, ?, ?)");
                break;
        }
        return sb.toString();
    }

    /**Private method creating the query for inserting elements.
     * @return the query to be executed by the delete method.
     */

    private String createDeleteQuery() {
        return "DELETE FROM " + type.getSimpleName() + " WHERE name = ?";
    }

    /**Private method creating the query for updating elements.
     * @return the query to be executed by the update method.
     */

    private String createUpdateQuery(String idName, String field) {
        return "UPDATE " +
                type.getSimpleName() +
                " SET `" +
                field +
                "` = ? WHERE " +
                idName +
                " = ?";
    }

    /**Creates a list of T objects extracted from the ResultSet object.
     * @return returns the list.
     */

    private ArrayList<T> createObjects(ResultSet resultSet) {
        ArrayList<T> list = new ArrayList<>();
        try {
            while(resultSet.next()) {
                T instance = type.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) ConnectionFactory.close(resultSet);
        }
        return list;
    }
}

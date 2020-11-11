package model;

/**Orders table entity. The fields of this class are the same as in the table. Provides getters and setters for all fields.
 * Even if the getters or setters may give unused warnings, they may actually be used through reflection.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

public class Orders {

    private int id;
    private int client_id;
    private int product_id;
    private int quantity;

    public Orders(int id, int client_id, int product_id, int quantity) {
        this.id = id;
        this.client_id = client_id;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    /**Empty constructor is needed when using reflection techniques.
     */

    @SuppressWarnings("unused")
    public Orders() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int clientId) {
        this.client_id = clientId;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int productId) {
        this.product_id = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}

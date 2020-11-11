package businessLayer;

import model.Products;
import presentation.PDFCreator;

/**Product Business logic class.
 * Uses the DAO objects created using the BLL parent class.
 * Provides easy modifications to each table in the database.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

import java.util.ArrayList;

public class ProductBLL extends BLL{

    /**Adds a new product to the table with the name, quantity and the price given as parameters.
     */

    public void addProduct(String name, int quantity, double price){
        ArrayList<Products> search = productsDAO.find("name", name);
        if(search == null) {
            int id = productsDAO.getLastID() + 1;
            productsDAO.insert(new Products(id, name, quantity, price));
        } else {
            int newQuantity = search.get(0).getQuantity() + quantity;
            productsDAO.update("id", search.get(0).getId(), "quantity", newQuantity);
        }
    }

    /**Deletes the product with the name given as parameter and all the orders this product was used into.
     */

    public void deleteProduct(String name) {
        ArrayList<Products> findProduct = productsDAO.find("name", name);
        if(findProduct != null) {
            deleteAllOrdersOfProduct(findProduct.get(0).getId());
            productsDAO.delete(name);
        } else {
            System.out.println("ERROR: Product " + name + " doesn't exist.");
        }
    }

    /**Private method that deletes all orders where a product was used into with the ID given as parameter.
     * This method is used by the deleteProduct method.
     */

    private void deleteAllOrdersOfProduct(int productID) {
        ordersDAO.deleteByIdOf("product_id", productID);
    }

    /**Creates a pdf report of the products table.
     */

    public void report() {
        ArrayList<Products> allProducts = productsDAO.getAllFields();
        PDFCreator.generateProductsReport(allProducts);
    }

}

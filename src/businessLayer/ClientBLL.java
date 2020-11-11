package businessLayer;

import model.Clients;
import presentation.PDFCreator;

import java.util.ArrayList;

/**Client Business logic class.
 * Uses the DAO objects created using the BLL parent class.
 * Provides easy modifications to each table in the database.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

public class ClientBLL extends BLL{

    /**Adds a new client to the table with the name and the address given as parameters.
     */

    public void addClient(String name, String address){
        if(clientsDAO.find("name", name) == null) {
            int id = clientsDAO.getLastID() + 1;
            clientsDAO.insert(new Clients(id, name, address));
        } else {
            System.out.println("ERROR: Client " + name + " already exists.");
        }
    }

    /**Deletes the client with the name given as parameter and all the orders this client has.
     */

    public void deleteClient(String name) {
        ArrayList<Clients> findClient = clientsDAO.find("name", name);
        if(findClient != null) {
            deleteAllOrdersOfClient(findClient.get(0).getId());
            clientsDAO.delete(name);
        } else {
            System.out.println("ERROR: Client " + name + " doesn't exist.");
        }
    }

    /**Creates a pdf report of the clients table.
     */

    public void report() {
        ArrayList<Clients> allClients = clientsDAO.getAllFields();
        PDFCreator.generateClientsReport(allClients);
    }

    /**Private method that deletes all orders of the client with the ID given as parameter.
     * This method is used by the deleteClient method.
     */

    private void deleteAllOrdersOfClient(int clientID) {
        ordersDAO.deleteByIdOf("client_id", clientID);
    }

}

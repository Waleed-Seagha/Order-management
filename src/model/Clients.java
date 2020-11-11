package model;

/**Clients table entity. The fields of this class are the same as in the table. Provides getters and setters for all fields.
 * Even if the getters or setters may give unused warnings, they may actually be used through reflection.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

public class Clients {

    private int id;
    private String name;
    private String address;

    public Clients(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    /**Empty constructor is needed when using reflection techniques.
     */

    @SuppressWarnings("unused")
    public Clients() {};

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

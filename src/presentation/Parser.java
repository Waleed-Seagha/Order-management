package presentation;

import businessLayer.ClientBLL;
import businessLayer.OrderBLL;
import businessLayer.ProductBLL;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.lang.System.exit;

/**Contains the input text file and may parse commands from a given text file.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

public class Parser {

    /**Used to read from the text file.
     */
    private Scanner scanner;

    /**The main types of commands that can be executed.
     */

    private enum Command {
        INSERT,
        DELETE,
        ORDER,
        REPORT
    }

    /**The command types that could be assigned to the main commands.
     */

    private enum CommandType {
        CLIENT,
        PRODUCT,
        ORDER
    }

    /**Creates a scanner or stops the execution of the program if the file doesn't exist.
     * @param filename the name of the file to be parsed if it's located in the folder
     *                 project or the path to the file.
     */

    public Parser(String filename) {
        try {
            this.scanner = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            System.out.println("File " + filename + " does not exist!");
            exit(1);
        }
    }

    /**Parses all the commands from the text file in Scanner object.
     */

    public void parseCommands() {
        try {
            while (true) { //while loop will end when scanner will throw the NoSuchElementException (i.e. when are no more lines to read)
                String commandString = scanner.nextLine();
                String[] elements = commandString.split(": ");
                String[] firstElements = elements[0].split(" ");

                Command command = assignCommand(commandString, firstElements);
                CommandType commandType = assignCommandType(firstElements, command, commandString);

                String parameters = elements.length == 2 ? elements[1] : null;
                if (!executeCommand(command, commandType, parameters)) {
                    System.out.println("Invalid command " + commandString + ".");
                }
            }
        } catch (NoSuchElementException ignored) {
        } //stop; there are no more lines to read
    }

    /**Closes the scanner.
     */

    public void close() {
        scanner.close();
    }

    /**Private method used by parseCommands method to get the command type.
     * @param commandString the whole command as a string.
     * @param firstElements the elements before ": " of the command.
     * @return the command of type Command enum.
     */

    private Command assignCommand(String commandString, String[] firstElements) {
        switch (firstElements[0].toLowerCase()) {
            case "insert":
                return Command.INSERT;
            case "delete":
                return Command.DELETE;
            case "order":
                return Command.ORDER;
            case "report":
                return Command.REPORT;
            default:
                System.out.println("Invalid command " + commandString + ".");
                return null;
        }
    }

    /**Private method used by parseCommands method to assign the second command type in case of INSERT, DELETE or REPORT commands.
     * @param firstElements the elements before ": " of the command.
     * @param command the first command type of the command (of type Command).
     * @param commandString the whole command as a string.
     * @return the second command type of type CommandType.
     */

    private CommandType assignCommandType(String[] firstElements, Command command, String commandString) {
        if ((firstElements.length == 2) && ((command == Command.INSERT) || (command == Command.DELETE) || command == Command.REPORT)) {
            switch (firstElements[1].toLowerCase()) {
                case "client":
                    return CommandType.CLIENT;
                case "product":
                    return CommandType.PRODUCT;
                case "order":
                    return CommandType.ORDER;
                default:
                    System.out.println("Invalid command " + commandString + ".");
            }
        }
        return null;
    }

    /**Executes the command given by the user.
     * @param command first command type.
     * @param commandType second command type.
     * @param parameters the parameters of the command (leave null if they don't exist).
     * @return true if the command is executes successfully.
     */

    private boolean executeCommand(Command command, CommandType commandType, String parameters) {
        if(command == null)
            return false;
        switch (command) {
            case INSERT:
                return executeInsert(parameters, commandType);
            case DELETE:
                return executeDelete(parameters.split(",")[0], commandType);
            case ORDER:
                return executeOrder(parameters);
            case REPORT:
                return executeReport(commandType);
            default:
                return false;
        }
    }

    /**Inserts a client or a product in the database based on the commandType variable.
     * @param parameters represents the data to be inserted (the name and the address fields, separated by a comma).
     * @param commandType where to insert the data (client table or product table).
     * @return true if the command is executes successfully.
     */

    private boolean executeInsert(String parameters, CommandType commandType) {
        if(parameters == null)
            return false;
        if (commandType == CommandType.CLIENT) {
            String[] parametersArray = parameters.split(", ");
            if (parametersArray.length == 2) {
                (new ClientBLL()).addClient(parametersArray[0], parametersArray[1]);
                return true;
            } else {
                return false;
            }
        } else if (commandType == CommandType.PRODUCT) {
            String[] parametersArray = parameters.split(", ");
            if (parametersArray.length == 3) {
                try {
                    int quantity = Integer.parseInt(parametersArray[1]);
                    double price = Double.parseDouble(parametersArray[2]);
                    (new ProductBLL()).addProduct(parametersArray[0], quantity, price);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**Deletes a client or a product in the database based on the commandType variable.
     * @param parameters represents the data to be deleted (the name).
     * @param commandType from where to delete the data (client table or product table).
     * @return true if the command is executes successfully.
     */

    private boolean executeDelete(String parameters, CommandType commandType) {
        if(parameters == null)
            return false;
        if (commandType == CommandType.CLIENT) {
            (new ClientBLL()).deleteClient(parameters);
            return true;
        } else if (commandType == CommandType.PRODUCT) {
            (new ProductBLL()).deleteProduct(parameters);
            return true;
        } else {
            return false;
        }
    }

    /**Creates an order in the database (tables orders and order_details) and creates a bill as a pdf file
     *  if there are enough products for the order or an understocked notice pdf if not.
     * @param parameters the client making the order, the product to order and the quantity, separated by commas.
     * @return true if the command is executes successfully.
     */

    private boolean executeOrder(String parameters) {
        if(parameters == null)
            return false;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        String[] parametersArray = parameters.split(", ");
        if (parametersArray.length == 3) {
            try {
                int quantity = Integer.parseInt(parametersArray[2]);
                (new OrderBLL()).createOrder(parametersArray[0], parametersArray[1], quantity, date);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**Creates a report as a pdf of the clients (directly the contents of the table), products (same as clients)
     * or of the orders (this report collects data from all tables, since the orders table contains id references
     * to those tables and order_details contains supplementary information regarding each order).
     * @param commandType indicates the type of report.
     * @return true if commandType is valid (CLIENT, PRODUCT or ORDER).
     */

    private boolean executeReport(CommandType commandType) {
        switch (commandType) {
            case CLIENT:
                (new ClientBLL()).report();
                return true;
            case PRODUCT:
                (new ProductBLL()).report();
                return true;
            case ORDER:
                (new OrderBLL()).report();
                return true;
            default:
                return false;
        }
    }
}

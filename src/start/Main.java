package start;

import presentation.Parser;

/**Main class. Contains only a main method, running the Order Management App.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

public class Main {

    /**Creates a parser and runs its parseCommands method to parse the commands given in the text file.
     * @param args Specifies the text file name if it is in the same file as the project or the file path.
     */

    public static void main(String[] args) {
        Parser parser = new Parser(args[0]);
        parser.parseCommands();
        parser.close();
    }

}

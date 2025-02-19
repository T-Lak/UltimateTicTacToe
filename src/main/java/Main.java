import api.Controller;
import py4j.GatewayServer;

/**
 * Entry point of the application.
 * Initializes the game controller and starts a Py4J GatewayServer to enable communication with Python.
 */
public class Main {

    public static void main(String[] args) {
        // Create an instance of the game controller
        Controller controller = new Controller();

        // Initialize the Py4J GatewayServer, allowing Python to interact with the Java controller
        GatewayServer server = new GatewayServer(controller);

        // Start the server to listen for incoming Python requests
        server.start();
    }

}

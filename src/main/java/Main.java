import api.Controller;
import py4j.GatewayServer;

public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller();
        GatewayServer server = new GatewayServer(controller);
        server.start();
    }

}

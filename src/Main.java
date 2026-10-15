import server.ServerManager;

public class Main {
    public static void main(String[] args) {

        ServerManager manager = new ServerManager(true);
        UserInterface ui = new UserInterface(manager);

        ui.run();
    }
}
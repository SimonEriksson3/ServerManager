package server;

import java.util.ArrayList;

/*
 * ServerManager handles the console menu, the server collection and user input.
 */
public class ServerManager {

    //The ArrayList that contains our servers.
    private final ArrayList<Server> servers = new ArrayList<>();

    //Standard constructor - defaults to true (loadDefaults)
    public ServerManager() {
        this(true);
    }

    //Overloaded constructor - gives you the choice of adding servers by yourself.
    public ServerManager(boolean loadDefaults) {
        if (loadDefaults) {
            //Adding a few servers to our list
            servers.add(new EmailServer("192.168.1.50", 25, true));
            servers.add(new DatabaseServer("10.0.0.15", 3306, true, 200));
            servers.add(new BackupServer("192.168.100.5", 22, false));
            servers.add(new EmailServer("192.168.1.51", 587, true));
        }
    }

    public void run() {
        System.out.println("ServerManager initialized with " + servers.size() + " servers.");
    }
}

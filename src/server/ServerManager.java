package server;

import java.util.ArrayList;
import java.util.List;

/*
 * ServerManager handles the server collection and performs operations on the servers.
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
            registerServer(new EmailServer("192.168.1.50", 25, true));
            registerServer(new DatabaseServer("10.0.0.15", 3306, true, 200));
            registerServer(new BackupServer("192.168.100.5", 22, false));
            registerServer(new GenericServer("192.168.1.51", 587, true));
        }

    }

    //Checks for duplicates and registers a new server to the collection.
    public void registerServer(Server server) {
        if (server == null) {
            throw new IllegalArgumentException("Server cannot be null.");
        }

        if (isServerDuplicate(server.getIpAddress(), server.getPort())) {
            throw new IllegalArgumentException("A server with the address: "
                    + server.getIpAddress() + ":" + server.getPort()
                    + " is already registered in the system.");
        }
        servers.add(server);
    }

    public void removeServers(ArrayList<Server> selectedServers) {
        if (!servers.isEmpty() && selectedServers != null) {
            servers.removeAll(selectedServers);
        }
    }

    //Returns an unmodifiable copy of the original server collection. Only the objects themselves can be modified.
    public List<Server> getServers() {
        return List.copyOf(servers);
    }

    //Getters to avoid creating a new copy of the list every time a function needs information.
    public int getServerCount() {
        return servers.size();
    }

    public boolean isEmpty() {
        return servers.isEmpty();
    }

    public Server get(int index) {
        return servers.get(index);
    }

    public int getOnlineServersCount() {
        int onlineCount = 0;

        for (Server server : servers) {
            if (server.getIsRunning()) {
                onlineCount++;
            }
        }
        return onlineCount;
    }

    public void startServers(ArrayList<Server> selectedServers) {
        for (Server server : selectedServers) {
            server.setServerStatus(true);
        }
    }

    public void stopServers(ArrayList<Server> selectedServers) {
        for (Server server : selectedServers) {
            server.setServerStatus(false);
        }
    }

    public void monitorServers(ArrayList<Server> selectedServers) {
        for (Server server : selectedServers) {
            //Checks if the server implements the Monitorable interface and executes its monitor implementation.
            if (server instanceof Monitorable m) {
                System.out.println(); //Empty line
                m.monitor();
            } else {
                System.out.println();
                System.out.println(server.getServerType() + " (" + server.getIpAddress() + ") is not monitorable.");
            }
        }
    }

    public void serverHealthChecks(ArrayList<Server> selectedServers) {
        for (Server server : selectedServers) {
            System.out.println();
            server.checkHealth();
        }
    }

    public void pingServers(ArrayList<Server> selectedServers) {
        for (Server server : selectedServers) {
            System.out.println();
            server.ping();
        }
    }

    public Server findServer(String ip, int port) {
        for (Server server : servers) {
            if (server.getIpAddress().equals(ip) && server.getPort() == port) {
                return server;
            }
        }
        return null;
    }

    //Checks if a server with the same IP-address and port already exist in the collection.
    public boolean isServerDuplicate(String ip, int port) {
        for (Server server : servers) {
            if (server.getIpAddress().equals(ip) && server.getPort() == port) {
                return true;
            }
        }
        return false;
    }
}

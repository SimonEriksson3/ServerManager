package server;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * ServerManager handles the server collection and performs operations on the server collection or the objects.
 */
public class ServerManager {

    //The ArrayList that contains our servers.
    private final ArrayList<Server> servers = new ArrayList<>();

    /**
     * Loads default server data into the server collection upon initialization.
     */
    public ServerManager() {
        this(true);
    }

    /**
     * Overloaded constructor, gives you the choice of adding servers by yourself.
     * @param loadDefaults true to initiate with dummy servers, false for an empty server collection.
     */
    public ServerManager(boolean loadDefaults) {
        if (loadDefaults) {
            //Adding a few servers to our list
            registerServer(new EmailServer("192.168.1.50", 25, true));
            registerServer(new DatabaseServer("10.0.0.15", 3306, true, 200));
            registerServer(new BackupServer("192.168.100.5", 22, false));
            registerServer(new GenericServer("192.168.1.51", 587, true));
            registerServer(new GenericServer("1.1.1.1", 80, true));
        }

    }

    /**
     * Checks for duplicates and registers a new server to the collection.
     * @param server the object to be registered in the collection.
     */
    public void registerServer(Server server) {
        if (server == null) {
            throw new IllegalArgumentException("Server cannot be null.");
        }

        if (isServerDuplicate(server.getIpAddress(), server.getPort())) {
            throw new IllegalArgumentException("A server with the address: "
                    + server.getEndpoint()
                    + " is already registered in the system.");
        }

        servers.add(server);
    }

    /**
     * Removes selected servers from the collection.
     * @param selectedServers the list of servers that should be removed.
     */
    public void removeServers(ArrayList<Server> selectedServers) {
        if (!servers.isEmpty() && selectedServers != null) {
            servers.removeAll(selectedServers);
        }
    }

    /**
     * Returns an unmodifiable copy of the original server collection. Only the objects themselves can be modified.
     * @return a copy of the original list.
     */
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

    /**
     * Starts the selected servers.
     * @param selectedServers the selected servers that should start.
     */
    public ArrayList<String> startServers(ArrayList<Server> selectedServers) {
        ArrayList<String> results = new ArrayList<String>();

        for (Server server : selectedServers) {
            results.add(server.setServerStatus(true));
        }
        return results;
    }

    /**
     * Stops the selected servers
     * @param selectedServers the selected servers that should be stopped.
     */
    public ArrayList<String> stopServers(ArrayList<Server> selectedServers) {
        ArrayList<String> results = new ArrayList<String>();

        for (Server server : selectedServers) {
            results.add(server.setServerStatus(false));
        }
        return results;
    }

    /**
     * Returns telemetry data of the selected servers.
     * @param selectedServers the selected servers to get telemetric data from.
     */
    public ArrayList<String> getMonitoringData(ArrayList<Server> selectedServers) {
        ArrayList<String> results = new ArrayList<String>();

        for (Server server : selectedServers) {
            //Checks if the server implements the Monitorable interface and executes its monitor implementation.
            if (server instanceof Monitorable m) {
                results.add(m.monitor());
            } else {
                results.add(server.getServerType() + " (" + server.getEndpoint() + ") is not monitorable.");
            }
        }
        return results;
    }

    /**
     * Performs simulated health checks from selected servers.
     * @param selectedServers the selected servers to run simulated health checks on.
     */
    public ArrayList<String> getHealthData(ArrayList<Server> selectedServers) {
        ArrayList<String> results = new ArrayList<String>();
        for (Server server : selectedServers) {
            results.add(server.checkHealth());
        }
        return results;
    }

    /**
     * Finds a specific server by its IP address and port.
     *
     * @param ip the IP address to search for.
     * @param port the port to search for.
     * @return the server object matching the specified endpoint, or null if not found.
     */
    public Server findServer(String ip, int port) {
        for (Server server : servers) {
            if (server.getIpAddress().equals(ip) && server.getPort() == port) {
                return server;
            }
        }
        return null;
    }

    /**
     * Checks if a server with the same IP-address and port already exist in the collection.
     * @param ip the IP address to search for.
     * @param port the port to search for
     * @return true if a server with specified endpoint already exist, false otherwise.
     */
    public boolean isServerDuplicate(String ip, int port) {
        return findServer(ip, port) != null;
    }
}

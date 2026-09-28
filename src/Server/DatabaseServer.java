package Server;

import java.util.Random;

public class DatabaseServer extends Server implements Monitorable {

    //The maximum number of connections the database-server is able to handle.
    //Assigned in the constructor
    private int maxConnections;
    //The current number of connections the database-server is currently handling.
    //Assigned randomly for simulation purposes.
    private int currentConnections;

    //Random used to simulate a number of current connections
    private Random rand = new Random();

    public DatabaseServer(String ipAdress, int port, boolean isRunning, int maxConnections) {
        super(ipAdress, port, isRunning);
        this.maxConnections = maxConnections;
        //Sets a random amount of current simulated connections.
        this.currentConnections = rand.nextInt(1, maxConnections + 1);
    }

    public int getCurrentConnections() {
        return currentConnections;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    //Overrides from the superclass.
    //This version of the method will also check how many additional connections the database-server can handle.
    @Override
    public void checkHealth() {
        super.checkHealth();

        //Checks the number of additional connections the server can handle.
        int availableConnections = getMaxConnections() - getCurrentConnections();
        if (availableConnections == 0) {
            System.out.println("WARNING: The server cannot handle any more connections.");
            System.out.println("Maximum number of connections: " + getMaxConnections() + " \nCurrent number of connections: " + getCurrentConnections());
        } else if (availableConnections <= 10) {
            System.out.println("WARNING: The server can only handle " + availableConnections + " more connections");
        } else {
            System.out.println("The server can handle " + (maxConnections - currentConnections) + " more connection(s).");
        }
    }

    @Override
    public void monitor() {

    }
}

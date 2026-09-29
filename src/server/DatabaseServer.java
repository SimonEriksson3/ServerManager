package server;

import java.util.Random;

public class DatabaseServer extends Server implements Monitorable {

    //The maximum number of connections the database-server is able to handle.
    //Assigned in the constructor.
    private int maxConnections;
    //The current number of connections the database-server is currently handling.
    //Assigned randomly for simulation purposes.
    private int currentConnections;

    //Random used to simulate a number of current connections.
    private Random rand = new Random();

    public DatabaseServer(String ipAddress, int port, boolean isRunning, int maxConnections) {
        super(ipAddress, port, isRunning);
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

    //Overrides the checkHealth() method from the superclass.
    //This version of the method will also check how many additional connections the database-server can handle.
    @Override
    public void checkHealth() {
        super.checkHealth();

        if (!getIsRunning()) {
            System.out.println("CRITICAL: Database server is unreachable!");
            return;
        }


        //Checks if connection pool is reaching its limit.
        int availableConnections = getMaxConnections() - getCurrentConnections();
        if (availableConnections == 0) {
            System.out.println("CRITICAL: Connection pool exhausted! Max connections reached.");
        } else if (availableConnections <= 10) {
            System.out.println("WARNING: Low connection headroom (" + availableConnections + " slots available).");
        } else {
            System.out.println("HEALTH OK: Connection pool is healthy (" + availableConnections + " slots available).");
        }
    }

    //Prints server telemetry.
    @Override
    public void monitor() {
        double loadPercentage = ((double)currentConnections / maxConnections) * 100;
        int availableConnections = getMaxConnections() - getCurrentConnections();

        //Organized for readability.
        String activeConnections = getCurrentConnections() + " / " + getMaxConnections();
        String serverLoad = String.format("%.0f", loadPercentage) + "%";

        System.out.println("=== Database Server Telemetry ===");
        System.out.println("Status              : " + (getIsRunning() ? "ONLINE" : "OFFLINE"));
        System.out.println("Active connections  : " + (getIsRunning() ? activeConnections : "N/A"));
        System.out.println("Available slots     : " + (getIsRunning() ? availableConnections : "N/A"));
        System.out.println("Server load         : " + (getIsRunning() ? serverLoad : "N/A"));
        System.out.println("=================================");
    }
}

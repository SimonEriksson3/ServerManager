package server;

import java.util.Random;

/**
 * Represents a database server, handling database-specific monitoring like active connections.
 */
public class DatabaseServer extends Server implements Monitorable {

    //The maximum number of connections the database-server is able to handle.
    //Assigned in the constructor.
    private int maxConnections;
    //The current number of connections the database-server is currently handling.
    //Assigned randomly for simulation purposes.
    private int currentConnections;

    //Random used to simulate a number of current connections.
    private Random rand = new Random();

    /**
     * Constructs a new DatabaseServer and initializes random active connection telemetry.
     *
     * @param ipAddress the IP address of the server.
     * @param port the port number of the server.
     * @param getIsRunning true if the server should start online, false otherwise.
     * @param maxConnections the maximum number of concurrent database connections allowed.
     */
    public DatabaseServer(String ipAddress, int port, boolean getIsRunning, int maxConnections) {
        super(ipAddress, port, getIsRunning);

        setMaxConnections(maxConnections);
        //Sets a random amount of current simulated connections.
        this.currentConnections = rand.nextInt(1, maxConnections + 1);
    }

    public int getCurrentConnections() {
        return currentConnections;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        if (maxConnections < 1) {
            throw new IllegalArgumentException("Max connections cannot be less than 1.");
        }

        if (this.currentConnections > maxConnections) {
            throw new IllegalStateException("Cannot reduce max connections below the current number of connections (" + getCurrentConnections() + ").");
        }

        this.maxConnections = maxConnections;
    }

    @Override
    public String getServerType() {
        return "Database server";
    }

    @Override
    public String getCreationSummary() {
        return super.getCreationSummary()
                + "\nMax connections: " + getMaxConnections();
    }

    /**
     * Overrides the checkHealth() method from the superclass.
     * This version of the method will also check how many additional connections the database-server can handle.
     */
    @Override
    public void checkHealth() {
        super.checkHealth();

        if (!getIsRunning()) {
            System.out.println("CRITICAL: Database server is unreachable!");
            return;
        }

        int availableConnections = getMaxConnections() - getCurrentConnections();

        //Checks if connection pool is reaching its limit.
        if (availableConnections == 0) {
            System.out.println("CRITICAL: Connection pool exhausted! Max connections reached.");
        } else if (availableConnections <= 10) {
            System.out.println("WARNING: Low connection headroom (" + availableConnections + " slots available).");
        } else {
            System.out.println("HEALTH OK: Connection pool is healthy (" + availableConnections + " slots available).");
        }
    }

    /**
     * Prints server telemetry including number of connections and maximum number of connections.
     */
    @Override
    public void monitor() {
        double loadPercentage = ((double) currentConnections / maxConnections) * 100;
        int availableConnections = getMaxConnections() - getCurrentConnections();

        //Organized for readability.
        String activeConnections = getCurrentConnections() + " / " + getMaxConnections();
        String serverLoad = String.format("%.0f", loadPercentage) + "%";

        System.out.println("=== Database Server Telemetry ===");
        System.out.println("IP-address          : " + getIpAddress());
        System.out.println("Port                : " + getPort());
        System.out.println("Status              : " + (getIsRunning() ? "ONLINE" : "OFFLINE"));
        System.out.println("Active connections  : " + (getIsRunning() ? activeConnections : "N/A"));
        System.out.println("Available slots     : " + (getIsRunning() ? availableConnections : "N/A"));
        System.out.println("Server load         : " + (getIsRunning() ? serverLoad : "N/A"));
        System.out.println("---------------------------------");
    }
}

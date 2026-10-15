package server;

/**
 * Represents a generic server without specialized fields.
 * Used for servers that do not fit into the other subclasses.
 */
public class GenericServer extends Server{

    public GenericServer(String ipAddress, int port, boolean isRunning) {
        super(ipAddress, port, isRunning);
    }

    @Override
    public String getServerType() {
        return "Generic server";
    }

    /**
     * Overrides the checkHealth() method from the superclass.
     * This version of the method only checks if the server is running or not.
     */
    @Override
    public void checkHealth() {
        super.checkHealth();

        if (!getIsRunning()) {
            System.out.println("Status: Server is currently offline.");
            return;
        }

        System.out.println("HEALTH OK: Server is running normally.");
    }
}

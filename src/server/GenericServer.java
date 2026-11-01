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
    public String checkHealth() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.checkHealth() + "\n");

        if (!getIsRunning()) {
            sb.append("Status: Server is currently offline.");
        } else {
            sb.append("HEALTH OK: Server is running normally.");
        }
        return sb.toString();
    }
}

package server;

/*
 * Server - the superclass.
 */

public abstract class Server {

    private String ipAddress;
    private int port;
    private boolean isRunning;

    public Server(String ipAddress, int port, boolean isRunning) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.isRunning = isRunning;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    //This method sets the status of the server, whether it should run or not.
    public void setServerStatus(boolean isRunning) {
        this.isRunning = isRunning;
        System.out.println("This server is now " + (getIsRunning() ? "ONLINE" : "OFFLINE"));
    }

    //Simulates pinging the address to check if there's a currently working connection.
    public void pingAddress() {
        System.out.println("Checking connection...");
        System.out.println("Pinging " + getIpAddress());

        if (getIsRunning()) {
            System.out.println("Received response from server.");
        } else {
            System.out.println("No response received from server.");
        }
    }

    //Returns the server type as a string.
    public abstract String getServerType();

    //Simulates if the server is healthy.
    public void checkHealth() {
        System.out.println("Running health diagnostic for " + getIpAddress() + " on port " + getPort());
    }

    /*
     * Checks if a string corresponds to a valid IP-address.
     */
    public static boolean isValidIpv4 (String ip) {
        String[] parts = ip.split("\\."); //Split into 4 parts

        if (parts.length != 4) {
            return false;
        }

        try {
            for (String part : parts) {
                //Prevents leading zeros to avoid invalid formatting.
                if (part.length() > 1 && part.startsWith("0")) {
                    return false;
                }

                int value = Integer.parseInt(part);
                if (value < 0 || value > 255) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

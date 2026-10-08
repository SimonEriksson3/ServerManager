package server;

/*
 * Server - the superclass.
 */

public abstract class Server {

    private String ipAddress;
    private int port;
    private boolean isRunning;

    public Server(String ipAddress, int port, boolean isRunning) {
        //Throw exception if IP is invalid
        if (ipAddress == null || !isValidIpv4(ipAddress)) {
            throw new IllegalArgumentException("Invalid IP-address format: " + ipAddress);
        }

        //Throw exception if port is invalid
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Invalid port: " + port + ". Port must be between 1 and 65535.");
        }

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
    public void setServerStatus(boolean newStatus) {
        if (getIsRunning() == newStatus) {
            System.out.println(getServerType() + " (" + getIpAddress() + ") is already " + (newStatus ? "running." : "stopped."));
            return;
        }
        this.isRunning = newStatus;
        System.out.println(getServerType() + " (" + getIpAddress() + ") is now " + (getIsRunning() ? "ONLINE" : "OFFLINE"));
    }

    //Returns the server type as a string.
    public abstract String getServerType();

    //Simulates if the server is healthy.
    public void checkHealth() {
        System.out.println("Running health diagnostic for " + getIpAddress() + " on port " + getPort() + ".");
    }

    //Simulates pinging the server.
    public void ping() {
        System.out.println("Pinging " + getServerType() + " at " + getIpAddress() + "...");
        sleep();

        if (!getIsRunning()) {
            System.out.println("Request timed out. Server is offline");
            return;
        }

        int latency = (int) (Math.random() * 42) + 4; //Random latency between 4-45ms
        System.out.println("Reply from " + getIpAddress() + ": time=" + latency + "ms TTL=64");
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }

    /*
     * Checks if a string corresponds to a valid IP-address.
     */
    public static boolean isValidIpv4(String ip) {
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

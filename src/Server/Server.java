package Server;

/*
 * Server - the superclass
 */
public class Server {

    private String ipAddress;
    private int port;
    private boolean isRunning;

    public Server(String ipAddress, int port, boolean isRunning) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.isRunning = isRunning;
    }

    //Getters for IP-adress, port and server-status.
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

    //Simulates if the server is healthy.
    public void checkHealth() {
        System.out.println("This server is currently " + (getIsRunning() ? "ONLINE" : "OFFLINE"));
    }
}

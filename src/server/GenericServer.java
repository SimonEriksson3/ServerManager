package server;

import java.util.Random;

public class GenericServer extends Server{

    private Random rand = new Random();

    public GenericServer(String ipAddress, int port, boolean isRunning) {
        super(ipAddress, port, isRunning);
    }

    @Override
    public String getServerType() {
        return "Generic server";
    }

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

package server;

public class GenericServer extends Server{

    public GenericServer(String ipAddress, int port, boolean isRunning) {
        super(ipAddress, port, isRunning);
    }

    @Override
    public String getServerType() {
        return "Generic server";
    }
}

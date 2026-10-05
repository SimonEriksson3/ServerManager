import server.EmailServer;
import server.ServerManager;

public class Main {
    public static void main(String[] args) {

        ServerManager s = new ServerManager(true);
        s.run();

    }
}
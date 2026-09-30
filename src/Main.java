import server.BackupServer;
import server.DatabaseServer;
import server.EmailServer;
import server.ServerManager;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        ServerManager s = new ServerManager();

        s.run();
    }
}
import Server.BackupServer;
import Server.DatabaseServer;
import Server.EmailServer;
import Server.Server;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        BackupServer b = new BackupServer("12", 12, true);
        b.monitor();
    }
}
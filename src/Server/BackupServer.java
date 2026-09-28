package Server;

import java.util.Random;

public class BackupServer extends Server implements Monitorable{

    //Boolean that simulates if the server contains an active backup
    private boolean containsActiveBackup;

    //Random used to assign a random value to the containsActiveBackup-boolean, for simluation purposes.
    private Random rand = new Random();

    public BackupServer(String ipAdress, int port, boolean isRunning) {
        super(ipAdress, port, isRunning);
        this.containsActiveBackup = rand.nextBoolean();
    }

    public boolean getContainsActiveBackup() {
        return containsActiveBackup;
    }

    //Override from the superclass.
    //This version of the method will also check if the backup-server contains a working backup.
    @Override
    public void checkHealth() {
        super.checkHealth();

        if (getContainsActiveBackup()) {
            System.out.println("This backup-server contains a working backup.");
        } else {
            System.out.println("This backup-server does NOT contain a working backup.");
        }
    }

    @Override
    public void monitor() {

    }
}

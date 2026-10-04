package server;

import java.util.Random;

/*
 * Represents a backup server, handling backup-specific monitoring like storage capacity and active backup status.
 */
public class BackupServer extends Server implements Monitorable{

    //Boolean that simulates if the server has an active backup.
    private boolean hasActiveBackup;
    private double backupSize;
    private double diskSize = 1000000; // = 1TB disc as an example.

    //Random used to assign a random value to the hasActiveBackup-boolean, for simulation purposes.
    private Random rand = new Random();

    public BackupServer(String ipAddress, int port, boolean getIsRunning) {
        super(ipAddress, port, getIsRunning);
        //Assigns random simulated values.
        this.hasActiveBackup = rand.nextBoolean();

        //Assigns random backup size.
        if (getHasActiveBackup()) {
            this.backupSize = rand.nextDouble(500000, 950000); // = 500GB-950GB
        } else {
            this.backupSize = 0;
        }
    }

    public boolean getHasActiveBackup() {
        return hasActiveBackup;
    }

    public double getBackupSize() {
        return backupSize;
    }

    public double getDiskSize() {
        return diskSize;
    }

    //Returns the server type as string.
    @Override
    public String getServerType() {
        return "Backup server";
    }

    //Overrides the checkHealth() method from the superclass.
    //This version of the method will also check if the backup-server has a working backup.
    @Override
    public void checkHealth() {
        super.checkHealth();

        if (!getIsRunning()) {
            System.out.println("CRITICAL: Backup server is unreachable!");
            return;
        }

        if (!getHasActiveBackup()) {
            System.out.println("CRITICAL: No active backup found! Data loss risk.");
        } else {
            System.out.println("HEALTH OK: Backup is active and healthy.");
        }
    }

    //Prints server telemetry.
    @Override
    public void monitor() {
        double backupSizeGb = getBackupSize() / 1024; //Converted from MB to GB
        double diskSizeGb = getDiskSize() / 1024; //Converted from MB to GB
        double diskUsagePercentage = (getBackupSize() / getDiskSize()) * 100; //Disk load percentage

        //Organized for readability
        String diskUsage = String.format("%.1f", diskUsagePercentage) + "% of " + String.format("%.1f", diskSizeGb) + " GB";
        String freeSpace = String.format("%.1f", (diskSizeGb - backupSizeGb)) + " GB remaining";

        System.out.println("=== Backup Server Telemetry ===");
        System.out.println("IP-address          : " + getIpAddress());
        System.out.println("Port                : " + getPort());
        System.out.println("Status              : " + (getIsRunning() ? "ONLINE" : "OFFLINE"));
        System.out.println("Active Backup       : " + (getIsRunning() ? (getHasActiveBackup() ? "YES" : "NO") : "N/A"));

        if (getHasActiveBackup() && getIsRunning()) {
            System.out.println("Backup Size         : " + String.format("%.1f", backupSizeGb) + " GB ");
        }

        System.out.println("Disk Usage          : " + (getIsRunning() ? diskUsage : "N/A"));
        System.out.println("Disk Free Space     : " + (getIsRunning() ? freeSpace : "N/A"));
        System.out.println("-------------------------------");
    }
}

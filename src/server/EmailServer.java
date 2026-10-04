package server;

import java.util.Random;

/*
 * Represents an email server that implements specific email telemetry.
 */
public class EmailServer extends Server implements Monitorable{

    //The number of queued emails.
    private int queuedEmails;
    private int maxQueuedEmails = 400;

    //Random used to simulate a random amount of queued emails.
    private Random rand = new Random();

    public EmailServer(String ipAddress, int port, boolean getIsRunning) {
        super(ipAddress, port, getIsRunning);

        //Sets a random amount of simulated queued emails.
        queuedEmails = rand.nextInt(1, 390);
    }

    public int getQueuedEmails() {
        return queuedEmails;
    }

    public int getMaxQueuedEmails() {
        return maxQueuedEmails;
    }

    //Returns the server type as string.
    @Override
    public String getServerType() {
        return "Email server";
    }

    //Overrides the checkHealth() method from the superclass.
    //This version of the method will also check if email queue is within safe limits.
    @Override
    public void checkHealth() {
        super.checkHealth();

        if (!getIsRunning()) {
            System.out.println("CRITICAL: Email server is unreachable!");
            return;
        }

        if (getQueuedEmails() > 300) {
            System.out.println("WARNING: High email queue (" + getQueuedEmails() + " messages pending).");
        } else {
            System.out.println("HEALTH OK: Email queue is within safe limits (" + getQueuedEmails() + " messages).");
        }
    }

    //Prints server telemetry.
    @Override
    public void monitor() {
        double queuePercentage = ((double)getQueuedEmails() / maxQueuedEmails) * 100; //Email queue load percentage

        //Organized for readability.
        String emailFlushTime = String.format("%.0f", getQueuedEmails() * 1.2);
        String queueLoad = getQueuedEmails() + " / " + getMaxQueuedEmails() + " emails";
        String capacityUsed = String.format("%.0f", queuePercentage) + "%";

        //Telemetry.
        System.out.println("=== Email Server Telemetry ===");
        System.out.println("IP-address          : " + getIpAddress());
        System.out.println("Port                : " + getPort());
        System.out.println("Status              : " + (getIsRunning() ? "ONLINE" : "OFFLINE"));

        //Conditional to display telemetry based on server status.
        System.out.println("Queue load          : " + (getIsRunning() ? queueLoad : "N/A"));
        System.out.println("Capacity used       : " + (getIsRunning() ? capacityUsed : "N/A"));
        System.out.println("Est. Flush Time     : " + (getIsRunning() ? emailFlushTime + " seconds" : "N/A"));

        System.out.println("------------------------------");
    }
}

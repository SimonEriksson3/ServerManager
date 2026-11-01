package server;

import java.util.Random;

/**
 * Represents an email server that implements specific email telemetry.
 */
public class EmailServer extends Server implements Monitorable {

    //The number of queued emails.
    private int queuedEmails;
    private int maxQueuedEmails = 400;

    //Random used to simulate a random amount of queued emails.
    private Random rand = new Random();

    public EmailServer(String ipAddress, int port, boolean isRunning) {
        super(ipAddress, port, isRunning);

        //Sets a random amount of simulated queued emails.
        queuedEmails = rand.nextInt(1, maxQueuedEmails + 1);
    }

    public int getQueuedEmails() {
        return queuedEmails;
    }

    public int getMaxQueuedEmails() {
        return maxQueuedEmails;
    }

    @Override
    public String getServerType() {
        return "Email server";
    }

    /**
     * Overrides the checkHealth() method from the superclass.
     * This version of the method will also check if email queue is within safe limits.
     */
    @Override
    public String checkHealth() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.checkHealth() + "\n");

        if (!getIsRunning()) {
            sb.append("CRITICAL: Email server is unreachable!");
        } else if (getQueuedEmails() > 300) {
            sb.append("WARNING: High email queue (" + getQueuedEmails() + " messages pending).");
        } else {
            sb.append("HEALTH OK: Email queue is within safe limits (" + getQueuedEmails() + " messages).");
        }

        return sb.toString();
    }

    /**
     * Prints server telemetry including number of queued emails and maximum number of queued emails.
     */
    @Override
    public String monitor() {
        double queuePercentage = ((double) getQueuedEmails() / maxQueuedEmails) * 100; //Email queue load percentage

        //Organized for readability.
        String emailFlushTime = String.format("%.0f", getQueuedEmails() * 1.2);
        String queueLoad = getQueuedEmails() + " / " + getMaxQueuedEmails() + " emails";
        String capacityUsed = String.format("%.0f", queuePercentage) + "%";

        StringBuilder sb = new StringBuilder();

        //Telemetry.
        sb.append("=== Email Server Telemetry ===\n");
        sb.append("IP-address          : " + getIpAddress() + "\n");
        sb.append("Port                : " + getPort() + "\n");
        sb.append("Status              : " + (getIsRunning() ? "ONLINE" : "OFFLINE") + "\n");

        //Conditional to display telemetry based on server status.
        sb.append("Queue load          : " + (getIsRunning() ? queueLoad : "N/A") + "\n");
        sb.append("Capacity used       : " + (getIsRunning() ? capacityUsed : "N/A") + "\n");
        sb.append("Est. Flush Time     : " + (getIsRunning() ? emailFlushTime + " seconds" : "N/A") + "\n");

        sb.append("------------------------------");

        return sb.toString();
    }
}

package Server;

import java.util.Random;

/*
 * Subclass EmailServer which represents an Email-Server
 */
public class EmailServer extends Server implements Monitorable{

    //The number of queued emails.
    private int queuedEmails;
    //Random used to simulate a random amount of queued emails
    private Random rand = new Random();

    public EmailServer(String ipAdress, int port, boolean isRunning) {
        super(ipAdress, port, isRunning);
        //Sets a random amount of simultaed queued emails.
        queuedEmails = rand.nextInt(1, 200);
    }

    public int getQueuedEmails() {
        return queuedEmails;
    }

    //Overrides from the superclass.
    //This version of the method will also check how many queued emails currently exist in the email-server.
    @Override
    public void checkHealth() {
        super.checkHealth();

        //Checks the current amount of queued emails.
        System.out.println("This email-server currently have " +
                getQueuedEmails() + " queued emails waiting to be sent. " +
                (getQueuedEmails() > 100 ? "WARNING: Email queue is getting high" : "Email queue is OK."));
    }

    @Override
    public void monitor() {

    }
}

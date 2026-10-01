package server;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * ServerManager handles the console menu, the server collection and user input.
 */
public class ServerManager {

    //The ArrayList that contains our servers.
    private final ArrayList<Server> servers = new ArrayList<>();

    //Flag for the loop.
    private boolean isRunning = true;
    private Scanner scanner = new Scanner(System.in);

    //Standard constructor - defaults to true (loadDefaults)
    public ServerManager() {
        this(true);
    }

    //Overloaded constructor - gives you the choice of adding servers by yourself.
    public ServerManager(boolean loadDefaults) {
        if (loadDefaults) {
            //Adding a few servers to our list
            servers.add(new EmailServer("192.168.1.50", 25, true));
            servers.add(new DatabaseServer("10.0.0.15", 3306, true, 200));
            servers.add(new BackupServer("192.168.100.5", 22, false));
            servers.add(new EmailServer("192.168.1.51", 587, true));
        }
        System.out.println("ServerManager initialized with " + servers.size() + " servers.");
    }

    public void run() {
        while (isRunning) {
            System.out.println("========================================");
            System.out.println("       SERVER MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("    1. List current servers");
            System.out.println("    2. Add a new server");
            System.out.println("    3. Ping server");
            System.out.println("    4. Start server");
            System.out.println("    5. Stop server");
            System.out.println("    6. Check server health");
            System.out.println("    7. Monitor server(s)");
            System.out.println("    8. Remove server");
            System.out.println("    9. Exit");
            System.out.println("----------------------------------------");
            System.out.print("Enter your choice (1-9): ");

            int choice = 0;

            try {
                choice = Integer.parseInt(scanner.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }

            switch (choice) {
                case 1:
                    listAllServers();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    monitorServers();
                    break;
                case 8:
                    break;
                case 9:
                    System.out.println("Exiting program. Goodbye!");
                    isRunning = false; //Breaks loop
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 9.");
                    break;
            }
        }
    }

    /*
     * Allows the user to monitor all the servers or a specific server chosen from a list.
     */

    public void monitorServers() {
        boolean validInput = false; //Loop until valid input is received.

        while (!validInput) {
            System.out.println("\n--- MONITOR SERVERS ---");
            System.out.println("1. Monitor all servers");
            System.out.println("2. Monitor specific server");
            System.out.print("Enter your choice (1-2): ");

            int choice = 0;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue; //Jump to next iteration
            }

            switch (choice) {
                case 1:
                    for (Server server : servers) {
                        //(Monitorable) interface reference so each server executes its own monitor implementation.
                        if (server instanceof Monitorable m) {
                            m.monitor();
                            System.out.println("\n"); //Empty line
                        }
                    }
                    validInput = true; //Breaks out of the loop
                    break;
                case 2:
                    if (servers.isEmpty()) {
                        System.out.println("No servers available to monitor.");
                        validInput = true;
                        break;
                    }

                    //Displays all the available servers.
                    System.out.println("\n--- AVAILABLE SERVERS ---");
                    for (int i = 0; i < servers.size(); i++) {
                        Server s = servers.get(i);
                        System.out.println((i + 1) + ". " + s.getServerType() + " - " + s.getIpAddress() + ":" + s.getPort());
                    }

                    System.out.println("Enter the specific server you want to monitor: ");
                    int serverChoice = -1;

                    try {
                        //Subtract 1 to match the list of servers shown to user (as opposed to using 0 as starting index).
                        serverChoice = Integer.parseInt(scanner.nextLine()) - 1;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                        continue;
                    }

                    if (serverChoice >= 0 && serverChoice < servers.size()) { //To avoid index out of bounds
                        Server selectedServer = servers.get(serverChoice);

                        if (selectedServer instanceof Monitorable m) {
                            m.monitor();
                        } else {
                            System.out.println("This server is not monitorable.");
                        }
                    } else {
                        System.out.println("Invalid server.");
                    }

                    validInput = true;
                    break;
                default:
                    System.out.println("Please enter 1 or 2.");
                    break;
            }
        }
    }

    public void listAllServers() {
        for (Server server : servers) {
            System.out.println(server.getIpAddress() + ":" + server.getPort());
        }
    }
}

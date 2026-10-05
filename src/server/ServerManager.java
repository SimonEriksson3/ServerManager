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

    /*
     * Starts the main loop, allowing the user to interact with the menu.
     */
    public void run() {
        while (isRunning) {
            System.out.println("========================================");
            System.out.println("       SERVER MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("\t1. List current servers");
            System.out.println("\t2. Add a new server");
            System.out.println("\t3. Ping server");
            System.out.println("\t4. Start server");
            System.out.println("\t5. Stop server");
            System.out.println("\t6. Check server health");
            System.out.println("\t7. Monitor server(s)");
            System.out.println("\t8. Remove server");
            System.out.println("\t9. Exit");
            System.out.println("----------------------------------------");

            int choice = getUserChoice("Enter your choice (1-9): ", 1, 9);

            switch (choice) {
                case 1:
                    displayAllServers();
                    break;
                case 2:
                    addServer();
                    break;
                case 3:
                    break;
                case 4:
                    startServer();
                    break;
                case 5:
                    stopServer();
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
            }
        }
    }

    /*
     * Adds a server to the collection.
     */
    private void addServer() {
        System.out.println("\n--- ADD SERVER ---");
        System.out.println("What kind of server would you like to add?");
        System.out.println("\t1. Backup server");
        System.out.println("\t2. Database server");
        System.out.println("\t3. Email server");

        //Get server type.
        int choice = getUserChoice("Enter your choice (1-3): ", 1, 3, true);

        //If canceled.
        if (choice == 0) {
            System.out.println("Adding server canceled.");
            promptEnterKey();
            return;
        }

        //Get the data that all servers share.
        String ip = getValidIpAddress();
        int port = getValidPort();

        //Create the specific server object based on user input.
        switch (choice) {
            case 1:
                servers.add(new BackupServer(ip, port, false));
                System.out.println("Backup server added successfully!");
                break;
            case 2:
                //Prompt the user for maxConnections (unique for DatabaseServer).
                int maxConnections = getUserChoice("Enter maximum number of connections (10-10000): ", 10, 10000);
                servers.add(new DatabaseServer(ip, port, false, maxConnections));
                System.out.println("Database server added successfully!");
                break;
            case 3:
                servers.add(new EmailServer(ip, port, false));
                System.out.println("Email server added successfully!");
                break;
        }
        promptEnterKey();
    }

    private void startServer() {
        Server chosenServer = selectServer("Enter the specific server you want to start (0 to cancel): ");
        if (chosenServer != null) {
            if (chosenServer.getIsRunning()) {
                System.out.println("Server is already running.");
            } else {
                chosenServer.setServerStatus(true);
            }
        }
        promptEnterKey();
    }

    private void stopServer() {
        Server chosenServer = selectServer("Enter the specific server you want to stop (0 to cancel): ");
        if (chosenServer != null) {
            if (!chosenServer.getIsRunning()) {
                System.out.println("Server is already stopped.");
            } else {
                chosenServer.setServerStatus(false);
            }
        }
        promptEnterKey();
    }

    /*
     * Allows the user to monitor all the servers or a specific server chosen from a list.
     */
    private void monitorServers() {
        if (servers.isEmpty()) {
            System.out.println("There are currently no servers available.");
            promptEnterKey();
            return;
        }

        System.out.println("\n--- MONITOR SERVERS ---");
        System.out.println("\t1. Monitor all servers");
        System.out.println("\t2. Monitor specific server");

        int choice = getUserChoice("Enter your choice (1-2 or 0 to cancel): ", 1, 2, true);

        if (choice == 0) {
            System.out.println("Monitoring canceled.");
            promptEnterKey();
            return;
        }

        switch (choice) {
            case 1:
                for (Server server : servers) {
                    //Checks if the server implements the Monitorable interface and executes its monitor implementation
                    if (server instanceof Monitorable m) {
                        System.out.println(); //Empty line to separate each server's telemetry.
                        m.monitor();
                    }
                }
                break;

            case 2:
                Server selectedServer = selectServer("Enter the specific server you want to monitor (0 to cancel): ");
                if (selectedServer != null) {
                    if (selectedServer instanceof Monitorable m) {
                        System.out.println();
                        m.monitor();
                    } else {
                        System.out.println("This server is not monitorable.");
                    }
                }
                break;
        }
        promptEnterKey();
    }

    private int getUserChoice(String prompt, int min, int max) {
        return getUserChoice(prompt, min, max, false);
    }

    /*
     * Handles user input safely and prompts the user continuously until a valid integer between the specified range is entered.
     */
    private int getUserChoice(String prompt, int min, int max, boolean allowCancel) {
        while (true) {
            System.out.print(prompt);

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                //0 to cancel.
                if (allowCancel && choice == 0) {
                    return 0;
                }
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    if (max == 1) {
                        System.out.println("Invalid choice. There is only option " + min + " available" + (allowCancel ? " (0 to cancel)." : "."));
                    } else {
                        System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + (allowCancel ? " (0 to cancel)." : "."));
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    //Pauses the program and waits for the user to press enter before continuing.
    private void promptEnterKey() {
        System.out.println("\nPress \"ENTER\" to return to main menu...");
        scanner.nextLine();
    }

    //Prompts the user for a valid IP-address.
    private String getValidIpAddress() {
        while (true) {
            System.out.print("Enter IP-address: ");
            String ip = scanner.nextLine().trim();

            if (Server.isValidIpv4(ip)) {
                return ip;
            } else {
                System.out.println("Invalid IP-address. Please use the format X.X.X.X (192.168.0.1 for example).");
            }
        }
    }

    //Prompts the user for a valid server port.
    private int getValidPort() {
        return getUserChoice("Enter port (1-65535): ", 1, 65535);
    }

    //Used only for displaying all servers
    private void displayAllServers() {
        if (servers.isEmpty()) {
            System.out.println("There are currently no servers available.");
        } else {
            for (Server server : servers) {
                System.out.println(server.getServerType() + ": " + server.getIpAddress() + ":" + server.getPort());
            }
        }
        promptEnterKey();
    }

    //Used when listing servers with an index for user selection.
    private void listAllServers() {
        //Lists all the available servers.
        System.out.println("\n--- AVAILABLE SERVERS ---");
        for (int i = 0; i < servers.size(); i++) {
            Server s = servers.get(i);
            System.out.println("\t" + (i + 1) + ". " + s.getServerType() + " - " + s.getIpAddress() + ":" + s.getPort());
        }
    }

    /*
     * Lists all available servers and allows the user to select one.
     * Returns the chosen server object or null if the action is canceled.
     */
    private Server selectServer(String prompt) {
        if (servers.isEmpty()) {
            System.out.println("There are currently no servers available.");
            return null;
        }

        listAllServers();
        int choice = getUserChoice(prompt, 1, servers.size(), true);
        if (choice == 0) {
            System.out.println("Selecting server canceled.");
            return null;
        }
        return servers.get(choice - 1);
    }
}

import server.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Handles the menu, user input and validations.
 */
public class UserInterface {

    private final ServerManager manager;

    //Flag for the loop.
    private boolean isRunning = true;
    //Scanner for user input.
    private final Scanner scanner = new Scanner(System.in);

    public UserInterface(ServerManager manager) {
        this.manager = manager;
    }

    /*
     * Starts the main loop, allowing the user to interact with the menu.
     */
    public void run() {
        System.out.println("Initialized with " + manager.getServerCount() + " servers.");

        while (isRunning) {
            clearConsole();
            System.out.println("========================================");
            System.out.println("       SERVER MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("\t1. List all servers");
            System.out.println("\t2. Add a new server");
            System.out.println("\t3. Start server");
            System.out.println("\t4. Stop server");
            System.out.println("\t5. Ping server");
            System.out.println("\t6. Check server health");
            System.out.println("\t7. Monitor server");
            System.out.println("\t8. Remove server");
            System.out.println("\t9. Exit");
            System.out.println("----------------------------------------");
            int choice = getUserChoice("Enter your choice (1-9): ", 1, 9);

            clearConsole(); //Removes all current text from the console.

            switch (choice) {
                case 1 -> displayAllServers();
                case 2 -> addServer();
                case 3 -> changeServerStatus(true);
                case 4 -> changeServerStatus(false);
                case 5 -> pingServer();
                case 6 -> checkHealth();
                case 7 -> monitorServers();
                case 8 -> removeServer();
                case 9 -> {
                    System.out.println("Exiting program. Goodbye!");
                    isRunning = false;
                    return;
                }
            }
            promptEnterKey();
        }
    }

    //Used only for displaying all servers.
    private void displayAllServers() {
        if (manager.isEmpty()) {
            System.out.println("There are currently no servers available.");
        } else {

            System.out.println("\n--- REGISTERED SERVERS ---");

            for (Server server : manager.getServers()) {
                System.out.println(server.getServerType() + ": " + server.getIpAddress() + ":" + server.getPort()
                        + " [" + (server.getIsRunning() ? "ONLINE" : "OFFLINE") + "]");
            }

            System.out.println("----------------------------------------");
            int getServerCount = manager.getServerCount();
            int onlineServersCount = manager.getOnlineServersCount();
            int offlineServersCount = getServerCount - onlineServersCount;

            System.out.println("Total: " + getServerCount
                    + " | Online: " + onlineServersCount
                    + " | Offline: " + offlineServersCount);

        }
    }

    //Used when listing servers with an index for user selection.
    private void listAllServers() {
        //Lists all the available servers.
        System.out.println("\n--- AVAILABLE SERVERS ---");

        for (int i = 0; i < manager.getServerCount(); i++) {
            Server s = manager.get(i);
            System.out.println("\t" + (i + 1) + ". " + s.getServerType() + " - " + s.getIpAddress() + ":" + s.getPort());
        }
    }

    private void removeServer() {
        System.out.println("\n--- REMOVE SERVER ---");

        ArrayList<Server> selectedServers = selectAllOrOne("Remove");

        if (selectedServers != null) {
            System.out.println("Are you sure you want to remove:");
            for (Server server : selectedServers) {
                System.out.println("\t- " + server.getServerType() + " (" + server.getIpAddress() + ":" + server.getPort() + ")");
            }

            int count = selectedServers.size();
            String serverLabel = count == 1 ? "server" : "servers"; //Checks if the success-message should use the word server in plural or singular.

            if (getConfirmation("Confirm deletion?")) {
                manager.removeServers(selectedServers);
                System.out.println(count + " " + serverLabel + " removed successfully.");
            }
        }
    }

    private void pingServer() {
        System.out.println("\n--- PING SERVER ---");

        ArrayList<Server> selectedServers = selectAllOrOne("Ping");

        if (selectedServers != null) {
            manager.pingServers(selectedServers);
        }
    }

    private void checkHealth() {
        System.out.println("\n--- CHECK HEALTH ---");

        ArrayList<Server> selectedServers = selectAllOrOne("Run health check on");

        if (selectedServers != null) {
            manager.serverHealthChecks(selectedServers);
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
        System.out.println("\t4. Generic server");

        //Get server type.
        int choice = getUserChoice("Enter your choice (1-4 or 0 to cancel): ", 1, 4, true);

        //If canceled.
        if (choice == 0) {
            System.out.println("Adding server canceled.");
            return;
        }

        //Get the data that all servers share.
        String ip = getValidIpAddress();
        int port = getValidPort();

        try {
            //Create the specific server object based on user input.
            Server server = switch (choice) {
                case 1 -> new BackupServer(ip, port, false);
                case 2 -> {
                    //Prompt the user for maxConnections (unique for DatabaseServer).
                    int maxConnections = getUserChoice("Enter maximum number of connections (10-10000): ", 10, 10000);
                    yield new DatabaseServer(ip, port, false, maxConnections);
                }
                case 3 -> new EmailServer(ip, port, false);
                case 4 -> new GenericServer(ip, port, false);
                default -> throw new IllegalArgumentException("Invalid server choice.");
            };

            System.out.println("\nThe following server will be added: \n" + server.getCreationSummary());

            if (getConfirmation("Add this server?")) {
                manager.registerServer(server);
                System.out.println(server.getServerType() + " added successfully!");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void changeServerStatus(boolean shouldRun) {
        String action = shouldRun ? "Start" : "Stop";

        System.out.println("\n--- " + action.toUpperCase() + " SERVERS ---");

        ArrayList<Server> selectedServers = selectAllOrOne(action);

        if (selectedServers != null) {
            int count = selectedServers.size();
            String serverLabel = count == 1 ? "server" : "servers";

            System.out.println("\nYou have selected " + count + " " + serverLabel + ".\n");
            String confirmationMessage = "Are you sure you want to " + action.toLowerCase() + " the selected " + serverLabel + "?";

            if (getConfirmation(confirmationMessage)) {
                if (shouldRun) {
                    manager.startServers(selectedServers);
                } else {
                    manager.stopServers(selectedServers);
                }
            }
        }
    }

    /*
     * Allows the user to monitor all the servers or a specific server chosen from a list.
     */
    private void monitorServers() {
        System.out.println("\n--- MONITOR SERVERS ---");

        ArrayList<Server> selectedServers = selectAllOrOne("Monitor");

        if (selectedServers != null) {
            manager.monitorServers(selectedServers);
        }
    }

    /*
     * Overloaded version of getUserChoice that defaults to not allowing cancellation.
     */
    private int getUserChoice(String prompt, int min, int max) {
        return getUserChoice(prompt, min, max, false);
    }

    /*
     * Handles user input safely and prompts the user continuously until a valid integer between the specified range is entered.
     * If allowCancel is true the user can also enter 0 to cancel the operation.
     */
    private int getUserChoice(String prompt, int min, int max, boolean allowCancel) {

        if (min > max) {
            throw new IllegalArgumentException("Min value (" + min + ") cannot be greater than max value (" + max + "). ");
        }

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

    //Prompts the user for a yes/no confirmation and loops until a valid input is received.
    private boolean getConfirmation(String promptMessage) {
        while (true) {
            System.out.print(promptMessage + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                System.out.println("Operation cancelled.");
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
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
                System.out.println("Invalid IP-address. Please use the format X.X.X.X (192.168.1.50 for example).");
            }
        }
    }

    //Prompts the user for a valid server port.
    private int getValidPort() {
        return getUserChoice("Enter port (1-65535): ", 1, 65535);
    }

    /*
     * Lists all available servers and allows the user to select one.
     * Returns the chosen server object or null if the action is canceled.
     */
    private Server selectServer(String prompt) {
        if (manager.isEmpty()) {
            System.out.println("There are currently no servers available.");
            return null;
        }

        listAllServers();
        int choice = getUserChoice(prompt, 1, manager.getServerCount(), true);
        if (choice == 0) {
            System.out.println("Selecting server canceled.");
            return null;
        }
        return manager.get(choice - 1);
    }

    /*
     * Allows the selection of all servers or a specific server.
     * Used to perform a specific action on selected servers.
     * Returns null if the user cancels the selection.
     */
    private ArrayList<Server> selectAllOrOne(String action) {
        if (manager.isEmpty()) {
            System.out.println("There are currently no servers available.");
            return null;
        }

        ArrayList<Server> selectedServers = new ArrayList<>();

        System.out.println("\t1. " + action + " specific server.");
        System.out.println("\t2. " + action + " all servers.");
        System.out.println("\t3. " + action + " server by IP and port.");

        int choice = getUserChoice("Enter your choice (1-3 or 0 to cancel): ", 1, 3, true);

        if (choice == 0) {
            System.out.println("Selection canceled.");
            return null;
        }

        switch (choice) {
            case 1:
                Server s = selectServer("Enter the specific server you want to " + action.toLowerCase() + " (or 0 to cancel): ");
                //If selectServer is canceled = return null.
                if (s == null) {
                    return null;
                }
                selectedServers.add(s);
                break;
            case 2:
                //Copy all servers to temporary selectedServers list.
                selectedServers.addAll(manager.getServers());
                break;
            case 3:
                String ip = getValidIpAddress();
                int port = getValidPort();

                Server server = manager.findServer(ip, port);
                if (server != null) {
                    selectedServers.add(server);
                }

                if (selectedServers.isEmpty()) {
                    System.out.println("Server " + ip + " at port " + port + " was not found.");
                    return null;
                }
                break;
        }

        return selectedServers;
    }

    /*
     * Clears the command prompt from text (not in any IDE).
     * Not my code. Comes from here:
     * https://www.baeldung.com/java-clear-console-screen
     */
    private void clearConsole() {
        try {
            new ProcessBuilder("cmd", "/c", "cls")
                    .inheritIO()
                    .start()
                    .waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

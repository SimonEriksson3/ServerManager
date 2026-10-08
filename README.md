# Server Management System

## Project Overview
A console-based application to manage a collection of simulated servers. The program allows the user to start and stop servers, add and remove servers from the collection, and inspect server status, health and telemetry data.

## Superclass
- Name: `Server`
- Common Fields: `ipAddress`, `port`, `isRunning`
- Common Methods: `setServerStatus`, `pingAddress`, `checkHealth`

## Subclasses 
1.	**EmailServer** - Overrides `checkHealth` to verify if the email queue is within safe limits.
2.	**BackupServer** - Overrides `checkHealth` to verify if the backup server has a working backup at the moment.
3.	**DatabaseServer** - Overrides `checkHealth` to check the number of available database connections relative to `maxConnections`.
4.	**GenericServer** - Inherits default server behaviour without telemetry data or custom health checks.
   
## Interface
- Name: `Monitorable`
- Method(s): `monitor` (displays simulated server telemetry)
- Implemented by: EmailServer, BackupServer, DatabaseServer

## Menu
1.	List all servers
2.	Add a new server
3.	Ping server
4.	Start server
5.	Stop server
6.	Check server health
7.	Monitor server
8.	Remove server
9.	Exit

## Error scenarios 
* **Invalid Port:** Entering text or an out-of-range value (outside of 1-65535) displays an error message and continuously prompts the user until a valid number is provided.
* **Invalid IP Address:** Entering an IP address in the wrong format (not matching the X.X.X.X pattern, or using letters or numbers outside the 0-255 range) is caught by validation loops similarly to port checks.
* **Duplicate Servers:** Trying to add a server with an IP address and port combination that already exists triggers an error message.
* **Action on Empty Collection/Non-Existing Server:** Trying to perform operations (like ping, start, stop or remove) when no servers exist or when selecting a server that doesn't exist is safely handled with error messages.

## Justification
I created a shared superclass (`Server`) to hold everything that all servers have in common, like an IP address, port and server status (on/off). In this way, when creating subclasses you can focus on what makes the subclass unique, which makes the code much easier to maintain. By letting the subclasses override the `checkHealth` method the program can run specific simulated diagnostics depending on the server type the user is interacting with. 

Since not all servers have relevant telemetry, I used an interface (`Monitorable`) only for the servers that should actually support it instead of placing it into the superclass where all servers have access to it regardless of if they need it or not.

To improve user experience and prevent the program from crashing when a user types something invalid (like a non-existing menu item or server, an invalid port or IP address, etc.), I used validation loops. The program catches the mistake and prompts the user to try again until the input is correct. The user can also cancel by inputting a zero.
And finally, in order to write DRY code and avoid repeating code, I used helper methods for things like selecting server(s), getting user input and handling user confirmations.

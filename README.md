# Projektidé
Ett program som hanterar en samling simulerade servrar. Programmet låter användaren starta och stoppa servrar, 
lägga till och ta bort servrar från samlingen samt kontrollera en servers status och hälsa.

# Superklass
•	Namn: Server
•	Gemensamma fält: IP-adress, port, isRunning
•	Gemensamma metoder: setServerStatus, pingAddress, checkHealth

# Subklasser (minst tre)
1.	EmailServer – overridar checkHealth för att kontrollera hur många köade emails som finns
2.	BackupServer — overridar checkHealth för att även kontrollera om backup-servern har en fungerande backup just nu
3.	DatabaseServer — overridar checkHealth för att även kontrollera hur många ytterligare anslutningar databasservern kan hantera
   
# Interface
•	Namn: Monitorable
•	Metod(er): monitor (samlar och skriver ut info om servern)
•	Implementeras av (minst två subklasser): EmailServer, BackupServer, DatabaseServer

# Meny
1.	Add server
2.	Ping server
3.	Start server
4.	Stop server
5.	List current servers
6.	Check health
7.	Monitor server
8.	Remove server
9.	Exit

# Felscenarion
•	Försök att ange en felaktig port (t.ex en sträng, eller en port utanför intervallet 0-65535)
•	Försök att monitorera en server som inte finns
•	Försök att lägga till en server med IP-adress och port som redan finns

# Motivering (fylls i senare i veckan)

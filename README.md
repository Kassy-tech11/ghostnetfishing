# Ghost Net Fishing

Ghost Net Fishing ist eine prototypische Webanwendung zum Melden und Bergen von Geisternetzen.  
Das Projekt wurde im Rahmen einer Fallstudie umgesetzt und verwendet den vorgegebenen Technologiestack mit Java, JSF, CDI-Beans, JPA, WildFly und MySQL.

## Ziel der Anwendung

Die Anwendung unterstützt einen einfachen Prozess zur Erfassung und Bergung von Geisternetzen:

1. Ein Geisternetz kann anonym gemeldet werden.
2. Offene Geisternetze können angezeigt werden.
3. Eine bergende Person kann sich für ein Geisternetz eintragen.
4. Ein übernommenes Geisternetz kann als geborgen markiert werden.
5. Die Zuordnung zwischen Geisternetz und bergender Person kann angezeigt werden.

## Technologiestack

- Java 21
- Maven
- Jakarta EE
- JSF / Jakarta Faces
- CDI-Beans
- JPA / Hibernate
- WildFly EE10
- MySQL
- MySQL Connector/J

## Umgesetzte User Stories

Die folgenden Anforderungen wurden im Prototyp umgesetzt:

| Nr. | User Story | Status |
|---|---|---|
| 1 | Geisternetz anonym melden | umgesetzt |
| 2 | Sich für die Bergung eines Geisternetzes eintragen | umgesetzt |
| 3 | Noch zu bergende Geisternetze anzeigen | umgesetzt |
| 4 | Geisternetz als geborgen melden | umgesetzt |
| 6 | Anzeigen, wer welche Geisternetze bergen möchte | umgesetzt |

Nicht umgesetzt wurden im ersten Prototyp:

| Nr. | User Story |
|---|---|
| 5 | Noch nicht geborgene Netze auf einer Weltkarte anzeigen |
| 7 | Geisternetz als verschollen melden |

Diese Funktionen eignen sich für einen späteren Sprint.

## Projektstruktur
```text
ghost-net-fishing/
├── pom.xml
├── README.md
├── .gitignore
├── src/
│   └── main/
│       ├── java/
│       │   └── de/
│       │       └── ghostnet/
│       │           ├── model/
│       │           │   ├── GhostNet.java
│       │           │   ├── GhostNetStatus.java
│       │           │   └── Person.java
│       │           ├── service/
│       │           │   └── GhostNetService.java
│       │           └── web/
│       │               ├── AssignmentOverviewBean.java
│       │               ├── ClaimGhostNetBean.java
│       │               ├── OpenGhostNetsBean.java
│       │               ├── RecoveryOverviewBean.java
│       │               └── ReportGhostNetBean.java
│       ├── resources/
│       │   └── META-INF/
│       │       └── persistence.xml
│       └── webapp/
│           ├── assignment-overview.xhtml
│           ├── claim.xhtml
│           ├── index.xhtml
│           ├── open-nets.xhtml
│           ├── recovery-overview.xhtml
│           ├── report.xhtml
│           ├── success.xhtml
│           └── WEB-INF/
│               ├── beans.xml
│               └── web.xml
└── docs/
    ├── diagrams/
    └── screenshots/
````
    
## Voraussetzungen


Für die lokale Ausführung werden folgende Komponenten benötigt:

Java 21
Maven
WildFly EE10
MySQL Server
MySQL Connector/J
Git, optional für Versionsverwaltung

## Datenbank einrichten

Die Anwendung verwendet eine MySQL-Datenbank mit dem Namen ghostnet.

Beispielhafte Einrichtung:

CREATE DATABASE ghostnet
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

CREATE USER 'ghostnet_user'@'localhost'
    IDENTIFIED BY 'ghostnet_password';

GRANT ALL PRIVILEGES ON ghostnet.*
    TO 'ghostnet_user'@'localhost';

FLUSH PRIVILEGES;
## WildFly-Datasource

In WildFly muss eine Datasource eingerichtet werden, die auf die MySQL-Datenbank zeigt.

Wichtige Werte:

Feld				| Wert

Datasource Name		| GhostNetDS

JNDI Name			| java:/jdbc/GhostNetDS

Connection URL		| jdbc:mysql://localhost:3306/ghostnet?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC

Benutzer			| ghostnet_user

Passwort			| ghostnet_password

Driver Class		| com.mysql.cj.jdbc.Driver


Der JNDI-Name wird in der Datei persistence.xml verwendet:

<jta-data-source>java:/jdbc/GhostNetDS</jta-data-source>

## Build

Das Projekt wird mit Maven gebaut.

Im Projektordner ausführen:

mvn clean package

Nach erfolgreichem Build wird die WAR-Datei erzeugt:

target/ghost-net-fishing.war

## Deployment

Die erzeugte WAR-Datei wird in den Deployment-Ordner von WildFly kopiert:

WILDFLY_HOME/standalone/deployments/

Beispiel unter Windows:

C:\dev\wildfly-ee-10-40.0.0.Final\standalone\deployments\

Nach erfolgreichem Deployment ist die Anwendung lokal erreichbar unter:

http://localhost:8080/ghost-net-fishing/

## Wichtige Seiten der Anwendung
Seite	Zweck
index.xhtml	Startseite und Navigation
report.xhtml	Geisternetz anonym melden
success.xhtml	Bestätigung nach erfolgreicher Meldung
open-nets.xhtml	Offene Geisternetze anzeigen
claim.xhtml	Bergung eines Geisternetzes übernehmen
recovery-overview.xhtml	Laufende Bergungen anzeigen und als geborgen markieren
assignment-overview.xhtml	Zuordnung zwischen Geisternetz und bergender Person anzeigen

## Datenmodell

Die Anwendung verwendet zwei zentrale JPA-Entities:

## GhostNet

Speichert die Daten eines Geisternetzes:

ID
Breitengrad
Längengrad
geschätzte Größe in Quadratmetern
Status
meldende Person, optional
bergende Person, optional
Erstellungszeitpunkt
Änderungszeitpunkt

## Person

Speichert Personen, die entweder meldende oder bergende Personen sein können:

ID
Name
Telefonnummer

Bei anonymen Meldungen bleibt die meldende Person leer.

## Statuswerte

Ein Geisternetz kann folgende Statuswerte besitzen:

Technischer Wert	Anzeige in der Oberfläche
REPORTED	Gemeldet
RECOVERY_PENDING	Bergung bevorstehend
RECOVERED	Geborgen
LOST	Verschollen

Der Status LOST wurde im Datenmodell vorgesehen, aber im ersten Prototyp nicht als eigene Funktion umgesetzt.

## Testablauf

Ein vollständiger Test kann folgendermaßen durchgeführt werden:

Anwendung starten.
Neues Geisternetz über report.xhtml melden.
Offene Geisternetze über open-nets.xhtml anzeigen.
Für ein gemeldetes Netz die Bergung übernehmen.
Bergungsübersicht öffnen.
Das Netz als geborgen markieren.
Zuordnungsübersicht öffnen.
Daten in MySQL prüfen.

Beispielhafte SQL-Prüfung:

USE ghostnet;

SELECT * FROM ghost_net;

SELECT * FROM person;
## Validierung

Die Eingaben im Meldeformular werden validiert:

Feld	Regel
Breitengrad	Muss zwischen -90 und 90 liegen
Längengrad	Muss zwischen -180 und 180 liegen
Geschätzte Größe	Muss größer als 0 sein

Ungültige Eingaben werden nicht gespeichert und führen zu einer Fehlermeldung in der Oberfläche.

## Hinweise zur Entwicklung

Während der Entwicklung traten unter anderem folgende typische Probleme auf:

WildFly musste in PowerShell mit .\standalone.bat gestartet werden.
Maven musste im Projektordner ausgeführt werden, da dort die pom.xml liegt.
Die WildFly-Datasource musste auf die korrekte Datenbank ghostnet zeigen.
In JSF musste die ID eines Geisternetzes beim Formular-Postback erhalten bleiben.
Für die Bergungsübernahme wurde deshalb eine @ViewScoped Bean verwendet.
Temporäre Debug-Ausgaben wurden nach erfolgreicher Fehlerbehebung wieder entfernt.
Hinweis zu Zugangsdaten

Die in dieser README genannten Zugangsdaten sind nur für die lokale Entwicklungsumgebung vorgesehen.
Für eine produktive Umgebung müssten sichere Passwörter, Umgebungsvariablen und ein geeignetes Berechtigungskonzept verwendet werden.

## Lizenz und Nutzung

Dieses Projekt ist ein Studienprototyp im Rahmen einer Fallstudie. Es ist nicht für den produktiven Einsatz vorgesehen.
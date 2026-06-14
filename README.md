# Ghost Net Fishing

Ghost Net Fishing ist eine prototypische Webanwendung zum Melden, Anzeigen und Bergen von Geisternetzen.  
Das Projekt wurde im Rahmen einer Fallstudie umgesetzt und verwendet den vorgegebenen Technologiestack mit Java, JSF, CDI-Beans, JPA, WildFly und MySQL.

Repository: <https://github.com/Kassy-tech11/ghostnetfishing>

## Ziel der Anwendung

Die Anwendung unterstützt einen einfachen Prozess zur Erfassung und Bergung von Geisternetzen:

1. Ein Geisternetz kann anonym gemeldet werden.
2. Alternativ kann die meldende Person freiwillig Name und Telefonnummer angeben.
3. Offene Geisternetze können angezeigt werden.
4. Eine bergende Person kann sich für ein Geisternetz eintragen.
5. Ein übernommenes Geisternetz kann als geborgen markiert werden.
6. Die Zuordnung zwischen Geisternetz und bergender Person kann angezeigt werden.
7. Für eine bereits geplante Bergung kann eine Übernahmeanfrage gestellt werden.
8. Übernahmeanfragen können angenommen oder abgelehnt werden.

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
- Git / GitHub
- CSS für die Gestaltung der Oberfläche

## Umgesetzte User Stories

Die folgenden Anforderungen aus der Aufgabenstellung wurden im Prototyp umgesetzt:

| Nr. | User Story | Status |
|---|---|---|
| 1 | Geisternetz anonym melden | umgesetzt |
| 2 | Sich für die Bergung eines Geisternetzes eintragen | umgesetzt |
| 3 | Noch zu bergende Geisternetze anzeigen | umgesetzt |
| 4 | Geisternetz als geborgen melden | umgesetzt |
| 6 | Anzeigen, wer welche Geisternetze bergen möchte | umgesetzt |

Zusätzlich wurden folgende Erweiterungen umgesetzt:

| Erweiterung | Beschreibung |
|---|---|
| Optionale meldende Person | Beim Melden eines Geisternetzes können Name und Telefonnummer freiwillig angegeben werden. Die anonyme Meldung bleibt weiterhin möglich. |
| Übernahmeanfragen | Für ein Geisternetz mit Status `RECOVERY_PENDING` kann eine weitere Person die Übernahme der Bergung anfragen. |
| Anfrageverwaltung | Offene Übernahmeanfragen können angezeigt, angenommen oder abgelehnt werden. |
| Einheitliches Styling | Die Oberfläche wurde mit einer zentralen CSS-Datei gestaltet. Buttons, Tabellen und Schriftarten wurden vereinheitlicht. |

Nicht umgesetzt wurden im ersten Prototyp:

| Nr. | User Story |
|---|---|
| 5 | Noch nicht geborgene Netze auf einer Weltkarte anzeigen |
| 7 | Geisternetz als verschollen melden |

Diese Funktionen eignen sich für einen späteren Sprint.

## Projektstamm und Projektstruktur

Der Projektstamm im GitHub-Repository ist die oberste Ebene des Maven-Projekts. Dort liegen die zentralen Projektdateien `pom.xml`, `.gitignore` und `README.md`.  
Der eigentliche Quellcode befindet sich unter dem Stamm `src/main`. Dieser Stamm ist für ein Maven-Webprojekt wichtig, weil dort Java-Code, Ressourcen und Webdateien getrennt abgelegt werden.

```text
ghostnetfishing/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    └── main/
        ├── java/
        │   └── de/
        │       └── ghostnet/
        │           ├── model/
        │           │   ├── GhostNet.java
        │           │   ├── GhostNetStatus.java
        │           │   ├── Person.java
        │           │   ├── RecoveryRequest.java
        │           │   └── RecoveryRequestStatus.java
        │           ├── service/
        │           │   └── GhostNetService.java
        │           └── web/
        │               ├── AssignmentOverviewBean.java
        │               ├── ClaimGhostNetBean.java
        │               ├── OpenGhostNetsBean.java
        │               ├── RecoveryOverviewBean.java
        │               ├── RecoveryRequestOverviewBean.java
        │               ├── ReportGhostNetBean.java
        │               └── TakeoverRequestBean.java
        ├── resources/
        │   └── META-INF/
        │       └── persistence.xml
        └── webapp/
            ├── css/
            │   └── style.css
            ├── assignment-overview.xhtml
            ├── claim.xhtml
            ├── index.xhtml
            ├── open-nets.xhtml
            ├── recovery-overview.xhtml
            ├── recovery-requests.xhtml
            ├── report.xhtml
            ├── success.xhtml
            ├── takeover-request.xhtml
            └── WEB-INF/
                ├── beans.xml
                └── web.xml
```

### Bedeutung der wichtigsten Projektbereiche

| Bereich | Zweck |
|---|---|
| `pom.xml` | Maven-Konfiguration für Build, Packaging und Abhängigkeiten |
| `src/main/java` | Java-Quellcode der Anwendung |
| `src/main/java/de/ghostnet/model` | JPA-Entities und Enums |
| `src/main/java/de/ghostnet/service` | Fachlogik und Datenbankzugriffe über JPA |
| `src/main/java/de/ghostnet/web` | CDI-Beans für die JSF-Seiten |
| `src/main/resources/META-INF/persistence.xml` | JPA-Konfiguration und Verbindung zur WildFly-Datasource |
| `src/main/webapp` | JSF/XHTML-Webseiten der Anwendung |
| `src/main/webapp/css/style.css` | Zentrale CSS-Datei für die Gestaltung |
| `src/main/webapp/WEB-INF/beans.xml` | Aktivierung von CDI |
| `src/main/webapp/WEB-INF/web.xml` | Konfiguration des JSF Faces Servlet |

## Voraussetzungen

Für die lokale Ausführung werden folgende Komponenten benötigt:

- Java 21
- Maven
- WildFly EE10
- MySQL Server
- MySQL Connector/J
- Git, optional für Versionsverwaltung

## Datenbank einrichten

Die Anwendung verwendet eine MySQL-Datenbank mit dem Namen `ghostnet`.

Beispielhafte Einrichtung:

```sql
CREATE DATABASE ghostnet
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

CREATE USER 'ghostnet_user'@'localhost'
    IDENTIFIED BY 'ghostnet_password';

GRANT ALL PRIVILEGES ON ghostnet.*
    TO 'ghostnet_user'@'localhost';

FLUSH PRIVILEGES;
```

## WildFly-Datasource

In WildFly muss eine Datasource eingerichtet werden, die auf die MySQL-Datenbank zeigt.

Wichtige Werte:

| Feld | Wert |
|---|---|
| Datasource Name | `GhostNetDS` |
| JNDI Name | `java:/jdbc/GhostNetDS` |
| Connection URL | `jdbc:mysql://localhost:3306/ghostnet?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC` |
| Benutzer | `ghostnet_user` |
| Passwort | `ghostnet_password` |
| Driver Class | `com.mysql.cj.jdbc.Driver` |

Der JNDI-Name wird in der Datei `persistence.xml` verwendet:

```xml
<jta-data-source>java:/jdbc/GhostNetDS</jta-data-source>
```

## Build

Das Projekt wird mit Maven gebaut.

Im Projektordner ausführen:

```powershell
mvn clean package
```

Nach erfolgreichem Build wird die WAR-Datei erzeugt:

```text
target/ghost-net-fishing.war
```

## Deployment

Die erzeugte WAR-Datei wird in den Deployment-Ordner von WildFly kopiert:

```text
WILDFLY_HOME/standalone/deployments/
```

Beispiel unter Windows:

```text
C:\dev\wildfly-ee-10-40.0.0.Final\standalone\deployments\
```

Nach erfolgreichem Deployment ist die Anwendung lokal erreichbar unter:

```text
http://localhost:8080/ghost-net-fishing/
```

## Wichtige Seiten der Anwendung

| Seite | Zweck |
|---|---|
| `index.xhtml` | Startseite und Navigation |
| `report.xhtml` | Geisternetz anonym oder mit Kontaktdaten melden |
| `success.xhtml` | Bestätigung nach erfolgreicher Meldung |
| `open-nets.xhtml` | Offene Geisternetze anzeigen |
| `claim.xhtml` | Bergung eines freien Geisternetzes übernehmen |
| `recovery-overview.xhtml` | Laufende Bergungen anzeigen und Geisternetze als geborgen markieren |
| `takeover-request.xhtml` | Übernahme einer laufenden Bergung anfragen |
| `recovery-requests.xhtml` | Offene Übernahmeanfragen anzeigen, annehmen oder ablehnen |
| `assignment-overview.xhtml` | Zuordnung zwischen Geisternetz und bergender Person anzeigen |

## Navigation

Die Startseite dient als zentraler Einstiegspunkt in den Prototyp. Von dort aus sind die Hauptseiten erreichbar:

- Geisternetz melden
- Offene Geisternetze anzeigen
- Bergungsübersicht anzeigen
- Zuordnungsübersicht anzeigen
- Übernahmeanfragen anzeigen

Detailseiten wie `claim.xhtml` und `takeover-request.xhtml` werden nicht direkt über die Startseite geöffnet, sondern über eine konkrete Netz-ID aus den jeweiligen Übersichten heraus aufgerufen.

## Datenmodell

Die Anwendung verwendet mehrere zentrale JPA-Entities.

### GhostNet

Speichert die Daten eines Geisternetzes:

- ID
- Breitengrad
- Längengrad
- geschätzte Größe in Quadratmetern
- Status
- meldende Person, optional
- bergende Person, optional
- Erstellungszeitpunkt
- Änderungszeitpunkt

### Person

Speichert Personen, die entweder meldende oder bergende Personen sein können:

- ID
- Name
- Telefonnummer

Bei anonymen Meldungen bleibt die meldende Person leer. Wenn beim Melden Name und Telefonnummer angegeben werden, wird eine Person gespeichert und als meldende Person mit dem Geisternetz verknüpft.

### RecoveryRequest

Speichert Übernahmeanfragen für bereits geplante Bergungen:

- ID
- Geisternetz
- anfragende Person
- Status der Anfrage
- Erstellungszeitpunkt

Dadurch wird die bestehende bergende Person nicht sofort überschrieben. Erst wenn eine Übernahmeanfrage angenommen wird, wird die anfragende Person als neue bergende Person am Geisternetz eingetragen.

## Statuswerte

Ein Geisternetz kann folgende Statuswerte besitzen:

| Technischer Wert | Anzeige in der Oberfläche |
|---|---|
| `REPORTED` | Gemeldet |
| `RECOVERY_PENDING` | Bergung bevorstehend |
| `RECOVERED` | Geborgen |
| `LOST` | Verschollen |

Der Status `LOST` wurde im Datenmodell vorgesehen, aber im ersten Prototyp nicht als eigene Funktion umgesetzt.

Eine Übernahmeanfrage kann folgende Statuswerte besitzen:

| Technischer Wert | Anzeige in der Oberfläche |
|---|---|
| `PENDING` | Offen |
| `ACCEPTED` | Angenommen |
| `REJECTED` | Abgelehnt |

## Styling der Oberfläche

Die Oberfläche wurde mit einer zentralen CSS-Datei gestaltet:

```text
src/main/webapp/css/style.css
```

Die Datei wird in den XHTML-Seiten über einen normalen Stylesheet-Link eingebunden:

```xhtml
<link rel="stylesheet" type="text/css" href="#{request.contextPath}/css/style.css" />
```

Gestaltungsmerkmale:

- weißer Seitenhintergrund
- größere Schrift zur besseren Lesbarkeit
- serifenbasierte Schriftart für Fließtext
- Pristina für Überschriften, falls auf dem System installiert
- Navigationsbuttons in hellen Blau- und Aqua-Tönen
- Aktionsbuttons in Aqua-/Grüntönen
- deaktivierte Buttons in einem ausgegrauten Farbton
- Tabellenkopf mit der Farbe `#ADF8FF`
- Tabellenrahmen in einem hellen Blau-Grau

Verwendete Farbtöne:

| Farbe | Verwendung |
|---|---|
| `#99DDFF` | Navigationsbuttons |
| `#5CC6FF` | Hover-Effekt für Navigationsbuttons |
| `#8EE1D7` | Aktionsbuttons |
| `#7EDDD2` | Hover-Effekt für Aktionsbuttons |
| `#ADF8FF` | Tabellenüberschrift |
| `#B7D7DF` | Rahmen und deaktivierte Elemente |
| `#C2EAFF` | Infoboxen |

## Testablauf

Ein vollständiger Test kann folgendermaßen durchgeführt werden:

1. Anwendung starten.
2. Neues Geisternetz anonym über `report.xhtml` melden.
3. Neues Geisternetz mit Name und Telefonnummer der meldenden Person melden.
4. Offene Geisternetze über `open-nets.xhtml` anzeigen.
5. Für ein gemeldetes Netz die Bergung übernehmen.
6. Bergungsübersicht öffnen.
7. Für ein Netz mit Status `RECOVERY_PENDING` eine Übernahmeanfrage stellen.
8. Übernahmeanfragen über `recovery-requests.xhtml` anzeigen.
9. Eine Übernahmeanfrage annehmen oder ablehnen.
10. Ein Netz als geborgen markieren.
11. Zuordnungsübersicht öffnen.
12. Daten in MySQL prüfen.

Beispielhafte SQL-Prüfung:

```sql
USE ghostnet;

SELECT * FROM ghost_net;

SELECT * FROM person;

SELECT * FROM recovery_request;
```

## Validierung

Die Eingaben im Meldeformular werden validiert:

| Feld | Regel |
|---|---|
| Breitengrad | Muss zwischen `-90` und `90` liegen |
| Längengrad | Muss zwischen `-180` und `180` liegen |
| Geschätzte Größe | Muss größer als `0` sein |
| Meldende Person | Name und Telefonnummer müssen entweder beide leer oder beide ausgefüllt sein |

Ungültige Eingaben werden nicht gespeichert und führen zu einer Fehlermeldung in der Oberfläche.

Auch bei Übernahmeanfragen werden Name und Telefonnummer als Pflichtfelder geprüft.

## Hinweise zur Entwicklung

Während der Entwicklung traten unter anderem folgende typische Probleme auf:

- WildFly musste in PowerShell mit `.\standalone.bat` gestartet werden.
- Maven musste im Projektordner ausgeführt werden, da dort die `pom.xml` liegt.
- Die WildFly-Datasource musste auf die korrekte Datenbank `ghostnet` zeigen.
- In JSF musste die ID eines Geisternetzes beim Formular-Postback erhalten bleiben.
- Für die Bergungsübernahme wurde deshalb eine `@ViewScoped` Bean verwendet.
- In einer JSF-Tabelle durfte die Variable nicht `request` heißen, da dieser Name mit dem HTTP-Request-Objekt kollidierte.
- Temporäre Debug-Ausgaben wurden nach erfolgreicher Fehlerbehebung wieder entfernt.
- Die CSS-Datei wurde über `src/main/webapp/css/style.css` eingebunden, um das Styling einfacher im Browser prüfen zu können.

## Hinweis zu Zugangsdaten

Die in dieser README genannten Zugangsdaten sind nur für die lokale Entwicklungsumgebung vorgesehen.  
Für eine produktive Umgebung müssten sichere Passwörter, Umgebungsvariablen und ein geeignetes Berechtigungskonzept verwendet werden.

## Lizenz und Nutzung

Dieses Projekt ist ein Studienprototyp im Rahmen einer Fallstudie. Es ist nicht für den produktiven Einsatz vorgesehen.

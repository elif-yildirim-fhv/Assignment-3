# Board Microservice

### Technologien
- H2-Datenbank (In-Memory)
- Feign Clients (für Kommunikation mit Player-Service)
- Resilience4j (Circuit Breaker)
- Eureka Service Discovery

## API-Endpunkte

### **POST /api/board/create**
- **Beschreibung**: Erstellt ein neues Spielfeld (10x10).

### **POST /api/board/placeShip**
- **Beschreibung**: Platziert ein Schiff auf dem Spielfeld.

### **POST /api/board/guess**
- **Beschreibung**: Verarbeitet einen Schuss auf dem Spielfeld.

### **POST /api/board/endGame**
- **Beschreibung**: Beendet das Spiel für ein bestimmtes Board.


## Konfiguration
- **Port**: `8083`
- **H2-Datenbank**: `jdbc:h2:mem:boardDb`
    - **Webkonsole**: `http://localhost:8083/h2-console`
    - **Benutzername**: `sa`
    - **Passwort**: `password`

- **Eureka Server**: `http://localhost:8761/eureka`


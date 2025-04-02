# Player Microservice

### Technologien
- H2-Datenbank (In-Memory)
- Resilience4j (Circuit Breaker)
- Eureka Service Discovery


## API-Endpunkte

### **POST /api/player/create**
- **Beschreibung**: Erstellt einen neuen Spieler.

### **GET /api/player/{playerId}**
- **Beschreibung**: Ruft Spielerdaten ab.

### **POST /api/player/{playerId}/updateScore**
- **Beschreibung**: Aktualisiert den Spieler-Score (bei Treffer/Verfehlen).

## Konfiguration
- **Port**: `8082`
- **H2-Datenbank**: `jdbc:h2:mem:playerDb`
    - **Webkonsole**: `http://localhost:8082/h2-console`
    - **Benutzername**: `sa`
    - **Passwort**: `password`

- **Eureka Server**: `http://localhost:8761/eureka`

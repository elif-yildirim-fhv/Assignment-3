 # Game Microservice

### Technologien
- H2-Datenbank (In-Memory)
- Feign Clients 
- Resilience4j (Circuit Breaker)
- Eureka Service Discovery

## API-Endpunkte

### **POST /api/game/create**
- **Beschreibung**: Erstellt ein neues Spiel. .

### **POST /api/game/{gameId}/addPlayer**
- **Beschreibung**: Fügt einen Spieler zu einem bestehenden Spiel hinzu.

### **POST /api/game/{gameId}/end**
- **Beschreibung**: Beendet ein Spiel.

## Konfiguration
- **Port**: `8081`
- **H2-Datenbank**: `jdbc:h2:mem:gameDb`
    - **Webkonsole**: `http://localhost:8081/h2-console`
    - **Benutzername**: `sa`
    - **Passwort**: `password`

- **Eureka Server**: `http://localhost:8761/eureka`

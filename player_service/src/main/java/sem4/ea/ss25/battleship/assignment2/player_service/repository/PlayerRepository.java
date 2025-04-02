package sem4.ea.ss25.battleship.assignment2.player_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sem4.ea.ss25.battleship.assignment2.player_service.domain.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
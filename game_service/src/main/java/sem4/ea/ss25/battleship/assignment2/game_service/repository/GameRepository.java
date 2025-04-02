package sem4.ea.ss25.battleship.assignment2.game_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sem4.ea.ss25.battleship.assignment2.game_service.domain.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}
package sem4.ea.ss25.battleship.assignment2.board_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sem4.ea.ss25.battleship.assignment2.board_service.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
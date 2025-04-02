package sem4.ea.ss25.battleship.assignment2.board_service.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.board_service.domain.Board;
import sem4.ea.ss25.battleship.assignment2.board_service.domain.Cell;
import sem4.ea.ss25.battleship.assignment2.board_service.domain.Ship;
import sem4.ea.ss25.battleship.assignment2.board_service.dto.BoardDTO;
import sem4.ea.ss25.battleship.assignment2.board_service.dto.CellDTO;
import sem4.ea.ss25.battleship.assignment2.board_service.dto.PlayerDTO;
import sem4.ea.ss25.battleship.assignment2.board_service.dto.ShipDTO;
import sem4.ea.ss25.battleship.assignment2.board_service.repository.BoardRepository;
import sem4.ea.ss25.battleship.assignment2.board_service.repository.ShipRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private ShipRepository shipRepository;

	@Autowired
	private PlayerServiceClient playerServiceClient;



	@CircuitBreaker(name = "boardServiceCB", fallbackMethod = "createBoardFallback")
	public BoardDTO createBoard() {
		Board board = new Board();
		board.initializeBoard(10);
		board = boardRepository.save(board);

		return new BoardDTO(
				board.getId(),
				board.getCells().stream()
						.map(cell -> new CellDTO(cell.getX(), cell.getY(), cell.isHasShip(), cell.isHit()))
						.collect(Collectors.toList())
		);
	}

	private BoardDTO createBoardFallback(Exception ex) {
		return new BoardDTO(-1L, List.of());
	}

	@CircuitBreaker(name = "gameServiceCB", fallbackMethod = "placeShipFallback")
	public ShipDTO placeShip(Long boardId, Long playerId, int length, int x, int y, boolean isHorizontal) {
		Board board = boardRepository.findById(boardId)
				.orElseThrow(() -> new RuntimeException("Board not found"));

		Ship ship = board.placeShip(length, x, y, isHorizontal);
		board = boardRepository.save(board);
		ship = shipRepository.save(ship);

		return new ShipDTO(ship.getId(), ship.getLength(), playerId);
	}

	private ShipDTO placeShipFallback(Long boardId, Long playerId, int length,
									  int x, int y, boolean isHorizontal, Exception ex) {
		return new ShipDTO(-1L, length, playerId);
	}

	@CircuitBreaker(name = "playerServiceCB", fallbackMethod = "makeGuessFallback")
	public String makeGuess(Long gameId, Long boardId, Long playerId, Long opponentId, int x, int y) {
		Board board = boardRepository.findById(boardId)
				.orElseThrow(() -> new RuntimeException("Board not found"));

		Cell cell = board.getCells().stream()
				.filter(c -> c.getX() == x && c.getY() == y)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Cell not found"));

		boolean isHit = cell.isHasShip();
		cell.setHit(true);
		boardRepository.save(board);

		if (isHit) {
			playerServiceClient.updateScore(playerId, true);
		}

		return isHit ? "Hit!" : "Miss!";
	}

	private String makeGuessFallback(Long gameId, Long boardId, Long playerId,
									 Long opponentId, int x, int y, Exception ex) {
		return "Service unavailable. Try again later.";
	}

	@CircuitBreaker(name = "boardServiceCB", fallbackMethod = "endGameFallback")
	public String endGame(Long boardId) {
		return "Game ended.";
	}

	private String endGameFallback(Long boardId, Exception ex) {
		return "Game ended due to service unavailability";
	}
}

package sem4.ea.ss25.battleship.assignment2.game_service.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.game_service.client.BoardServiceClient;
import sem4.ea.ss25.battleship.assignment2.game_service.client.PlayerServiceClient;
import sem4.ea.ss25.battleship.assignment2.game_service.domain.Game;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.BoardDTO;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.CellDTO;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.GameDTO;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.PlayerDTO;
import sem4.ea.ss25.battleship.assignment2.game_service.repository.GameRepository;

import java.util.List;

@Service
public class GameService {
	private static final Logger log = LoggerFactory.getLogger(GameService.class);

	@Autowired
	private PlayerServiceClient playerServiceClient;

	@Autowired
	private BoardServiceClient boardServiceClient;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private GameMessageSender gameMessageSender;

	@CircuitBreaker(name = "boardServiceCB", fallbackMethod = "createGameFallback")
	@Transactional
	public GameDTO createGame() {
		BoardDTO board = boardServiceClient.createBoard();
		Game game = new Game();
		game.setBoardId(board.getId());
		game = gameRepository.save(game);

		GameDTO gameDTO = new GameDTO(game.getId(), game.getBoardId(), List.of());
		return gameDTO;
	}

	private GameDTO createGameFallback(Exception ex) {
		log.error("Failed to create game", ex);
		Game game = new Game();
		game.setBoardId(-1L);
		game = gameRepository.save(game);
		return new GameDTO(game.getId(), -1L, List.of());
	}

	@CircuitBreaker(name = "playerServiceCB", fallbackMethod = "addPlayerToGameFallback")
	@Transactional
	public void addPlayerToGame(Long gameId, Long playerId) {
		PlayerDTO playerDTO = playerServiceClient.getPlayer(playerId);
		if (playerDTO == null) {
			throw new RuntimeException("Player not found");
		}

		Game game = gameRepository.findById(gameId)
				.orElseThrow(() -> new RuntimeException("Game not found"));
		game.addPlayerId(playerId);
		gameRepository.save(game);
	}

	private void addPlayerToGameFallback(Long gameId, Long playerId, Exception ex) {
		log.error("Failed to add player {} to game {}", playerId, gameId, ex);
	}

	@CircuitBreaker(name = "boardServiceCB", fallbackMethod = "endGameFallback")
	@Transactional
	public String endGame(Long gameId) {
		Game game = gameRepository.findById(gameId)
				.orElseThrow(() -> new RuntimeException("Game not found"));

		String result = boardServiceClient.endGame(game.getBoardId());
		game.setStatus(Game.GameStatus.FINISHED);
		gameRepository.save(game);

		return result;
	}

	private String endGameFallback(Long gameId, Exception ex) {
		log.error("Failed to end game {}", gameId, ex);
		return "Game ended with fallback due to service unavailability";
	}

	@CircuitBreaker(name = "boardServiceCB", fallbackMethod = "checkGameOverFallback")
	@Transactional
	public boolean checkGameOver(Long gameId) {
		Game game = gameRepository.findById(gameId)
				.orElseThrow(() -> new RuntimeException("Game not found"));

		BoardDTO board = boardServiceClient.getBoard(game.getBoardId());

		// Check if all cells with ships are hit
		boolean allShipsHit = true;
		for (CellDTO cell : board.getCells()) {
			if (cell.isHasShip() && !cell.isHit()) {
				allShipsHit = false;
				break;
			}
		}

		return allShipsHit;
	}

	private boolean checkGameOverFallback(Long gameId, Exception ex) {
		log.error("Failed to check game over for game {}", gameId, ex);
		return false;
	}

	@CircuitBreaker(name = "gameServiceCB", fallbackMethod = "setWinnerFallback")
	@Transactional
	public void setWinner(Long gameId, Long playerId) {
		Game game = gameRepository.findById(gameId)
				.orElseThrow(() -> new RuntimeException("Game not found"));

		game.setWinner(playerId);
		gameRepository.save(game);
	}

	private void setWinnerFallback(Long gameId, Long playerId, Exception ex) {
		log.error("Failed to set winner for game {}", gameId, ex);
	}

	@CircuitBreaker(name = "gameServiceCB", fallbackMethod = "getGameFallback")
	public GameDTO getGame(Long gameId) {
		Game game = gameRepository.findById(gameId)
				.orElseThrow(() -> new RuntimeException("Game not found"));

		return new GameDTO(game.getId(), game.getBoardId(), game.getPlayerIds());
	}

	private GameDTO getGameFallback(Long gameId, Exception ex) {
		log.error("Failed to get game {}", gameId, ex);
		return new GameDTO(-1L, -1L, List.of());
	}
}
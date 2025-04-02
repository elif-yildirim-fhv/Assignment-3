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
import sem4.ea.ss25.battleship.assignment2.game_service.dto.*;
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

	@CircuitBreaker(name = "boardServiceCB", fallbackMethod = "createGameFallback")
	@Transactional
	public GameDTO createGame() {
		BoardDTO board = boardServiceClient.createBoard();
		Game game = new Game();
		game.setBoardId(board.id());
		game = gameRepository.save(game);
		return new GameDTO(game.getId(), game.getBoardId(), List.of());
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

		return board.cells().stream()
				.filter(CellDTO::hasShip)
				.allMatch(CellDTO::isHit);
	}

	private boolean checkGameOverFallback(Long gameId, Exception ex) {
		log.error("Failed to check game over for game {}", gameId, ex);
		return false;
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
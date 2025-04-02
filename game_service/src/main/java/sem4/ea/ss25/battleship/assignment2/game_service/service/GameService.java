package sem4.ea.ss25.battleship.assignment2.game_service.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.game_service.domain.Game;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.BoardDTO;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.GameDTO;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.PlayerDTO;
import sem4.ea.ss25.battleship.assignment2.game_service.repository.GameRepository;

import java.util.List;

@Slf4j
@Service
public class GameService {

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
	public String endGame(Long gameId) {
		return boardServiceClient.endGame(gameId);
	}

	private String endGameFallback(Long gameId, Exception ex) {
		return "Game ended with fallback due to service unavailability";
	}
}
package sem4.ea.ss25.battleship.assignment2.player_service.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.player_service.domain.Player;
import sem4.ea.ss25.battleship.assignment2.player_service.dto.PlayerDTO;
import sem4.ea.ss25.battleship.assignment2.player_service.repository.PlayerRepository;

@Service
@Slf4j
public class PlayerService {

	@Autowired
	private PlayerRepository playerRepository;

	@CircuitBreaker(name = "playerServiceCB", fallbackMethod = "createPlayerFallback")
	public PlayerDTO createPlayer(String name) {
		Player player = new Player(name);
		player = playerRepository.save(player);
		return new PlayerDTO(player.getId(), player.getName(), player.getScore());
	}

	private PlayerDTO createPlayerFallback(String name, Throwable t) {
		log.warn("Create player fallback triggered", t);
		return new PlayerDTO(-1L, "Fallback Player", 0);
	}

	@CircuitBreaker(name = "playerServiceCB", fallbackMethod = "getPlayerFallback")
	public PlayerDTO getPlayer(Long playerId) {
		Player player = playerRepository.findById(playerId)
				.orElseThrow(() -> new RuntimeException("Player not found"));
		return new PlayerDTO(player.getId(), player.getName(), player.getScore());
	}

	private PlayerDTO getPlayerFallback(Long playerId, Throwable t) {
		log.warn("Get player fallback triggered for player {}", playerId, t);
		return new PlayerDTO(-1L, "Fallback Player", 0);
	}

	@CircuitBreaker(name = "playerServiceCB", fallbackMethod = "updateScoreFallback")
	public PlayerDTO updateScore(Long playerId, boolean isHit) {
		Player player = playerRepository.findById(playerId)
				.orElseThrow(() -> new RuntimeException("Player not found"));
		player.updateScore(isHit);
		player = playerRepository.save(player);
		return new PlayerDTO(player.getId(), player.getName(), player.getScore());
	}

	private PlayerDTO updateScoreFallback(Long playerId, boolean isHit, Throwable t) {
		log.warn("Update score fallback triggered for player {}", playerId, t);
		return new PlayerDTO(-1L, "Fallback Player", 0);
	}
}
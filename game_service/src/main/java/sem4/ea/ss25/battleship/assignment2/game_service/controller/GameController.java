package sem4.ea.ss25.battleship.assignment2.game_service.controller;

import sem4.ea.ss25.battleship.assignment2.game_service.dto.GameDTO;
import sem4.ea.ss25.battleship.assignment2.game_service.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

	@Autowired
	private GameService gameService;

	@PostMapping("/create")
	public ResponseEntity<GameDTO> createGame() {
		GameDTO game = gameService.createGame();
		return ResponseEntity.ok(game);
	}

	@PostMapping("/{gameId}/addPlayer")
	public ResponseEntity<Void> addPlayerToGame(@PathVariable Long gameId, @RequestParam Long playerId) {
		gameService.addPlayerToGame(gameId, playerId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{gameId}/end")
	public ResponseEntity<String> endGame(@PathVariable Long gameId) {
		String result = gameService.endGame(gameId);
		return ResponseEntity.ok(result);
	}
}

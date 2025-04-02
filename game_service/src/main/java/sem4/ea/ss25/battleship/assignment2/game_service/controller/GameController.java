package sem4.ea.ss25.battleship.assignment2.game_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sem4.ea.ss25.battleship.assignment2.game_service.service.GameService;
import sem4.ea.ss25.battleship.assignment2.game_service.service.GameMessageSender;

@RestController
@RequestMapping("/api/game")
public class GameController {

	@Autowired
	private GameService gameService;

	@Autowired
	private GameMessageSender gameMessageSender;

	@PostMapping("/create")
	public ResponseEntity<String> createGame() {
		gameMessageSender.sendCreateGameMessage();
		return ResponseEntity.ok("Game creation request sent");
	}

	@PostMapping("/{gameId}/addPlayer")
	public ResponseEntity<Void> addPlayerToGame(
			@PathVariable Long gameId,
			@RequestParam Long playerId) {
		gameService.addPlayerToGame(gameId, playerId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{gameId}/end")
	public ResponseEntity<String> endGame(@PathVariable Long gameId) {
		String result = gameService.endGame(gameId);
		return ResponseEntity.ok(result);
	}
}
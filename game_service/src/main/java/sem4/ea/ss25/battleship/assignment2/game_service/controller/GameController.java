package sem4.ea.ss25.battleship.assignment2.game_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.GameDTO;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.GameMessage;
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
		GameMessage message = new GameMessage();
		message.setType("CREATE_GAME");

		gameMessageSender.sendCreateGameCommand(message);
		return ResponseEntity.accepted().body("Game creation request sent");
	}

	@PostMapping("/{gameId}/addPlayer")
	public ResponseEntity<Void> addPlayerToGame(
			@PathVariable Long gameId,
			@RequestParam Long playerId) {

		GameMessage message = new GameMessage();
		message.setType("ADD_PLAYER");
		message.setGameId(gameId);
		message.setPlayerId(playerId);

		gameMessageSender.sendCreateGameCommand(message);
		return ResponseEntity.accepted().build();
	}

	@PostMapping("/{gameId}/end")
	public ResponseEntity<String> endGame(@PathVariable Long gameId) {
		GameMessage message = new GameMessage();
		message.setType("END_GAME");
		message.setGameId(gameId);

		gameMessageSender.sendCreateGameCommand(message);
		return ResponseEntity.accepted().body("Game end request sent");
	}

	@GetMapping("/{gameId}")
	public ResponseEntity<GameDTO> getGame(@PathVariable Long gameId) {
		GameDTO game = gameService.getGame(gameId);
		return ResponseEntity.ok(game);
	}
}
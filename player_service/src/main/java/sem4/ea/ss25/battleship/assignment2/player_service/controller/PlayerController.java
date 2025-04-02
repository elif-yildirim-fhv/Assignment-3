package sem4.ea.ss25.battleship.assignment2.player_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sem4.ea.ss25.battleship.assignment2.player_service.dto.PlayerDTO;
import sem4.ea.ss25.battleship.assignment2.player_service.domain.PlayerMessage;
import sem4.ea.ss25.battleship.assignment2.player_service.service.PlayerService;
import sem4.ea.ss25.battleship.assignment2.player_service.service.PlayerMessageSender;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

	@Autowired
	private PlayerService playerService;

	@Autowired
	private PlayerMessageSender playerMessageSender;

	@PostMapping("/create")
	public ResponseEntity<String> createPlayer(@RequestParam String name) {
		PlayerMessage message = new PlayerMessage();
		message.setType("CREATE_PLAYER");
		message.setPlayerName(name);

		playerMessageSender.sendCreatePlayerCommand(message);
		return ResponseEntity.accepted().body("Player creation request sent");
	}

	@GetMapping("/{playerId}")
	public ResponseEntity<PlayerDTO> getPlayer(@PathVariable Long playerId) {
		PlayerDTO player = playerService.getPlayer(playerId);
		return ResponseEntity.ok(player);
	}

	@PostMapping("/{playerId}/updateScore")
	public ResponseEntity<String> updateScore(
			@PathVariable Long playerId,
			@RequestParam boolean isHit) {

		PlayerMessage message = new PlayerMessage();
		message.setType("UPDATE_SCORE");
		message.setPlayerId(playerId);
		message.setHit(isHit);

		playerMessageSender.sendCreatePlayerCommand(message);
		return ResponseEntity.accepted().body("Score update request sent");
	}
}
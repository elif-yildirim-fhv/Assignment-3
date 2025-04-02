package sem4.ea.ss25.battleship.assignment2.player_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sem4.ea.ss25.battleship.assignment2.player_service.dto.PlayerDTO;
import sem4.ea.ss25.battleship.assignment2.player_service.service.PlayerService;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
	@Autowired
	private PlayerService playerService;

	@PostMapping("/create")
	public ResponseEntity<PlayerDTO> createPlayer(@RequestParam String name) {
		return ResponseEntity.ok(playerService.createPlayer(name));
	}

	@GetMapping("/{playerId}")
	public ResponseEntity<PlayerDTO> getPlayer(@PathVariable Long playerId) {
		return ResponseEntity.ok(playerService.getPlayer(playerId));
	}

	@PostMapping("/{playerId}/updateScore")
	public ResponseEntity<PlayerDTO> updateScore(@PathVariable Long playerId, @RequestParam boolean isHit) {
		return ResponseEntity.ok(playerService.updateScore(playerId, isHit));
	}
}
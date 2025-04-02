package sem4.ea.ss25.battleship.assignment2.game_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.PlayerDTO;

@FeignClient(name = "player-service")
public interface PlayerServiceClient {

	@GetMapping("/api/player/{playerId}")
	PlayerDTO getPlayer(@PathVariable Long playerId);

	@PostMapping("/api/player/create")
	PlayerDTO createPlayer(@RequestParam String name);

	@PostMapping("/api/player/{playerId}/updateScore")
	PlayerDTO updateScore(@PathVariable Long playerId, @RequestParam boolean isHit);
}
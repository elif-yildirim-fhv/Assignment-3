package sem4.ea.ss25.battleship.assignment2.board_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sem4.ea.ss25.battleship.assignment2.board_service.dto.PlayerDTO;

@FeignClient(name = "player-service")
public interface PlayerServiceClient {
	@GetMapping("/api/player/{playerId}")
	PlayerDTO getPlayer(@PathVariable Long playerId);

	@PostMapping("/api/player/{playerId}/updateScore")
	PlayerDTO updateScore(@PathVariable Long playerId, @RequestParam boolean isHit);
}
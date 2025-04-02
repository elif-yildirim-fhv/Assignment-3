package sem4.ea.ss25.battleship.assignment2.player_service.domain;

import sem4.ea.ss25.battleship.assignment2.player_service.dto.PlayerDTO;

import java.io.Serializable;

public class PlayerMessage implements Serializable {
	private String type;
	private Long playerId;
	private String playerName;
	private boolean isHit;
	private PlayerDTO playerData;

	public PlayerMessage() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public boolean isHit() {
		return isHit;
	}

	public void setHit(boolean hit) {
		isHit = hit;
	}

	public PlayerDTO getPlayerData() {
		return playerData;
	}

	public void setPlayerData(PlayerDTO playerData) {
		this.playerData = playerData;
	}
}
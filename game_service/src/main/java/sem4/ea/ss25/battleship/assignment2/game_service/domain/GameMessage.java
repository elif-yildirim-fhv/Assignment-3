package sem4.ea.ss25.battleship.assignment2.game_service.dto;

import java.io.Serializable;
import java.util.List;

public class GameMessage implements Serializable {
	private String type;
	private Long gameId;
	private Long boardId;
	private Long playerId;
	private GameDTO gameData;
	private List<Long> playerIds;

	public GameMessage() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Long getBoardId() {
		return boardId;
	}

	public void setBoardId(Long boardId) {
		this.boardId = boardId;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public GameDTO getGameData() {
		return gameData;
	}

	public void setGameData(GameDTO gameData) {
		this.gameData = gameData;
	}

	public List<Long> getPlayerIds() {
		return playerIds;
	}

	public void setPlayerIds(List<Long> playerIds) {
		this.playerIds = playerIds;
	}
}
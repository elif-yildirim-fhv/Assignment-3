package sem4.ea.ss25.battleship.assignment2.player_service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Player {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private int score;
	private int wins;
	private int losses;

	public Player() {
	}

	public Player(String name) {
		this.name = name;
		this.score = 0;
		this.wins = 0;
		this.losses = 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public void updateScore(boolean isHit) {
		if (isHit) {
			this.score += 10;
		}
	}

	public void incrementWins() {
		this.wins++;
	}

	public void incrementLosses() {
		this.losses++;
	}
}
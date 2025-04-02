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


	public Player() {}

	public Player(String name) {
		this.name = name;
		this.score = 0; // Standardmäßig ist der Score 0
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

	public void updateScore(boolean isHit) {
		if (isHit) {
			this.score += 1;
		} else {
			this.score = Math.max(this.score - 1, 0);
		}
	}
}
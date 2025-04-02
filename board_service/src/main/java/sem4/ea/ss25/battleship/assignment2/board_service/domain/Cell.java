package sem4.ea.ss25.battleship.assignment2.board_service.domain;


import jakarta.persistence.*;

@Entity
public class Cell {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int x;
	private int y;
	private boolean hasShip;
	private boolean isHit;

	public Cell() {}

	public Cell(int x, int y, boolean hasShip, boolean isHit) {
		this.x = x;
		this.y = y;
		this.hasShip = hasShip;
		this.isHit = isHit;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isHasShip() {
		return hasShip;
	}

	public void setHasShip(boolean hasShip) {
		this.hasShip = hasShip;
	}

	public boolean isHit() {
		return isHit;
	}

	public void setHit(boolean hit) {
		isHit = hit;
	}
}
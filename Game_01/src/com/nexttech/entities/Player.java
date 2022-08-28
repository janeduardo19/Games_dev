package com.nexttech.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.nexttech.main.Game;
import com.nexttech.world.Camera;
import com.nexttech.world.World;

public class Player extends Entity {

	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public double speed = 1.4;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private static double life = 100;
	private static double maxLife = 100;
	public int ammo = 0;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		for(int i = 0; i <= maxIndex; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		for(int i = 0; i <= maxIndex; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
	}
	
	public void update() {
		moved = false;
		if(right && World.isFree((int)(x + speed), this.getY())) {
			dir = right_dir;
			moved = true;
			x+=speed;
		}else if(left && World.isFree((int)(x - speed), this.getY())) {
			dir = left_dir;
			moved = true;
			x-=speed;
		}
		
		if(up && World.isFree(this.getX(), (int)(y - speed))) {
			moved = true;
			y-=speed;
		}else if(down && World.isFree(this.getX(), (int)(y + speed))) {
			moved = true;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		checkCollisionLifepack();
		checkCollisionAmmo();
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
		
	}
	
	public void render(Graphics g) {
		if(dir == right_dir) {
			g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else if(dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
		//g.setColor(Color.BLUE);
		//g.fillRect(getX() + maskx - Camera.x, getY() + masky - Camera.y, mwidth, mheight);
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(Entity.isColliding(this, atual)) {
				if(atual instanceof Bullet) {
					ammo += 10;
					//System.out.println("Munição:" + ammo);
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkCollisionLifepack() {
		for(int i = 0; i < Game.lifepacks.size(); i++) {
			Entity atual = Game.lifepacks.get(i);
			if(Entity.isColliding(this, atual)) {
				setLife(getLife() + 10);
				if(getLife() > 100) {
					setLife(100);
				}
				Game.lifepacks.remove(atual);
				Game.entities.remove(atual);
			}
		}
	}

	public static double getLife() {
		return life;
	}

	public static void setLife(double life) {
		Player.life = life;
	}

	public static double getMaxLife() {
		return maxLife;
	}

	public static void setMaxLife(double maxLife) {
		Player.maxLife = maxLife;
	}

}

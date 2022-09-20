package com.nexttech.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.nexttech.main.Game;
import com.nexttech.world.Camera;
import com.nexttech.world.World;

public class Player extends Entity {

	public boolean right, up, left, down;
	public boolean isDamaged = false;
	public boolean shoot = false;
	public boolean jump = false, isJumping = false;
	public boolean jumpUp = false, jumpDown = false;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public int z = 0;
	public int jumpHeight = 50, jumpCur = 0;
	public int jumpSpd = 2;
	public double speed = 1.4;
	
	private boolean moved = false;
	private boolean hasWeapon = false;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private int damageFrames = 0;
	private double life = 100, maxLife = 100;
	private double mana = 0, maxMana = 100;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage damagePlayer;
	private BufferedImage damageWeapon;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		damagePlayer = Game.spritesheet.getSprite(0, 16, 16, 16);
		damageWeapon = Game.spritesheet.getSprite(16, 16, 16, 16);
		for(int i = 0; i <= maxIndex; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		for(int i = 0; i <= maxIndex; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
	}
	
	public double getLife() {
		return life;
	}

	public void setLife(double life) {
		this.life = life;
	}

	public double getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(double maxLife) {
		this.maxLife = maxLife;
	}
	
	public double getMana() {
		return mana;
	}

	public void setMana(double mana) {
		this.mana = mana;
	}
	
	public double getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(double maxMana) {
		this.maxMana = maxMana;
	}
	
	public void update() {
		if(jump) {
			if(isJumping == false) {
				//System.out.println("isJumping está ativo");
				jump = false;
				isJumping = true;
				jumpUp = true;
			}
		}
		
		if(isJumping == true) {
			if(jumpUp) {
				jumpCur += jumpSpd;
			} else if (jumpDown) {
				jumpCur -= jumpSpd;
				if(jumpCur <= 0) {
					isJumping = false;
					jumpDown = false;
					jumpUp = false;
				}
			}
			z = jumpCur;
			if(jumpCur >= jumpHeight) {
				jumpUp = false;
				jumpDown = true;
			}
		}
		
		moved = false;
		if(right && World.isFree((int)(x + speed), this.getY(), z)) {
			dir = right_dir;
			moved = true;
			x+=speed;
		}else if(left && World.isFree((int)(x - speed), this.getY(), z)) {
			dir = left_dir;
			moved = true;
			x-=speed;
		}
		
		if(up && World.isFree(this.getX(), (int)(y - speed), z)) {
			moved = true;
			y-=speed;
		}else if(down && World.isFree(this.getX(), (int)(y + speed), z)) {
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
		
		checkCollisionLifePotion();
		checkCollisionManaPotion();
		checkCollisionWeapon();
		
		if(isDamaged) {
			damageFrames++;
			if(damageFrames == 6) {
				damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if(shoot && hasWeapon && mana > 0) {
			mana -= 2;
			//Criar bala e atirar
			shoot = false;
			int dx = 0;
			int px = 0;
			int py = 2;
			if(dir == right_dir) {
				px = 8;
				dx = 1;
			} else {
				px = 14;
				dx = -1;
			}
			
			Shoot shootMagic = new Shoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0); 
			Game.shoots.add(shootMagic);
		}
		
		if(life <= 0) {
			/* Game Over! */
			life = 0;
			Game.gameState = "GAME_OVER";
		}
		
		updateCamera();
		
	}
	
	public void render(Graphics g) {
		
		if(!isDamaged) { 
			if(dir == right_dir) {
				if(hasWeapon) {
					//Desenha arma
					g.drawImage(WEAPON_PL, this.getX() + 5 - Camera.x, this.getY() + 1 - Camera.y - z, null);
				}
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			}else if(dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasWeapon) {
					//Desenha arma
					g.drawImage(WEAPON_PL, this.getX() + 5 - Camera.x, this.getY() + 1 - Camera.y - z, null);
				}
			}
		} else {
			g.drawImage(damagePlayer, this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			if(hasWeapon) {
				//Desenha arma
				g.drawImage(damageWeapon, this.getX() + 5 - Camera.x, this.getY() + 1 - Camera.y - z, null);
			}
		}
		if(isJumping) {
			g.setColor(Color.black);
			g.fillOval(this.getX() - Camera.x + 4, this.getY() - Camera.y + 8, 8, 8);
		}
		
		//g.setColor(Color.BLUE);
		//g.fillRect(getX() + maskx - Camera.x, getY() + masky - Camera.y, mwidth, mheight);
	}
	
	public void checkCollisionLifePotion() {
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
	
	public void checkCollisionManaPotion() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(Entity.isColliding(this, atual)) {
				if(atual instanceof ManaPotion) {
					setMana(getMana() + 10);
					if(getMana() > 100) {
						setMana(100);
					}
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkCollisionWeapon() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(Entity.isColliding(this, atual)) {
				if(atual instanceof Weapon) {
					hasWeapon = true;
					setMana(100);
					System.out.println("Pegou a arma!");
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}

}

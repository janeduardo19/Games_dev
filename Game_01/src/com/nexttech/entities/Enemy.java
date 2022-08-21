package com.nexttech.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.nexttech.main.Game;
import com.nexttech.world.Camera;
import com.nexttech.world.World;

public class Enemy extends Entity{

	private double speed = 1;
	//Definições da mascara de colisao
	private int maskx = 6, masky = 6, maskw = 4, maskh = 7;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	public void update() {
		if((int)x < Game.player.getX() 
				&& World.isFree((int)(x+speed), this.getY())
				&& !isColliding((int)(x+speed), this.getY())) {
			x+=speed;
		} else if((int)x > Game.player.getX() 
				&& World.isFree((int)(x-speed), this.getY())
				&& !isColliding((int)(x-speed), this.getY())) {
			x-=speed;
		}
		
		if((int)y < Game.player.getY() 
				&& World.isFree(this.getX(), (int)(y+speed))
				&& !isColliding(this.getX(), (int)(y+speed))) {
			y+=speed;
		} else if((int)y > Game.player.getY() 
				&& World.isFree(this.getX(), (int)(y-speed))
				&& !isColliding(this.getX(), (int)(y-speed))) {
			y-=speed;
		}
	}
	
	public void render(Graphics g) {
		super.render(g);
		//Renderiza mascara atual
		//g.setColor(Color.BLUE);
		//g.fillRect(getX() + maskx - Camera.x, getY() + masky - Camera.y, maskw, maskh);
	}
	
	public boolean isColliding(int xnext, int ynext) {
		Rectangle currentEnemy = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			
			if(e == this)
				continue;
			
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			
			if(currentEnemy.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}
}
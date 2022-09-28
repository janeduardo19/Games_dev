package com.nexttech.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.nexttech.main.Game;
import com.nexttech.world.Camera;
import com.nexttech.world.World;

public class Player extends Entity{
	
	public BufferedImage sprite_left, sprite_up, sprite_down;
	public boolean right,up,left,down;
	public int lastDir = 1;

	public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		sprite_left = Game.spritesheet.getSprite(48, 0, 16, 16);
		sprite_up = Game.spritesheet.getSprite(48, 16, 16, 16);
		sprite_down = Game.spritesheet.getSprite(80, 16, 16, 16);
	}
	
	public void tick(){
		depth = 1;
		if(right && World.isFree((int)(x+speed),this.getY())) {
			x+=speed;
			lastDir = 1;
		}
		else if(left && World.isFree((int)(x-speed),this.getY())) {
			x-=speed;
			lastDir = -1;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed))){
			y-=speed;
			lastDir = -2;
		}
		else if(down && World.isFree(this.getX(),(int)(y+speed))){
			y+=speed;
			lastDir = 2;
		}
	}
	public void render(Graphics g) {
		if(lastDir == 1) {
			super.render(g);
		} else if (lastDir == -1) {
			g.drawImage(sprite_left, this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (lastDir == -2) {
			g.drawImage(sprite_up, this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (lastDir == 2) {
			g.drawImage(sprite_down, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}

}

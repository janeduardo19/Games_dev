package com.nexttech.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.nexttech.main.Game;
import com.nexttech.world.Camera;

public class Shoot extends Entity{
	
	private int dx;
	private int dy;
	private double spd = 4;
	private int life = 60, curLife = 0;
	
	public Shoot(int x, int y, int width, int height, BufferedImage sprite, int dx, int dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		x += dx * spd;
		y += dy * spd;
		curLife++;
		if(curLife == life) {
			Game.shoots.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}
}

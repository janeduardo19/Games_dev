package com.nexttech.graficos;

import java.awt.Color;
import java.awt.Graphics;

import com.nexttech.entities.Player;

public class UI {

	public void render(Graphics g) {	
		g.setColor(Color.red);
		g.fillRect(8, 8, 50, 6);
		g.setColor(Color.green);
		g.fillRect(8, 8, (int)((Player.life/Player.maxLife)*50), 6);
	}
}

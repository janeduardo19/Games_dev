package com.nexttech.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.nexttech.entities.Player;
import com.nexttech.main.Game;

public class UI {

	public void render(Graphics g) {	
		g.setColor(Color.red);
		g.fillRect(8, 8, 80, 8);
		g.setColor(Color.green);
		g.fillRect(8, 8, (int)((Game.player.getLife()/Game.player.getMaxLife())*80), 8);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.setColor(Color.white);
		g.drawString((int)Game.player.getLife() + "/" + (int)Game.player.getMaxLife(), 24, 15);
		g.setColor(Color.red);
		g.fillRect(8, 18, 80, 8);
		g.setColor(Color.BLUE);
		g.fillRect(8, 18, (int)((Game.player.getMana()/Game.player.getMaxMana())*80), 8);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.setColor(Color.white);
		g.drawString((int)Game.player.getMana() + "/" + (int)Game.player.getMaxMana(), 24, 25);
	}
}

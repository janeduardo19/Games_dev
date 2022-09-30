package com.nexttech.graficos;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.nexttech.main.Game;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 18));
		g.drawString("Pontos: " + Game.comida_atual, 30, 25);
	}
	
}

package com.nexttech.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.nexttech.entities.*;
import com.nexttech.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy*map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					switch (pixelAtual) {
						case 0xFF000000:
							//Floor
							tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
							break;
						case 0xFFFFF6F6:
							//Wall
							tiles[xx + (yy * WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
							break;
						case 0xFF091ED9:
							//Player
							Game.player.setX(xx*16);
							Game.player.setY(yy*16);
							Game.player.setMask(3, 0, 9, 16);
							break;
						case 0xFFF80000:
							//Enemy
							Enemy en = new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_EN);
							Game.entities.add(en);
							Game.enemies.add(en);
							break;
						case 0xFF6C6C6C:
							//Weapon
							Weapon weapon = new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN);
							weapon.setMask(0, 6, 16, 4);
							Game.entities.add(weapon);
							break;
						case 0xFFE77878:
							//Life Pack
							LifePotion pack = new LifePotion(xx*16, yy*16, 16, 16, Entity.LIFEPACK_EN);
							pack.setMask(5, 10, 6, 5);
							Game.entities.add(pack);
							Game.lifepacks.add(pack);
							break;
						case 0xFFFFEC07:
							//Bullet
							ManaPotion mana = new ManaPotion(xx*16, yy*16, 16, 16, Entity.BULLET_EN);
							mana.setMask(5, 10, 6, 5);
							Game.entities.add(mana);
							break;
					}
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;
		
		int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;
		
		//Verifica se o espaço ao redor do player está livre
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile)
				|| (tiles[x2 + (y2*World.WIDTH)] instanceof WallTile)
				|| (tiles[x3 + (y3*World.WIDTH)] instanceof WallTile)
				|| (tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	}
	
	public void render(Graphics g) {
		//Inicio camera
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		//Fim camera
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}

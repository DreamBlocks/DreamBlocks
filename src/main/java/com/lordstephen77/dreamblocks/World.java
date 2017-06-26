/*
 _(`-')      (`-')  (`-')  _(`-')  _ <-. (`-')  <-.(`-')                               <-.(`-')  (`-').->
( (OO ).-><-.(OO )  ( OO).-/(OO ).-/    \(OO )_  __( OO)    <-.        .->    _         __( OO)  ( OO)_
 \    .'_ ,------,)(,------./ ,---.  ,--./  ,-.)'-'---.\  ,--. )  (`-')----.  \-,-----.'-'. ,--.(_)--\_)
 '`'-..__)|   /`. ' |  .---'| \ /`.\ |   `.'   || .-. (/  |  (`-')( OO).-.  '  |  .--./|  .'   //    _ /
 |  |  ' ||  |_.' |(|  '--. '-'|_.' ||  |'.'|  || '-' `.) |  |OO )( _) | |  | /_) (`-')|      /)\_..`--.
 |  |  / :|  .   .' |  .--'(|  .-.  ||  |   |  || /`'.  |(|  '__ | \|  |)|  | ||  |OO )|  .   ' .-._)   \
 |  '-'  /|  |\  \  |  `---.|  | |  ||  |   |  || '--'  / |     |'  '  '-'  '(_'  '--'\|  |\   \\       /
 `------' `--' '--' `------'`--' `--'`--'   `--'`------'  `-----'    `-----'    `-----'`--' '--' `-----'
                                     _                    _ ___
                                    |_) _    _ ._  _|  __|_  |o._ _  _
                                    |_)(/_\/(_)| |(_| (_)|   ||| | |(/_
                                          /


Copyright (C) 2017 Stefano Peris

nickname: LordStephen77
eMail: lordstephen77@gmail.com
Github: https://github.com/DreamBlocks

is free software: you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package com.lordstephen77.dreamblocks;

import java.util.Random;

import com.lordstephen77.dreamblocks.Constants.TileID;

public class World implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	public Tile[][] tiles;
	public int width;
	public int height;
	public Int2 spawnLocation;
	
	private int chunkNeedsUpdate;
	private int chunkCount;
	private int chunkWidth = 16;
	private boolean chunkFillRight = true;
	private Random random;
	private long ticksAlive = 0;
	private final int dayLength = 20000;
	private LightingEngine lightingEngineSun;
	private LightingEngine lightingEngineSourceBlocks;
	
	// private int[] columnHeights;
	
	public World(int width, int height, Random random) {
		
		TileID[][] generated = WorldGenerator.generate(width, height, random);
		WorldGenerator.visibility = null;
		this.spawnLocation = WorldGenerator.playerLocation;
		tiles = new Tile[width][height];
		// columnHeights = new int[width];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Tile tile = Constants.tileTypes.get(generated[i][j]);
				if (tile == null) {
					tiles[i][j] = Constants.tileTypes.get(TileID.AIR);
				} else {
					tiles[i][j] = Constants.tileTypes.get(generated[i][j]);
				}
				
			}
		}
		this.width = width;
		this.height = height;
		this.chunkCount = (int) Math.ceil((double) width / chunkWidth);
		this.chunkNeedsUpdate = 0;
		this.random = random;
		lightingEngineSun = new LightingEngine(width, height, tiles, true);
		lightingEngineSourceBlocks = new LightingEngine(width, height, tiles, false);
	}
	
	public void chunkUpdate() {
		ticksAlive++;
		for (int i = 0; i < chunkWidth; i++) {
			boolean isDirectLight = true;
			for (int j = 0; j < height; j++) {
				int x = i + chunkWidth * chunkNeedsUpdate;
				if (x >= width || x < 0) {
					continue;
				}
				int y = j;
				if (!chunkFillRight) {
					x = width - 1 - x;
					y = height - 1 - y;
				}
				if (isDirectLight && tiles[x][y].type.name == TileID.DIRT) {
					if (random.nextDouble() < .005) {
						tiles[x][y] = Constants.tileTypes.get(TileID.GRASS);
					}
				} else if (tiles[x][y].type.name == TileID.GRASS
						&& tiles[x][y - 1].type.name != TileID.AIR
						&& tiles[x][y - 1].type.name != TileID.LEAVES
						&& tiles[x][y - 1].type.name != TileID.WOOD) {
					if (random.nextDouble() < .25) {
						tiles[x][y] = Constants.tileTypes.get(TileID.DIRT);
					}
				} else if (tiles[x][y].type.name == TileID.SAND) {
					if (isAir(x, y + 1) || isLiquid(x, y + 1)) {
						changeTile(x, y + 1, tiles[x][y]);
						changeTile(x, y, Constants.tileTypes.get(TileID.AIR));
					}
				} else if (tiles[x][y].type.name == TileID.SAPLING) {
					if (random.nextDouble() < .01) {
						addTemplate(TileTemplate.tree, x, y);
					}
				} else if (tiles[x][y].type.liquid) {
					if (isAir(x + 1, y)) {
						changeTile(x + 1, y, tiles[x][y]);
					}
					if (isAir(x - 1, y)) {
						changeTile(x - 1, y, tiles[x][y]);
					}
					if (isAir(x, y + 1)) {
						changeTile(x, y + 1, tiles[x][y]);
					}
				}
				if ((!tiles[x][y].type.passable || tiles[x][y].type.liquid)
						&& tiles[x][y].type.name != TileID.LEAVES) {
					isDirectLight = false;
				}
			}
		}
		chunkNeedsUpdate = (chunkNeedsUpdate + 1) % chunkCount;
		if (chunkNeedsUpdate == 0) {
			chunkFillRight = !chunkFillRight;
		}
		
	}
	
	private void addTemplate(TileTemplate tileTemplate, int x, int y) {
		for (int i = 0; i < tileTemplate.template.length; i++) {
			for (int j = 0; j < tileTemplate.template[0].length; j++) {
				if (tileTemplate.template[i][j] != TileID.NONE && x - tileTemplate.spawnY + i >= 0
						&& x - tileTemplate.spawnY + i < tiles.length
						&& y - tileTemplate.spawnX + j >= 0
						&& y - tileTemplate.spawnX + j < tiles[0].length) {
					addTile(x - tileTemplate.spawnY + i, y - tileTemplate.spawnX + j,
							tileTemplate.template[i][j]);
				}
			}
		}
	}
	
	public boolean addTile(Int2 pos, TileID name) {
		return addTile(pos.x, pos.y, name);
	}

	public boolean addTile(int x, int y, TileID name) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}
		Tile tile = Constants.tileTypes.get(name);
		if (tile == null) {
			return false;
		}
		if (name == TileID.SAPLING && y + 1 < height) {
			if (tiles[x][y + 1].type.name != TileID.DIRT
					&& tiles[x][y + 1].type.name != TileID.GRASS) {
				return false;
			}
		}
		tiles[x][y] = tile;
		lightingEngineSun.addedTile(x, y);
		lightingEngineSourceBlocks.addedTile(x, y);
		return true;
	}
	
	public TileID removeTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return TileID.NONE;
		}
		TileID name = tiles[x][y].type.name;
		tiles[x][y] = Constants.tileTypes.get(TileID.AIR);
		lightingEngineSun.removedTile(x, y);
		lightingEngineSourceBlocks.removedTile(x, y);
		return name;
	}
	
	public void changeTile(int x, int y, Tile tile) {
		tiles[x][y] = tile;
		if (tile.type.lightBlocking > 0) {
			lightingEngineSun.addedTile(x, y);
		} else {
			lightingEngineSun.removedTile(x, y);
		}
	}
	
	private TileID[] breakWood = new TileID[] { TileID.WOOD, TileID.PLANK, TileID.CRAFTING_BENCH };
	private TileID[] breakStone = new TileID[] { TileID.STONE, TileID.COBBLE, TileID.COAL_ORE };
	private TileID[] breakMetal = new TileID[] { TileID.IRON_ORE };
	private TileID[] breakDiamond = new TileID[] { TileID.DIAMOND_ORE };
	
	public int breakTicks(int x, int y, Item item) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return Integer.MAX_VALUE;
		}
		TileID currentName = tiles[x][y].type.name;
		
		TileID[] breakType = null; // hand breakable by all
		for (TileID element : breakWood) {
			if (element == currentName) {
				breakType = breakWood;
			}
		}
		for (TileID element : breakStone) {
			if (element == currentName) {
				breakType = breakStone;
			}
		}
		for (TileID element : breakMetal) {
			if (element == currentName) {
				breakType = breakMetal;
			}
		}
		for (TileID element : breakDiamond) {
			if (element == currentName) {
				breakType = breakDiamond;
			}
		}
		if (item == null || item.getClass() != Tool.class) {
			return handResult(breakType);
		}
		Tool tool = (Tool) item;
		if (breakType == breakWood && tool.toolType == Tool.ToolType.Axe) {
			return (int) (getSpeed(tool) * 20);
		} else if (breakType != breakWood && breakType != null
				&& tool.toolType == Tool.ToolType.Pick) {
			return (int) (getSpeed(tool) * 25);
		} else if (breakType == null && tool.toolType == Tool.ToolType.Shovel) {
			return (int) (getSpeed(tool) * 15);
		} else {
			return handResult(breakType);
		}
		
	}
	
	private double getSpeed(Tool tool) {
		// if(tool == null)
		// return 5;
		if (tool.toolPower == Tool.ToolPower.Wood) {
			return 3;
		} else if (tool.toolPower == Tool.ToolPower.Stone) {
			return 2.5;
		} else if (tool.toolPower == Tool.ToolPower.Metal) {
			return 2;
		} else {
			return 1;
		}
	}
	
	private int handResult(TileID[] breakType) {
		if (breakType == null) {
			return 50;
		} else if (breakType == breakWood) {
			return 75;
		} else {
			return 500;
		}
	}
	
	public void draw(GraphicsHandler g, int x, int y, int screenWidth, int screenHeight,
			float cameraX, float cameraY, int tileSize) {
		SpriteStore spriteStore = SpriteStore.get();
		Int2 pos;
		
		pos = StockMethods.computeDrawLocationInPlace(cameraX, cameraY, screenWidth, screenHeight,
				tileSize, 0, height / 2);
		g.setColor(Color.darkGray);
		g.fillRect(pos.x, pos.y, width * tileSize, height * tileSize / 2);
		
		pos = StockMethods.computeDrawLocationInPlace(cameraX, cameraY, screenWidth, screenHeight,
				tileSize, 0, 0);
		g.setColor(getSkyColor());
		g.fillRect(pos.x, pos.y, width * tileSize, height * tileSize / 2 - 1);
		for (int i = 0; i < width; i++) {
			int posX = (int) ((i - cameraX) * tileSize);
			int posY = (int) ((height - cameraY) * tileSize);
			if (posX < 0 - tileSize || posX > screenWidth || posY < 0 - tileSize
					|| posY > screenHeight) {
				continue;
			}
			TileType tileType = Constants.tileTypes.get(TileID.ADMINITE).type;
			Sprite tileSprite = spriteStore.getSprite(tileType.getRef());
			tileSprite.draw(g, posX, posY, tileSize, tileSize);
		}
		
		for (int j = height / 2; j < height; j++) {
			int posX = (int) ((-1 - cameraX) * tileSize);
			int posY = (int) ((j - cameraY) * tileSize);
			if (!(posX < 0 - tileSize || posX > screenWidth || posY < 0 - tileSize || posY > screenHeight)) {
				TileType tileType = Constants.tileTypes.get(TileID.ADMINITE).type;
				Sprite tileSprite = spriteStore.getSprite(tileType.getRef());
				tileSprite.draw(g, posX, posY, tileSize, tileSize);
			}
			
			posX = (int) ((width - cameraX) * tileSize);
			if (!(posX < 0 - tileSize || posX > screenWidth)) {
				TileType tileType = Constants.tileTypes.get(TileID.ADMINITE).type;
				Sprite tileSprite = spriteStore.getSprite(tileType.getRef());
				tileSprite.draw(g, posX, posY, tileSize, tileSize);
			}
		}
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int posX = Math.round(((i - cameraX) * tileSize));
				int posY = Math.round(((j - cameraY) * tileSize));
				if (posX < 0 - tileSize || posX > screenWidth || posY < 0 - tileSize
						|| posY > screenHeight) {
					continue;
				}
				
				int lightIntensity = (int) (getLightValue(i, j) * 255);
				Color tint = new Color(16, 16, 16, 255 - lightIntensity);
				
				if (tiles[i][j].type.name != TileID.AIR) {
					Sprite tileSprite = spriteStore.getSprite(tiles[i][j].type.getRef());
					tileSprite.draw(g, posX, posY, tileSize, tileSize, tint);
				} else {
					g.setColor(tint);
					g.fillRect(posX, posY, tileSize, tileSize);
				}
			}
		}
	}
	
	public boolean passable(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}
		return tiles[x][y].type == null || tiles[x][y].type.passable;
	}
	
	public boolean isLiquid(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}
		return tiles[x][y].type != null && tiles[x][y].type.liquid;
	}
	
	public boolean isAir(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}
		return tiles[x][y].type != null && tiles[x][y].type.name == TileID.AIR;
	}
	
	public boolean isBreakable(int x, int y) {
		return !(isAir(x, y) || isLiquid(x, y));
	}
	
	public boolean isClimbable(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}
		return tiles[x][y].type != null
				&& (tiles[x][y].type.name == TileID.WOOD || tiles[x][y].type.name == TileID.PLANK
						|| tiles[x][y].type.name == TileID.LADDER || tiles[x][y].type.liquid);
	}
	
	public boolean isCraft(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}
		return tiles[x][y].type != null && (tiles[x][y].type.name == TileID.CRAFTING_BENCH);
	}
	
	/**
	 * @return a light value [0,1]
	 **/
	public float getLightValue(int x, int y) {
		if (Constants.DEBUG_VISIBILITY_ON)
			return 1;
		float daylight = getDaylight();
		float lightValueSun = ((float) lightingEngineSun.getLightValue(x, y))
				/ Constants.LIGHT_VALUE_SUN * daylight;
		float lightValueSourceBlocks = ((float) lightingEngineSourceBlocks.getLightValue(x, y))
				/ Constants.LIGHT_VALUE_SUN;
		if (lightValueSun >= lightValueSourceBlocks)
			return lightValueSun;
		return lightValueSourceBlocks;
	}
	
	public float getDaylight() {
		float timeOfDay = getTimeOfDay();
		if (timeOfDay > .4f && timeOfDay < .6f) {
			return 1 - StockMethods.smoothStep(.4f, .6f, timeOfDay);
		} else if (timeOfDay > .9) {
			return StockMethods.smoothStep(.9f, 1.1f, timeOfDay);
		} else if (timeOfDay < .1) {
			return StockMethods.smoothStep(-.1f, .1f, timeOfDay);
		} else if (timeOfDay > .5f) {
			return 0;
		} else {
			return 1;
		}
		
	}
	
	// returns a float in the range [0,1)
	// 0 is dawn, 0.25 is noon, 0.5 is dusk, 0.75 is midnight
	public float getTimeOfDay() {
		return ((float) (ticksAlive % dayLength)) / dayLength;
	}
	
	public boolean isNight() {
		return getTimeOfDay() > 0.5f;
	}
	
	static final Color dawnSky = new Color(255, 217, 92);
	static final Color noonSky = new Color(132, 210, 230);
	static final Color duskSky = new Color(245, 92, 32);
	static final Color midnightSky = new Color(0, 0, 0);
	
	public Color getSkyColor() {
		float time = getTimeOfDay();
		if (time < 0.25f) {
			return dawnSky.interpolateTo(noonSky, 4 * time);
		} else if (time < 0.5f) {
			return noonSky.interpolateTo(duskSky, 4 * (time - 0.25f));
		} else if (time < 0.75f) {
			return duskSky.interpolateTo(midnightSky, 4 * (time - 0.5f));
		} else {
			return midnightSky.interpolateTo(dawnSky, 4 * (time - 0.75f));
		}
	}
	
}

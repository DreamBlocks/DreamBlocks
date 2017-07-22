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
import com.lordstephen77.dreamblocks.light.LightingEngine;

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
	
	// private int[] columnHeights;

	public World(int width, int height, Random random) {
		this.width = width;
		this.height = height;
		this.chunkCount = (int) Math.ceil((double) width / chunkWidth);
		this.chunkNeedsUpdate = 0;
		this.random = random;
	}

	public void fillWith(TileStore tileStore, WorldGenerator worldGenerator){
		TileID[][] generated = worldGenerator.generate();
		this.spawnLocation = worldGenerator.playerLocation;
		tiles = new Tile[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tiles[i][j] = tileStore.make(generated[i][j]);
			}
		}
	}
	
	public void chunkUpdate(LightingEngine sun, LightingEngine sourceBlocks, TileStore tileStore) {
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
				if (isDirectLight && tiles[x][y].type.id == TileID.DIRT) {
					if (random.nextDouble() < .005) {
						tiles[x][y] = tileStore.make(TileID.GRASS);
					}
				} else if (tiles[x][y].type.id == TileID.GRASS
						&& tiles[x][y - 1].type.id != TileID.AIR
						&& tiles[x][y - 1].type.id != TileID.LEAVES
						&& tiles[x][y - 1].type.id != TileID.WOOD) {
					if (random.nextDouble() < .25) {
						tiles[x][y] = tileStore.make(TileID.DIRT);
					}
				} else if (tiles[x][y].type.id == TileID.SAND) {
					if (isAir(x, y + 1) || isLiquid(x, y + 1)) {
						changeTile(x, y + 1, tiles[x][y], sun, sourceBlocks);
						changeTile(x, y, tileStore.make(TileID.AIR), sun, sourceBlocks);
					}
				} else if (tiles[x][y].type.id == TileID.SAPLING) {
					if (random.nextDouble() < .01) {
						addTemplate(TileTemplate.tree, x, y, sun, sourceBlocks, tileStore);
					}
				} else if (tiles[x][y].type.liquid) {
					if (isAir(x + 1, y)) {
						changeTile(x + 1, y, tiles[x][y], sun, sourceBlocks);
					}
					if (isAir(x - 1, y)) {
						changeTile(x - 1, y, tiles[x][y], sun, sourceBlocks);
					}
					if (isAir(x, y + 1)) {
						changeTile(x, y + 1, tiles[x][y], sun, sourceBlocks);
					}
				}
				if ((!tiles[x][y].type.passable || tiles[x][y].type.liquid)
						&& tiles[x][y].type.id != TileID.LEAVES) {
					isDirectLight = false;
				}
			}
		}
		chunkNeedsUpdate = (chunkNeedsUpdate + 1) % chunkCount;
		if (chunkNeedsUpdate == 0) {
			chunkFillRight = !chunkFillRight;
		}
		
	}
	
	private void addTemplate(TileTemplate tileTemplate, int x, int y, LightingEngine sun, LightingEngine sourceBlocks, TileStore tileStore) {
		for (int i = 0; i < tileTemplate.template.length; i++) {
			for (int j = 0; j < tileTemplate.template[0].length; j++) {
				if (tileTemplate.template[i][j] != TileID.NONE && x - tileTemplate.spawnY + i >= 0
						&& x - tileTemplate.spawnY + i < tiles.length
						&& y - tileTemplate.spawnX + j >= 0
						&& y - tileTemplate.spawnX + j < tiles[0].length) {
					addTile(x - tileTemplate.spawnY + i, y - tileTemplate.spawnX + j,
							tileTemplate.template[i][j], sun, sourceBlocks, tileStore);
				}
			}
		}
	}
	
	public boolean addTile(Int2 pos, TileID name, LightingEngine sun, LightingEngine sourceBlocks, TileStore tileStore) {
		return addTile(pos.x, pos.y, name, sun, sourceBlocks, tileStore);
	}

	public boolean addTile(int x, int y, TileID tileID, LightingEngine sun, LightingEngine sourceBlocks, TileStore tileStore) {
		if (isOutOfWorld(x, y)) {
			return false;
		}
		Tile tile = tileStore.make(tileID);
		if (tile == null) {
			return false;
		}
		if (tileID == TileID.SAPLING && y + 1 < height) {
			if (tiles[x][y + 1].type.id != TileID.DIRT
					&& tiles[x][y + 1].type.id != TileID.GRASS) {
				return false;
			}
		}
		tiles[x][y] = tile;
		sun.addedTile(x, y);
		sourceBlocks.addedTile(x, y);
		return true;
	}

	/**
	 * Returns tileID at given position.
	 * If coordinates are out of bounds, then return NONE.
	 * @param x - x of tile
	 * @param y - y of tile
	 * @return instance of TileID
	 */
	public TileID tileIdAt(int x, int y){
		if (isOutOfWorld(x, y)){
			return TileID.NONE;
		}
		return tiles[x][y].type.getId();
	}


	/**
	 * Removes tile at position. Places TYPE_AIR instead. Updates light values
	 * @param pos position of tile to remove
	 * @param sun lighting engine of sun type to update light values
	 * @param sourceBlocks lighting engine of sourceBlocks type to update light values
	 * @return instance of TileType of removed tile
	 */
	public TileType removeTile(Int2 pos, LightingEngine sun, LightingEngine sourceBlocks) {
		if (pos.x < 0 || pos.x >= width || pos.y < 0 || pos.y >= height) {
			return TileStore.TYPE_AIR;
		}
		TileType oldType = tiles[pos.x][pos.y].type;
		if (oldType.id != TileID.NONE){
			tiles[pos.x][pos.y] = TileStore.TILE_AIR;
			sun.removedTile(pos.x, pos.y);
			sourceBlocks.removedTile(pos.x, pos.y);
		}
		return oldType;
	}

	/**
	 * Changes tile at given coordinates. Updates light values.
	 * @param x coordinate X of tile to change
	 * @param y coordinate Y of tile to change
	 * @param tile instance of tile to replace
	 * @param sun lighting engine to update light values
	 * @return instance of TileType of previous tile
	 */
	public TileType changeTile(int x, int y, Tile tile, LightingEngine sun, LightingEngine sourceBlocks) {
		TileType oldType = tiles[x][y].type;
		tiles[x][y] = tile;
		if (tile.type.opacity > 0) {
			sun.addedTile(x, y);
			sourceBlocks.addedTile(x, y);
		} else {
			sun.removedTile(x, y);
			sourceBlocks.removedTile(x, y);
		}
		return oldType;
	}

	/**
	 * Changes tile at given coordinates.
	 * Returns AIR if out of bounds.
	 * @param x coordinate X of tile to change
	 * @param y coordinate Y of tile to change
	 * @param tile instance of tile to replace
	 * @return instance of TileType of previous tile
	 */
	public TileType changeTile(int x, int y, Tile tile) {
		if (isOutOfWorld(x, y)){
			return TileStore.TYPE_AIR;
		}
		TileType oldType = tiles[x][y].type;
		tiles[x][y] = tile;
		return oldType;
	}
	
	private TileID[] breakWood = new TileID[] { TileID.WOOD, TileID.PLANK, TileID.CRAFTING_BENCH };
	private TileID[] breakStone = new TileID[] { TileID.STONE, TileID.COBBLE, TileID.COAL_ORE };
	private TileID[] breakMetal = new TileID[] { TileID.IRON_ORE };
	private TileID[] breakDiamond = new TileID[] { TileID.DIAMOND_ORE };
	
	public int breakTicks(int x, int y, Item item) {
		if (isOutOfWorld(x, y)) {
			return Integer.MAX_VALUE;
		}
		TileID currentName = tiles[x][y].type.id;
		
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
	
	public void draw(GraphicsHandler g, SpriteStore spriteStore, int x, int y, int screenWidth, int screenHeight,
			float cameraX, float cameraY, int tileSize, LightingEngine sun, LightingEngine sourceBlocks, TileStore tileStore) {
		Int2 pos;

		pos = StockMethods.calculatePosition(0, height / 2, cameraX, cameraY, tileSize);
		g.setColor(Color.darkGray);
		g.fillRect(pos.x, pos.y, width * tileSize, height * tileSize / 2);

		pos = StockMethods.calculatePosition(0, 0, cameraX, cameraY, tileSize);
		g.setColor(getSkyColor());
		g.fillRect(pos.x, pos.y, width * tileSize, height * tileSize / 2 - 1);
		for (int i = 0; i < width; i++) {
			int posX = (int) ((i - cameraX) * tileSize);
			int posY = (int) ((height - cameraY) * tileSize);
			if (posX < 0 - tileSize || posX > screenWidth || posY < 0 - tileSize
					|| posY > screenHeight) {
				continue;
			}
			TileType tileType = tileStore.tileTypes.get(TileID.ADMINITE);
			Sprite tileSprite = spriteStore.getSprite(tileType.getSpriteid());
			g.drawImage(tileSprite, posX, posY, tileSize, tileSize);
		}
		
		for (int j = height / 2; j < height; j++) {
			int posX = (int) ((-1 - cameraX) * tileSize);
			int posY = (int) ((j - cameraY) * tileSize);
			if (!(posX < 0 - tileSize || posX > screenWidth || posY < 0 - tileSize || posY > screenHeight)) {
				TileType tileType = tileStore.tileTypes.get(TileID.ADMINITE);
				Sprite tileSprite = spriteStore.getSprite(tileType.getSpriteid());
				g.drawImage(tileSprite, posX, posY, tileSize, tileSize);
			}
			
			posX = (int) ((width - cameraX) * tileSize);
			if (!(posX < 0 - tileSize || posX > screenWidth)) {
				TileType tileType = tileStore.tileTypes.get(TileID.ADMINITE);
				Sprite tileSprite = spriteStore.getSprite(tileType.getSpriteid());
				g.drawImage(tileSprite, posX, posY, tileSize, tileSize);
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
				
				int lightIntensity = (int) (getLightValue(i, j, sun, sourceBlocks) * 255);
				Color tint = new Color(16, 16, 16, 255 - lightIntensity);
				
				if (tiles[i][j].type.id != TileID.AIR) {
					Sprite tileSprite = spriteStore.getSprite(tiles[i][j].type.getSpriteid());
					g.drawImage(tileSprite, posX, posY, tileSize, tileSize, tint);
				} else {
					g.setColor(tint);
					g.fillRect(posX, posY, tileSize, tileSize);
				}
			}
		}
	}
	
	public boolean passable(int x, int y) {
		if (isOutOfWorld(x, y)) {
			return false;
		}
		return tiles[x][y].type == null || tiles[x][y].type.passable;
	}
	
	public boolean isLiquid(int x, int y) {
		if (isOutOfWorld(x, y)) {
			return false;
		}
		return tiles[x][y].type != null && tiles[x][y].type.liquid;
	}
	
	public boolean isAir(int x, int y) {
		if (isOutOfWorld(x, y)) {
			return false;
		}
		return tiles[x][y].type != null && tiles[x][y].type.id == TileID.AIR;
	}
	
	public boolean isBreakable(int x, int y) {
		return !(isAir(x, y) || isLiquid(x, y));
	}

	public boolean isOutOfWorld(int x, int y){
		return x < 0 || x >= width || y < 0 || y >= height;
	}

	public boolean isOutOfWorld(int px, int py, Direction direction){
		int x = px + direction.dx;
		int y = py + direction.dy;
		return isOutOfWorld(x, y);
	}

	public boolean isClimbable(int x, int y) {
		if (isOutOfWorld(x, y)) {
			return false;
		}
		return tiles[x][y].type != null
				&& (tiles[x][y].type.id == TileID.WOOD || tiles[x][y].type.id == TileID.PLANK
						|| tiles[x][y].type.id == TileID.LADDER || tiles[x][y].type.liquid);
	}
	
	public boolean isCraft(int x, int y) {
		if (isOutOfWorld(x, y)) {
			return false;
		}
		return tiles[x][y].type != null && (tiles[x][y].type.id == TileID.CRAFTING_BENCH);
	}
	
	/**
	 * @return a light value [0,1]
	 **/
	public float getLightValue(int x, int y, LightingEngine sun, LightingEngine sourceBlocks) {
		if (Constants.DEBUG_VISIBILITY_ON)
			return 1;
		float daylight = getDaylight();
		float lightValueSun = ((float) sun.getLightValue(x, y))
				/ LightingEngine.DIRECT_SUN * daylight;
		float lightValueSourceBlocks = ((float) sourceBlocks.getLightValue(x, y))
				/ LightingEngine.DIRECT_SUN;
		if (lightValueSun >= lightValueSourceBlocks)
			return lightValueSun;
		return lightValueSourceBlocks;
	}
	
	public float getDaylight() {
		return 1;
//		float timeOfDay = getTimeOfDay();
//		if (timeOfDay > .4f && timeOfDay < .6f) {
//			return 1 - StockMethods.smoothStep(.4f, .6f, timeOfDay);
//		} else if (timeOfDay > .9) {
//			return StockMethods.smoothStep(.9f, 1.1f, timeOfDay);
//		} else if (timeOfDay < .1) {
//			return StockMethods.smoothStep(-.1f, .1f, timeOfDay);
//		} else if (timeOfDay > .5f) {
//			return 0;
//		} else {
//			return 1;
//		}

	}
	
	// returns a float in the range [0,1)
	// 0 is dawn, 0.25 is noon, 0.5 is dusk, 0.75 is midnight
	public float getTimeOfDay() {
		return ((float) (ticksAlive % dayLength)) / dayLength;
	}
	
	public boolean isNight() {
		return getTimeOfDay() > 0.5f;
	}
	
	/**
	 * <p>Progressive change day/night<p>
	 */
	static final Color dawnSky = new Color(255, 217, 92);
	static final Color noonSky = new Color(132, 210, 230);
	static final Color duskSky = new Color(245, 92, 32);
	static final Color midnightSky = new Color(0, 0, 0);
	
	/**
	 * 
	 * @return The day/night cycle resumes
	 */
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

	public Ray<Tile> castRay(int x, int y, Direction dir){
		return new Ray<>(this.tiles, x, y, dir.angleInRad);
	}

	public Ray<Tile> castRay(int x, int y, double angleInRad){
		return new Ray<>(this.tiles, x, y, angleInRad);
	}

}

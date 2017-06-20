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


package com.lgs.dreamblocks;

import java.util.ArrayList;
import java.util.Random;

import com.lgs.dreamblocks.Constants.TileID;

public class WorldGenerator {
	
	public static boolean[][] visibility;
	public static Int2 playerLocation;
	
	public static TileID[][] generate(int width, int height, Random random) {
		TileID[][] world = new TileID[width][height];
		visibility = new boolean[width][height];
		for (int i = 0; i < visibility.length; i++) {
			for (int j = 0; j < visibility[0].length; j++) {
				visibility[i][j] = true;
				world[i][j] = TileID.NONE;
			}
		}
		
		playerLocation = new Int2(width / 2, 5);
		
		int seed = random.nextInt();
		System.out.println("Seed: " + seed);
		random.setSeed(seed);
		int median = (int) (.5 * height);
		
		int minDirtDepth = 2;
		int maxDirtDepth = 5;
		int minSurface = (int) (.25 * height);
		int maxSurface = (int) (.75 * height);
		
		int surface = median;// maxSurface-5;
		int dirtDepth = 3;
		
		ArrayList<Int2> trees = new ArrayList<Int2>();
		
		int surfaceSum = 0;
		
		boolean playerLocFound = false;
		double chance;
		// loop left to right dirt and stone
		for (int i = 0; i < width; i++) {
			if (surface > median) {
				surfaceSum++;
			} else {
				surfaceSum--;
			}
			
			chance = random.nextDouble();
			if (chance > .75) {
				dirtDepth = Math.min(maxDirtDepth, dirtDepth + 1);
			} else if (chance > .5) {
				dirtDepth = Math.max(minDirtDepth, dirtDepth - 1);
			}
			
			chance = random.nextDouble();
			if (chance > .75) {
				surface = Math.min(maxSurface, surface + 1);
			} else if (chance > .5) {
				surface = Math.max(minSurface, surface - 1);
			}
			
			if (surfaceSum > width / 16) {
				surface = Math.min(maxSurface, surface - 3);
			}
			if (surfaceSum < -width / 16) {
				surface = Math.min(maxSurface, surface + 3);
			}
			
			if (random.nextDouble() > .8) {
				trees.add(new Int2(i, surface - 1));
			}
			
			if (i > width / 4 && surface < median && world[i - 1][surface - 1] == TileID.NONE
					&& world[i - 1][surface] == TileID.GRASS && !playerLocFound) {
				playerLocation.x = i;
				playerLocation.y = surface - 2;
				playerLocFound = true;
			}
			
			for (int j = 0; j <= surface; j++) {
				setVisible(i + 1, j);
				setVisible(i, j + 1);
				setVisible(i - 1, j);
				setVisible(i, j - 1);
			}
			
			world[i][surface] = TileID.GRASS;
			for (int j = 1; j <= dirtDepth; j++) {
				world[i][surface + j] = TileID.DIRT;
				visibility[i][surface + j] = false;
			}
			for (int j = dirtDepth; surface + j < height; j++) {
				world[i][surface + j] = TileID.STONE;
				visibility[i][surface + j] = false;
			}
		}
		
		// water
		for (int i = 0; i < width; i++) {
			if (world[i][median] != TileID.NONE) {
				continue;
			}
			
			// flood fill down
			for (int j = median; j < height; j++) {
				// setVisible(i+1,j);
				// setVisible(i,j+1);
				// setVisible(i-1,j);
				// setVisible(i,j-1);
				
				if (world[i][j] != TileID.NONE) {
					carve(world, i, j - 1, 1 + random.nextDouble() * 2, TileID.SAND, new TileID[] {
							TileID.WATER, TileID.NONE }, false);
					break;
				}
				world[i][j] = TileID.WATER;
			}
		}
		
		uniformlyAddMinerals(world, TileID.COAL_ORE, .01f, (int) (height * .4),
				(int) (height * .9), new TileID[] { TileID.DIRT, TileID.SAND, TileID.WATER,
						TileID.NONE }, random);
		
		uniformlyAddMinerals(world, TileID.IRON_ORE, .005f, (int) (height * .5), height,
				new TileID[] { TileID.DIRT, TileID.SAND, TileID.WATER, TileID.NONE }, random);
		
		uniformlyAddMinerals(world, TileID.DIAMOND_ORE, .001f, (int) (height * .9), height,
				new TileID[] { TileID.DIRT, TileID.SAND, TileID.WATER, TileID.NONE }, random);
		
		TileID[] caveIgnore = new TileID[] { TileID.DIRT, TileID.COAL_ORE, TileID.WATER,
				TileID.GRASS, TileID.SAND, TileID.NONE };
		// caves
		int caveCount = (int) (width / 16 + random.nextDouble() * 3);
		for (int i = 0; i < caveCount; i++) {
			int posX = random.nextInt(width);
			int posY = random.nextInt(height / 8) + height * 7 / 8;
			int caveLength = random.nextInt(width);
			int directionX = -1 + random.nextInt(3);
			int directionY = -1 + random.nextInt(3);
			for (int j = 0; j < caveLength; j++) {
				chance = random.nextDouble();
				// change direction
				if (chance > .9) {
					directionX = -1 + random.nextInt(3);
					directionY = -1 + random.nextInt(3);
				}
				posX += directionX + -1 + random.nextInt(3);
				posY += directionY + -1 + random.nextInt(3);
				if (posX < 0 || posX >= width || posY <= median || posY >= height) {
					break;
				}
				double caveSize = 1 + random.nextDouble() * .45;
				carve(world, posX, posY, caveSize, TileID.NONE, caveIgnore, false);
			}
		}
		
		for (Int2 pos : trees) {
			if (world[pos.x][pos.y + 1] == TileID.GRASS) {
				addTemplate(world, TileTemplate.tree, pos);
			}
		}
		
		return world;
	}
	
	// Density [0,1]
	private static void uniformlyAddMinerals(TileID[][] world, TileID mineral, float density,
			int minDepth, int maxDepth, TileID[] ignoreTypes, Random random) {
		int missesAllowed = 100;
		int width = world.length;
		int totalHeight = maxDepth - minDepth;
		int desired = (int) (density * width * totalHeight);
		int added = 0;
		int iterations = 0;
		while (added < desired && added - iterations < missesAllowed) {
			int posX = random.nextInt(width);
			int posY = random.nextInt(totalHeight) + minDepth;
			if (world[posX][posY] == TileID.STONE) {
				double mineralSize = 1 + random.nextDouble() * .6;
				carve(world, posX, posY, mineralSize, mineral, ignoreTypes, false);
				added++;
			}
			iterations++;
		}
	}
	
	private static void setVisible(int x, int y) {
		if (x < 0 || x >= visibility.length || y < 0 || y >= visibility[0].length) {
			return;
		}
		visibility[x][y] = true;
	}
	
	private static void carve(TileID[][] world, int x, int y, double distance, TileID type,
			TileID[] ignoreTypes, boolean left) {
		for (int i = -(int) distance; (!left && i <= (int) distance) || (left && i <= 0); i++) {
			int currentX = x + i;
			if (currentX < 0 || currentX >= world.length) {
				continue;
			}
			for (int j = -(int) distance; j <= (int) distance; j++) {
				int currentY = y + j;
				if (currentY < 0 || currentY >= world[0].length) {
					continue;
				}
				boolean ignoreThis = false;
				for (TileID ignore : ignoreTypes) {
					if (world[currentX][currentY] == ignore) {
						ignoreThis = true;
					}
				}
				if (ignoreThis) {
					continue;
				}
				if (Math.sqrt(i * i + j * j) <= distance) {
					world[currentX][currentY] = type;
				}
			}
		}
	}
	
	private static void addTemplate(TileID[][] world, TileTemplate tileTemplate, Int2 position) {
		for (int i = 0; i < tileTemplate.template.length; i++) {
			for (int j = 0; j < tileTemplate.template[0].length; j++) {
				if (tileTemplate.template[i][j] != TileID.NONE
						&& position.x - tileTemplate.spawnY + i >= 0
						&& position.x - tileTemplate.spawnY + i < world.length
						&& position.y - tileTemplate.spawnX + j >= 0
						&& position.y - tileTemplate.spawnX + j < world[0].length) {
					world[position.x - tileTemplate.spawnY + i][position.y - tileTemplate.spawnX
							+ j] = tileTemplate.template[i][j];
				}
			}
		}
	}
}

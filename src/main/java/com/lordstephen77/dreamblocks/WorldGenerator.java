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

import com.lordstephen77.dreamblocks.Constants.TileID;

import java.util.Random;

public class WorldGenerator {

    private final int minDirtDepth = 2;
    private final int maxDirtDepth = 5;
    private final int minSurface;
    private final int maxSurface;
    private final int width;
    private final int height;
    private final int median;
    private final Random random;

	public Int2 playerLocation;

    public WorldGenerator(int width, int height, Random random){
        this.width = width;
        this.height = height;
        this.minSurface = (int) (.25 * height);
        this.maxSurface = (int) (.75 * height);
        this.median = (int) (.5 * height);
        this.random = random;
    }
	
	public TileID[][] generate() {
		TileID[][] world = new TileID[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				world[i][j] = TileID.NONE;
			}
		}

		playerLocation = new Int2(width / 2, 5);
		
		int surface = median;// maxSurface-5;
		int dirtDepth = 3;
		
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
			
			if (i > width / 4 && surface < median && world[i - 1][surface - 1] == TileID.NONE
					&& world[i - 1][surface] == TileID.GRASS && !playerLocFound) {
				playerLocation.x = i;
				playerLocation.y = surface - 2;
				playerLocFound = true;
			}

			
			world[i][surface] = TileID.GRASS;
			for (int j = 1; j <= dirtDepth; j++) {
				world[i][surface + j] = TileID.DIRT;
			}
			for (int j = dirtDepth; surface + j < height; j++) {
				world[i][surface + j] = TileID.STONE;
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
				carve(world, posX, posY, (int)caveSize, TileID.NONE, caveIgnore);
			}
		}
		
		return world;
	}
	
	// Density [0,1]
	private void uniformlyAddMinerals(TileID[][] world, TileID mineral, float density,
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
				carve(world, posX, posY, (int)mineralSize, mineral, ignoreTypes);
				added++;
			}
			iterations++;
		}
	}

	/**
	 * Replaces tiles around given x and y with tile of "type".
	 * Only if present tile is not in array of ignore types.
	 * @param world - tileID data of the world
	 * @param x - x of center
	 * @param y - y of center
	 * @param distance - distance by x or by y from center
	 * @param type - new type
	 * @param ignoreTypes - old type
	 */
	public void carve(TileID[][] world, int x, int y, int distance, TileID type,
			TileID[] ignoreTypes) {
		for (int i = -distance; i <= distance; i++) {
			int currentX = x + i;
			if (currentX < 0 || currentX >= world.length) {
				continue;
			}
			for (int j = -distance; j <= distance; j++) {
				int currentY = y + j;
				if (currentY < 0 || currentY >= world[0].length) {
					continue;
				}
				boolean ignoreThis = false;
				for (TileID ignore : ignoreTypes) {
					if (world[currentX][currentY] == ignore) {
						ignoreThis = true;
						break;
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
}

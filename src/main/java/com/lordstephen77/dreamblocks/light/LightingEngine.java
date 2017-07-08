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


package com.lordstephen77.dreamblocks.light;

import com.lordstephen77.dreamblocks.Constants;
import com.lordstephen77.dreamblocks.Direction;
import com.lordstephen77.dreamblocks.Tile;
import com.lordstephen77.dreamblocks.World;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public abstract class LightingEngine implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Direction[][] lightFlow;
	
	protected int[][] lightValues;
	protected int width, height;
	protected Tile[][] tiles;
	protected World world;

	
	public int getLightValue(int x, int y) {
		return lightValues[x][y];
	}
	
	public void resetLighting(int x, int y) {
		int left = Math.max(x - Constants.LIGHT_VALUE_SUN, 0);
		int right = Math.min(x + Constants.LIGHT_VALUE_SUN, width - 1);
		int top = Math.max(y - Constants.LIGHT_VALUE_SUN, 0);
		int bottom = Math.min(y + Constants.LIGHT_VALUE_SUN, height - 1);
		List<LightingPoint> sources = new LinkedList<>();
		
		// safely circle around the target zeroed zone
		boolean bufferLeft = (x - Constants.LIGHT_VALUE_SUN > 0);
		boolean bufferRight = (x + Constants.LIGHT_VALUE_SUN < width - 1);
		boolean bufferTop = (y - Constants.LIGHT_VALUE_SUN > 0);
		boolean bufferBottom = (y + Constants.LIGHT_VALUE_SUN < height - 1);
		if (bufferTop) {
			if (bufferLeft) {
				sources.add(getLightingPoint(left - 1, top - 1));
				zeroLightValue(left - 1, top - 1);
			}
			if (bufferRight) {
				sources.add(getLightingPoint(right + 1, top - 1));
				zeroLightValue(right + 1, top - 1);
			}
			for (int i = left; i <= right; i++) {
				sources.add(getLightingPoint(i, top - 1));
				zeroLightValue(i, top - 1);
			}
		}
		if (bufferBottom) {
			if (bufferLeft) {
				sources.add(getLightingPoint(left - 1, bottom + 1));
				zeroLightValue(left - 1, bottom + 1);
			}
			if (bufferRight) {
				sources.add(getLightingPoint(right + 1, bottom + 1));
				zeroLightValue(right + 1, bottom + 1);
			}
			for (int i = left; i <= right; i++) {
				sources.add(getLightingPoint(i, bottom + 1));
				zeroLightValue(i, bottom + 1);
			}
		}
		if (bufferLeft) {
			for (int i = top; i <= bottom; i++) {
				sources.add(getLightingPoint(left - 1, i));
				zeroLightValue(left - 1, i);
			}
		}
		if (bufferRight) {
			for (int i = top; i <= bottom; i++) {
				sources.add(getLightingPoint(right + 1, i));
				zeroLightValue(right + 1, i);
			}
		}
		for (int i = left; i <= right; i++) {
			for (int j = top; j <= bottom; j++) {
				if (lightFlow[i][j] == Direction.SOURCE) {
					sources.add(getLightingPoint(i, j));
				}
				lightValues[i][j] = 0;
			}
		}
		spreadLightingDijkstra(sources);
	}

	public abstract void addedTile(int x, int y);
	public abstract void removedTile(int x, int y);
	
	private void zeroLightValue(int x, int y) {
		lightValues[x][y] = 0;
	}
	
	private LightingPoint getLightingPoint(int x, int y) {
		return new LightingPoint(x, y, lightFlow[x][y], lightValues[x][y]);
	}

	private LightingPoint makeLightingPoint(int x, int y, Direction dir, int lightingValue){
		return new LightingPoint(x + dir.dx, y + dir.dy, dir, lightingValue);
	}

    public List<LightingPoint> getNeighbors(LightingPoint p) {
		if (tiles[p.x][p.y].type.getOpacity() == Constants.OPAQUE) {
			return new LinkedList<>();
		}
		int newValue = p.lightValue - 1 - tiles[p.x][p.y].type.getOpacity();
		LinkedList<LightingPoint> neighbors = new LinkedList<>();

		for(Direction dir : Direction.values()){
			if (!world.isOutOfWorld(p.x, p.y, dir)){
				neighbors.add(makeLightingPoint(p.x, p.y, dir, newValue));
			}
		}
		return neighbors;
	}

	void spreadLightingDijkstra(List<LightingPoint> sources) {
		if (sources.isEmpty())
			return;
		HashSet<LightingPoint> out = new HashSet<>();
		PriorityQueue<LightingPoint> in = new PriorityQueue<>(sources.size(),
                new LightingPoint.LightValueComparator());
		// consider that the input sources are done (this is not a good assumption if different
		// light sources have different values......)
		out.addAll(sources);
		
		in.addAll(sources);
		while (!in.isEmpty()) {
			LightingPoint current = in.poll();
			out.add(current);
			
			if (current.lightValue <= lightValues[current.x][current.y] || current.lightValue < 0) {
				continue;
			}
			lightValues[current.x][current.y] = current.lightValue;
			lightFlow[current.x][current.y] = current.flow;
			if (lightFlow[current.x][current.y] == Direction.SOURCE
					&& current.flow != Direction.SOURCE) {
				System.out.println("There's a bug in the source map!");
			}
			List<LightingPoint> neighbors = getNeighbors(current);
			for (LightingPoint next : neighbors) {
				if (out.contains(next)) {
					continue;
				}
				in.add(next);
			}
		}
	}
}

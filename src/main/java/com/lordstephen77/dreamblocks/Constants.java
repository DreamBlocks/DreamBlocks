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

import java.util.Map;

public class Constants {
	
	public enum TileID {
		DIRT(100), GRASS(0), LEAVES(0), PLANK(112), WOOD(119), STONE(115), AIR(0), WATER(0), SAND(
				110), IRON_ORE(105), COAL_ORE(99), DIAMOND_ORE(109), COBBLE(98), CRAFTING_BENCH(102), ADMINITE(
				0), SAPLING(83), LADDER(76), TORCH(106), NONE(0);
		
		// This is for json to link to... It represents what the item will break into
		public final int id;

		TileID(int id) {
			this.id = id;
		}

		public static TileID of(int code){
			for (TileID id : TileID.values()){
				if (id.id == code){
					return id;
				}
			}
			return NONE;
		}
	}
	
	public static Map<Character, Item> itemTypes;
	static {
		itemTypes = ItemLoader.loadItems(16);
	}
	
	public static final int LIGHT_VALUE_TORCH = 13;
	public static final int LIGHT_VALUE_SUN = 15;
	// not final so that we can set it via command-line arg
	public static boolean DEBUG = false;
	public static final boolean DEBUG_VISIBILITY_ON = false;
	public static final int LIGHT_VALUE_OPAQUE = 10000;
}

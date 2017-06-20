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
Github: https://github.com/LordStephen77/SkyBlocks-Beyond-of-Time

"DreamBlocks - Beyond of Time" is free software: you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

"DreamBlocks - Beyond of Time" is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package com.lgs.dreamblocks;

import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;

public class ItemLoader {
	private static final Gson gson = new Gson();
	
	public static HashMap<Character, Item> loadItems(int size) {
		ToolDefinition[] tools = null;
		ItemDefinition[] items = null;
		// TODO: use the streaming API: https://sites.google.com/site/gson/streaming
		try {
			tools = gson
					.fromJson(StockMethods.readFile("items/tools.json"), ToolDefinition[].class);
			items = gson
					.fromJson(StockMethods.readFile("items/items.json"), ItemDefinition[].class);
		} catch (IOException e) {
		}
		if (tools == null || items == null) {
			System.err.println("Failed to load items from json.");
			System.exit(5);
		}
		
		HashMap<Character, Item> itemTypes = new HashMap<Character, Item>();
		for (ToolDefinition td : tools) {
			itemTypes.put((char) td.item_id, td.makeTool(size));
		}
		for (ItemDefinition id : items) {
			itemTypes.put((char) id.item_id, id.makeItem(size));
		}
		return itemTypes;
	}
}

class ItemDefinition {
	int item_id;
	String name;
	String spriteRef;
	int[][] recipe;
	int yield;
	
	public ItemDefinition(int id, String n, String s, int[][] t, int y) {
		item_id = id;
		name = n;
		spriteRef = s;
		recipe = t;
		yield = y;
	}
	
	public Item makeItem(int size) {
		return new Item(spriteRef, size, item_id, name, recipe, yield);
	}
}

class ToolDefinition extends ItemDefinition {
	Tool.ToolType type;
	Tool.ToolPower power;
	
	public ToolDefinition(int id, String n, String s, int[][] t, int y, Tool.ToolType tt,
			Tool.ToolPower tp) {
		super(id, n, s, t, y);
		type = tt;
		power = tp;
	}
	
	public Tool makeTool(int size) {
		return new Tool(spriteRef, size, item_id, name, recipe, yield, type, power);
	}
}

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

import java.util.Optional;

public class Inventory implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
    private int tileSize = 16;
    private int seperation = 15;
	
	public InventoryItem[][] inventoryItems;
    private InventoryItem[] hotbarRow;
	public int tableSizeAvailable = 2;
	
	private int maxCount = 64;
	private int playerRow;
	private InventoryItem holding = new InventoryItem(null);
    private Int2 clickPos = new Int2(0, 0);
    private int craftingSize = 2;
    private InventoryItem[][] craftingGrid = new InventoryItem[craftingSize][craftingSize];
	public int craftingHeight;
	private char[][] tableTwo = new char[2][2];
	private char[][] tableThree = new char[3][3];
	private InventoryItem craftable = new InventoryItem(null);
	
	public Inventory(int width, int height, int craftingHeight) {
		inventoryItems = new InventoryItem[width][height + craftingHeight];
        hotbarRow = new InventoryItem[width];
        playerRow = height + craftingHeight - 1;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height + craftingHeight; j++) {
				inventoryItems[i][j] = new InventoryItem(null);
			}
			hotbarRow[i] = inventoryItems[i][playerRow];
		}
        for (int rowIdx = 0; rowIdx < craftingSize; rowIdx++){
            for (int colIdx = 0; colIdx < craftingSize; colIdx++){
                craftingGrid[colIdx][rowIdx] = new InventoryItem(null);
            }
        }
		this.craftingHeight = craftingHeight;
	}
	
	public void addItem(Item item, int count) {
		// try active slots
		int itemsToGo = inventoryItems[0][playerRow].add(item, count);
		for (int i = 0; i < inventoryItems.length && itemsToGo > 0; i++) {
			itemsToGo = inventoryItems[i][playerRow].add(item, count);
		}
		
		// try the rest
		for (int i = 0; i < inventoryItems.length && itemsToGo > 0; i++) {
			for (int j = 0; j < inventoryItems[0].length - 1 && itemsToGo > 0; j++) {
				if ((j < craftingHeight && i < inventoryItems.length - tableSizeAvailable)
						|| (craftingHeight != tableSizeAvailable && j == tableSizeAvailable)) {
					continue;
				}
				itemsToGo = inventoryItems[i][playerRow].add(item, count);
			}
		}
	}

	// returns true if the mouse hit in the inventory
	public boolean updateInventory(int screenWidth, int screenHeight,
			Int2 mousePos, boolean leftClick, boolean rightClick) {
        updateCraftingResult();
		int panelWidth = inventoryItems.length * (tileSize + seperation) + seperation;
		int panelHeight = inventoryItems[0].length * (tileSize + seperation) + seperation;
		int x = screenWidth / 2 - panelWidth / 2;
		int y = screenHeight / 2 - panelHeight / 2;

		if (!isMouseInsideInventory(mousePos, x, y, panelWidth, panelHeight)) {
			return false;
		}

		if (leftClick || rightClick) {
            Int2 position = mouseToCoor(mousePos.x - x, mousePos.y - y, seperation, tileSize);
            if (position != null) {
                InventoryItem itemUnderCursor = inventoryItems[position.x][position.y];
                if (holding.isEmpty()) {
                    if (rightClick && itemUnderCursor.count > 1) {
                        pickHalfOfStack(itemUnderCursor);
                    } else {
                        pickWholeStack(itemUnderCursor);
                    }
                } else if (itemUnderCursor.item == null) {
                    if (rightClick) {
                        dropSingleItemToEmptyTile(itemUnderCursor);
                    } else {
                        dropWholeStackToEmptyTile(itemUnderCursor);
                    }
                } else if (holding.item.item_id == itemUnderCursor.item.item_id
                        && itemUnderCursor.count < maxCount) {
                    if ((holding.item.getClass() == Tool.class)
                            || (itemUnderCursor.item.getClass() == Tool.class)) {
                    } else if (rightClick) {
                        dropSingleItemToStack(itemUnderCursor);
                    } else {
                        dropStackToStack(itemUnderCursor);
                    }
                } else {
                    swapItems(itemUnderCursor);
                }
            }

            if (isMouseOverCraftingResult(screenWidth, screenHeight, panelWidth, panelHeight, mousePos)){
                craftItem();
            }
        }
        return true;
	}

    private void pickHalfOfStack(InventoryItem itemUnderCursor){
        holding.item = itemUnderCursor.item;
        holding.count = (int) Math.ceil((double) itemUnderCursor.count / 2);
        itemUnderCursor.count = (int) Math.floor((double) itemUnderCursor.count / 2);
    }

    private void pickWholeStack(InventoryItem itemUnderCursor){
        holding.item = itemUnderCursor.item;
        holding.count = itemUnderCursor.count;
        itemUnderCursor.item = null;
        itemUnderCursor.count = 0;
    }

    private void dropSingleItemToEmptyTile(InventoryItem itemUnderCursor){
        itemUnderCursor.item = holding.item;
        itemUnderCursor.count = 1;
        holding.count--;
        if (holding.count <= 0) {
            holding.item = null;
        }
    }

    private void dropWholeStackToEmptyTile(InventoryItem itemUnderCursor){
        itemUnderCursor.item = holding.item;
        itemUnderCursor.count = holding.count;
        holding.item = null;
        holding.count = 0;
    }

    private void dropSingleItemToStack(InventoryItem itemUnderCursor){
        itemUnderCursor.count++;
        holding.count--;
        if (holding.count <= 0) {
            holding.item = null;
        }
    }

    private void dropStackToStack(InventoryItem itemUnderCursor){
        itemUnderCursor.count += holding.count;
        if (itemUnderCursor.count > maxCount) {
            holding.count = maxCount - itemUnderCursor.count;
            itemUnderCursor.count = maxCount;
        } else {
            holding.item = null;
            holding.count = 0;
        }
    }

    private void swapItems(InventoryItem itemUnderCursor){
        Item item = itemUnderCursor.item;
        int count = itemUnderCursor.count;
        itemUnderCursor.item = holding.item;
        itemUnderCursor.count = holding.count;
        holding.item = item;
        holding.count = count;
    }

    private boolean isMouseInsideInventory(Int2 mousePos, int x, int y, int panelWidth, int panelHeight){
        return x <= mousePos.x && mousePos.x <= x + panelWidth
                && y <= mousePos.y && mousePos.y <= y + panelHeight;
    }

	private boolean isMouseOverCraftingResult(int screenWidth, int screenHeight, int panelWidth, int panelHeight, Int2 mousePos){
	    int x, y;
        x = screenWidth / 2 - panelWidth / 2;
        y = screenHeight / 2 - panelHeight / 2;
        x = x + (inventoryItems.length - tableSizeAvailable - 1) * (tileSize + seperation) - 5;
        y = y + seperation * 2 + tileSize - 5;
	    return mousePos.x >= x && mousePos.x <= x + tileSize + 10 && mousePos.y >= y
                && mousePos.y <= y + tileSize * 2 + 10;
    }

	private Optional<Item> findCraftingResult(){
        char[][] currentTable = computeCraftTable();
        for (Item entry : Constants.itemTypes.values()) {
            if (entry.template.compare(currentTable)) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

    private void takeRecipeMaterials(){
        for (int i = 0; i < tableSizeAvailable; i++) {
            for (int j = 0; j < tableSizeAvailable; j++) {
                inventoryItems[i + inventoryItems.length - tableSizeAvailable][j].count -= 1;
                if (inventoryItems[i + inventoryItems.length - tableSizeAvailable][j].count <= 0) {
                    inventoryItems[i + inventoryItems.length - tableSizeAvailable][j].item = null;
                    inventoryItems[i + inventoryItems.length - tableSizeAvailable][j].count = 0;
                }
            }
        }
    }

    private void craftItem(){
        Optional<Item> recipeResult = findCraftingResult();
        if (recipeResult.isPresent()) {
            if (recipeResult.get().getClass() != Tool.class || holding.isEmpty()) {
                takeRecipeMaterials();
                int count = recipeResult.get().template.outCount;
                holding.add(recipeResult.get().clone(), count);
            }
        }
    }

	private void updateCraftingResult(){
		Optional<Item> nextResult = findCraftingResult();
        if (nextResult.isPresent()){
            craftable.item = nextResult.get();
            craftable.count = nextResult.get().template.outCount;
        } else {
            craftable.item = null;
            craftable.count = 0;
        }
	}
	
	private char[][] computeCraftTable() {
		char[][] currentTable;
		if (tableSizeAvailable == 2) {
			currentTable = tableTwo;
		} else {
			currentTable = tableThree;
		}
		
		for (int i = 0; i < tableSizeAvailable; i++) {
			for (int j = 0; j < tableSizeAvailable; j++) {
				Item item = inventoryItems[i + inventoryItems.length - tableSizeAvailable][j].item;
				if (item != null) {
					currentTable[j][i] = (char) item.item_id;
				} else {
					currentTable[j][i] = (char) 0;
				}
			}
		}
		return currentTable;
	}
	
	// relative x/y in px
	private Int2 mouseToCoor(int x, int y, int seperation, int tileSize) {
		clickPos.x = x / (seperation + tileSize);
		clickPos.y = y / (seperation + tileSize) - 1;
		if (clickPos.x < 0
				|| clickPos.y < 0
				|| clickPos.x >= inventoryItems.length
				|| clickPos.y >= inventoryItems[0].length
				|| ((clickPos.y < craftingHeight && clickPos.x < inventoryItems.length
						- tableSizeAvailable) || (craftingHeight != tableSizeAvailable && clickPos.y == tableSizeAvailable))) {
			return null;
		}
		return clickPos;
	}

	public InventoryItem[] getHotbarRow(){
	    return hotbarRow;
    }

    private void drawPanel(GraphicsHandler g, int x, int y, int panelWidth, int panelHeight){
        g.setColor(Color.gray);
        g.fillRect(x, y, panelWidth, panelHeight);
    }

    private void drawCraftingBackground(GraphicsHandler g, int panelWidth, int x, int y){
        g.setColor(Color.DARK_GRAY);
        int widgetX = x + panelWidth - tableSizeAvailable * (tileSize + seperation) - seperation;
        int widgetWidth = tableSizeAvailable * (tileSize + seperation) + seperation;
        int widgetHeight = tableSizeAvailable * (tileSize + seperation) + seperation;
        g.fillRect(widgetX, y, widgetWidth, widgetHeight);
    }

    private void drawInventoryCell(GraphicsHandler g, int x, int y, InventoryItem item){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x + seperation - 2, y + seperation - 2, tileSize + 4, tileSize + 4);
        item.draw(g, x + seperation, y + seperation, tileSize);
    }

    private void drawCraftingResult(GraphicsHandler g, int x, int y){
        g.setColor(Color.orange);
        x = x + (inventoryItems.length - tableSizeAvailable - 1) * (tileSize + seperation);
        y = y + seperation * 2 + tileSize;
        g.fillRect(x - 5, y - 5, tileSize + 10, tileSize + 10);
        craftable.draw(g, x, y, tileSize);
    }

    private void drawBackpack(GraphicsHandler g, int screenWidth, int screenHeight, int panelWidth, int panelHeight){
        int x;
        int y = screenHeight / 2 - panelHeight / 2 + (tileSize + seperation) * 3;
        for (int rowIdx = 3; rowIdx < inventoryItems[0].length; rowIdx++){
            x = screenWidth / 2 - panelWidth / 2;
            for (int colIdx = 0; colIdx < inventoryItems.length; colIdx++){
                drawInventoryCell(g, x, y, inventoryItems[colIdx][rowIdx]);
                x += tileSize + seperation;
            }
            y += tileSize + seperation;
        }
    }

    private void drawCraftingGrid(GraphicsHandler g, int screenWidth, int screenHeight, int panelWidth, int panelHeight){
        int x;
        int dxInItems = inventoryItems.length - tableSizeAvailable;
        int y = screenHeight / 2 - panelHeight / 2;
        for (int rowIdx = 0; rowIdx < tableSizeAvailable; rowIdx++){
            x = screenWidth / 2 - panelWidth / 2 + (tileSize + seperation) * dxInItems;
            for (int colIdx = dxInItems; colIdx < inventoryItems.length; colIdx++){
                drawInventoryCell(g, x, y, inventoryItems[colIdx][rowIdx]);
                x += tileSize + seperation;
            }
            y += tileSize + seperation;
        }
    }

    public void draw(GraphicsHandler g, int screenWidth, int screenHeight, Int2 mousePos) {
        int panelWidth, panelHeight, x, y;

        panelWidth = inventoryItems.length * (tileSize + seperation) + seperation;
        panelHeight = inventoryItems[0].length * (tileSize + seperation) + seperation;
        x = screenWidth / 2 - panelWidth / 2;
        y = screenHeight / 2 - panelHeight / 2;

        drawPanel(g, x, y, panelWidth, panelHeight);
        drawCraftingBackground(g, panelWidth, x, y);
        drawCraftingGrid(g, screenWidth, screenHeight, panelWidth, panelHeight);
        drawBackpack(g, screenWidth, screenHeight, panelWidth, panelHeight);
        drawCraftingResult(g, x, y);

        int holdingX = mousePos.x - tileSize / 2;
        int holdingY = mousePos.y - tileSize - tileSize / 2;
        holding.draw(g, holdingX, holdingY, tileSize);
    }
}

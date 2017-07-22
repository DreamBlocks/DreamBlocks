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

import com.lordstephen77.dreamblocks.ui.CraftingGrid;
import com.lordstephen77.dreamblocks.ui.InventorySlot;
import com.lordstephen77.dreamblocks.ui.Resizable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>Property of the inventory</p>
 * @author Stefano Peris
 * @version 0.3
 */
public class Inventory implements java.io.Serializable, Resizable {
	private static final long serialVersionUID = 1L;
	
	// Size inventory panel and block cells
    private int tileSize = 25; // Cells size
    private int seperation = 20; // Inventory size

	public InventoryItem[][] inventoryItems;
	private List<InventorySlot> slotList;
    private InventoryItem[] hotbarRow;
    private int x;
    private int y;
    private int panelWidth;
    private int panelHeight;

	private int playerRow;
	private InventoryItem holding = new InventoryItem();
    private CraftingGrid craftingGrid;
	public int craftingHeight;
	private InventoryItem craftable = new InventoryItem();
	
	public Inventory(int width, int height, int craftingHeight) {
		inventoryItems = new InventoryItem[width][height + craftingHeight];
        panelWidth = inventoryItems.length * (tileSize + seperation) + seperation;
        panelHeight = inventoryItems[0].length * (tileSize + seperation) + seperation;
        hotbarRow = new InventoryItem[width];
        slotList = new ArrayList<>();
        playerRow = height + craftingHeight - 1;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height + craftingHeight; j++) {
				inventoryItems[i][j] = new InventoryItem();
			}
			hotbarRow[i] = inventoryItems[i][playerRow];
		}
		for (int i = 0; i < width; i++){
		    for (int j = craftingHeight; j < height + craftingHeight; j++){
                slotList.add(new InventorySlot(
                    this.x + i * (tileSize + seperation) + seperation,
                    this.y + j * (tileSize + seperation) + seperation,
                    tileSize,
                    tileSize,
                    inventoryItems[i][j]
                ));
            }
        }
        int craftingX = this.x + panelWidth - 2 * (tileSize + seperation) - seperation;
		craftingGrid = new CraftingGrid(craftingX, y, tileSize, seperation);
        craftingGrid.setInventoryItems(inventoryItems);

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
				if ((j < craftingHeight && i < inventoryItems.length - craftingGrid.getTableSizeAvailable())
						|| (craftingHeight != craftingGrid.getTableSizeAvailable() && j == craftingGrid.getTableSizeAvailable())) {
					continue;
				}
				itemsToGo = inventoryItems[i][playerRow].add(item, count);
			}
		}
	}

    @Override
    public void resize(int screenWidth, int screenHeight){
	    int oldX = this.x;
	    int oldY = this.y;
        this.x = screenWidth / 2 - panelWidth / 2;
        this.y = screenHeight / 2 - panelHeight / 2;
        this.craftingGrid.move(this.x - oldX, this.y - oldY);
        for (InventorySlot slot : slotList) {
            slot.move(this.x - oldX, this.y - oldY);
        }
    }

	// returns true if the mouse hit in the inventory
	public boolean handleClick(Int2 mousePos, boolean leftClick, boolean rightClick) {
		if (!isMouseInsideInventory(mousePos)) {
			return false;
		}

        if (leftClick){
		    InventoryItem itemUnderCursor = getItemUnderCursor(mousePos);
		    if(itemUnderCursor != null){
		        itemUnderCursor.handleLeftClick(holding);
            }
            if (isMouseOverCraftingResult(mousePos)){
                craftItem();
            }
        }
		if (rightClick) {
            InventoryItem itemUnderCursor = getItemUnderCursor(mousePos);
            if(itemUnderCursor != null){
                itemUnderCursor.handleRightClick(holding);
            }
        }
        return true;
	}

	private InventoryItem getItemUnderCursor(Int2 mousePos){
        int correctionY = 16;//for some reason, y of mousepos is greater than actual y for this amount
        int x = mousePos.x;
        int y = mousePos.y - correctionY;
        for (InventorySlot slot : slotList) {
            if(slot.isInside(x, y)){
                return slot.getStack();
            }
        }
        return null;
    }

    private boolean isMouseInsideInventory(Int2 mousePos){
        return x <= mousePos.x && mousePos.x <= x + panelWidth
                && y <= mousePos.y && mousePos.y <= y + panelHeight;
    }

	private boolean isMouseOverCraftingResult(Int2 mousePos){
        int widgetX = x + (inventoryItems.length - craftingGrid.getTableSizeAvailable() - 1) * (tileSize + seperation) - 5;
        int widgetY = y + seperation * 2 + tileSize - 5;
	    return widgetX <= mousePos.x && mousePos.x <= widgetX + tileSize + 10
            && widgetY <= mousePos.y && mousePos.y <= widgetY + tileSize * 2 + 10;
    }


    private void craftItem(){
        Optional<Item> recipeResult = craftingGrid.getResult();
        if (recipeResult.isPresent()) {
            if (recipeResult.get().getClass() != Tool.class || holding.isEmpty()) {
                craftingGrid.takeRecipeMaterials();
                int count = recipeResult.get().template.outCount;
                holding.add(recipeResult.get().clone(), count);
            }
        }
    }

	private void updateCraftingResult(){
		Optional<Item> nextResult = craftingGrid.getResult();
        if (nextResult.isPresent()){
            craftable.item = nextResult.get();
            craftable.count = nextResult.get().template.outCount;
        } else {
            craftable.item = null;
            craftable.count = 0;
        }
	}

    public InventoryItem[] getHotbarRow(){
	    return hotbarRow;
    }

    private void drawPanel(GraphicsHandler g){
        g.setColor(Color.gray);
        g.fillRect(x, y, panelWidth, panelHeight);
    }

    private void drawCraftingResult(GraphicsHandler g, SpriteStore spriteStore){
        g.setColor(Color.orange);
        int x = this.x + (inventoryItems.length - craftingGrid.getTableSizeAvailable() - 1) * (tileSize + seperation);
        int y = this.y + seperation * 2 + tileSize;
        g.fillRect(x - 5, y - 5, tileSize + 10, tileSize + 10);
        craftable.draw(g, spriteStore, x - seperation, y - seperation, tileSize, seperation);
    }

    private void drawBackpack(GraphicsHandler g, SpriteStore spriteStore){
        for (InventorySlot slot : slotList) {
            slot.draw(g, spriteStore);
        }
    }

    public void draw(GraphicsHandler g, SpriteStore spriteStore, Int2 mousePos) {
        updateCraftingResult();
        drawPanel(g);
        craftingGrid.draw(g);
        drawBackpack(g, spriteStore);
        drawCraftingResult(g, spriteStore);

        int holdingX = mousePos.x - tileSize / 2;
        int holdingY = mousePos.y - tileSize - tileSize / 2;
        if(!holding.isEmpty()) {
            Sprite sprite = spriteStore.getSprite(holding.getItem().getSpriteId());
            g.drawImage(sprite, holdingX, holdingY, tileSize, tileSize);
        }
    }

    public void setTableSizeAvailable(int tableSizeAvailable) {
        this.craftingGrid.setTableSizeAvailable(tableSizeAvailable);
    }
}

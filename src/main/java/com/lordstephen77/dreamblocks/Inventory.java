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
    private int widthInSlots;
    private int heightInSlots;

	private InventoryItem holding = new InventoryItem();
    private CraftingGrid playerGrid;
    private CraftingGrid tableGrid;
    private CraftingGrid currentGrid;
	
	public Inventory(int widthInSlots, int heightInSlots) {
	    this.widthInSlots = widthInSlots;
	    this.heightInSlots = heightInSlots;
		inventoryItems = new InventoryItem[widthInSlots][heightInSlots];
        hotbarRow = new InventoryItem[widthInSlots];
        slotList = new ArrayList<>();
		for (int i = 0; i < widthInSlots; i++) {
			for (int j = 0; j < heightInSlots; j++) {
				inventoryItems[i][j] = new InventoryItem();
			}
			hotbarRow[i] = inventoryItems[i][heightInSlots - 1];
		}
        for (int j = heightInSlots - 1; j > 0; j--){
		    for (int i = 0; i < widthInSlots; i++){
                slotList.add(new InventorySlot(
                    this.x + i * (tileSize + seperation) + seperation,
                    this.y + j * (tileSize + seperation) + seperation,
                    tileSize,
                    tileSize,
                    inventoryItems[i][j]
                ));
            }
        }
        int craftingX = this.x + getPanelWidthInPx() - 2 * (tileSize + seperation) - seperation;
		playerGrid = new CraftingGrid(craftingX, y, tileSize, seperation, 2);

        craftingX = this.x + getPanelWidthInPx() - 3 * (tileSize + seperation) - seperation;
        tableGrid = new CraftingGrid(craftingX, y, tileSize, seperation, 3);

        currentGrid = playerGrid;
        moveSlots(0, currentGrid.getHeight());
	}
	
	public int addItem(Item item, int count) {
	    int leftover = count;
	    int i = 0;
	    while(leftover > 0 && i < slotList.size()){
	        InventoryItem stack = slotList.get(i).getStack();
	        leftover = stack.add(item, leftover);
	        i++;
        }
        return leftover;
	}

    @Override
    public void resize(int screenWidth, int screenHeight){
	    int oldX = this.x;
	    int oldY = this.y;
        this.x = screenWidth / 2 - getPanelWidthInPx() / 2;
        this.y = screenHeight / 2 - getPanelHeightInPx() / 2;
        int dx = this.x - oldX;
        int dy = this.y - oldY;
        this.playerGrid.move(dx, dy);
        this.tableGrid.move(dx, dy);
        moveSlots(dx, dy);
    }

	// returns true if the mouse hit in the inventory
	public boolean handleClick(Int2 mousePos, boolean leftClick, boolean rightClick) {
		if (!isMouseInsideInventory(mousePos)) {
			return false;
		}

        if (leftClick){
            for (InventorySlot slot : slotList) {
                slot.handleLeftClick(mousePos, holding);
            }
            currentGrid.handleLeftClick(mousePos, holding);
        }
		if (rightClick) {
            for (InventorySlot slot : slotList) {
                slot.handleRightClick(mousePos, holding);
            }
            currentGrid.handleRightClick(mousePos, holding);
        }
        return true;
	}

    private boolean isMouseInsideInventory(Int2 mousePos){
        return x <= mousePos.x && mousePos.x <= x + getPanelWidthInPx()
                && y <= mousePos.y && mousePos.y <= y + getPanelHeightInPx();
    }

    public InventoryItem[] getHotbarRow(){
	    return hotbarRow;
    }

    private void drawPanel(GraphicsHandler g){
        g.setColor(Color.gray);
        g.fillRect(x, y, getPanelWidthInPx(), getPanelHeightInPx());
    }

    private void drawBackpack(GraphicsHandler g, SpriteStore spriteStore){
        for (InventorySlot slot : slotList) {
            slot.draw(g, spriteStore);
        }
    }

    public void draw(GraphicsHandler g, SpriteStore spriteStore, Int2 mousePos) {
        drawPanel(g);
        currentGrid.draw(g, spriteStore);
        drawBackpack(g, spriteStore);

        int holdingX = mousePos.x - tileSize / 2;
        int holdingY = mousePos.y - tileSize / 2;
        if(!holding.isEmpty()) {
            Sprite sprite = spriteStore.getSprite(holding.getItem().getSpriteId());
            g.drawImage(sprite, holdingX, holdingY, tileSize, tileSize);
        }
    }

    public void usePlayerGrid(){
        int oldY = currentGrid.getHeight();
        currentGrid = playerGrid;
        int dy = currentGrid.getHeight() - oldY;
        moveSlots(0, dy);
    }

    public void useTableGrid(){
        int oldY = currentGrid.getHeight();
        currentGrid = tableGrid;
        int dy = currentGrid.getHeight() - oldY;
        moveSlots(0, dy);
    }

    private void moveSlots(int dx, int dy){
        for (InventorySlot slot : slotList) {
            slot.move(dx, dy);
        }
    }

    public int getPanelHeightInPx() {
        int inventoryHeightInPx = heightInSlots * (tileSize + seperation) + seperation;
        return currentGrid.getHeight() + inventoryHeightInPx;
    }

    public int getPanelWidthInPx(){
        return widthInSlots * (tileSize + seperation) + seperation;
    }
}

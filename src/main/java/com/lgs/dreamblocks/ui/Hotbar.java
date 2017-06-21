package com.lgs.dreamblocks.ui;

import com.lgs.dreamblocks.Color;
import com.lgs.dreamblocks.GraphicsHandler;
import com.lgs.dreamblocks.Inventory;
import com.lgs.dreamblocks.InventoryItem;

/**
 * Created by Александр on 21.06.2017.
 */
public class Hotbar {
    private int hotbarIdx;
    private InventoryItem[] hotbarRow;
    private int tileSize;
    private int margin;
    private int width;
    private int height;

    public Hotbar(int tileSize, int margin){
        this.tileSize = tileSize;
        this.margin = margin;
    }

    public void setInventory(Inventory inventory) {
        this.hotbarRow = inventory.getHotbarRow();
        this.width = hotbarRow.length * (tileSize + margin) + margin;
        this.height = tileSize + margin * 2;
        this.hotbarIdx = 0;
    }

    public void moveSelectionRight(int value){
        int oldValue = hotbarIdx;
        hotbarIdx = (hotbarRow.length + hotbarIdx + value) % hotbarRow.length;
        System.out.println("moved from " + oldValue + " to " + hotbarIdx);
    }

    public void decreaseSelected(int count){
        hotbarRow[hotbarIdx].remove(count);
    }

    public InventoryItem getSelected(){
        return hotbarRow[hotbarIdx];
    }

    public void setHotbarIdx(int hotbarIdx) {
        this.hotbarIdx = hotbarIdx;
    }

    public void draw(GraphicsHandler g, int screenWidth, int screenHeight){
        int x = screenWidth / 2 - width / 2;
        int y = screenHeight - height - margin;

        g.setColor(Color.gray);
        g.fillRect(x, y, width, height);
        for (int j = 0; j < hotbarRow.length; j++) {
            InventoryItem current = hotbarRow[j];
            if (hotbarIdx == j) {
                g.setColor(Color.blue);
                g.fillRect(x + margin - 2, y + margin - 2, tileSize + 4, tileSize + 4);
            }
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x + margin, y + margin, tileSize, tileSize);

            current.draw(g, x + margin, y + margin, tileSize);
            x += tileSize + margin;
        }
    }
}

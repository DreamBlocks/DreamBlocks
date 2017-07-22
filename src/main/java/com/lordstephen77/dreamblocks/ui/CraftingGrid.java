package com.lordstephen77.dreamblocks.ui;

import com.lordstephen77.dreamblocks.*;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by Александр on 25.06.2017.
 */
public class CraftingGrid implements Serializable{
    private static final long serialVersionUID = 1L;

    private SpriteStore spriteStore;
    private InventoryItem[][] inventoryItems;
    private int tableSizeAvailable = 2;
    private int x;
    private int y;
    private int panelWidth;
    private int panelHeight;
    private int tileSize;
    private int seperation;
    private char[][] tableTwo = new char[2][2];
    private char[][] tableThree = new char[3][3];

    public CraftingGrid(int tileSize, int seperation, int panelWidth, int panelHeight){
        this.spriteStore = SpriteStore.get();
        this.tileSize = tileSize;
        this.seperation = seperation;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }

    public int getTableSizeAvailable() {
        return tableSizeAvailable;
    }

    public void setTableSizeAvailable(int tableSizeAvailable) {
        this.tableSizeAvailable = tableSizeAvailable;
    }

    public void setInventoryItems(InventoryItem[][] inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public void resize(int screenWidth, int screenHeight){
        x = screenWidth / 2 - panelWidth / 2;
        y = screenHeight / 2 - panelHeight / 2;
    }

    public void draw(GraphicsHandler g){
        drawBackground(g);
        int x;
        int dxInItems = inventoryItems.length - tableSizeAvailable;
        int y = this.y;
        for (int rowIdx = 0; rowIdx < tableSizeAvailable; rowIdx++){
            x = this.x + (tileSize + seperation) * dxInItems;
            for (int colIdx = dxInItems; colIdx < inventoryItems.length; colIdx++){
                inventoryItems[colIdx][rowIdx].draw(g, spriteStore, x, y, tileSize, seperation);
                x += tileSize + seperation;
            }
            y += tileSize + seperation;
        }
    }

    public Optional<Item> getResult(){
        char[][] currentTable = compute();
        for (Item entry : Constants.itemTypes.values()) {
            if (entry.template.compare(currentTable)) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

    private char[][] compute() {
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

    private void drawBackground(GraphicsHandler g){
        g.setColor(Color.DARK_GRAY);
        int widgetX = this.x + panelWidth - tableSizeAvailable * (tileSize + seperation) - seperation;
        int width = tableSizeAvailable * (tileSize + seperation) + seperation;
        int height = tableSizeAvailable * (tileSize + seperation) + seperation;
        g.fillRect(widgetX, y, width, height);
    }
}

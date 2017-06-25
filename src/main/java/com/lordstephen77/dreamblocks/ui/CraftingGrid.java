package com.lordstephen77.dreamblocks.ui;

import com.lordstephen77.dreamblocks.*;

import java.util.Optional;

/**
 * Created by Александр on 25.06.2017.
 */
public class CraftingGrid {
    private InventoryItem[][] inventoryItems;
    private int tableSizeAvailable = 2;
    private int tileSize;
    private int seperation;
    private char[][] tableTwo = new char[2][2];
    private char[][] tableThree = new char[3][3];

    public CraftingGrid(int tileSize, int seperation){
        this.tileSize = tileSize;
        this.seperation = seperation;
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

    public void draw(GraphicsHandler g, int screenWidth, int screenHeight, int panelWidth, int panelHeight){
        drawBackground(g, screenWidth, screenHeight, panelWidth, panelHeight);
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

    private void drawBackground(GraphicsHandler g, int panelWidth, int panelHeight, int screenWidth, int screenHeight){
        g.setColor(Color.DARK_GRAY);
        int x = screenWidth / 2 - panelWidth / 2 + panelWidth - tableSizeAvailable * (tileSize + seperation) - seperation;
        int y = screenHeight / 2 - panelHeight / 2;
        int width = tableSizeAvailable * (tileSize + seperation) + seperation;
        int height = tableSizeAvailable * (tileSize + seperation) + seperation;
        g.fillRect(x, y, width, height);
    }

    private void drawInventoryCell(GraphicsHandler g, int x, int y, InventoryItem item){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x + seperation - 2, y + seperation - 2, tileSize + 4, tileSize + 4);
        item.draw(g, x + seperation, y + seperation, tileSize);
    }
}

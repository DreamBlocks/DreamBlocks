package com.lordstephen77.dreamblocks.ui;

import com.lordstephen77.dreamblocks.*;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Александр on 25.06.2017.
 */
public class CraftingGrid implements Serializable{
    private static final long serialVersionUID = 1L;

    private CraftingResultSlot resultSlot;
    private InventoryItem[][] inventoryItems;
    private int tableSizeAvailable = 2;
    private int x;
    private int y;
    private int tileSize;
    private int seperation;
    private List<InventorySlot> table;
    private char[][] tableChar;

    public CraftingGrid(int x, int y, int tileSize, int seperation, int tableSizeAvailable){
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
        this.seperation = seperation;
        this.tableSizeAvailable = tableSizeAvailable;
        inventoryItems = new InventoryItem[tableSizeAvailable][tableSizeAvailable];
        table = new ArrayList<>(tableSizeAvailable*tableSizeAvailable);
        for (int i = 0; i < tableSizeAvailable; i++) {
            for (int j = 0; j < tableSizeAvailable; j++) {
                inventoryItems[i][j] = new InventoryItem();
                table.add(new InventorySlot(
                        this.x + i * (tileSize + seperation) + seperation,
                        this.y + j * (tileSize + seperation) + seperation,
                        tileSize,
                        tileSize,
                        inventoryItems[i][j]
                ));
            }
        }
        tableChar = new char[tableSizeAvailable][tableSizeAvailable];
        resultSlot = new CraftingResultSlot(x - tileSize - seperation, y + tileSize + seperation, tileSize, tileSize, this);
    }

    public void move(int dx, int dy){
        this.x += dx;
        this.y += dy;
        resultSlot.move(dx, dy);
        for (InventorySlot slot : table) {
            slot.move(dx, dy);
        }
    }

    public void draw(GraphicsHandler g, SpriteStore spriteStore){
        drawBackground(g);
        for (InventorySlot slot : table) {
            slot.draw(g, spriteStore);
        }
        resultSlot.draw(g, spriteStore);
    }

    public boolean hasResult(){
        char[][] currentTable = compute();
        for (Item entry : Constants.itemTypes.values()) {
            if (entry.template.compare(currentTable)) {
                return true;
            }
        }
        return false;
    }

    public Item getResult(){
        char[][] currentTable = compute();
        for (Item entry : Constants.itemTypes.values()) {
            if (entry.template.compare(currentTable)) {
                return entry;
            }
        }
        throw new InvalidStateException("No result");
    }

    public void takeRecipeMaterials(){
        for (int i = 0; i < tableSizeAvailable; i++) {
            for (int j = 0; j < tableSizeAvailable; j++) {
                int actualI = i + inventoryItems.length - tableSizeAvailable;
                inventoryItems[actualI][j].count -= 1;
                if (inventoryItems[actualI][j].count <= 0) {
                    inventoryItems[actualI][j].item = null;
                    inventoryItems[actualI][j].count = 0;
                }
            }
        }
    }

    private char[][] compute() {
        char[][] currentTable = tableChar;

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
        int height = getHeight();
        int width = getWidth();
        g.fillRect(x, y, width, height);
    }

    public int getHeight(){
        return tableSizeAvailable * (tileSize + seperation) + seperation;
    }

    //just to keep semantics. May be it will change sometime
    public int getWidth(){
        return getHeight();
    }

    public void handleLeftClick(Int2 mousePos, InventoryItem holding) {
        for(InventorySlot slot : table){
            slot.handleLeftClick(mousePos, holding);
        }
        resultSlot.handleLeftClick(mousePos, holding);
    }

    public void handleRightClick(Int2 mousePos, InventoryItem holding){
        for(InventorySlot slot : table){
            slot.handleRightClick(mousePos, holding);
        }
    }
}

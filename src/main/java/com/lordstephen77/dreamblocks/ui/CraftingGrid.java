package com.lordstephen77.dreamblocks.ui;

import com.lordstephen77.dreamblocks.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    private int tileSize;
    private int seperation;
    private List<InventorySlot> twoTable;
    private List<InventorySlot> threeTable;
    private char[][] tableTwo = new char[2][2];
    private char[][] tableThree = new char[3][3];

    public CraftingGrid(int x, int y, int tileSize, int seperation){
        this.x = x;
        this.y = y;
        this.spriteStore = SpriteStore.get();
        this.tileSize = tileSize;
        this.seperation = seperation;
        twoTable = new ArrayList<>(4);
        threeTable = new ArrayList<>(9);
    }

    public int getTableSizeAvailable() {
        return tableSizeAvailable;
    }

    public void setTableSizeAvailable(int tableSizeAvailable) {
        this.tableSizeAvailable = tableSizeAvailable;
    }

    public void setInventoryItems(InventoryItem[][] inventoryItems) {
        this.inventoryItems = inventoryItems;
        twoTable = new ArrayList<>(4);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                twoTable.add(new InventorySlot(
                        this.x + i * (tileSize + seperation) + seperation,
                        this.y + j * (tileSize + seperation) + seperation,
                        tileSize,
                        tileSize,
                        inventoryItems[i + inventoryItems.length - 2][j]
                ));
            }
        }
        threeTable = new ArrayList<>(9);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                threeTable.add(new InventorySlot(
                        this.x + i * (tileSize + seperation) + seperation,
                        this.y + j * (tileSize + seperation) + seperation,
                        tileSize,
                        tileSize,
                        inventoryItems[i + inventoryItems.length - 3][j]
                ));
            }
        }
    }

    public List<InventorySlot> getCurrentTable(){
        if(tableSizeAvailable == 2){
            return twoTable;
        } else {
            return threeTable;
        }
    }

    public void move(int dx, int dy){
        this.x += dx;
        this.y += dy;
        for (InventorySlot slot : twoTable) {
            slot.move(dx, dy);
        }
        for (InventorySlot slot : threeTable) {
            slot.move(dx, dy);
        }
    }

    public void draw(GraphicsHandler g){
        drawBackground(g);
        if(tableSizeAvailable == 2) {
            for (InventorySlot slot : twoTable) {
                slot.draw(g, spriteStore);
            }
        } else {
            for (InventorySlot slot : threeTable) {
                slot.draw(g, spriteStore);
            }
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

    public void takeRecipeMaterials(){
        for (int i = 0; i < getTableSizeAvailable(); i++) {
            for (int j = 0; j < getTableSizeAvailable(); j++) {
                int actualI = i + inventoryItems.length - getTableSizeAvailable();
                inventoryItems[actualI][j].count -= 1;
                if (inventoryItems[actualI][j].count <= 0) {
                    inventoryItems[actualI][j].item = null;
                    inventoryItems[actualI][j].count = 0;
                }
            }
        }
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
        int width = tableSizeAvailable * (tileSize + seperation) + seperation;
        int height = tableSizeAvailable * (tileSize + seperation) + seperation;
        g.fillRect(x, y, width, height);
    }
}

package com.lordstephen77.dreamblocks.ui;

import com.lordstephen77.dreamblocks.*;
import com.lordstephen77.dreamblocks.Color;

import java.awt.*;

/**
 * Created by Александр on 22.07.2017.
 */
public class CraftingResultSlot {
    private Rectangle bounds;
    private CraftingGrid craftingGrid;
    private static final int BORDER_WIDTH = 5;
    private InventoryItem craftable = new InventoryItem();

    public CraftingResultSlot(int x, int y, int width, int height, CraftingGrid grid){
        this.bounds = new Rectangle(x, y, width, height);
        this.craftingGrid = grid;
    }

    public boolean isInside(Int2 pos){
        return this.bounds.contains(pos.x, pos.y);
    }

    public void move(int dx, int dy){
        this.bounds.x += dx;
        this.bounds.y += dy;
    }

    public void draw(GraphicsHandler g, SpriteStore spriteStore){
        g.setColor(com.lordstephen77.dreamblocks.Color.orange);
        g.fillRect(bounds.x - BORDER_WIDTH, bounds.y - BORDER_WIDTH, bounds.width + BORDER_WIDTH*2, bounds.height + BORDER_WIDTH*2);

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        if (craftingGrid.hasResult()){
            Item nextResult = craftingGrid.getResult();
            craftable.item = nextResult;
            craftable.count = nextResult.template.outCount;
            Sprite sprite = spriteStore.getSprite(craftable.item.getSpriteId());
            g.drawImage(sprite, bounds.x, bounds.y, bounds.width, bounds.height);

            g.setColor(Color.white);
            g.drawString(String.valueOf(craftable.count), bounds.x, bounds.y + bounds.height / 2);
        }
    }
}

package com.lordstephen77.dreamblocks.ui;

import com.lordstephen77.dreamblocks.*;

import java.awt.*;

/**
 * Created by Александр on 22.07.2017.
 */
public class InventorySlot {
    private Rectangle bounds;
    private InventoryItem stack;
    private static final int BORDER_WIDTH = 2;

    public InventorySlot(int x, int y, int width, int height, InventoryItem stack){
        this.bounds = new Rectangle(x, y, width, height);
        this.stack = stack;
    }

    public boolean isInside(Int2 pos){
        return this.bounds.contains(pos.x, pos.y);
    }

    public InventoryItem getStack() {
        return stack;
    }

    public void move(int dx, int dy){
        this.bounds.x += dx;
        this.bounds.y += dy;
    }

    public void handleLeftClick(Int2 mousePos, InventoryItem holding){
        if(!isInside(mousePos)){
            return;
        }
        if (holding.isEmpty()) {
            stack.pickWholeStack(holding);
        } else if (stack.item == null) {
            stack.dropWholeStackToEmptyTile(holding);
        } else if (holding.item.item_id == stack.item.item_id && stack.count < stack.maxCount) {
            if (!holding.item.isTool() && !stack.item.isTool()) {
                stack.dropStackToStack(holding);
            }
        } else {
            stack.swapItems(holding);
        }
    }

    public void handleRightClick(Int2 mousePos, InventoryItem holding){
        if(!isInside(mousePos)){
            return;
        }
        if (holding.isEmpty()) {
            if (stack.count > 1) {
                stack.pickHalfOfStack(holding);
            } else {
                stack.pickWholeStack(holding);
            }
        } else if (stack.item == null) {
            stack.dropSingleItemToEmptyTile(holding);
        } else if (holding.item.item_id == stack.item.item_id && stack.count < stack.maxCount) {
            if (holding.item.isTool() && stack.item.isTool()) {
                stack.dropSingleItemToStack(holding);
            }
        } else {
            stack.swapItems(holding);
        }
    }

    public void draw(GraphicsHandler g, SpriteStore spriteStore) {
        g.setColor(com.lordstephen77.dreamblocks.Color.LIGHT_GRAY);
        g.fillRect(bounds.x - BORDER_WIDTH, bounds.y - BORDER_WIDTH, bounds.width + BORDER_WIDTH*2, bounds.height + BORDER_WIDTH*2);
        if (stack.getCount() <= 0) {
            return;
        }
        Sprite sprite = spriteStore.getSprite(stack.item.getSpriteId());
        g.drawImage(sprite, bounds.x, bounds.y, bounds.width, bounds.height);
        if (stack.getCount() > 1) {
            g.setColor(com.lordstephen77.dreamblocks.Color.white);
            g.drawString(String.valueOf(stack.getCount()), bounds.x, bounds.y + bounds.height / 2);
        }
        if (stack.item.isTool()) {
            Tool tool = (Tool) stack.item;
            if (tool.getUses() != 0) {
                int left = bounds.x + BORDER_WIDTH;
                int width = (int) (tool.getFractionOfUses() * bounds.width);
                int top = bounds.y + bounds.height - 4;
                int height = 2;
                g.setColor(com.lordstephen77.dreamblocks.Color.green);
                g.fillRect(left, top, width, height);
            }
        }
    }
}

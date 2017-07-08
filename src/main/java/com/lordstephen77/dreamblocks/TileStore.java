package com.lordstephen77.dreamblocks;

import java.util.HashMap;
import java.util.Map;

import static com.lordstephen77.dreamblocks.light.LightingEngine.TORCH;
import static com.lordstephen77.dreamblocks.Constants.TRANSPARENT;
import static com.lordstephen77.dreamblocks.Constants.TileID;
import static java.util.Collections.unmodifiableMap;

/**
 * Created by Александр on 30.06.2017.
 */
public class TileStore {
    public Map<TileID, TileType> tileTypes;
    private Map<TileType, Tile> tilesOfType;
    public static final TileType TYPE_AIR = new TileType("sprites/tiles/air.png", TileID.AIR, true, false, 0, '\0');
    public static final Tile TILE_AIR = new Tile(TYPE_AIR);

    public TileStore(){
        tileTypes = unmodifiableMap(populate(new HashMap<>()));
        //All tiles are scenery. That is why just iterate over all and populate this map.
        tilesOfType = new HashMap<>();
        for (Map.Entry<TileID, TileType> entry : tileTypes.entrySet()){
            tilesOfType.put(entry.getValue(), new Tile(entry.getValue()));
        }
    }

    /**
     * Returns instance of Tile. Not always new. Air by default.
     * All scenery tiles (grass, wood, air) are same objects.
     * Tiles with instance-specific data (chests, signs, protection blocks) should be different objects.
     * @param tileID - id of tile to return
     * @return instance of class Tile
     */
    public Tile make(TileID tileID){
        if (tileTypes.containsKey(tileID)) {
            return tilesOfType.get(tileTypes.get(tileID));
        } else {
            return tilesOfType.get(TYPE_AIR);
        }
    }

    /**
     * Returns instance of Tile. Not always new. Air by default.
     * All scenery tiles (grass, wood, air) are same objects.
     * Tiles with instance-specific data (chests, signs, protection blocks) should be different objects.
     * @param tileType - type of tile to return
     * @return instance of class Tile
     */
    public Tile make(TileType tileType){
        if (tilesOfType.containsKey(tileType)){
            return tilesOfType.get(tileType);
        } else {
            return tilesOfType.get(TYPE_AIR);
        }
    }

    private Map<TileID, TileType> populate(Map<TileID, TileType> subject){
        subject.put(TileID.DIRT, new TileType("sprites/tiles/dirt.png", TileID.DIRT, 'd'));
        subject.put(TileID.GRASS, new TileType("sprites/tiles/dirtwithgrass.png", TileID.GRASS, 'd'));
        subject.put(TileID.LEAVES, new TileType("sprites/tiles/leaves.png", TileID.LEAVES, false, false, TRANSPARENT, 'S'));
        subject.put(TileID.PLANK, new TileType("sprites/tiles/plank.png", TileID.PLANK, 'p'));
        subject.put(TileID.WOOD, new TileType("sprites/tiles/wood.png", TileID.WOOD, true, false, TRANSPARENT, 'w'));
        subject.put(TileID.AIR, TYPE_AIR);
        subject.put(TileID.STONE, new TileType("sprites/tiles/stone.png", TileID.STONE, 's'));
        subject.put(TileID.WATER, new TileType("sprites/tiles/water.png", TileID.WATER, true, true, TRANSPARENT, '\0'));
        subject.put(TileID.SAND, new TileType("sprites/tiles/sand.png", TileID.SAND, 'n'));
        subject.put(TileID.IRON_ORE, new TileType("sprites/tiles/ironore.png", TileID.IRON_ORE, 'i'));
        subject.put(TileID.COAL_ORE, new TileType("sprites/tiles/coalore.png", TileID.COAL_ORE, 'c'));
        subject.put(TileID.DIAMOND_ORE, new TileType("sprites/tiles/diamondore.png", TileID.DIAMOND_ORE, 'm'));
        subject.put(TileID.COBBLE, new TileType("sprites/tiles/cobble.png", TileID.COBBLE, 'b'));
        subject.put(TileID.CRAFTING_BENCH, new TileType("sprites/tiles/craft.png", TileID.CRAFTING_BENCH, 'f'));
        subject.put(TileID.ADMINITE, new TileType("sprites/tiles/adminite.png", TileID.ADMINITE, '\0'));
        subject.put(TileID.SAPLING, new TileType("sprites/tiles/sapling.png", TileID.SAPLING, true, false, TRANSPARENT, 'S'));
        subject.put(TileID.LADDER, new TileType("sprites/tiles/ladder.png", TileID.LADDER, true, false, TRANSPARENT, 'L'));
        subject.put(TileID.TORCH, new TileType("sprites/tiles/torch.png", TileID.TORCH, true, false, TRANSPARENT, TORCH, 'j'));

        return subject;
    }
}

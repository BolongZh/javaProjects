package byow.TileEngine;

import java.awt.Color;
import java.io.File;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you","byow/TileEngine/yummy_prizes/yp_blackbirdpie.png");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
//    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
//            "floor","/Users/esthersue/cs61bl/fruit/fruitassets_05.png");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
        "floor", "byow/TileEngine/fruit/fruitassets_21.png");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door", "byow/TileEngine/yummy_prizes/yp_sundae.png");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
//    public static final TETile COCONUT = new TETile('t', Color.BLACK,
//            Color.BLACK, "coconut",
//            "/Users/esthersue/cs61bl/SimpleGloomRPG 16x16 by Xenodroidz/Icons/m_star.png");
    public static final TETile COCONUT = new TETile('≈', Color.blue, Color.black, "COCONUT","byow/TileEngine/SimpleGloomRPG 16x16 by Xenodroidz/Icons/shield_02.png");
    public static final TETile POMEGRANATE = new TETile('≈', Color.blue, Color.black, "POMEGRANATE","byow/TileEngine/fruit/fruitassets_08.png");
    public static final TETile ORANGE = new TETile('≈', Color.blue, Color.black, "ORANGE","byow/TileEngine/fruit/fruitassets_06.png");
    public static final TETile APPLE = new TETile('≈', Color.blue, Color.black, "APPLE","byow/TileEngine/fruit/fruitassets_00.png");
    public static final TETile COCO = new TETile('≈', Color.blue, Color.black, "COCO","byow/TileEngine/fruit/fruitassets_16.png");
    public static final TETile WATERMELON = new TETile('≈', Color.blue, Color.black, "WATERMELON","byow/TileEngine/fruit/fruitassets_14.png");
    public static final TETile CARROT = new TETile('≈', Color.blue, Color.black, "CARROT","byow/TileEngine/fruit/fruitassets_20.png");
    public static final TETile LEMON = new TETile('≈', Color.blue, Color.black, "LEMON","byow/TileEngine/fruit/fruitassets_02.png");
}



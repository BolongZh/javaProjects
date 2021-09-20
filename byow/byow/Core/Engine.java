package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.StdDraw;
import jdk.jshell.execution.Util;


import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final int WIDTH = 80;
    public static final int HEIGHT = 45;
    private final boolean DEBUG = true;
    public static final File r = Utils.join(CWD,"Random.txt");
    public static final File s = Utils.join(CWD,"Seed.txt");
    public static final File locx = Utils.join(CWD,"LocationX.txt");
    public static final File locy = Utils.join(CWD,"LocationY.txt");
    public static final File lit = Utils.join(CWD,"lit.txt");
    public static final File appear = Utils.join(CWD,"appearance.txt");
    public static final File speedToggle = Utils.join(CWD,"speedToggle.txt");
    public static final File points = Utils.join(CWD,"myPoint.txt");
    public boolean gameOver = false;
    public Random RANDO;
    public String SEED;
    public TETile[][] world;
    public Point avatarPos;
    public Point unlockedDoor;
    public String inputString;
    public boolean inputting;
    public TETile[][] blindWorld;
    public boolean isLit = true;
    public int pointScore=700;
    public Point dressing;
    public TETile appearance = Tileset.AVATAR;
    public TreeMap<String, Integer> fruitPoints = new TreeMap<>(){};
    public TreeMap<String, TETile> stringtoTile = new TreeMap<>(){};
    public boolean toggleRockSurfing = false;
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */

    public void interactWithKeyboard() {
        mainMenu();
        TETile[] fruits = new TETile[] {Tileset.ORANGE,Tileset.APPLE,
                Tileset.COCO,Tileset.AVATAR,Tileset.POMEGRANATE,Tileset.LEMON,Tileset.WATERMELON,Tileset.CARROT};
        for (int i = 0; i<fruits.length;i++) {
            fruitPoints.put(fruits[i].description(),i+1);
        }
        if (DEBUG) {
            while (!gameOver) {
                if (isLit) {
                    ter.renderFrame(world);
                } else {
                    ter.renderFrame(blindWorld);
                }
                HUD((int) StdDraw.mouseX(), (int) StdDraw.mouseY());
                if (StdDraw.hasNextKeyTyped()) {
                    handleKeyPress(StdDraw.nextKeyTyped());
                }
                StdDraw.show();
            }
        }
    }

    public void handleKeyPress(char c) {


        int x = avatarPos.getX_corr();
        int y = avatarPos.getY_corr();

        int xDress = dressing.getX_corr();
        int yDress = dressing.getY_corr();

//        if (!world[xDress][yDress].equals(Tileset.FLOWER)&&!avatarPos.equals(dressing)) {
//            world[xDress][yDress] = Tileset.FLOWER;
//        }
        if (inputting) {
            if (c == 'q' || c == 'Q' && !gameOver) {

                Utils.writeObject(r, RANDO);
                Utils.writeObject(s, SEED);
                Utils.writeObject(locx, avatarPos.x_corr);
                Utils.writeObject(locy, avatarPos.y_corr);
                Utils.writeObject(lit, isLit);
                Utils.writeObject(points,pointScore);
                Utils.writeObject(speedToggle, toggleRockSurfing);
                Utils.writeObject(appear, appearance.description());
                if (DEBUG) {
                    System.exit(0);
                }
            }

        }
        if (c == 'w' || c == 'W') {
            pointScore = pointScore - fruitPoints.get(appearance.description());
            if (toggleRockSurfing && checkWallAdjacent() && !world[x][y + 2].equals(Tileset.COCONUT) && !world[x][y + 2].equals(Tileset.NOTHING)) {
                if (!world[x][y + 1].equals(Tileset.COCONUT) ) {
                    if (world[x][y+1] ==Tileset.UNLOCKED_DOOR) {
                        avatarPos = unlockedDoor;
                    }
                    else {
                        world[x][y + 2] = world[x][y];
                        world[x][y] = Tileset.FLOOR;
                        avatarPos.y_corr = y + 2;
                        lightHelper();
                    }
                }
            } else {
                if (!world[x][y + 1].equals(Tileset.COCONUT)) {
                    world[x][y + 1] = world[x][y];
                    world[x][y] = Tileset.FLOOR;
                    avatarPos.y_corr = y + 1;
                    lightHelper();
                }
            }
            dressingHelper();
            if (!isLit) {
                pointScore = pointScore + 2;
            }
        } else if (c == 'a' || c == 'A') {
            pointScore = pointScore - fruitPoints.get(appearance.description());
            if (toggleRockSurfing && checkWallAdjacent() && !world[x - 2][y].equals(Tileset.COCONUT)&& !world[x-2][y].equals(Tileset.NOTHING)) {
                if (!world[x - 1][y].equals(Tileset.COCONUT)) {
                    if (world[x-1][y] ==Tileset.UNLOCKED_DOOR) {
                        avatarPos = unlockedDoor;
                    }
                    else {
                        world[x - 2][y] = world[x][y];
                        world[x][y] = Tileset.FLOOR;
                        avatarPos.x_corr = x - 2;
                        lightHelper();
                    }
                }
            } else {
                if (!world[x - 1][y].equals(Tileset.COCONUT)) {
                    world[x - 1][y] = world[x][y];
                    world[x][y] = Tileset.FLOOR;
                    avatarPos.x_corr = x - 1;
                    lightHelper();
                }
            }
            dressingHelper();
            if (!isLit) {
                pointScore = pointScore + 2;
            }
        } else if (c == 's' || c == 'S') {
            pointScore = pointScore - fruitPoints.get(appearance.description());
            if (toggleRockSurfing && checkWallAdjacent() && !world[x][y - 2].equals(Tileset.COCONUT)&& !world[x][y -2].equals(Tileset.NOTHING)) {
                if (!world[x][y - 1].equals(Tileset.COCONUT)) {
                    if (world[x][y-1] ==Tileset.UNLOCKED_DOOR) {
                        avatarPos = unlockedDoor;
                    }
                    else {
                        world[x][y - 2] = world[x][y];
                        world[x][y] = Tileset.FLOOR;
                        avatarPos.y_corr = y - 2;
                        lightHelper();
                    }
                }
            } else {
                if (!world[x][y - 1].equals(Tileset.COCONUT)) {
                    world[x][y - 1] = world[x][y];
                    world[x][y] = Tileset.FLOOR;
                    avatarPos.y_corr = y - 1;
                    lightHelper();
                }
            }
            dressingHelper();
            if (!isLit) {
                pointScore = pointScore + 2;
            }
        } else if (c == 'd' || c == 'D') {
            pointScore = pointScore - fruitPoints.get(appearance.description());
            if (toggleRockSurfing && checkWallAdjacent() && !world[x + 2][y].equals(Tileset.COCONUT)&& !world[x+2][y].equals(Tileset.NOTHING)) {
                if (!world[x + 1][y].equals(Tileset.COCONUT)) {
                    if (world[x+1][y] ==Tileset.UNLOCKED_DOOR) {
                        avatarPos = unlockedDoor;
                    }
                    else {
                        world[x + 2][y] = world[x][y];
                        world[x][y] = Tileset.FLOOR;
                        avatarPos.x_corr = x + 2;
                        lightHelper();
                    }
                }
            } else {
                if (!world[x + 1][y].equals(Tileset.COCONUT)) {
                    world[x + 1][y] = world[x][y];
                    world[x][y] = Tileset.FLOOR;
                    avatarPos.x_corr = x + 1;
                    lightHelper();
                }
            }
            dressingHelper();
            if (!isLit) {
                pointScore = pointScore + 2;
            }
        } else if (c == ':') {
            inputting = true;
        }

        //* illuminate *//
        else if (c == 'i' || c == 'I') {
            if (isLit) {
                isLit = false;
                lightHelper();
            } else {
                isLit = true;
            }
            pointScore = pointScore - fruitPoints.get(appearance.description()) -4;

        }


         else if (c == 'r' || c == 'R') {
            toggleRockSurfing = !toggleRockSurfing;
            pointScore = pointScore - fruitPoints.get(appearance.description()) -4;
        }
//        if (avatarPos.equals(dressing)) {
//            TETile[] fruits = new TETile[]{Tileset.ORANGE, Tileset.APPLE, Tileset.AVATAR,
//                    Tileset.COCO, Tileset.CARROT, Tileset.POMEGRANATE, Tileset.LEMON, Tileset.WATERMELON};
//            int r = RANDO.nextInt(fruits.length);
//            appearance = fruits[r];
//            world[avatarPos.getX_corr()][avatarPos.getY_corr()] = appearance;
//            return;
//        }


        if (pointScore <= 0) {
            gameOver = true;
            if (DEBUG) {
                StdDraw.setPenColor(Color.BLUE);
                Font fontBig = new Font("Monaco", Font.BOLD, 60);
                Font fontSmall = new Font("Monaco", Font.BOLD, 30);
                StdDraw.setFont(fontBig);
                StdDraw.clear(Color.RED);
                StdDraw.text(WIDTH / 2, HEIGHT / 2, "You lose.");
                StdDraw.show();
            }
        }
        if (avatarPos.equals(unlockedDoor) || checkNothing()) {
            gameOver = true;
            if (DEBUG) {
                StdDraw.setPenColor(Color.RED);
                Font fontBig = new Font("Monaco", Font.BOLD, 60);
                Font fontSmall = new Font("Monaco", Font.BOLD, 30);
                StdDraw.setFont(fontBig);
                StdDraw.clear(Color.orange);
                StdDraw.text(WIDTH / 2, HEIGHT / 2, "You win!");
                StdDraw.show();
            }
        }

        if (!world[xDress][yDress].equals(Tileset.FLOWER) && !avatarPos.equals(dressing)) {
            world[xDress][yDress] = Tileset.FLOWER;
        }

        lightHelper();


        if (DEBUG && !gameOver) {
            if (isLit) {
                ter.renderFrame(world);
            } else {
                ter.renderFrame(blindWorld);
            }

            StdDraw.show();
        }
    }
    //* speed up near the wall *//
    public boolean checkWallAdjacent() {
        int x = avatarPos.getX_corr();
        int y = avatarPos.getY_corr();

        return world[x][y + 1] == Tileset.COCONUT || world[x][y - 1] == Tileset.COCONUT || world[x - 1][y] == Tileset.COCONUT || world[x + 1][y] == Tileset.COCONUT;


    }

    public boolean checkNothing() {
        int x = avatarPos.x_corr;
        int y = avatarPos.y_corr;
        return world[x][y]==Tileset.NOTHING || world[x+1][y]==Tileset.NOTHING || world[x-1][y] == Tileset.NOTHING || world[x][y+1] == Tileset.NOTHING || world[x][y-1] == Tileset.NOTHING;
    }

    public void dressingHelper() {
        if (avatarPos.equals(dressing)) {
            TETile[] fruits = new TETile[]{Tileset.ORANGE, Tileset.APPLE, Tileset.AVATAR,
                    Tileset.COCO, Tileset.CARROT, Tileset.POMEGRANATE, Tileset.LEMON, Tileset.WATERMELON};
            int r = RANDO.nextInt(fruits.length);
            appearance = fruits[r];
            world[avatarPos.getX_corr()][avatarPos.getY_corr()] = appearance;
        }
    }

    //* help turn on or turn off the light *//\
    public void lightHelper() {
        int x = avatarPos.getX_corr();
        int y = avatarPos.getY_corr();
        if (!isLit) {
            blindWorld = TETile.copyOf(world);
            for (int i = 0;i<WIDTH;i++) {
                for (int j=0;j<HEIGHT;j++) {
                    if (Math.abs(i-x)>=6||Math.abs(j-y)>=6) {
                        blindWorld[i][j] = Tileset.NOTHING;
                    }
                }
            }
        }
    }

    public void HUD(int x, int y) {
        if (DEBUG) {
            String message = "";
            Font fontBig = new Font("Monaco", Font.BOLD, 60);
            Font fontSmall = new Font("Monaco", Font.BOLD, 10);
            StdDraw.setFont(fontSmall);
            StdDraw.setPenColor(Color.WHITE);
            if (x > 0 && x < world.length && y - 3 > 0 && y - 3 < world[0].length) {
                TETile t = world[(int) StdDraw.mouseX()][(int) StdDraw.mouseY() - 3];
                if (t.equals(Tileset.COCONUT)) {
                    message = "This is a wall!!";
                } else if (t.equals(appearance)) {
                    message = "That's you!";
                } else if (t.equals(Tileset.FLOOR)) {
                    message = "Floor that you walk on";
                } else if (t.equals(Tileset.UNLOCKED_DOOR)) {
                    message = "That's your goal-- ice cream!";
                }
                else if (t.equals(Tileset.FLOWER)) {
                    message = "Hurry-- change in here ;)";
                }
                StdDraw.text(10, world[0].length + 5, message);
//            StdDraw.show();
            }
            if (isLit) {
                StdDraw.text(20, world[0].length + 5, "Current Score: " + pointScore);
//            StdDraw.show();
            } else {
                StdDraw.text(20, world[0].length + 5, "Walking in the Dark");
//            StdDraw.show();
            }
        }

//        while (!StdDraw.hasNextKeyTyped()&& StdDraw.mouseX()==x &&StdDraw.mouseY()==y) {
//
//            StdDraw.pause(1);
//        }
    }
    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        TETile[] fruits = new TETile[] {Tileset.ORANGE,Tileset.APPLE,
                Tileset.COCO,Tileset.AVATAR,Tileset.POMEGRANATE,Tileset.LEMON,Tileset.WATERMELON,Tileset.CARROT};
        for (int i = 0; i<fruits.length;i++) {
            fruitPoints.put(fruits[i].description(),i+1);
        }
        String rest = "";
        for (int x = 0; x<input.length(); x++) {
            interactMainMenu(input.charAt(x));
            if (input.charAt(x)=='s' || input.charAt(x)=='S') {
                rest = input.substring(x+1);
                break;
            }
            else if (input.charAt(x)=='L' || input.charAt(x)=='l') {
                rest = input.substring(x+1);
                break;
            }
        }
        if (world==null) {
            TETile[][] finalWorldFrame = interactHelp(input);
            world = finalWorldFrame;
        }

        for(int x = 0; x<rest.length(); x++) {
            handleKeyPress(rest.charAt(x));
        }
        if (DEBUG) {
            if (isLit) {
                ter.renderFrame(world);
            } else {
                ter.renderFrame(blindWorld);
            }

            StdDraw.show();
        }
        return world;
    }



    public TETile[][]  interactHelp(String input) {
        SEED = input;
        long theSeed = Long.parseLong(SEED);

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
        RANDO = new Random(theSeed);
        Point tt = new Point(RANDO.nextInt(WIDTH/2)+WIDTH/4, RANDO.nextInt(HEIGHT/2)+HEIGHT/4, appearance);
        Room r = new Room(RANDO.nextInt(2)+4, RANDO.nextInt(2)+4, tt, RANDO, true, true,0);
        r.createRoom(finalWorldFrame);
        int numFloor = 0;
        int numWall = 0;
        Point real=null;
        if (avatarPos!=null) {
            real = avatarPos;
        }
        // agent born on a random floor
        for (TETile[] teTiles : finalWorldFrame) {
            for (int j = 0; j < finalWorldFrame[0].length; j++) {
                if (teTiles[j].equals(Tileset.FLOOR)) {
                    numFloor += 1;
                }
                if (teTiles[j].equals(Tileset.COCONUT)) {
                    numWall += 1;
                }
            }
        }

        int imBornHere = RANDO.nextInt(numFloor);
        int doorIsHere = RANDO.nextInt(numWall);
        int dressingRoom = RANDO.nextInt(numFloor-1);
        if (imBornHere==dressingRoom) {
            dressingRoom=dressingRoom+1;
        }
        for (int i = 0; i<finalWorldFrame.length; i++) {
            for (int j = 0; j < finalWorldFrame[0].length; j++) {
                if (finalWorldFrame[i][j].equals(Tileset.FLOOR)) {
                    if (imBornHere==0) {
                        finalWorldFrame[i][j] = appearance;
                        real = new Point(i,j,appearance);
                    }
                    if (dressingRoom==0) {
                        finalWorldFrame[i][j]= Tileset.FLOWER;
                        dressing = new Point(i,j,Tileset.FLOWER);
                    }
                    imBornHere -= 1;
                    dressingRoom-=1;
                }
            }
        }
        // create an unlocked door as the goal
        for (int i = 0; i<finalWorldFrame.length; i++) {
            for (int j = 0; j < finalWorldFrame[0].length; j++) {
                if (finalWorldFrame[i][j].equals(Tileset.COCONUT)) {
                    if (doorIsHere==0 && (finalWorldFrame[i][j+1].equals(Tileset.FLOOR)|| finalWorldFrame[i][j-1].equals(Tileset.FLOOR) || finalWorldFrame[i-1][j].equals(Tileset.FLOOR) || finalWorldFrame[i+1][j].equals(Tileset.FLOOR))) {
                        finalWorldFrame[i][j] = Tileset.UNLOCKED_DOOR;
                        unlockedDoor = new Point(i,j,Tileset.UNLOCKED_DOOR);
                    }
                    else if (doorIsHere==0) {
                        continue;
                    }
                    doorIsHere -= 1;
                }
                else if (i==finalWorldFrame.length-1 && j == finalWorldFrame[0].length-1 && unlockedDoor==null) {
                    i = 0;
                    j = 0;
                    doorIsHere = RANDO.nextInt(numWall);;
                }
            }

        }


        avatarPos=real;
        if (DEBUG) {
            ter.initialize(WIDTH, HEIGHT+7,0,3);
            ter.renderFrame(finalWorldFrame);
            StdDraw.show();
        }
        return finalWorldFrame;
    }
    public void mainMenu() {
        if (DEBUG)
            ter.initialize(WIDTH, HEIGHT);
            StdDraw.setPenColor(Color.WHITE);
            Font fontBig = new Font("Monaco", Font.BOLD, 60);
            Font fontSmall = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(fontBig);
            StdDraw.text(WIDTH / 2, 3 * HEIGHT / 4, "CS61BL: The GAME");
            StdDraw.setFont(fontSmall);
            StdDraw.text(WIDTH / 2, HEIGHT / 2, "NEW GAME (N)");
            StdDraw.text(WIDTH / 2, 3 * HEIGHT / 8, "LOAD GAME (L)");
            StdDraw.text(WIDTH / 2, HEIGHT / 4, "QUIT (Q)");
            StdDraw.text(WIDTH / 2, HEIGHT / 8, "BACKGROUND LORE (B)");
            StdDraw.show();
            boolean outerLoop = true;
            while (outerLoop) {
                if (!outerLoop) {
                    break;
                }
                if (StdDraw.hasNextKeyTyped()) {
                    char c = StdDraw.nextKeyTyped();
                    outerLoop = interactMainMenu(c);
                }
            }

    }

    public boolean interactMainMenu(char c) {
        TETile[] fruits = new TETile[] {Tileset.ORANGE,Tileset.APPLE,
                Tileset.COCO,Tileset.AVATAR,Tileset.POMEGRANATE,Tileset.LEMON,Tileset.WATERMELON,Tileset.CARROT};
        for (int i = 0; i<fruits.length;i++) {
            stringtoTile.put(fruits[i].description(),fruits[i]);
        }
        if (inputting) {
            if (DEBUG) {
                StdDraw.clear(Color.BLACK);
                StdDraw.text(WIDTH / 2, 3 * HEIGHT / 4, "SEED:");
            }
            if ( c=='s' || c == 'S') {
                inputting = false;
                world = interactWithInputString(inputString);
                return false;
            }
            if (Character.isDigit(c)) {
                inputString = inputString + c;
                if (DEBUG) {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, inputString);
                    StdDraw.show();
                }
            }
        }
        else if (c == 'n' || c== 'N') {
            if (DEBUG) {
                StdDraw.clear(Color.BLACK);
                StdDraw.text(WIDTH / 2, 3 * HEIGHT / 4, "SEED:");
            }
            inputting = true;
            inputString="";

            if (DEBUG) {
                StdDraw.show();
            }
        } else if (c == 'l' || c == 'L') {
            if (s.exists()) {
                String t = Utils.readObject(s, String.class);
                world = interactWithInputString(t);
                world[avatarPos.x_corr][avatarPos.y_corr] = Tileset.FLOOR;
                pointScore = Utils.readObject(points,Integer.class);
                appearance = stringtoTile.get(Utils.readObject(appear, String.class));
                avatarPos = new Point(Utils.readObject(locx, Integer.class), Utils.readObject(locy, Integer.class), appearance);
                world[avatarPos.x_corr][avatarPos.y_corr] = appearance;
                RANDO = Utils.readObject(r, Random.class);
                isLit = Utils.readObject(lit, Boolean.class);
                toggleRockSurfing = Utils.readObject(speedToggle, Boolean.class);
                lightHelper();
            }
            if (DEBUG) {
                    ter.renderFrame(world);
                }
                return false;
            }
        else if (c == 'q' || c == 'Q') {
            System.exit(0);
        }
        else if (DEBUG && (c=='b' || c=='B') ) {
            StdDraw.clear(Color.BLACK);
            Font fontSmall = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(fontSmall);
            StdDraw.text(WIDTH/2, HEIGHT-2, "You are an ice cream fanatic.");
            StdDraw.text(WIDTH/2, 5*HEIGHT/6, "You travel through worlds searching for the perfect icecream cone and flavor.");
            StdDraw.text(WIDTH/2, 4*HEIGHT/6, "One day, you discovered an underground space rumored to enshrine the most ancient ice cream cone in");
            StdDraw.text(WIDTH/2, HEIGHT/2, "the history of mankind. As an ice cream lover, you are not going to miss this opportunity.");
            StdDraw.text(WIDTH/2, HEIGHT/3, "While this dungeon is treacherously difficult to navigate, you brought your trusty torch to light the way.");
            StdDraw.text(WIDTH/2, HEIGHT/6, "Will you successfully taste a piece of heaven?");
            StdDraw.show();
        }
        return true;
    }
}

package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeSet;

public class Room {
    //Instance variables:
    //Int Width
    //Int Height
    //List of (x,y) openings
    //Point source
    //Methods:
    //checkOpenings(): checks if openings are connected to a hallway
    //If not, make a hallway
    //createRoom(Tile[][] x)


    private int width;
    private int height;
    private ArrayList<Point> walls;
    private TreeSet<Point> openings = new TreeSet<>();
    private Point source;
    private ArrayList<Point> corners = new ArrayList<>();
    private final Random rng;
    private boolean direction;
    private int recursionDepth;

    public Room(int w, int h, Point s, Random rng, boolean orientation, boolean d, int recursionDepth) {
        direction = d;
        this.width = w;
        this.height = h;
        this.walls = new ArrayList<Point>();
        this.source = s;
        this.rng = rng;
        this.recursionDepth = recursionDepth;
        if (direction) {
            for (int i = s.x_corr - 1; i <= s.x_corr + width; i++) {
                this.walls.add(new Point(i, s.y_corr - 1, Tileset.AVATAR));
                this.walls.add(new Point(i, s.y_corr + height, Tileset.AVATAR));
            }
            for (int i = s.y_corr; i < s.y_corr + height; i++) {
                this.walls.add(new Point(s.x_corr - 1, i, Tileset.AVATAR));
                this.walls.add(new Point(s.x_corr + width, i, Tileset.AVATAR));
            }
            this.corners.add(new Point(s.x_corr-1, s.y_corr-1,Tileset.AVATAR));
            this.corners.add(new Point(s.x_corr-1,s.y_corr+height,Tileset.AVATAR));
            this.corners.add(new Point(s.x_corr+width,s.y_corr+height,Tileset.AVATAR));
            this.corners.add(new Point(s.x_corr+width,s.y_corr-1,Tileset.AVATAR));
        }
        else {
            for (int i = s.x_corr - 1; i >= s.x_corr - width; i--) {
                this.walls.add(new Point(i, s.y_corr - 1, Tileset.AVATAR));
                this.walls.add(new Point(i, s.y_corr - height, Tileset.AVATAR));
            }
            for (int i = s.y_corr; i > s.y_corr - height; i--) {
                this.walls.add(new Point(s.x_corr - 1, i, Tileset.AVATAR));
                this.walls.add(new Point(s.x_corr - width, i, Tileset.AVATAR));
            }
            this.corners.add(new Point(s.x_corr-width,s.y_corr-height,Tileset.AVATAR));
            this.corners.add(new Point(s.x_corr-width,s.y_corr+1,Tileset.AVATAR));
            this.corners.add(new Point(s.x_corr+1, s.y_corr+1,Tileset.AVATAR));
            this.corners.add(new Point(s.x_corr+1,s.y_corr-height,Tileset.AVATAR));
        }

    }

    public boolean[] checkOpenings() {
        return null;
    }

    //
    public void connect(Point p, int x, int y, boolean alignment) {
    }

    public boolean outOfBoundDetector(TETile[][] canvas) {
        int x = source.x_corr;
        int y = source.y_corr;


        if (direction) {
            boolean b = x - 1 <= 0 || x + width > canvas.length - 2 || y + height > canvas[0].length - 2 || y - 1 <= 0;

            return b;
        } else {
            return x - width < 2 || y - height <  2 || x+1 >  canvas.length- 2 || y+1>canvas[0].length - 2;
        }
    }
    public boolean conflict(TETile[][] canvas) {
//        if (this.outOfBoundDetector(canvas))
//            return true;
        int x = source.x_corr;
        int y = source.y_corr;

        if (direction) {
            for (int i = x; i < x + width; i++) {
                for (int j = y - 1; j < y + height; j++) {

                    if (!canvas[i][j].equals(Tileset.NOTHING))  //(canvas[i][j].equals(Tileset.NOTHING))return false;
                        return true;
                }
            }
        }
        else {
            for (int i = x; i >= x - width; i--) {
                for (int j = y + 1; j > y - height; j--) {
                    if (!canvas[i][j].equals(Tileset.NOTHING))
                        return true;
                }
            }
        }
        return false;
    }

    public int checkMaxEx(Point p, boolean[][] obstacles, TETile[][] t) {
        for (int i = p.x_corr; i<obstacles[0].length; i++) {
            if (obstacles[i][p.y_corr])
                return i-p.x_corr;
        }
        return obstacles[0].length-p.x_corr;
    }

    public boolean createRoom(TETile[][] t) {
        int x = source.x_corr;
        int y = source.y_corr;
        if (outOfBoundDetector(t) || conflict(t)) {
            return false;
        }


        if (direction) {
            createBox(x,x+width-1,y,y+height-1,t);
//            for (int i = x; i<x+width; i++) {
//                for (int j = y; j<y+ height; j++) {
//                    t[i][j] = Tileset.FLOOR;
//                }
//            }
//            for (Point p : walls) {
//                t[p.x_corr][p.y_corr] = Tileset.WALL;
//            }
        }
        else {
            createBox(x,x-width+1,y,y-height+1,t);

//            for (int i = x; i>x-width; i--) {
//                for (int j = y; j>y- height; j--) {
//                    t[i][j] = Tileset.FLOOR;
//                }
//            }
//            for (Point p : walls) {
//                t[p.x_corr][p.y_corr] = Tileset.WALL;
//            }
        }

        int r = rng.nextInt(8);
        if (r<8) {
            Point dummy = new Point(corners.get(0).x_corr,rng.nextInt(height)+1+corners.get(0).y_corr,Tileset.AVATAR);
            t[dummy.x_corr][dummy.y_corr] = Tileset.FLOOR;
            openings.add(dummy);

        }
        r = rng.nextInt(8);
        if (r<8) {
            Point dummy = new Point(rng.nextInt(width)+corners.get(0).x_corr+1,corners.get(1).y_corr,Tileset.AVATAR);
            t[dummy.x_corr][dummy.y_corr] = Tileset.FLOOR;
            openings.add(dummy);

        }
        r = rng.nextInt(8);
        if (r<8) {
            Point dummy = new Point(corners.get(2).x_corr,rng.nextInt(height)+corners.get(0).y_corr+1,Tileset.AVATAR);
            t[dummy.x_corr][dummy.y_corr] = Tileset.FLOOR;
            openings.add(dummy);

        }
        r = rng.nextInt(8);
        if (r<8) {
            Point dummy = new Point(rng.nextInt(width)+corners.get(0).x_corr+1,corners.get(0).y_corr,Tileset.AVATAR);
            t[dummy.x_corr][dummy.y_corr] = Tileset.FLOOR;
            openings.add(dummy);

        }

        fillOpenings(t,recursionDepth+1);
        return true;
    }


    public void fillOpenings(TETile[][] w, int recursionDepth) {
        if (recursionDepth>7) {
            return;
        }

        if (openings.isEmpty()) {
            return;
        }

        for (Point p: openings) {
            int openingx = p.x_corr;
            int openingy = p.y_corr;
            boolean dir;
            boolean ori;
            if (w[openingx+1][openingy].equals(Tileset.NOTHING)) {
                ori = true;
                dir = true;
            }
            else if (w[openingx-1][openingy].equals(Tileset.NOTHING)) {
                ori = true;
                dir = false;
            }
            else if (w[openingx][openingy-1].equals(Tileset.NOTHING)) {
                ori = false;
                dir = false;
            }
            else {
                ori = false;
                dir = true;
            }
//            if (direction && p.x_corr== source.x_corr-1 || p.x_corr==source.x_corr+width) {
//                ori = true;
//            }
//            else if (direction && p.x_corr!= source.x_corr-1 && p.x_corr!=source.x_corr+width)
//                ori = false;
//            if (p.y_corr!=source.y_corr+height && p.x_corr!=source.x_corr+width)
//                dir = false;
//            else
//                dir = true;

            if (ori&&dir) {
                p.x_corr += 1;
            }
            else if (ori&&!dir) {
                p.x_corr -= 1;
            }
            else if (!ori&&dir) {
                p.y_corr += 1;
            }
            else {
                p.y_corr -= 1;
            }

            int rr = rng.nextInt(2)+1;
            for (int x = 8; x>0; x--) {
                Hallway h = new Hallway(rng.nextInt(x)+3,
                        rr, p, ori, dir, rng, this.recursionDepth + 1);
//                if (!h.createHall(w)) {
//                    w[openingx][openingy] = Tileset.FLOWER;
//                }
                if (!h.outOfBoundDetector(w) && !h.conflict(w)) {

                    h.createHall(w);
                    break;
                }
                else {
                    w[openingx][openingy] = Tileset.COCONUT;
                    break;
                }
            }
        }

    }

    public void createBox(int x1, int x2, int y1, int y2, TETile[][] canvas) {
        // a.x is always less than or equal to b.x
        // a.y is always less than or equal to b.y

        // the source point is bottom left; direction is true
        if (x1<x2) {
            for (int i = x1; i <= x2; i++) {
                for (int j = y1; j <= y2; j++) {
                    canvas[i][j] = Tileset.FLOOR;
                }
            }
            // bottom
            createLine(x1-1,x1+width,y1-1,y1-1,Tileset.COCONUT,canvas);
            // top
            createLine(x1-1,x1+width,y1+height,y1+height,Tileset.COCONUT,canvas);
            // left
            createLine(x1-1,x1-1,y1-1,y1+height,Tileset.COCONUT,canvas);
            // right
            createLine(x1+width,x1+width,y1-1,y1+height,Tileset.COCONUT,canvas);
        }
        // the source point is top right; direction is false
        else {
            for (int i = x2; i <= x1; i++) {
                for (int j = y2; j <= y1; j++) {
                    canvas[i][j] = Tileset.FLOOR;
                }
            }
            // bottom
            createLine(x2-1,x2+width,y2-1,y2-1,Tileset.COCONUT,canvas);
            // top
            createLine(x2-1,x2+width,y2+height,y2+height,Tileset.COCONUT,canvas);
            // left
            createLine(x2-1,x2-1,y2-1,y2+height,Tileset.COCONUT,canvas);
            // right
            createLine(x2+width,x2+width,y2-1,y2+height,Tileset.COCONUT,canvas);
            }
        }






    public void createLine(int x1, int x2, int y1, int y2, TETile tileType, TETile[][] canvas) {
        // a.x is always less than or equal to b.x
        // a.y is always less than or equal to b.y

        for (int i = x1; i<= x2; i++) {
            for (int j = y1; j<=y2; j++) {
                canvas[i][j] = tileType;
            }
        }
    }


}

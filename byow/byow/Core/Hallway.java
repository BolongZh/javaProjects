package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class Hallway {

//    Class: Hallway
//    Instance Variables:
//    Int width
//    Int height
//    Boolean horizontal
//    Point source
//    Methods:
//    createHall(Tile[][] x)
//-maybe add openings class

    private int height;
    private int width; // 1 or 2
    private boolean horizontal;
    private boolean direction; // false means left and down true means right and up
    private Point source;
    private final Random rng;
    private Point endPointFirst;
    private Point endPointSecond;
    private ArrayList<Point> walls = new ArrayList<>();
    private int recursionDepth;

    public Hallway(int h, int w, Point p, boolean alignment, boolean d, Random rng, int recursionDepth) {
        this.height = h;
        this.width = w;
        this.source = p;
        this.horizontal = alignment;
        this.direction = d;
        this.rng = rng;
    }

    public boolean outOfBoundDetector(TETile[][] canvas) {
        int x = source.x_corr;
        int y = source.y_corr;
        if (horizontal && direction) {
            return x-1<=2||x+height>canvas.length-3 || y + width>canvas[0].length-3 || y-1<=2;
        }
        else if (!horizontal && direction) {
            return x-width<=2||x+width>canvas.length-3 || y + height>canvas[0].length-3;
        }
        else if (horizontal && !direction) {
            return y+1> canvas[0].length-3|| x-height<3 || y - width<3;
        }
        else {
            return x+1> canvas.length-3|| x-width<3 || y - height<3;
        }
    }

    public boolean conflict(TETile[][] canvas) {
        int x = source.x_corr;
        int y = source.y_corr;

        if (direction) {
            if (horizontal) {
                for (int i = x ; i < x + height; i++) {
                    for (int j = y - 1; j <= y + width; j++)
                        if (!canvas[i][j].equals(Tileset.NOTHING))

                            return true;
                }
            }
            else {
                for (int i = x - 1; i <= x + width; i++) {
                    for (int j = y ; j < y + height; j++)
                        if (!canvas[i][j].equals(Tileset.NOTHING))
                            return true;
                }
            }
        }
        else {
            if (horizontal) {
                for (int i = x ; i > x - height; i--) {
                    for (int j = y + 1; j >= y - width; j--)
                        if (!canvas[i][j].equals(Tileset.NOTHING))
                            return true;
                }
            }
            else {
                for (int i = x ; i >= x - width; i--) {
                    for (int j = y ; j > y - height; j--)
                        if (!canvas[i][j].equals(Tileset.NOTHING))
                            return true;
                }
            }
        }
        return false;
    }
//    public void fillOpenings(TETile[][] w) {
//
//        if (openings.isEmpty()) {
//            return;
//        }
//        for (Point p: openings) {
//            boolean direction = false;
//            boolean orientation = false;
//            if (p.x_corr== source.x_corr-1 || p.x_corr==source.x_corr+width) {
//                orientation = true;
//            }
//            else
//                orientation = false;
//            if (p.y_corr!=source.y_corr+height && p.x_corr!=source.x_corr+width)
//                direction = false;
//            else
//                direction = true;
//            Hallway h = new Hallway(rng.nextInt(8)+3, rng.nextInt(2)+1, p, orientation, direction,rng);
//            h.createHall(w);
//        }
//    }


    public void fillEnd(TETile[][] canvas, int recursionDepth) {

        boolean filled = false;
        if (recursionDepth>7) {
            return;
        }
        int hallOrRoom = rng.nextInt(3);
        if (hallOrRoom >= 1) {
            for (int x = 12; x>0; x--) {
                Room j = new Room(rng.nextInt(x) + 2, rng.nextInt(x+1) + 2,
                        endPointFirst, rng, true, direction, recursionDepth + 1);
                boolean y = j.outOfBoundDetector(canvas);
                if (y || j.conflict(canvas)) {
                    continue;
                }
                else {
                    filled = j.createRoom(canvas);
                    break;
                }
            }

            if (filled) {
                canvas[endPointSecond.x_corr][endPointSecond.y_corr] = Tileset.FLOOR;
            }
            else {
                if (!horizontal&&!direction) {
                    createLine(endPointSecond.x_corr-width,endPointSecond.x_corr+1,
                            endPointSecond.y_corr,endPointSecond.y_corr,Tileset.COCONUT,canvas);
                }
                if (horizontal&&direction) {
                    createLine(endPointSecond.x_corr,endPointSecond.x_corr,
                            endPointSecond.y_corr-1,endPointSecond.y_corr+width,Tileset.COCONUT,canvas);
                }
                if (!horizontal&&direction) {
                    createLine(endPointSecond.x_corr-1,endPointSecond.x_corr+width,
                            endPointSecond.y_corr,endPointSecond.y_corr,Tileset.COCONUT,canvas);
                }
                if (horizontal&&!direction) {
                    createLine(endPointSecond.x_corr,endPointSecond.x_corr,
                            endPointSecond.y_corr-width,endPointSecond.y_corr+1,Tileset.COCONUT,canvas);
                }
            }
        }
        else {
            boolean newDirection = rng.nextInt(2)==1;

            Hallway j = new Hallway(rng.nextInt(12) + 2,
                    rng.nextInt(2) + 1, endPointFirst, !horizontal, newDirection,
                    rng,recursionDepth+1);
            filled = j.createHall(canvas);

            if (filled) {
                canvas[endPointSecond.x_corr][endPointSecond.y_corr] = Tileset.FLOOR;

                if (horizontal&&direction&&newDirection) {
                    createLine(endPointSecond.x_corr,endPointSecond.x_corr+j.width+1,
                            endPointSecond.y_corr-1,endPointSecond.y_corr-1,Tileset.COCONUT,canvas);
                }
                else if (!horizontal&&direction&&newDirection) {
                    createLine(endPointSecond.x_corr-1,endPointSecond.x_corr-1,
                            endPointSecond.y_corr,endPointSecond.y_corr+j.width+1,Tileset.COCONUT,canvas);
                }
                else if (horizontal&&direction&&!newDirection) {
                    createLine(endPointSecond.x_corr,endPointSecond.x_corr+j.width+1,
                            endPointSecond.y_corr +width,endPointSecond.y_corr+width,Tileset.COCONUT,canvas);

                    createLine(endPointSecond.x_corr,endPointSecond.x_corr+j.width,
                            endPointSecond.y_corr +width -1,endPointSecond.y_corr+width -1,Tileset.FLOOR,canvas);

                    createLine(endPointSecond.x_corr+j.width+1,endPointSecond.x_corr+j.width+1,
                            endPointSecond.y_corr ,endPointSecond.y_corr+width-1,Tileset.COCONUT,canvas);
                }
                else if (!horizontal&&!direction&&!newDirection) {
                    createLine(endPointSecond.x_corr+1,endPointSecond.x_corr+1,
                            endPointSecond.y_corr-j.width-1,endPointFirst.y_corr+1,Tileset.COCONUT,canvas);
                }

                else if (horizontal&&!direction&&!newDirection) {
                    createLine(endPointSecond.x_corr-j.width-1,endPointSecond.x_corr,
                            endPointSecond.y_corr+1,endPointFirst.y_corr+1,Tileset.COCONUT,canvas);
                }

                else if (!horizontal&&direction&&!newDirection) {
                    createLine(endPointSecond.x_corr,endPointSecond.x_corr+width,
                            endPointSecond.y_corr +j.width+1,endPointSecond.y_corr +j.width+1,Tileset.COCONUT,canvas);

                    createLine(endPointSecond.x_corr+width-1,endPointSecond.x_corr+width-1,
                            endPointSecond.y_corr ,endPointSecond.y_corr+j.width ,Tileset.FLOOR,canvas);

                    createLine(endPointSecond.x_corr+width,endPointSecond.x_corr+width,
                            endPointSecond.y_corr ,endPointSecond.y_corr+j.width+1,Tileset.COCONUT,canvas);
                }

                else if (horizontal&&!direction&&newDirection) {
                    createLine(endPointSecond.x_corr-j.width-1,endPointSecond.x_corr,
                            endPointSecond.y_corr -width,endPointSecond.y_corr -width,Tileset.COCONUT,canvas);

                    createLine(endPointSecond.x_corr-j.width,endPointSecond.x_corr,
                            endPointSecond.y_corr-width+1 ,endPointSecond.y_corr-width+1 ,Tileset.FLOOR,canvas);

                    createLine(endPointSecond.x_corr-j.width-1,endPointSecond.x_corr-j.width-1,
                            endPointSecond.y_corr-width ,endPointSecond.y_corr,Tileset.COCONUT,canvas);
                }

                else if (!horizontal&&!direction&&newDirection) {
                    createLine(endPointSecond.x_corr-width,endPointSecond.x_corr-width,
                            endPointSecond.y_corr - j.width-1,endPointSecond.y_corr,Tileset.COCONUT,canvas);

                    createLine(endPointSecond.x_corr-width+1,endPointSecond.x_corr-width+1,
                            endPointSecond.y_corr-j.width ,endPointSecond.y_corr ,Tileset.FLOOR,canvas);

                    createLine(endPointSecond.x_corr-width,endPointSecond.x_corr,
                            endPointSecond.y_corr-j.width-1 ,endPointSecond.y_corr-j.width-1,Tileset.COCONUT,canvas);
                }


            }

            else {
                if (!horizontal&&!direction) {
                    createLine(endPointSecond.x_corr-width,endPointSecond.x_corr+1,
                            endPointSecond.y_corr,endPointSecond.y_corr,Tileset.COCONUT,canvas);
                }
                if (horizontal&&direction) {
                    createLine(endPointSecond.x_corr,endPointSecond.x_corr,
                            endPointSecond.y_corr-1,endPointSecond.y_corr+width,Tileset.COCONUT,canvas);
                }
                if (!horizontal&&direction) {
                    createLine(endPointSecond.x_corr-1,endPointSecond.x_corr+width,
                            endPointSecond.y_corr,endPointSecond.y_corr,Tileset.COCONUT,canvas);
                }
                if (horizontal&&!direction) {
                    createLine(endPointSecond.x_corr,endPointSecond.x_corr,
                            endPointSecond.y_corr-width,endPointSecond.y_corr+1,Tileset.COCONUT,canvas);
                }
            }
        }

    }

    public boolean createHall(TETile[][] canvas) {
        int x = source.x_corr;
        int y = source.y_corr;

        // start building the hallway
        // with the bottom left grid
        if (outOfBoundDetector(canvas) || conflict(canvas)) {
            return false;
        }

        if (direction) {
            if (horizontal) {
                endPointFirst = new Point(x+height+1,y,Tileset.AVATAR);
                endPointSecond = new Point(x+height,y,Tileset.AVATAR);
                createLine(x,x+height-1,y,y,Tileset.FLOOR,canvas);
                createLine(x,x+height-1,y+width-1,y+width-1,Tileset.FLOOR,canvas);
                createLine(x,x+height-1,y-1,y-1,Tileset.COCONUT,canvas);
                createLine(x,x+height-1,y+width,y+width,Tileset.COCONUT,canvas);


            } else {
                endPointFirst = new Point(x,y+height+1,Tileset.AVATAR);
                endPointSecond = new Point(x,y+height,Tileset.AVATAR);

                createLine(x,x,y,y+height-1,Tileset.FLOOR,canvas);
                createLine(x+width-1,x+width-1,y,y+height-1,Tileset.FLOOR,canvas);
                createLine(x-1,x-1,y,y+height-1,Tileset.COCONUT,canvas);
                createLine(x+width,x+width,y,y+height-1,Tileset.COCONUT,canvas);

            }
        }
        else {

            if (horizontal) {
                endPointFirst = new Point(x-height-1,y,Tileset.AVATAR);
                endPointSecond = new Point(x-height,y,Tileset.AVATAR);
                createLine(x-height+1,x,y,y,Tileset.FLOOR,canvas);
                createLine(x-height+1,x,y-width+1,y-width+1,Tileset.FLOOR,canvas);
                createLine(x-height+1,x,y+1,y+1,Tileset.COCONUT,canvas);
                createLine(x-height+1,x,y-width,y-width,Tileset.COCONUT,canvas);

            }
            else {
                endPointFirst = new Point(x,y-height-1,Tileset.AVATAR);
                endPointSecond = new Point(x,y-height,Tileset.AVATAR);

                createLine(x,x,y-height+1,y,Tileset.FLOOR,canvas);
                createLine(x-width+1,x-width+1,y-height+1,y,Tileset.FLOOR,canvas);
                createLine(x+1,x+1,y-height+1,y,Tileset.COCONUT,canvas);
                createLine(x-width,x-width,y-height+1,y,Tileset.COCONUT,canvas);

            }


        }
        fillEnd(canvas,recursionDepth+1);
        return true;
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

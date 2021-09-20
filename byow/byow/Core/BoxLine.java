package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class BoxLine {
    private Point a;
    private Point b;
    private TETile[][] canvas;
    public BoxLine(Point a, Point b, TETile[][] canvas) {
        this.a = a;
        this.b = b;
        this.canvas = canvas;

    }

    public boolean conflictDetector() {
        return false;
    }

    public void createLine(TETile tileType) {
        // a.x is always less than or equal to b.x
        // a.y is always less than or equal to b.y
        for (int i = a.x_corr; i<= b.x_corr; i++) {
            for (int j = a.y_corr; j<=b.y_corr; j++) {
                canvas[i][j] = tileType;
            }
        }
    }

    public void createBox(TETile tileType) {

    }
}

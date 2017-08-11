import java.awt.*;

/**
 * Created by Maciek on 17.01.2017.
 */
public class Area {
    private int w;
    private int n;
    private Color terrain;
    private String ground;
    private int groundQl;
    private String tree; //also: building
    private int treeQl;

    public Area(int w, int n, Color terrain, String ground, int groundQl, String tree, int treeQl) {
        this.w = w;
        this.n = n;
        this.terrain = terrain;
        this.ground = ground;
        this.groundQl = groundQl;
        this.tree = tree;
        this.treeQl = treeQl;
    }

    public int getW() {
        return w;
    }

    public int getN() {
        return n;
    }

    public Color getTerrain() {
        return terrain;
    }

    public String getGroundType() {
        return ground;
    }

    public int getGroundQuality() {
        return groundQl;
    }

    public String getTreeType() {
        return tree;
    }

    public int getTreeQuality() {
        return treeQl;
    }

    public String toString() {
        if (tree == "" && w < 10000) return ("0" + w + "W" + n + "N " + ground + " " + groundQl);
        else if (tree == "" && w >= 10000) return (w + "W" + n + "N " + ground + " " + groundQl);
        else if (w < 10000) return ("0" + w + "W" + n + "N " + ground + " " + groundQl + " " + tree + " " + treeQl);
        else return (w + "W" + n + "N " + ground + " " + groundQl + " " + tree + " " + treeQl);
    }
}

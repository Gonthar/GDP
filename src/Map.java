/**
 * Created by Maciek on 21.01.2017.
 */
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Map extends JPanel {

    public static int rows = 40;
    public static int cols = 40;
    public static final int PREFERRED_GRID_SIZE_PIXELS = 25;

    private Color[][] terrainGrid;
    private int[][] qualityGrid;

    public Map(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.terrainGrid = new Color[rows][cols];
        this.qualityGrid = new int[rows][cols];
        int preferredWidth = cols * PREFERRED_GRID_SIZE_PIXELS;
        int preferredHeight = rows * PREFERRED_GRID_SIZE_PIXELS;
        setPreferredSize(new Dimension(preferredWidth, preferredHeight));
    }

    public void updateMap(int x, int y, Color color, int quality) {
        terrainGrid[x][y] = color;
        qualityGrid[x][y] = quality;
    }

    @Override
    public void paintComponent(Graphics g) {
        // Important to call super class method
        super.paintComponent(g);
        // Clear the board
        g.clearRect(0, 0, getWidth(), getHeight());
        // Draw the grid
        int rectWidth = getWidth() / cols;
        int rectHeight = getHeight() / rows;

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                // Lower right corner of this terrain rect
                int x = i * rectWidth;
                int y = j * rectHeight;
                Color terrainColor = terrainGrid[i][j];
                g.setColor(terrainColor);
                g.fillRect(x, y, rectWidth, rectHeight);
                g.setColor(Color.black);
                g.drawRect(x, y, rectWidth, rectHeight);
                // quality values
                if (qualityGrid[i][j] != 0) {
                    g.setColor(Color.black);
                    g.drawString(String.valueOf(qualityGrid[i][j]), x, y + rectHeight);
                }
            }
        }
    }
}

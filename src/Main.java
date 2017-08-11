import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.util.concurrent.*;

public class Main {
    private static final int STW = 8376;
    private static final int STN = 13529;
    private static final int SIZE = 110;
    private static int TIMES;

    public void seek(int w, int n) throws Exception {
        LinkedList<Area> db = new LinkedList<>();
        Map map = new Map(SIZE * TIMES, SIZE * TIMES);
        Map treemap = new Map(SIZE * TIMES, SIZE * TIMES);
        List<Future<?>> futures = new LinkedList<>();
        ExecutorService exec = Executors.newFixedThreadPool(100);

        for (int i = SIZE - 1; i >= 0; --i) {
            for (int j = SIZE - 1; j >= 0; --j) {
                Runnable digger = new Digger(w, n, i, j, db, map, treemap);
                Future<?> f = exec.submit(digger);
                futures.add(f);
            }
        }

        for (Future<?> future : futures) {
            future.get();
        }

        exec.shutdown();

        //new FileOutputStream("0" + String.valueOf(w) + "W " + String.valueOf(n) + "N.txt"), "utf-8"))) {
        /*
        */

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Game");
                frame.add(map);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Trees");
                frame.add(treemap);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            try {
                TIMES = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
                System.exit(1);
            }
        }

        Main m = new Main();
        for (int i = 0; i < TIMES; ++i) {
            for (int j = 0; j < TIMES; ++j) {
                m.seek(STW - i * SIZE, STN - j * SIZE);
            }
        }
    }
}
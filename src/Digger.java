
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.awt.*;

public class Digger implements Runnable {
    private int w;
    private int n;
    private int x;
    private int y;
    private LinkedList<Area> db;
    private Map map;
    private Map treemap;

    public Digger(int w, int n, int x, int y, LinkedList<Area> db, Map map, Map treemap) {
        this.w = w;
        this.n = n;
        this.x = x;
        this.y = y;
        this.db = db;
        this.map = map;
        this.treemap = treemap;
    }

    private Color assignColor(String ground) {
        Color c;
        switch (ground) {
            case "Plain": c = new Color(255,204,102);
                break;
            case "Meadow": c = new Color(102,153,0);
                break;
            case "Bank":
            case "Coast": c = new Color(238,232,170);
                break;
            case "Shrubland": c = new Color(128,128,0);
                break;
            case "Bushland": c = new Color(119,93,61);
                break;
            case "Clayey soil": c = new Color(153, 102, 51);
                break;
            case "Schist": c = new Color(205,204,202);
                break;
            case "Chalky soil": c = new Color(215,214,212);
                break;
            case "Salt flat": c = new Color(186,85,211);
                break;
            case "Limey plateau": c = new Color(175,174,172);
                break;
            case "Sea":
            case "Ocean":
            case "Canal":
            case "Pond":
            case "River": c = new Color(30,144,255);
                break;
            case "Marsh":
            case "Salt marsh": c = new Color(180,240,180);
                break;
            case "Tundra": c = new Color(130,137,121);
                break;
            case "Copper deposit":
            case "Tin deposit":
            case "Iron deposit":
            case "Lead deposit": c = new Color(184, 115, 51);
                break;
            case "Diamond deposit":
            case "Sapphire deposit":
            case "Emerald deposit":
            case "Ruby deposit": c = new Color(255,69,0);
                break;
            case "Gold deposit":
            case "Silver deposit": c = new Color(255,120,0);
                break;
            case "Sulphur deposit": c = new Color(255,255,51);
                break;
            case "Marble deposit": c = new Color(211,211,211);
                break;
            case "Basaltic rock":
            case "Granitic rock":
            case "Limestone":
            case "Sandstone": c = new Color(128,128,128);
                break;
            case "Volcano": c = new Color(255,0,255);
                break;
            default: c = new Color(240,240,240);
                break;
        }
        return c;
    }

    private Color assignTreeColor(String tree) {
        Color c;
        if (tree.endsWith("maples")) c = new Color(146, 69, 68);
        else if (tree.endsWith("pine trees")) c = new Color(40, 88, 71);
        else if (tree.endsWith("nutmeg trees")) c = new Color(120,72,57);
        else if (tree.endsWith("holm oaks")) c = new Color(174, 159, 128);
        else if (tree.endsWith("oaks")) c = new Color(206, 187, 158);
        else if (tree.endsWith("cork oaks")) c = new Color(156, 111, 62);
        else if (tree.endsWith("apple trees")) c = new Color(91, 194, 54);
        else if (tree.endsWith("heveas")) c = new Color(119, 136, 153);
        else if (tree.endsWith("mahoganies")) c = new Color(192, 64, 0);
        else if (tree.endsWith("teaks")) c = new Color(163,139,95);
        else if (tree.endsWith("apok trees")) c = new Color(152,251,152);
        else if (tree.endsWith("anana trees")) c = new Color(255, 225, 53);
        else if (tree.endsWith("walnut trees")) c = new Color(78, 71, 59);
        else if (tree.endsWith("acacias")) c = new Color(254, 237, 186);
        else if (tree.endsWith("alm trees")) c = new Color(116, 181, 96);
        else if (tree.endsWith("plum trees")) c = new Color(142, 69, 133);
        else if (tree.endsWith("hazels")) c = new Color(174, 159, 128);
        else if (tree.endsWith("alm trees")) c = new Color(116, 181, 96);
        else if (tree.endsWith("poplars")) c = new Color(220, 208,92);
        else if (tree.endsWith("alders")) c = new Color(184, 184, 184);
        else if (tree.endsWith("birches")) c = new Color(246, 246, 246);
        else if (tree.endsWith("beeches")) c = new Color(186, 168, 104);
        else if (tree.endsWith("bamboos")) c = new Color(22,124,50);
        else if (tree.endsWith("firs")) c = new Color(25, 57, 37);
        else if (tree.endsWith("junipers")) c = new Color(93,120,93);
        else if (tree.endsWith("spruces")) c = new Color(18, 85, 43);
        else if (tree.endsWith("cedars")) c = new Color(104, 38, 0);
        else if (tree.endsWith("chestnut trees")) c = new Color(149, 69, 53);
        else if (tree.endsWith("cypresses")) c = new Color(54, 74, 32);
        else if (tree.endsWith("ash trees")) c = new Color(178, 190, 181);
        else if (tree.endsWith("lime trees")) c = new Color(69, 237, 83);
        else if (tree.endsWith("larches")) c = new Color(33,94,63);
        else if (tree.endsWith("alders")) c = new Color(174, 142, 127);
        else if (tree.endsWith("pear trees")) c = new Color(195, 217, 56);
        else if (tree.endsWith("cherry trees")) c = new Color(166, 31, 52);
        else if (tree.endsWith("almond trees")) c = new Color(	255,	235,	205);
        else if (tree.endsWith("oconut trees")) c = new Color( 247, 249, 224);



        else c = new Color(255,204,102);
        return c;
    }

    private int parseQuality(Element e) {
        String QualityString = e.attr("title");
        String QualityValue = QualityString.replaceAll("[^0-9]", "");
        return Integer.parseInt(QualityValue);
    }

    private String generateUrl(int w, int n) {
        if (w < 10000) return "http://www.landsoflords.com/map/0" + String.valueOf(w) + "W" + String.valueOf(n) + "N";
        else return "http://www.landsoflords.com/map/" + String.valueOf(w) + "W" + String.valueOf(n) + "N";
    }

    public void run() {
        String url = generateUrl(w - x, n - y);
        try {
            Document document = Jsoup.connect(url).get();
            String tree = "";
            String ground = "";
            int treeQl = 0;

            //if there are trees/buildings
            if (document.select("div.qual.roundTop").size() > 0) {
                treeQl = parseQuality(document.getElementsByClass("qual roundTop").first());
                tree = document.select("h2").get(1).text(); //second
                ground = document.select("h2").get(2).text(); //third
            } else {
                try {
                    Element groundEl = document.select("h2").get(1); //second
                    ground = groundEl.text();
                } catch(Exception E) {
                    // informs us where exactly program failed
                    throw new Exception("spot: " + url, E);
                }
            }
            int groundQl = parseQuality(document.getElementsByClass("qual round").first());
            //deletes unnecessary data starting from " ("
            ground = ground.substring(0, ground.indexOf("(") - 1);
/*
            if ((ground.equals("Gold deposit") || ground.equals("Silver deposit")
                    || ground.equals("Emerald deposit") || ground.equals("Diamond deposit")
                    || ground.equals("Sapphire deposit") || ground.equals("Ruby deposit"))
                    && groundQl >= 75) {
                db.add(new Area(w - x, n - y, assignColor(ground), ground, groundQl, tree, treeQl));
            }
            */
/*
            if (tree.endsWith("chestnut trees") || tree.endsWith("rosewoods")
                    || tree.endsWith("mahoganies") || tree.endsWith("ash trees")
                    || tree.endsWith("cedars") || tree.endsWith("larches")
                    || tree.endsWith("beeches") || tree.endsWith("nutmeg trees")) {
                //here = new Area(w - x, n - y, assignColor(ground), "OMGOMGOMGOMG" + ground, groundQl, tree, treeQl);
                //db.add(here);
                System.out.println(here.toString());
            }
            */

            if (tree.endsWith("weirwoods")) {
                //here = new Area(w - x, n - y, assignColor(ground), "OMGOMGOMGOMG" + ground, groundQl, tree, treeQl);
                //db.add(here);
                System.out.println("OMGOMGOMGOMGOMGOMGOMGOMGOMGOMGOMGOMGOMG " + (w - x) + "W " + (n - y) + "N");
            }

             /********
             try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(System.lineSeparator());
                    sb.append(here.toString());
                    Files.write(Paths.get("out.txt"), sb.toString().getBytes(), StandardOpenOption.APPEND);
                } catch (Exception ex) {}
            }
             */
            /*
            if (groundQl >= 80 && (ground.endsWith("deposit") || ground.equals("Granitic rock"))) {
                db.add(new Area(w - x, n - y, assignColor(ground), ground, groundQl, tree, treeQl));
            }
            if (groundQl >= 95) {
                db.add(new Area(w - x, n - y, assignColor(ground), ground, groundQl, tree, treeQl));
            }
            if (treeQl >= 80 && (tree.endsWith("oaks") || tree.endsWith("chestnut trees")
                    || tree.endsWith("larches") || tree.endsWith("teaks") || tree.endsWith("cedars"))) {
                db.add(new Area(w - x, n - y, assignColor(ground), ground, groundQl, tree, treeQl));
            }
            if (treeQl >= 90) {
                db.add(new Area(w - x, n - y, assignColor(ground), ground, groundQl, tree, treeQl));
            }
            if (groundQl >= 60 && (ground.endsWith("Clayey soil"))) {
                db.add(new Area(w - x, n - y, assignColor(ground), ground, groundQl, tree, treeQl));
            }
            */

            db.add(new Area(w, n, assignColor(ground), ground, groundQl, tree, treeQl));
            map.updateMap(x, y, assignColor(ground), groundQl);
            if (ground.equals("Pond") || ground.equals("River") || ground.equals("Sea")
                    || ground.equals("Ocean") || ground.equals("Canal")) {
               treemap.updateMap(x, y, new Color(30,144,255), 0);
            }else if (ground.equals("Basaltic rock") || ground.equals("Granitic rock") || ground.equals("Sandstone")
                    || ground.equals("Limestone") || ground.equals("Marble deposit") || ground.equals("Volcano")) {
                treemap.updateMap(x, y, new Color(28,28,28), 0);
            } else {
               treemap.updateMap(x, y, assignTreeColor(tree), treeQl);
            }
        } catch (Exception ex) {
            Thread t = Thread.currentThread();
            t.getUncaughtExceptionHandler().uncaughtException(t, ex);
        }
    }
}

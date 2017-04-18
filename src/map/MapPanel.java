/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author ABC
 */
public final class MapPanel extends JPanel {

    public Map<String, Street> streets;
    public Map<Coordinate, MeetingPoint> mps;
    public List<String> listStreets;

    Graphics g;

    public MapPanel() {
        streets = new HashMap<>();
        mps = new HashMap<>();
        listStreets = new ArrayList<>();
        printCordinate();
        this.setBorder(BorderFactory.createLineBorder(Color.RED));
    }

// <editor-fold defaultstate="collapsed" desc="paint component">  
    @Override
    public void paintComponent(Graphics g) {
        this.g = g;
        super.paintComponent(this.g);
        drawMap();
        loadData();
        checkData();
    }

    //</editor-fold>
// <editor-fold defaultstate="collapsed" desc="calculateLength">  
    private double calculateStreetLength(List<Coordinate> c) {
        double length = 0;
        for (int i = 0; i < c.size() - 1; i++) {
            Coordinate get1 = c.get(i);
            Coordinate get2 = c.get(i + 1);
            length = length + calculateLength(get2, get2);
        }
        return length;
    }

    private double calculateLength(Coordinate start, Coordinate end) {
        return Math.sqrt((Math.abs(start.getX() - end.getX()) * Math.abs(start.getX() - end.getX())) + (Math.abs(start.getY() - end.getY()) - Math.abs(start.getY() - end.getY())));
    }
//</editor-fold>
// <editor-fold defaultstate="collapsed" desc="draw background">  

    public void drawMap() {
        try {
            BufferedImage img = ImageIO.read(new File("src/map/peta.png"));
            int imgWidth = img.getWidth();
            int imgHeight = img.getHeight();
            int panelWidth = super.getWidth();
            int panelHeight = super.getHeight();
            double imgAspect = (double) imgHeight / imgWidth;
            double panelAspect = (double) panelHeight / panelWidth;
            int x1 = 0;
            int x2;
            int y1 = 0;
            int y2;
            if (imgWidth < panelWidth && imgHeight < panelHeight) {
                x1 = (panelWidth - imgWidth) / 2;
                y1 = (panelHeight - imgHeight) / 2;
                x2 = imgWidth + x1;
                y2 = imgHeight + y1;
            } else {
                if (panelAspect > imgAspect) {
                    y1 = panelHeight;
                    panelHeight = (int) (panelWidth * imgAspect);
                    y1 = (y1 - panelHeight) / 2;
                } else {
                    x1 = panelWidth;
                    panelWidth = (int) (panelHeight / imgAspect);
                    x1 = (x1 - panelWidth) / 2;
                }
                x2 = panelWidth + x1;
                y2 = panelHeight + y1;
            }
            g.drawImage(img, x1, y1, x2, y2, 0, 0, imgWidth, imgHeight, null);
        } catch (IOException ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //</editor-fold>
// <editor-fold defaultstate="collapsed" desc="read data">  
    public void loadData() {
        try {
            BufferedReader tmp = new BufferedReader(new FileReader("src/map/data jalan fix.txt"));
            Object[] a = tmp.lines().toArray();
            String b[];
            String[] c;
            for (Object object : a) {
                b = String.valueOf(object).split(":");
                c = b[1].split("-");
                List<Coordinate> coordinates = new ArrayList<>();
                for (String string : c) {
                    coordinates.add(new Coordinate(Integer.valueOf(string.split("/")[0]), Integer.valueOf(string.split("/")[1])));
                }
                streets.put(b[0], new Street(b[0], coordinates, calculateStreetLength(coordinates)));
                listStreets.add(b[0]);
            }
            //-----------------------------------------------------------------
            BufferedReader meet = new BufferedReader(new FileReader("src/map/data pertemuan fix.txt"));
            Object[] a1 = meet.lines().toArray();
            String[] b1;
            String[] c1;
            int i = 0;
            for (Object object : a1) {
                b1 = String.valueOf(object).split(":");
                Coordinate cor = new Coordinate(Integer.valueOf(b1[0].split("/")[0]), Integer.valueOf(b1[0].split("/")[1]));
                c1 = b1[1].split("-");
                mps.put(cor, new MeetingPoint(cor, c1));
//==============================================================
// <editor-fold defaultstate="collapsed" desc="development only : to draw string coordinate and map into panel">  
//            Object[] a = tmp.lines().toArray();
//            String b[];
//            String[] c;
//            for (Object object : a) {
//
//                b = String.valueOf(object).split(":");
//                c = b[1].split("-");
//
//                int[] x = new int[c.length];
//                int[] y = new int[c.length];
//                for (int j = 0; j < c.length; j++) {
//                    x[j] = Integer.valueOf(c[j].split("/")[0]);
//                    y[j] = Integer.valueOf(c[j].split("/")[1]);
//                }
//                g.drawPolyline(x, y, c.length);
//            }
//            Object[] a1 = meet.lines().toArray();
//            String[] b1;
//            int i = 0;
//            for (Object object : a1) {
//                b1 = String.valueOf(object).split(":")[0].split("/");
//                g.drawString(String.valueOf(object).split(":")[0], Integer.valueOf(b1[0]), Integer.valueOf(b1[1]));
//</editor-fold>
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//</editor-fold>
// <editor-fold defaultstate="collapsed" desc="print">  

    private void printCordinate() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                System.out.println("x Coordinate : " + me.getX());
                System.out.println("y Coordinate : " + me.getY());
            }
        });
    }

    private void printStreetData(Street street) {
        System.out.println("------------------");
        System.out.println("" + street.getName());
        for (int i = 0; i < street.getPoints().size(); i++) {
            Coordinate get = street.getPoints().get(i);
            System.out.print("X : " + get.getX() + " Y : " + get.getY());
            System.out.println("");
        }
        System.out.println("------------------");
    }

    private void printMeetData(MeetingPoint mp) {
        System.out.println("------------");
        System.out.println("Coordinate : " + mp.getCoordinate());
        System.out.println("Street     : " + mp.getStreet());
        System.out.println("------------");
    }

//</editor-fold>
// <editor-fold defaultstate="collapsed" desc="check data">  
    public void checkData() {
//        int i = 1;
//  print street data
//        for (Map.Entry<String, Street> entry : streets.entrySet()) {
//            String key = entry.getKey();
//            Street value = entry.getValue();
//            System.out.println("====== No " + i + " ======");
//            System.out.println("key : " + key);
//            System.out.println("data : ");
//            System.out.println("\tname : " + value.getName());
//            System.out.println("\tlength : " + value.getLength());
//            i++;
//        }
//print meeting coordinate data
//        for (Map.Entry<Coordinate, MeetingPoint> entry : mps.entrySet()) {
//            Coordinate key = entry.getKey();
//            MeetingPoint value = entry.getValue();
//            System.out.println("====== No " + i + " ======");
//            System.out.println("key : " + key.getX() + "/" + key.getY());
//            System.out.println("data : ");
//            System.out.println("\tmpCoordinate : " + value.getCoordinate().getX() + "->" + value.getCoordinate().getY());
//            for (int j = 0; j < value.getStreet().length; j++) {
//                String object = value.getStreet()[j];
//                System.out.println("\tmpStreet : " + object);
//            }
//            i++;
//        }
    }

    //</editor-fold>
    public void inferensi(String start, String end) {
        ArrayList<ArrayList<Street>> tmpTracks = new ArrayList<>();
        ArrayList<Street> tmpTrack = new ArrayList<>();
        System.out.println(cekKetemu(streets.get(start), streets.get(end)));
    }

    public boolean cekKetemu(Street start, Street end) {
        Coordinate startStart = start.getPoints().get(0);
        Coordinate endStart = start.getPoints().get(start.getPoints().size() - 1);
        Coordinate startEnd = end.getPoints().get(0);
        Coordinate endEnd = end.getPoints().get(end.getPoints().size() - 1);
        return cekKesamaanKordinat(startStart, startEnd) || cekKesamaanKordinat(startStart, endEnd) || cekKesamaanKordinat(endStart, startEnd) || cekKesamaanKordinat(endStart, endEnd);
    }

    public boolean cekKesamaanKordinat(Coordinate first, Coordinate second) {
        return first.getX() == second.getX() && first.getY() == second.getY();
    }
}

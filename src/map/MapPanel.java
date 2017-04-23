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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
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

    public HashMap<String, Street> streets;
    public HashMap<Coordinate, MeetingPoint> mps;
    public HashMap<String, ArrayList<String>> fakta;
    public ArrayList<String> listNamaJalan;
    public ArrayList<Street> jalan;
    Graphics g;

    public MapPanel() {
        streets = new HashMap<>();
        mps = new HashMap<>();
        fakta = new HashMap<>();
        listNamaJalan = new ArrayList<>();
        jalan = new ArrayList<>();
        printCordinate();
        this.setBorder(BorderFactory.createLineBorder(Color.RED));
        loadData();
//        checkData();
    }

// <editor-fold defaultstate="collapsed" desc="paint component">  
    @Override
    public void paintComponent(Graphics g) {
        this.g = g;
        super.paintComponent(this.g);
        drawMap();
        drawLine(jalan);
        drawStringCoordinate();
    }
//</editor-fold>
// <editor-fold defaultstate="collapsed" desc="calculateStreetLength">  

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

    public void drawLine(ArrayList<Street> streets) {
        if (streets.size() > 0) {
            for (int i = 0; i < streets.size(); i++) {
                int[] x = new int[streets.get(i).getPoints().size()];
                int[] y = new int[streets.get(i).getPoints().size()];
                for (int j = 0; j < streets.get(i).getPoints().size(); j++) {
                    x[j] = streets.get(i).getPoints().get(j).getX();
                    y[j] = streets.get(i).getPoints().get(j).getY();
                }
                g.setColor(Color.BLACK);
                g.drawPolyline(x, y, streets.get(i).getPoints().size());
                System.out.println(streets.get(i).getName());
                System.out.println(Arrays.toString(x));
                System.out.println(Arrays.toString(y));
            }
        }
    }

    public void drawStringCoordinate() {
        mps.entrySet().forEach((entry) -> {
            g.drawString(String.valueOf(entry.getValue().getCoordinate().getX() + "/" + entry.getValue().getCoordinate().getY()), entry.getValue().getCoordinate().getX(), entry.getValue().getCoordinate().getY());
        });
    }
//</editor-fold>
// <editor-fold defaultstate="collapsed" desc="read data">  

    public void loadData() {
        try {
//fill the streets
            BufferedReader tmp = new BufferedReader(new FileReader("src/jalan"));
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
                jalan.add(new Street(b[0], coordinates, calculateStreetLength(coordinates)));
                listNamaJalan.add(b[0]);
            }
//fill the fakta
            BufferedReader fact = new BufferedReader(new FileReader("src/fakta"));
            Object[] fa = fact.lines().toArray();
            String fb[];
            String fc[];
            for (Object object : fa) {
                fb = String.valueOf(object).split(":");
                fc = fb[1].split("-");
                ArrayList<String> faa = new ArrayList<>();
                faa.addAll(Arrays.asList(fc));
                fakta.put(fb[0], faa);
            }
//fill the mps
//            BufferedReader meet = new BufferedReader(new FileReader("src/pertemuan"));
            BufferedReader meet = new BufferedReader(new FileReader("src/tesdatapertemuan.txt"));
            Object[] a1 = meet.lines().toArray();
            String[] b1;
            String[] c1;
            for (Object object : a1) {
                b1 = String.valueOf(object).split(":");
                Coordinate cor = new Coordinate(Integer.valueOf(b1[0].split("/")[0]), Integer.valueOf(b1[0].split("/")[1]));
                c1 = b1[1].split("-");
                ArrayList<Street> c1Streets = new ArrayList<>();
                for (String c11 : c1) {
                    c1Streets.add(streets.get(c11));
                }
                mps.put(cor, new MeetingPoint(cor, c1Streets));
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
        System.out.println("Street     : " + Arrays.toString(mp.getStreets().toArray()));
        System.out.println("------------");
    }
//</editor-fold>
// <editor-fold defaultstate="collapsed" desc="check data">  

    public void checkData() {
        int i = 1;
// print street data
        for (HashMap.Entry<String, Street> entry : streets.entrySet()) {
            String key = entry.getKey();
            Street value = entry.getValue();
            System.out.println("====== No " + i + " ======");
            System.out.println("key : " + key);
            System.out.println("data : ");
            System.out.println("\tname : " + value.getName());
            System.out.println("\tlength : " + value.getLength());
            i++;
        }
// print meeting coordinate data
        for (HashMap.Entry<Coordinate, MeetingPoint> entry : mps.entrySet()) {
            Coordinate key = entry.getKey();
            MeetingPoint value = entry.getValue();
            System.out.println("====== No " + i + " ======");
            System.out.println("key : " + key.getX() + "/" + key.getY());
            System.out.println("data : ");
            System.out.println("\tmpCoordinate : " + value.getCoordinate().getX() + "->" + value.getCoordinate().getY());
            for (int j = 0; j < value.getStreets().size(); j++) {
                Street object = value.getStreets().get(j);
                System.out.println("\tmpStreet : " + object.getName());
            }
            i++;
        }
    }

    //</editor-fold>
    public void konversiJalan(ArrayList<String> track) {
        jalan.clear();
        track.forEach((street) -> {
            jalan.add(streets.get(street));
        });
    }

    private ArrayList<String> getShortestTrack(ArrayList<ArrayList<String>> tracktrack) {
        double length = 0;
        int index = 0;
        for (int i = 0; i < tracktrack.size(); i++) {
            ArrayList<String> track = tracktrack.get(i);
            System.out.println("tracktrack.get-" + i + "-.size=" + track.size());
            double x = 0;
            for (int j = 0; j < track.size(); j++) {
                String get = track.get(j);
                System.out.print(get + "-");
                x = x + streets.get(get).getLength();
            }
            System.out.println("");
            if (i == 0) {
                length = x;
            } else {
                if (x < length) {
                    length = x;
                    index = i;
                }
            }
            System.out.println("x=" + x);
            System.out.println("length=" + length);
        }
        return tracktrack.get(index);
    }

    private void printTracktrack(ArrayList<ArrayList<String>> tracktrack) {
        for (int i = 0; i < tracktrack.size(); i++) {
            System.out.println("============");
            ArrayList<String> x = tracktrack.get(i);
            System.out.println("tracktrack.get-" + i);
            for (int j = 0; j < x.size(); j++) {
                System.out.print(x.get(j) + "-");
            }
            System.out.println("");
        }
    }

    private void printTrack(ArrayList<String> track) {
        System.out.println("++++++++++");
        for (int i = 0; i < track.size(); i++) {
            System.out.println(track.get(i));
        }
    }

    public ArrayList<String> inferensi(String start, String end) {
        ArrayList<ArrayList<String>> tracktrack = new ArrayList<>();
        Stack<Stack<String>> stackstack = new Stack<>();
        ArrayList<String> track = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        String tmpStart;
        if (start.equals(end)) {
            track.add(start);
            tracktrack.add(track);
        } else {
            stack.addAll(fakta.get(start));
            stackstack.push(stack);
            while (!stackstack.isEmpty() && tracktrack.size() <= 100) {
                if (!stackstack.peek().isEmpty()) {
                    tmpStart = stackstack.peek().pop();
                    if (!(track.contains(tmpStart)) && !(start.equals(tmpStart))) {
                        if (tmpStart.equals(end)) {
                            System.out.println("--->>>ketemu");
                            track.add(tmpStart);
                            ArrayList<String> a = new ArrayList<>();
                            a.addAll(track);
                            tracktrack.add(a);
//                            printTrack(track);
                            track.clear();
                        } else {
                            track.add(tmpStart);
//                            printTrack(track);
                            if (fakta.get(tmpStart) != null) {
                                stack.clear();
                                stack.addAll(fakta.get(tmpStart));
                                stackstack.add(stack);
                            }
                        }
                    }
                } else {
                    stackstack.pop();
                }
            }
        }
//        printTracktrack(tracktrack);
        track.clear();
        track.addAll(getShortestTrack(tracktrack));
        track.add(0, start);
        return track;
    }

    public void searchTrack(String start, String end, String via) {
        ArrayList<String> track = new ArrayList<>();
        if (via.isEmpty()) {
            track.addAll(inferensi(start, end));
        } else {
            track.addAll(inferensi(start, via));
            track.addAll(inferensi(via, end));
        }
        konversiJalan(track);
        this.repaint();
    }
}

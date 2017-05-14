/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
    public String start;
    public String end;
    public String via;
    int xCor, yCor;
    Graphics g;

    public MapPanel() {
        streets = new HashMap<>();
        mps = new HashMap<>();
        fakta = new HashMap<>();
        listNamaJalan = new ArrayList<>();
        jalan = new ArrayList<>();
        start = null;
        end = null;
        via = null;
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
//        drawStringCoordinate();
    }
//</editor-fold>
// <editor-fold defaultstate="collapsed" desc="calculateStreetLength">  

    private double calculateStreetLength(ArrayList<Coordinate> c) {
        double length = 0.0;
        for (int i = 0; i < c.size() - 1; i++) {
            Coordinate get1 = c.get(i);
            Coordinate get2 = c.get(i + 1);
            length = length + calculateLength(get1, get2);
        }
        return length;
    }

    private double calculateLength(Coordinate start, Coordinate end) {
        return (double) Math.sqrt((Math.abs(start.getX() - end.getX()) * Math.abs(start.getX() - end.getX())) + (Math.abs(start.getY() - end.getY()) * Math.abs(start.getY() - end.getY())));
    }

    public String getNearestStreet(Coordinate coor) {
        double distance = 0.0;
        Coordinate x = null;
        String street = null;
        for (Map.Entry<String, Street> entry : streets.entrySet()) {
            for (int i = 0; i < entry.getValue().getPoints().size(); i++) {
                double tmpDistance = calculateLength(coor, entry.getValue().getPoints().get(i));
                if (distance == 0.0) {
                    distance = tmpDistance;
                    street = entry.getValue().getName();
                    x = entry.getValue().getPoints().get(i);
                } else if (tmpDistance < distance) {
                    distance = tmpDistance;
                    street = entry.getValue().getName();
                    x = entry.getValue().getPoints().get(i);
                }
            }
        }
        System.out.println("--> " + x.getX() + " / " + x.getY());
        return street;
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
//                System.out.println("----gambar jalan----");
//                System.out.println(streets.get(i).getName());
//                System.out.println(Arrays.toString(x));
//                System.out.println(Arrays.toString(y));
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
                ArrayList<Coordinate> coordinates = new ArrayList<>();
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
            BufferedReader meet = new BufferedReader(new FileReader("src/pertemuan"));
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
//        for (HashMap.Entry<String, Street> entry : streets.entrySet()) {
//            String key = entry.getKey();
//            Street value = entry.getValue();
//            System.out.println("====== No " + i + " ======");
//            System.out.println("key : " + key);
//            System.out.println("data : ");
//            System.out.println("\tname : " + value.getName());
//            System.out.println("\tlength : " + value.getLength());
//            System.out.println("fakta : " + Arrays.toString(fakta.get(value.getName()).toArray()));
//            i++;
//        }
// print meeting coordinate data
//        for (HashMap.Entry<Coordinate, MeetingPoint> entry : mps.entrySet()) {
//            Coordinate key = entry.getKey();
//            MeetingPoint value = entry.getValue();
//            System.out.println("====== No " + i + " ======");
//            System.out.println("key : " + key.getX() + "/" + key.getY());
//            System.out.println("data : ");
//            System.out.println("\tmpCoordinate : " + value.getCoordinate().getX() + "->" + value.getCoordinate().getY());
//            for (int j = 0; j < value.getStreets().size(); j++) {
//                Street object = value.getStreets().get(j);
//                System.out.println("\tmpStreet : " + object.getName());
//            }
//            i++;
//        }
//
//print fakta
        System.out.println("print fakta");
        for (HashMap.Entry<String, ArrayList<String>> entry : fakta.entrySet()) {
            String key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            System.out.println(key + "->" + Arrays.toString(value.toArray()));
        }
    }

    //</editor-fold>
    public void konversiJalan(ArrayList<String> track) {
        System.out.println("masuk konversi jalan");
        System.out.println(Arrays.toString(track.toArray()));
        jalan.clear();
        track.forEach((street) -> {
            jalan.add(streets.get(street));
        });
    }

    private ArrayList<String> getShortestTrack(ArrayList<ArrayList<String>> tracktrack, String start, String end) {
        System.out.println("jumlah hasil : " + tracktrack.size());
        double length = -1.0;
        int index = -1;
        for (int i = 0; i < tracktrack.size(); i++) {
            ArrayList<String> track = tracktrack.get(i);
            System.out.println("tracktrack.get-" + i + "-.size=" + track.size());
            double x = 0.0;
            for (int j = 0; j < track.size(); j++) {
                System.out.print(track.get(j) + "(" + streets.get(track.get(j)).getLength() + ")" + "-");
                x = x + streets.get(track.get(j)).getLength();
            }
            System.out.println("");
            if (i == 0) {
                length = x;
                index = i;
            } else {
                if (x < length) {
                    length = x;
                    index = i;
                }
                System.out.println("x=" + x);
                System.out.println("length=" + length);
            }
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
            System.out.print(track.get(i) + "-");
        }
        System.out.println("");
    }

    public void searchTrack(String start, String end, String via) {
        ArrayList<String> track = new ArrayList<>();
        if (via.length() <= 0) {
            track.addAll(cari(start, end));

        } else {
            track.addAll(cari(start, via));
            track.addAll(cari(via, end));
        }
        konversiJalan(track);
        this.repaint();
    }

    public ArrayList<String> cariRute(String start, String end) {
        ArrayList<String> track = new ArrayList<>();
        ArrayList<ArrayList<String>> tracktrack = new ArrayList<>();
        
        return track;
    }

    public ArrayList<String> cari(String start, String end) {
        ArrayList<ArrayList<String>> tracktrack = new ArrayList<>();
        ArrayList<String> track = new ArrayList<>();
        ArrayList<String> faktaDone = new ArrayList<>();
        Stack<Stack<String>> stackstack = new Stack<>();
        Stack<String> stack = new Stack<>();
        String tmpStart;
        if (start.equals(end)) {
            System.out.println("start equals end");
            track.add(end);
        } else {
            tmpStart = start;
            System.out.println("start not equals end");
            System.out.println("first add into stack " + Arrays.toString(fakta.get(tmpStart).toArray()));
            stack.addAll(fakta.get(tmpStart));
            faktaDone.add(tmpStart);
            track.add(tmpStart);
            stackstack.push(stack);
            while (!stackstack.isEmpty()) {
                System.out.println("stackstack.peek.isempty : " + stackstack.peek().isEmpty());
                if (!stackstack.peek().isEmpty()) {
                    System.out.println("stackstack size : " + stackstack.size());
                    System.out.println("stackstack.peek size : " + stackstack.peek().size());
                    tmpStart = stackstack.peek().pop();
                    if (tmpStart.equals(end)) {
                        System.out.println("----->ketemu");
                        track.add(tmpStart);
                        tracktrack.add(track);
                    } else {
                        if (track.contains(tmpStart)) {
                            System.out.println("tmpStart dibuang : " + tmpStart);
                        } else {
                            System.out.println("add into track : " + tmpStart);
                            track.add(tmpStart);
                            System.out.println("clearing stack");
                            stack = new Stack<>();
                            System.out.println("add into stack : " + Arrays.toString(fakta.get(tmpStart).toArray()));
                            if (faktaDone.contains(tmpStart)) {
                                System.out.println("fakta has been added");
                            } else {
                                stack.addAll(fakta.get(tmpStart));
                                faktaDone.add(tmpStart);
                                stackstack.push(stack);
                            }
                        }
                    }
                } else {
                    System.out.println("--------stackstack pop");
                    stackstack.pop();
                }
            }
            track = new ArrayList<>();
            track.addAll(getShortestTrack(tracktrack, start, end));
//            track.add(0, start);
        }
        return track;
    }
}

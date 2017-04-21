/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author ABC
 */
public class ITBMap extends JFrame {

    private JLabel lblAsal;
    private JLabel lblTujuan;
    private JTextField inAsal;
    private JTextField inTujuan;
    private JButton btnSubmit;
    private MapPanel pan;
    private AutoCompletter autoComplete;

    public ITBMap() {
        init();
//        setTextField();
    }

// <editor-fold defaultstate="collapsed" desc="inisialisai tampilan">  
    private void init() {
        lblAsal = new JLabel();
        inAsal = new JTextField();
        lblTujuan = new JLabel();
        inTujuan = new JTextField();
        btnSubmit = new JButton();
        pan = new MapPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(false);
        setResizable(false);
        setUndecorated(false);

        lblAsal.setText("Asal : ");

        lblTujuan.setText("Tujuan : ");

        btnSubmit.setText("Submit");
        btnSubmit.addActionListener((ActionEvent ae) -> {
            btnSubmitAction(ae);
        });

        javax.swing.GroupLayout panLayout = new javax.swing.GroupLayout(pan);
        pan.setLayout(panLayout);
        panLayout.setHorizontalGroup(
                panLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        panLayout.setVerticalGroup(
                panLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 549, Short.MAX_VALUE)
        );
        //------------------
//        javax.swing.GroupLayout lineLayout = new javax.swing.GroupLayout(line);
//        line.setLayout(lineLayout);
//        lineLayout.setHorizontalGroup(
//                panLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGap(0, 0, Short.MAX_VALUE)
//        );
//        lineLayout.setVerticalGroup(
//                panLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGap(0, 549, Short.MAX_VALUE)
//        );
        //------------------
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblAsal)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(inAsal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblTujuan)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(inTujuan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnSubmit)
                                                .addGap(0, 324, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblAsal)
                                        .addComponent(inAsal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblTujuan)
                                        .addComponent(inTujuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSubmit))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }

    //</editor-fold>
//<editor-fold defaultstate="collapsed" desc="main method">
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            ITBMap m = new ITBMap();
            m.setLocationRelativeTo(null);
            m.setVisible(true);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ITBMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//</editor-fold>

    private void setTextField() {
        inAsal.getDocument().addDocumentListener(new AutoCompletter(inAsal, pan.listStreets));
        inTujuan.getDocument().addDocumentListener(new AutoCompletter(inTujuan, pan.listStreets));
        for (int i = 0; i < pan.listStreets.size(); i++) {
            System.out.println(pan.listStreets.get(i));
        }
        System.out.println(pan.listStreets.size());
    }

    private void btnSubmitAction(ActionEvent ae) {
        System.out.println("---------");
        pan.inferensi(inAsal.getText(), inTujuan.getText());
    }
}

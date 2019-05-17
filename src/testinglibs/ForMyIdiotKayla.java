/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testinglibs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import static java.awt.SystemColor.text;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author aribdhuka
 */
public class ForMyIdiotKayla extends javax.swing.JFrame{

    /**
     * Creates new form ForMyIdiotKayla
     */
    public ForMyIdiotKayla() {
 
        //create stuffs
        initComponents();
        
        //create drawings
        createDrawing();
        
    }
    
    /**
     * returns String (lat + "," + long) of corresponding time
     * @param time 
     */
    public String highlight(long time) throws FileNotFoundException, IOException{
        
        String currentTag = "";
        String line = "";
        // lat is [0] long is [1]
        double[] GPS = new double [2];
        String highRec = "";
        
        /*find lat and long in csv*/
        
        //open file
        BufferedReader br = new BufferedReader(new FileReader("test.csv"));
        
        while ((line = br.readLine()) != null){
            String[] fields = line.split(",");
            
            //if has tag
            if( fields[0].equals("Time")){
                currentTag = fields[1];
            //if is not equal to end or empty then it is a number
            } else if (!(fields[0].equals("END")) && !(fields[0].isEmpty()) && fields[0] != null && fields[1] != null && !(fields[1].isEmpty())){
                //if current tag is latitude get latitude
                if (currentTag.equals("latitude")){
                    if(Double.parseDouble(fields[0]) == time){
                        GPS[0] = Double.parseDouble(fields[1]);
                    }
                //if current tag is longitude get longitude
                } else if (currentTag.equals("longitude")){
                    if(Double.parseDouble(fields[0]) == time){
                        GPS[1] = Double.parseDouble(fields[1]);
                    }
                }
            }
        }
        
        br.close();
        
        /* TODO: call function to output long and lat of highlighted box */
        
        /*repeat sizing algorithm*/
        //first editor
        int latEditor = (((Math.abs((int)((GPS[0] * 10000) % 1000)))/100) /**- 1*/ )* 100;
        int longEditor = (((Math.abs((int)((GPS[1] * 10000) % 1000)))/100) /**- 1*/ )* 100;
        //second editor
        int secLatEd = (((((Math.abs((int)((GPS[0] * 10000) % 1000))) - latEditor) * 8) / 100) -2 ) * 100;
        int secLongEd = (((((Math.abs((int)((GPS[1] * 10000) % 1000))) - longEditor) * 8) / 100) -2 )* 100;
        
        int xi = (Math.abs((int)(GPS[0] * 10000)%1000));
        int yi = (Math.abs((int)(GPS[1] * 10000)%1000));
        
        xi = ((xi - latEditor) * 8)- secLatEd;
        yi = ((yi - longEditor) * 8)- secLongEd;
        /* end sizing algoirthm */
        
        /* place scaled lat and long into returned String */
        
        String xs = Integer.toString(xi);
        String ys = Integer.toString(yi);
        
        highRec = xs + "," + ys;
        
        return highRec;
        
    }
    
    /**
     * Read CSV
     */
    public static String[] readCSV() throws FileNotFoundException, IOException {
        
        String line = "";
        String currentTag = "";
        LinkedHashMap <String, String> lats = new LinkedHashMap<>();
        LinkedHashMap <String, String> longs = new LinkedHashMap<>();
        ArrayList<String> xys = new ArrayList<>();
        
        //open file
        BufferedReader br = new BufferedReader(new FileReader("test.csv"));
        
        //transverse through data and put lat and long into tree lists
        while ((line = br.readLine()) != null){
            String[] fields = line.split(",");
            
            //if has tag
            if( fields[0].equals("Time")){
                currentTag = fields[1];
            //if is not equal to end or empty then it is a number
            } else if (!(fields[0].equals("END")) && !(fields[0].isEmpty()) && fields[0] != null && fields[1] != null && !(fields[1].isEmpty())){
                //if current tag is latitude
                if (currentTag.equals("latitude")){
                    lats.put(fields[0],fields[1]);
                //if current tag is longitude
                } else if (currentTag.equals("longitude")){
                    longs.put(fields[0],fields[1]);
                }
            }
        }
        
        /* sizing algorithm */
        //first editor
        int latEditor = (((Math.abs((int)(((Double.parseDouble(lats.get(lats.keySet().toArray()[0]))) * 10000) % 1000)))/100))* 100;
        int longEditor = (((Math.abs((int)(((Double.parseDouble(longs.get(longs.keySet().toArray()[0]))) * 10000) % 1000)))/100))* 100;
        //second editor
        int secLatEd = (((((Math.abs((int)(((Double.parseDouble(lats.get(lats.keySet().toArray()[0]))) * 10000) % 1000))) - latEditor) * 8) / 100) -2 ) * 100;
        int secLongEd = (((((Math.abs((int)(((Double.parseDouble(longs.get(longs.keySet().toArray()[0]))) * 10000) % 1000))) - longEditor) * 8) / 100) -2 )* 100;
      
        // put together the two LinkedHashMaps into one ArrayList
        for(String key: lats.keySet()){
            //transform double values from key into int so it works in drawingPanel
            //edit integers to get the three digits that'll change during the track
            int xi = (Math.abs((int)(Double.parseDouble(lats.get(key)) * 10000)%1000));
            int yi = (Math.abs((int)(Double.parseDouble(longs.get(key)) * 10000)%1000));
            
            //sizing algorithm
            xi = ((xi - latEditor) * 8)- secLatEd;
            yi = ((yi - longEditor) * 8)- secLongEd;
            
            String xs = Integer.toString(xi);
            String ys = Integer.toString(yi);
            
            xys.add(xs + "," + ys);
        }
        
        //create array as big as enteries
        String[] data = new String[xys.size()];
        
        //put ArrayList into String Array
        for(int i=0; i<xys.size(); i++){
            //put into string array
            data[i] = xys.get(i);
        }
        
        br.close();
        return data;
    }
    
    public void createDrawing(){

        try{
            //read from csv
            String[] data = readCSV();
            
            //draw track
            drawingPanel = new Panel2(data);
            drawingPanel.repaint();
            this.setContentPane(drawingPanel);
            pack();
            
        } catch(Exception e){
            System.out.println("ERROR: File not found");
            return;
        } 
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        drawingPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout drawingPanelLayout = new javax.swing.GroupLayout(drawingPanel);
        drawingPanel.setLayout(drawingPanelLayout);
        drawingPanelLayout.setHorizontalGroup(
            drawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 698, Short.MAX_VALUE)
        );
        drawingPanelLayout.setVerticalGroup(
            drawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 551, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(drawingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(drawingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws FileNotFoundException, IOException{
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ForMyIdiotKayla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ForMyIdiotKayla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ForMyIdiotKayla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ForMyIdiotKayla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ForMyIdiotKayla().setVisible(true);
            }
        });
        
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel drawingPanel;
    // End of variables declaration//GEN-END:variables

    class Panel2 extends JPanel{
        
        Color shapeColor = Color.BLACK;
        String[] values;
        
        public Panel2() {
            // set a preferred size for the custom panel.
            setPreferredSize(new Dimension(600,400));
        }
        
        public Panel2(String[] values) {
            setPreferredSize(new Dimension(600,400));
            this.values = values;
        }
        
        public void setShapeColor(Color color){
            this.shapeColor = color;
        }

        @Override
        public void paintComponent(Graphics g){
            try{
                //find highlighted x and y cordinates and store into hl
                String hl = highlight(2507);
                super.paintComponent(g);
                
                for(String s : values) {
                    String[] split = s.split(",");
                    
                    //highlight rectangle
                    if( s == null ? hl == null : s.equals(hl) ){
                        g.setColor(Color.BLUE);
                    } else{
                        g.setColor(Color.BLACK);
                    }
               
                    //edit size here
                    g.fillRect(Integer.parseInt(split[0]), Integer.parseInt(split[1]), 5, 5);
                }
            }catch(Exception e){
                System.out.println("ERROR: File not found");
                return;
            } 
             
        }
        
    }
    
}

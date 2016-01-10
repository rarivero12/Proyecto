
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoinvo;
 
import java.awt.ScrollPane;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.JFileChooser;

/**
 *
 * @author Rafael
 */
public class principal extends javax.swing.JFrame {
logico log;
ArrayList<Integer> arrayDem,proDem,diasEsp,probaEspe,diasEntregas,probEntregas;
ArrayList<Double> costos ;
JTable tablaMejor,tablaRes;

    /**
     * Creates new form principal
     */
    public principal() {
        initComponents();
      jLabel16.setText("f.txt");
    }

    
    public void leerTodo() throws FileNotFoundException, IOException{
        ArrayList<String> cad;
        String sCadena;
        cad= new ArrayList();
        
        FileReader fr = new FileReader(jLabel16.getText());
        BufferedReader bf = new BufferedReader(fr);
        
        while ((sCadena = bf.readLine())!=null) {
           // System.out.println(sCadena);
            cad.add(sCadena);
        }
        
        //Arreglo con los costos y el inventario inicial
        costos= new ArrayList();
        costos.add(Double.valueOf(cad.get(4)));
        costos.add(Double.valueOf(cad.get(0))); 
        costos.add(Double.valueOf(cad.get(1)));
        costos.add(Double.valueOf(cad.get(2)));
        costos.add(Double.valueOf(cad.get(3)));
       
        
      //Obtengo las cadenas ingresadas en los textfield  
      String dem=cad.get(5);
      String probDem=cad.get(6);
      String diasEntre=cad.get(7);
      String probDiasEntrega=cad.get(8);
      String DiasEsper=cad.get(9);
      String ProbDiasEspera=cad.get(10);
      
      
      //Declaro los arreglos para guardar la informacion
      arrayDem= new ArrayList();
      proDem= new ArrayList();
      diasEntregas= new ArrayList();
      probEntregas= new ArrayList();
      diasEsp= new ArrayList();
      probaEspe= new ArrayList();
      
      
      //Varibale auxiliar para leer e texfield
      String aux="";
      
      //Recorro cada uno de los String guardando la informacion que necesito
      for(int i=0;i<dem.length();i++){
          if( String.valueOf(dem.charAt(i)).equals(";") ){
               arrayDem.add(Integer.valueOf(aux));
               aux="";
          }else{
           aux=aux+dem.charAt(i);
          }     
      }
      
      aux="";
      
       for(int i=0;i<probDem.length();i++){
          if( String.valueOf(probDem.charAt(i)).equals(";") ){
               proDem.add(Integer.valueOf(aux));
               aux="";
          }else{
           aux=aux+probDem.charAt(i);
          }     
      }
       
       aux="";
          for(int i=0;i<diasEntre.length();i++){
          if( String.valueOf(diasEntre.charAt(i)).equals(";") ){
               diasEntregas.add(Integer.valueOf(aux));
               aux="";
          }else{
           aux=aux+diasEntre.charAt(i);
          }     
      }
          
          
             aux="";
          for(int i=0;i<probDiasEntrega.length();i++){
          if( String.valueOf(probDiasEntrega.charAt(i)).equals(";") ){
               probEntregas.add(Integer.valueOf(aux));
               aux="";
          }else{
           aux=aux+probDiasEntrega.charAt(i);
          }     
      }
          
                      aux="";
          for(int i=0;i<DiasEsper.length();i++){
          if( String.valueOf(DiasEsper.charAt(i)).equals(";") ){
               diasEsp.add(Integer.valueOf(aux));
               aux="";
          }else{
           aux=aux+DiasEsper.charAt(i);
          }     
      }
          
          
                          aux="";
          for(int i=0;i<ProbDiasEspera.length();i++){
          if( String.valueOf(ProbDiasEspera.charAt(i)).equals(";") ){
               probaEspe.add(Integer.valueOf(aux));
               aux="";
          }else{
           aux=aux+ProbDiasEspera.charAt(i);
          }     
      }
       
         if(validar()){
             log=new logico(arrayDem,proDem,diasEsp,probaEspe,diasEntregas,probEntregas,costos); 
          }else{
              
          Logger.getLogger(principal.class.getName()).log(Level.SEVERE, "El archivo no esta bien escrito");
       
          }
       
    }
    
    public void llenarTablas(){
     String[] columnNames = {"Dia", "Inv Ini.", "No. Aleatorio",
 "Demanda", "Inv Final","inv Promedio", "Faltante", "No. Orden",
 "No. Aleatorio", "Tiempo Entrega","No. Aleatorio", "Tiempo Espera"};
     
     String[] columnNames2 = {"Q", "R", "Costo Inventario","Costo Orden",
         "Costo Faltante","Costo Total"};
     
     ArrayList<ArrayList<Integer>> aux=log.getTablaMejor();
     ArrayList<ArrayList> aux2=log.getResultados();
     
     Object[][] tabla2;
     Integer tabla[][];
     
     tabla2=new Object[aux2.size()][6];
     tabla=new Integer[366][12];
   
     for(int i=0;i<365;i++){
         for(int j=0;j<12;j++){
             tabla[i][j]=aux.get(i).get(j);
         }
     }
     
      for(int i=0;i<aux2.size();i++){
         for(int j=0;j<6;j++){
             tabla2[i][j]=aux2.get(i).get(j);
         }
     }
     
         
      tablaRes = new JTable(tabla2,columnNames2);
      tablaMejor=new JTable(tabla,columnNames);
      tablaRes.setDefaultRenderer (Object.class, new MiRender(log.getIndexMejor()));
      

      jScrollPane2.setViewportView(tablaRes);
     jScrollPane1.setViewportView(tablaMejor);
     
    jLabel3.setText(log.getMejorQ());
    jLabel9.setText(log.getMejorR());
    jLabel10.setText(log.getMejorCostoInventario());
    jLabel11.setText(log.getMejorCostoOrden());
    jLabel12.setText(log.getMejorCostoFaltante());
    jLabel13.setText(log.getMejorCosto());
    
     
    }
    
         public boolean validar( ){ //Funcion que ve si los datos ingresados son los correctos
             int aux1=0;
             for(int i=0;i<proDem.size();i++){
                 aux1=aux1+proDem.get(i);
             }
             
             int aux=0;
             for(int i=0;i<probaEspe.size();i++){
                 aux=aux+probaEspe.get(i);
             }
             
             int aux2=0;
             for(int i=0;i<probEntregas.size();i++){
                 aux2=aux2+probEntregas.get(i);
             }
             
             if(aux!=100 || aux2!=100 || aux1!=100){
                 return false;
             }
             
             if(arrayDem.size()!=proDem.size()){
                 return false;
             }
             if(diasEsp.size()!=probaEspe.size()){
                 return false;
             }
             if(diasEntregas.size()!=probEntregas.size()){
                 return false;
             } 
           
                  
             return true;
         }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulacion de Inventario");

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("MEJOR ALTERNATIVA");

        jLabel2.setText("Q:");

        jLabel4.setText("R:");

        jLabel5.setText("Costo Inventario:");

        jLabel6.setText("Costo de Orden:");

        jLabel7.setText("Costo Faltante:");

        jLabel8.setText("Costo Total:");

        jToggleButton1.setText("Cambiar");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jLabel15.setText("Archivo:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel12))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel13))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(52, 52, 52)
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel16))
                                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addGap(38, 38, 38)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 689, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel9)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel10))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jToggleButton1)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    try {
        
        leerTodo();
        
    } catch (IOException ex) {
        Logger.getLogger(principal.class.getName()).log(Level.SEVERE, null, ex);
    }
    llenarTablas();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(this);
     if(returnVal == JFileChooser.APPROVE_OPTION) {
       System.out.println("You chose to open this file: " +
           chooser.getSelectedFile().getName());
       jLabel16.setText( chooser.getSelectedFile().getName());
    }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}

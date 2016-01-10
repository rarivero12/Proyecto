package proyectoinvo;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;



// Clase para cambiarle el color a una celda esto es equis
public class MiRender extends DefaultTableCellRenderer
{
    int leM;

public MiRender(int elmejor){
   leM=elmejor;
}
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row,int column)
{

super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);

if(row==leM){
this.setOpaque(true);
this.setBackground(Color.RED);
this.setForeground(Color.BLUE);
}else{
this.setOpaque(true);
this.setBackground(Color.WHITE);
this.setForeground(Color.BLACK);
}
// JOptionPane.showMessageDialog(null,"position");


return this;
}
}
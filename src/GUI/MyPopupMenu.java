package GUI;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;


import javax.swing.*;
import javax.swing.table.TableColumn;


/*
自定义一个弹出菜单
 */
public class MyPopupMenu extends JPopupMenu {
	private JPopupMenu m_popupMenu = new JPopupMenu();
	private ArrayList<JMenuItem> menuItemList = new ArrayList<JMenuItem>();


    MyPopupMenu(){
        menuItemList.add(new JMenuItem("  删除行  "));
        menuItemList.add(new JMenuItem("  删除列  "));
        menuItemList.forEach(m_popupMenu::add);
    }

	public void show(Component invoker, int x, int y, int row, int col,int rowIndex, int colIndex) {


        /*
        监听删除行的动作
         */
        menuItemList.get(0).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

            	System.out.println("行:" + row);
            	System.out.println("列:" + col);
                System.out.println("删除行");
                JTable table = (JTable)invoker;

                for(int i = row ; i < rowIndex - 2; i++){
                    for(int j = 1; j < colIndex ; j++){
                        String data = (String) table.getValueAt(i + 1, j);
                        table.setValueAt(data, i ,j);
                    }
                }
                System.out.println(rowIndex + " " + colIndex);
                for(int i = 1; i < colIndex - 1; i++){
                    table.setValueAt("", rowIndex - 2, i);
                }

            }
        });

        
        /*
        监听删除列的动作
         */
        menuItemList.get(1).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.out.println("行:" + row);
            	System.out.println("列:" + col);
                System.out.println("删除列");
                JTable table = (JTable)invoker;

                System.out.println(rowIndex + " " + colIndex);
                for(int i = col; i <= colIndex - 2; i++){
                    for(int j = 0; j < rowIndex - 1; j++){
                        String data = (String) table.getValueAt(j, i + 1);
                        table.setValueAt(data, j, i);
                    }
                }

                for(int i = 0; i < rowIndex - 1; i++){
                    table.setValueAt("", i, colIndex - 1);
                }
            }
        });
        
        m_popupMenu.show(invoker, x, y);
	}

}

package GUI;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class MyPopupMenu extends JPopupMenu {
	private JPopupMenu m_popupMenu = new JPopupMenu();
	private ArrayList<JMenuItem> menuItemList = new ArrayList<JMenuItem>();
	
    {
        menuItemList.add(new JMenuItem("  删除行  "));
        menuItemList.add(new JMenuItem("  删除列  "));
       
        menuItemList.forEach(m_popupMenu::add);
	}
	
	public void show(Component invoker, int x, int y, int row, int col) {
		
        menuItemList.get(0).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.out.println("行:" + row);
            	System.out.println("列:" + col);
                System.out.println("删除行");
            }
        });
        
        
        menuItemList.get(1).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.out.println("行:" + row);
            	System.out.println("列:" + col);
                System.out.println("删除列");
            }
        });
        
        m_popupMenu.show(invoker, x, y);
	}

}

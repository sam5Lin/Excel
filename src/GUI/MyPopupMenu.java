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
        menuItemList.add(new JMenuItem("  ɾ����  "));
        menuItemList.add(new JMenuItem("  ɾ����  "));
       
        menuItemList.forEach(m_popupMenu::add);
	}
	
	public void show(Component invoker, int x, int y, int row, int col) {
		
        menuItemList.get(0).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.out.println("��:" + row);
            	System.out.println("��:" + col);
                System.out.println("ɾ����");
            }
        });
        
        
        menuItemList.get(1).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.out.println("��:" + row);
            	System.out.println("��:" + col);
                System.out.println("ɾ����");
            }
        });
        
        m_popupMenu.show(invoker, x, y);
	}

}

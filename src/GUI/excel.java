package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class excel extends JFrame {

	private JTable table;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					excel frame = new excel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * �����û�仯 �Ǿ����ݿⲻ�ı�����
	 * ����з����˱仯 ���ݿ��ɾ������
	 * ���ʼ�ձ������һ��һ��Ϊ�գ���֤�����������ݣ�add(sql���)
	 * �Ҽ�ɾ��(���)�С���   ����ÿ�еĴ�С
	 * ���������ݿ⣬����һ����ť����������sql���
	 */
	public excel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("���");
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		String[] columnNames= {"����","����"};
		String[][] tableValues = {{"��ïɭ","19"},{"��ïԴ","20"},{"��ï��","18"},{"",""}};
		DefaultTableModel tableModel = new DefaultTableModel(tableValues, columnNames);
		JTable table = new JTable(tableModel);
		table.setRowSorter(new TableRowSorter<>(tableModel));  //������
		scrollPane.setViewportView(table);
	}
}

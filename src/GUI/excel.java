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
	 * 如果列没变化 那就数据库不改变属性
	 * 如果列发生了变化 数据库就删掉重做
	 * 表格始终保持最后一个一行为空，保证可以填入数据，add(sql语句)
	 * 右键删除(添加)行、列   控制每列的大小
	 * 保存至数据库，设置一个按钮，批量处理sql语句
	 */
	public excel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("表格");
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		String[] columnNames= {"姓名","年龄"};
		String[][] tableValues = {{"林茂森","19"},{"林茂源","20"},{"林茂彬","18"},{"",""}};
		DefaultTableModel tableModel = new DefaultTableModel(tableValues, columnNames);
		JTable table = new JTable(tableModel);
		table.setRowSorter(new TableRowSorter<>(tableModel));  //排序功能
		scrollPane.setViewportView(table);
	}
}

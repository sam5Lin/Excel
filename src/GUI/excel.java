package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public class excel extends JFrame {
	
	private JTable table;
	private MyPopupMenu m_popupMenu = new MyPopupMenu();
	private DefaultTableModel tableModel;
	private ColName colName;
	private static int colIndex;
	private static int rowIndex;
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
	 * 列名固定ABCD...
	 * 第一列固定是123456...
	 * UI界面
	 * 排序时左边的序号不发生变化
	 */
	public excel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("表格");
		getContentPane().setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 434, 261);
		getContentPane().add(scrollPane);
		
		Vector<String> columnNames = new Vector<String>(26);
		Vector<Vector<String> > tableValues = new Vector<Vector<String> >();
		
		try {
			read(columnNames, tableValues);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		tableModel = new DefaultTableModel(tableValues, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column == 0) {
					return false;
				}
				return true;
			}
		};
		
		table = new JTable(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//其他所有列不调整（添加横向滚动栏的关键代码）
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();   
		r.setHorizontalAlignment(JLabel.CENTER);   
		table.setDefaultRenderer(Object.class, r);//设置居中
		table.setRowHeight(20);
		for(int i = 0; i < colIndex; i++) {			
			TableColumn tableColumn = table.getColumnModel().getColumn(i);
			tableColumn.setMinWidth(50);
		}
		table.setRowSorter(new TableRowSorter<>(tableModel));  //排序功能
		scrollPane.setViewportView(table);
		
		table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
            	mouseRightButtonClick(evt);      
            }
		});
		
		try {
			write(tableModel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//读函数
	public static void read(Vector<String> columnNames, Vector<Vector<String> > tableValues) throws IOException {

		// 读文件
		String filePath = new File("").getAbsolutePath() + "/src/file/" + "data" + ".txt";
		FileInputStream inputStream = new FileInputStream(filePath);            
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);            
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);            
		String lineString = ""; 
		Vector<String> vec ;
		/*
		 * 初始化列名
		 */
		columnNames.add("");
		for(colIndex = 1;colIndex <= 1000;colIndex++) {
			columnNames.add(ColName.toColName(colIndex));
		}
		rowIndex = 1;
		while ((lineString = bufferedReader.readLine()) != null) {   
			vec = new Vector<String>();
			String[] str = lineString.split(" ");
			vec.add(String.valueOf(rowIndex++));
			for(String s : str) {
				vec.add(s);
			}
			tableValues.add(vec);
		}
		
		for(int i = 0 ;i < 500;i++) {
			vec = new Vector<String>();
			vec.add(String.valueOf(rowIndex++));
			tableValues.add(vec);
		}
		         
		bufferedReader.close();
		inputStream.close();
		
	}
	
	
	
	//需要更新
	public static void write(DefaultTableModel tableModel) throws IOException {
		// 写文件
		String filePath2 = new File("").getAbsolutePath() + "/src/file/" + "data00" + ".txt";
		File file = new File(filePath2);  
		// 存在则删除重建          
		if(file.exists()){            
		    file.delete();            
		}            
		 
		file.createNewFile();            
		FileOutputStream fileOutputStream = new FileOutputStream(filePath2);            
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);            
		BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);            
		// 写一行
		int col = tableModel.getColumnCount();
		int row = tableModel.getRowCount();
		Vector vec = tableModel.getDataVector();
		String colNames = "";
		for(int i = 0;i < col;i++) {
			colNames += tableModel.getColumnName(i);
			colNames += " ";
		}
		String rowStr = "";
		for(int i = 0;i < vec.size();i++) {
			
		}
		String content = colNames;
		bufferedWriter.write(content + "\n");
		
		bufferedWriter.close();// 必须加入，否则缓存区未满时不写入，最后会有部分内容缺失
		fileOutputStream.close();
	}
	
	
	
	

	//右键弹出菜单
	 private void mouseRightButtonClick(MouseEvent evt) {
	        //判断是否为鼠标的BUTTON3按钮，BUTTON3为鼠标右键
	        if (evt.getButton() == MouseEvent.BUTTON3) {
	            //通过点击位置找到点击为表格中的行
	            int focusedRowIndex = table.rowAtPoint(evt.getPoint());
	            int focusedColIndex = table.columnAtPoint(evt.getPoint());
	            if (focusedRowIndex == -1 && focusedColIndex == -1) {
	                return;
	            }
	            System.out.println("右键");
	            //将表格所选项设为当前右键点击的行
	            table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
	            //弹出菜单
	            m_popupMenu.show(table, evt.getX(), evt.getY(), focusedRowIndex, focusedColIndex);
	        }
	 }
	 
	 
	 //插入列
	 private void addCol() {
		 System.out.println("插入列" + ColName.toColName(colIndex));
		 tableModel.addColumn(ColName.toColName(colIndex++));
		 System.out.println(colIndex);
	 }
	 
	 //删除行
	 private void delRow(int row) {
		 System.out.println("删除行:" + row);
		 
	 }
}

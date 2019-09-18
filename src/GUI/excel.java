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
	 * �����̶�ABCD...
	 * ��һ�й̶���123456...
	 * UI����
	 * ����ʱ��ߵ���Ų������仯
	 */
	public excel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("���");
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
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//���������в���������Ӻ���������Ĺؼ����룩
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();   
		r.setHorizontalAlignment(JLabel.CENTER);   
		table.setDefaultRenderer(Object.class, r);//���þ���
		table.setRowHeight(20);
		for(int i = 0; i < colIndex; i++) {			
			TableColumn tableColumn = table.getColumnModel().getColumn(i);
			tableColumn.setMinWidth(50);
		}
		table.setRowSorter(new TableRowSorter<>(tableModel));  //������
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
	
	
	//������
	public static void read(Vector<String> columnNames, Vector<Vector<String> > tableValues) throws IOException {

		// ���ļ�
		String filePath = new File("").getAbsolutePath() + "/src/file/" + "data" + ".txt";
		FileInputStream inputStream = new FileInputStream(filePath);            
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);            
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);            
		String lineString = ""; 
		Vector<String> vec ;
		/*
		 * ��ʼ������
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
	
	
	
	//��Ҫ����
	public static void write(DefaultTableModel tableModel) throws IOException {
		// д�ļ�
		String filePath2 = new File("").getAbsolutePath() + "/src/file/" + "data00" + ".txt";
		File file = new File(filePath2);  
		// ������ɾ���ؽ�          
		if(file.exists()){            
		    file.delete();            
		}            
		 
		file.createNewFile();            
		FileOutputStream fileOutputStream = new FileOutputStream(filePath2);            
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);            
		BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);            
		// дһ��
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
		
		bufferedWriter.close();// ������룬���򻺴���δ��ʱ��д�룬�����в�������ȱʧ
		fileOutputStream.close();
	}
	
	
	
	

	//�Ҽ������˵�
	 private void mouseRightButtonClick(MouseEvent evt) {
	        //�ж��Ƿ�Ϊ����BUTTON3��ť��BUTTON3Ϊ����Ҽ�
	        if (evt.getButton() == MouseEvent.BUTTON3) {
	            //ͨ�����λ���ҵ����Ϊ����е���
	            int focusedRowIndex = table.rowAtPoint(evt.getPoint());
	            int focusedColIndex = table.columnAtPoint(evt.getPoint());
	            if (focusedRowIndex == -1 && focusedColIndex == -1) {
	                return;
	            }
	            System.out.println("�Ҽ�");
	            //�������ѡ����Ϊ��ǰ�Ҽ��������
	            table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
	            //�����˵�
	            m_popupMenu.show(table, evt.getX(), evt.getY(), focusedRowIndex, focusedColIndex);
	        }
	 }
	 
	 
	 //������
	 private void addCol() {
		 System.out.println("������" + ColName.toColName(colIndex));
		 tableModel.addColumn(ColName.toColName(colIndex++));
		 System.out.println(colIndex);
	 }
	 
	 //ɾ����
	 private void delRow(int row) {
		 System.out.println("ɾ����:" + row);
		 
	 }
}

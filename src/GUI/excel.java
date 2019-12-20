package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;



/*
EXCEL主程序
 */
public class excel extends JFrame {
	
	private JTable table; //表格
	private DefaultTableModel tableModel; //表格的Model
	private static int colIndex; //列数
	private static int rowIndex; //行数
	private int[] updown;   //记录每一列的表头当前是降序还是升序
    private JScrollPane scrollPane; //滚动栏
    private JTableHeader header; //表头
    private Vector<String> columnNames; //用Vector记录列名
    private Vector<Vector<String> > tableValues; //记录表格每一项的值


	/**
	 * Launch the application.
	 */


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					excel frame = new excel();
					frame.setVisible(true);   //设置可见
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


    /*
    排序时将中间的空格移到后面去
     */

    /*
    初始化表格
        设置大小
        设置滚动栏
     */
    public void init(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setTitle("表格");
        getContentPane().setLayout(new BorderLayout());
        scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 0, 434, 261);
        getContentPane().add(scrollPane);
    }


    /*
    设置顶部的ToolBar，加一个保存按钮
     */
    public void toolBar(){
        final JToolBar toolBar = new JToolBar("工具栏");// 创建工具栏对象
        toolBar.setFloatable(false);// 设置为不允许拖动

        String path = new File("").getAbsolutePath() + "/src/image/" + "save" + ".png"; //获取图片路径
        ImageIcon icon = new ImageIcon(path);

        final JButton saveButton = new JButton(icon);
        saveButton.setBounds(0,0, 20, 20);
        Image temp = icon.getImage().getScaledInstance(saveButton.getWidth(), saveButton.getHeight(), icon.getImage().SCALE_DEFAULT);
        icon = new ImageIcon(temp);

        saveButton.setIcon(icon);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    write();//写文件
                    JOptionPane.showMessageDialog(getContentPane(), "保存成功", "Excel",JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        toolBar.add(saveButton);
        toolBar.addSeparator();// 添加默认大小的分隔符

        getContentPane().add(toolBar, BorderLayout.NORTH); //将ToolBar加入面板
    }

    /*
    设置第一列不可以修改，一直显示行号
     */
    public void tableModel(){
        columnNames = new Vector<String>(26);  //列名
        tableValues = new Vector<Vector<String> >();       //表格里每一项的值

        try {
            read(columnNames, tableValues);   //读取文件
        } catch (IOException e) {
            e.printStackTrace();
        }

        updown = new int[colIndex];    //每一列表头下一次点击是升序还是降序
        for (int i = 0 ;i < colIndex;i++){
            updown[i] = 0;   //0为升序 1为降序
        }

        tableModel = new DefaultTableModel(tableValues, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column == 0) {       //设置第一列不可修改
                    return false;
                }
                return true;
            }
        };

        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow() ;// 获得触发此次事件的表格行索引
                int column = e.getColumn() ;// 获得触发此次事件的表格列索引
                for(int i = 1; i < column; i++){      //把该行中之前的null都设置为“”，这个是关键点，这样写文件就方便了
                    if(table.getValueAt(row, i) == null){
                        table.setValueAt("",row,i);
                    }
                }
            }
        });


    }

    public void table(){
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//设置横向滚动
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);  //设置表格某项失去聚焦之后保留已输入的值

        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r); //设置文本居中

        table.setRowHeight(20); //设置每一行高度
        for(int i = 0; i < colIndex; i++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(i);
            tableColumn.setMinWidth(50); //设置每一列最小宽度
        }

        table.addMouseListener(new MouseAdapter() {     //监听鼠标右键
            public void mouseClicked(MouseEvent evt) {
                mouseRightButtonClick(evt);  //弹出菜单栏
            }
        });

        scrollPane.setViewportView(table);    //设置可滑动
    }



    /*
    点击表头可以进行升序或者降序
    设置排序函数，忽略第一行，忽略第一列，
    并且对当列进行排序，其他列跟着换行，
    列排序的准则是基于数字和字符串
    数字 < 字符串
    ACSII 大写 < 小写
     */
    public void header(){
        header = table.getTableHeader();
        //表头增加监听  
        header.addMouseListener (new MouseAdapter() {
            public void mouseClicked (MouseEvent e) {
                // 只截取单击事件
                System.out.println("点击");
                int column = header.columnAtPoint(e.getPoint());
                System.out.println(column); //获取列数


                if(updown[column] == 0){
                    updown[column] = 1;
                    for(int i = 1;i < rowIndex - 1;i++) {
                        boolean flag = false;
                        for(int j = i + 1;j < rowIndex - 1;j++){
                            String data1 = (String) table.getValueAt(i, column);
                            String data2 = (String)table.getValueAt(j,column);

                            if (data1 != null && data1.length() != 0 && data2 != null  && data2.length() != 0 ){
                                if(isInteger(data1) && isInteger(data2)){ //如果都是数字
                                    double num1 = Double.valueOf(data1);
                                    double num2 = Double.valueOf(data2);
                                    if(num1 > num2){
                                        table.setValueAt(replace(String.valueOf(num2)), i, column);
                                        table.setValueAt(replace(String.valueOf(num1)), j, column);
                                        flag = true;
                                        for(int index = 1; index < colIndex ; index++){
                                            if(index != column){
                                                data1 = (String) table.getValueAt(i, index);
                                                data2 = (String)table.getValueAt(j, index);

                                                table.setValueAt(data2, i, index);
                                                table.setValueAt(data1, j, index);
                                            }
                                        }
                                    }
                                }
                                else if(!isInteger(data1) && isInteger(data2)){ //第一个字符串，第二个数字
                                    double num2 = Double.valueOf(data2);
                                    System.out.println(data1 + " " + num2);
                                    table.setValueAt(replace(String.valueOf(num2)), i, column);
                                    table.setValueAt(data1, j, column);
                                    flag = true;
                                    for(int index = 1; index < colIndex ; index++){
                                        if(index != column){
                                            data1 = (String) table.getValueAt(i, index);
                                            data2 = (String) table.getValueAt(j, index);
                                            table.setValueAt(data2, i, index);
                                            table.setValueAt(data1, j, index);
                                        }
                                    }
                                }
                                else if(!isInteger(data1) && !isInteger(data2)){  //两个都不是数字，按照字符串来比较
                                    System.out.println(data1 + " " + data2);
                                    System.out.println(data1.compareTo(data2));
                                    if(data1.compareTo(data2) > 0){
                                        table.setValueAt(data2, i, column);
                                        table.setValueAt(data1, j, column);
                                        flag = true;
                                        for(int index = 1; index < colIndex ; index++){
                                            if(index != column){
                                                data1 = (String) table.getValueAt(i, index);
                                                data2 = (String)table.getValueAt(j, index);
                                                table.setValueAt(data2, i, index);
                                                table.setValueAt(data1, j, index);
                                            }
                                        }
                                    }
                                }
                            }
                            else if((data1 == null || data1.length() == 0) && (data2 != null && data2.length() != 0)){   //空格无论是降序还是升序都是最后面
                                table.setValueAt(data2, i, column);
                                table.setValueAt(data1, j, column);
                                flag = true;
                                for(int index = 1; index < colIndex ; index++){
                                    if(index != column){
                                        data1 = (String) table.getValueAt(i, index);
                                        data2 = (String)table.getValueAt(j, index);
                                        table.setValueAt(data2, i, index);
                                        table.setValueAt(data1, j, index);
                                    }
                                }
                            }
                        }
                        if(!flag) break;   //如果当前已经是正确的顺序，直接跳出循环
                    }
                }
                else {
                    updown[column] = 0;
                    for(int i = 1;i < rowIndex - 1;i++) {
                        boolean flag = false;
                        for(int j = i + 1;j < rowIndex - 1;j++){
                            String data1 = (String) table.getValueAt(i, column);
                            String data2 = (String)table.getValueAt(j,column);

                            if (data1 != null && data1.length() != 0 && data2 != null  && data2.length() != 0 ){
                                if(isInteger(data1) && isInteger(data2)){ //如果都是数字
                                    double num1 = Double.valueOf(data1);
                                    double num2 = Double.valueOf(data2);
                                    if(num1 < num2){
                                        table.setValueAt(replace(String.valueOf(num2)), i, column);
                                        table.setValueAt(replace(String.valueOf(num1)), j, column);
                                        flag = true;
                                        for(int index = 1; index < colIndex ; index++){
                                            if(index != column){
                                                data1 = (String) table.getValueAt(i, index);
                                                data2 = (String)table.getValueAt(j, index);
                                                System.out.println(data1);
                                                System.out.println(data2);
                                                table.setValueAt(data2, i, index);
                                                table.setValueAt(data1, j, index);
                                            }
                                        }
                                    }
                                }
                                else if(isInteger(data1) && !isInteger(data2)){ //第一个数字，第二个字符串
                                    double num1 = Double.valueOf(data1);
                                    System.out.println(data1 + " " + num1);
                                    table.setValueAt(data2, i, column);
                                    table.setValueAt(replace(String.valueOf(num1)), j, column);
                                    flag = true;
                                    for(int index = 1; index < colIndex ; index++){
                                        if(index != column){
                                            data1 = (String) table.getValueAt(i, index);
                                            data2 = (String)table.getValueAt(j, index);
                                            table.setValueAt(data2, i, index);
                                            table.setValueAt(data1, j, index);
                                        }
                                    }
                                }
                                else if(!isInteger(data1) && !isInteger(data2)){  //两个都不是数字，按照字符串来比较
                                    System.out.println(data1 + " " + data2);
                                    System.out.println(data1.compareTo(data2));
                                    flag = true;
                                    if(data1.compareTo(data2) < 0){
                                        table.setValueAt(data2, i, column);
                                        table.setValueAt(data1, j, column);
                                        System.out.println(colIndex);
                                        for(int index = 1; index < colIndex ; index++){
                                            if(index != column){
                                                data1 = (String) table.getValueAt(i, index);
                                                data2 = (String)table.getValueAt(j, index);
                                                table.setValueAt(data2, i, index);
                                                table.setValueAt(data1, j, index);
                                            }
                                        }
                                    }
                                }
                            }
                            else if((data1 == null || data1.length() == 0) && (data2 != null && data2.length() != 0)){  //空格排在最后面
                                table.setValueAt(data2, i, column);
                                table.setValueAt(data1, j, column);
                                flag = true;
                                for(int index = 1; index < colIndex ; index++){
                                    if(index != column){
                                        data1 = (String) table.getValueAt(i, index);
                                        data2 = (String)table.getValueAt(j, index);
                                        table.setValueAt(data2, i, index);
                                        table.setValueAt(data1, j, index);
                                    }
                                }
                            }
                        }
                        if(!flag) break; //如果当前已经是正确的顺序，直接跳出循环
                    }
                }

            }
        });
    }


    /*
    初始化Excel
     */
	public excel() {
		init();
		toolBar();
		tableModel();
        table();
        header();
	}

	//数字去多余0
	public static String replace(String s){
		if(null != s && s.indexOf(".") > 0){
			s = s.replaceAll("0+?$", "");//去掉多余的0
			s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
		}
		return s;
	}

	//判断是否是数字
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?");
		return pattern.matcher(str).matches();
	}

	//读文件
	public static void read(Vector<String> columnNames, Vector<Vector<String> > tableValues) throws IOException {
		String filePath = new File("").getAbsolutePath() + "/src/file/" + "data" + ".txt";
		FileInputStream inputStream = new FileInputStream(filePath);            
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);            
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);            
		String lineString = ""; 
		Vector<String> vec ;

		/*
		 * 设置列名
		 * A - Z - AA 一直往下排列
		 */
		columnNames.add("");
		for(colIndex = 1;colIndex <= 100;colIndex++) {
			columnNames.add(ColName.toColName(colIndex));
		}

		/*
		读文件，写入二维Vector中
		 */
		rowIndex = 1;
		while ((lineString = bufferedReader.readLine()) != null) {   
			vec = new Vector<String>();
			String[] str = lineString.split("!@!@!@");
			vec.add(String.valueOf(rowIndex++));
			for(String s : str) {
				vec.add(s);
			}
			tableValues.add(vec);
		}
		         
		bufferedReader.close();
		inputStream.close();
		
	}
	


	
	/*
	写文件   可以考虑每一行都要写吗？
	 */
	public  void write() throws IOException {
		String filePath2 = new File("").getAbsolutePath() + "/src/file/" + "data" + ".txt";
		File file = new File(filePath2);  
		// 如果已经存在
		if(file.exists()){            
		    file.delete();            
		}            
		 
		file.createNewFile();            
		FileOutputStream fileOutputStream = new FileOutputStream(filePath2);            
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);            
		BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);            

		int col = table.getColumnCount();
		int row = table.getRowCount();
        System.out.println(col + " "  + row);

		StringBuffer rowStr = new StringBuffer();
		for(int i = 0;i < row;i++) {
            rowStr.delete(0,rowStr.length());
			for(int j = 1;j < col;j++){
			    String str = (String)(table.getValueAt(i, j));
			    if(str != null){
                    rowStr.append(str + "!@!@!@");
                }

            }
            bufferedWriter.write(rowStr + "\n");
		}

		bufferedWriter.close();
		fileOutputStream.close();
	}


	//鼠标右键动作
	 private void mouseRightButtonClick(MouseEvent evt) {

	        if (evt.getButton() == MouseEvent.BUTTON3) {
	            //获得鼠标位置
	            int focusedRowIndex = table.rowAtPoint(evt.getPoint());
	            int focusedColIndex = table.columnAtPoint(evt.getPoint());
	            if (focusedRowIndex == -1 && focusedColIndex == -1) {
	                return;
	            }
	            String str = (String)table.getValueAt(focusedRowIndex, focusedColIndex);
                System.out.println(str);

	            //行选中 可以指定点击第一列时选定整行
	            table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
	            table.setColumnSelectionInterval(focusedColIndex, focusedColIndex);

	            //弹出菜单
                if(focusedColIndex != 0){
				    new MyPopupMenu().show(table, evt.getX(), evt.getY(), focusedRowIndex, focusedColIndex, rowIndex, colIndex);
                }
	        }
	 }
	 

}

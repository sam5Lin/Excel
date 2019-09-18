package GUI;

import org.junit.Test;

public class ColName {
	public static String toColName(int index) {
		
		int m = 0;
		String colName = "";
		while(index > 0) {
			index -= 1;
			m = index % 26 + 1;
			char c = (char)('@' + m);
			colName = c + colName; 
			index /= 26;
		}
		
		return colName;
	}
	
	
	
}

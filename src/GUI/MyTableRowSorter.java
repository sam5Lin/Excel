package GUI;

import java.util.Comparator;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


public class MyTableRowSorter<M extends TableModel> extends TableRowSorter<M> {

	@Override
	protected boolean useToString(int column) {
		Comparator comparator = super.getComparator(column);  
	    if (comparator != null) {  
	        return false;  
	    }  
	    Class columnClass = getModel().getColumnClass(column);  
	    if (columnClass == String.class) {  
	        return false;  
	    }  
	    if (Comparable.class.isAssignableFrom(columnClass)) {  
	        return false;  
	    }  
	    return true;  
	}
	
}

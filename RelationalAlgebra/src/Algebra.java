import java.io.IOException;
import java.util.Scanner;

public class Algebra {
	public Ttable Project(Ttable _table, String _columnsWanted) {
		Ttable projection = null;
		String[][] pTable;
		_columnsWanted = this.removeSpaces(_columnsWanted);
		if(this.allColumnsValid(_table, _columnsWanted)) {
			int numOfCols = this.colNum(_columnsWanted);
			pTable = new String[_table.getTable().length][numOfCols];
			pTable = this.fillProjectedTable(pTable, _columnsWanted, _table.getTable());
			projection = new Ttable(pTable);
		}
		return projection;
	}
	public Ttable Restrict(Ttable _table, String _restriction) {
		_restriction = this.removeSpaces(_restriction);
		boolean columnValid = false;
		int colIndex;
		int valIndex;
		String[][] restrictTable;
		String[] allValues = this.grabAllValues(_restriction);
		Ttable restrict = null;
		columnValid = this.isColumnValid(_table,allValues[0]);
		colIndex = this.grabColumnIndex(_table, allValues[0]);
		restrictTable = createTableDimensions(_table, allValues[1], colIndex, allValues[2]);
		if(restrictTable.length == 1 || !columnValid || colIndex == 500) {
			return restrict;
		}else {
			restrictTable = this.fillRestrictedTable(restrictTable, allValues[1], _table, allValues[2], allValues[0]);
			restrict = new Ttable(restrictTable);
		}
		return restrict;
	}
	public Ttable load(String _textFile) throws IOException {
		Ttable table = new Ttable(_textFile);
		return table;
	}
	private String[] grabAllValues(String _restrictArg) {
		String[] values = new String[3];
		if(_restrictArg.charAt(0) != '\'') {
			values = this.parseColNameFirst(_restrictArg);
		}
		else {
			values = this.parseValueFirst(_restrictArg);
		}
		return values;
	}
	private String[] parseValueFirst(String _restrictArg) {
		String[] values = new String[3];
		boolean grabValue = true;
		boolean grabOperand = true;
		String colValue = "";
		String operand = "";
		String colName = "";
		for(int i=0;i<_restrictArg.length();i++) {
			if(grabValue && _restrictArg.charAt(i) != '>'&& _restrictArg.charAt(i) != '<'&& _restrictArg.charAt(i) != '!' &&_restrictArg.charAt(i) != '=' ) {
				colValue+= _restrictArg.charAt(i);
			}
			else if(grabOperand && (_restrictArg.charAt(i) == '>'|| _restrictArg.charAt(i) == '<'|| _restrictArg.charAt(i) == '!' || _restrictArg.charAt(i) == '=' )) {
				operand+= _restrictArg.charAt(i);
				grabValue=false;
			}else {
				grabOperand=false;
				colName+= _restrictArg.charAt(i);
			}
		}
		colValue = colValue.substring(1, colValue.length()-1);
		values[0] = colName;
		values[1] = colValue;
		values[2] = operand;
		return values;
	}
	private String[] parseColNameFirst(String _restrictArg) {
		String[] values = new String[3];
		boolean grabName = true;
		boolean grabOperand = true;
		String colName = "";
		String colValue = "";
		String operand = "";
		for(int i=0;i<_restrictArg.length();i++) {
			if(_restrictArg.charAt(i) != '>'&& _restrictArg.charAt(i) != '<'&& _restrictArg.charAt(i) != '!' &&_restrictArg.charAt(i) != '=' && grabName) {
				colName+= _restrictArg.charAt(i);
			}
			else if(grabOperand && _restrictArg.charAt(i) !='\'') {
				grabName = false;
				operand+= _restrictArg.charAt(i);
			}else {
				grabOperand = false;
				colValue += _restrictArg.charAt(i);
			}
		}
		colValue = colValue.substring(1, colValue.length()-1);
		values[0] = colName;
		values[1] = colValue;
		values[2] = operand;
		
		return values;
	}
	private String[][] fillProjectedTable(String[][] _pTable, String _columnsWanted, String[][] _baseTable){
		Scanner scan = new Scanner(_columnsWanted);
		int row = 0;
		int col = 0;
		scan.useDelimiter(",");
		while(scan.hasNext() && col<_pTable[row].length) {
			String colValue = scan.next();
			_pTable[row][col] = colValue;
			this.fillProjectedColumn(_pTable,_baseTable,row, col);
			col++;
		}
		scan.close();
		return _pTable;
	}
	private String[][] fillProjectedColumn(String[][] _pTable,String[][] _baseTable, int _row, int _col){
		String colValue = _pTable[_row][_col];
		int colNum = this.getColumnToFill(_baseTable, colValue);
		for(int r=1;r<_baseTable.length;r++) {
			for(int c=colNum;c==colNum;c++) {
				_pTable[r][_col] = _baseTable[r][c];
			}
		}
		return _pTable;
		
	}
	private int getColumnToFill(String[][] _baseTable, String _colValue) {
		int colNum = 10000;
		for(int row=0;row==0;row++) {
			for(int col=0;col< _baseTable[row].length;col++) {
				if(_baseTable[row][col].equalsIgnoreCase(_colValue)) {
					colNum = col;
				}
			}
		}
		return colNum;
	}
	private int colNum(String _columnsWanted) {
		int num = 0;
		Scanner scan = new Scanner(_columnsWanted);
		scan.useDelimiter(",");
		while(scan.hasNext()) {
			scan.next();
			num++;
		}
		scan.close();
		return num;
		
	}
	private boolean allColumnsValid(Ttable _table, String _columnsWanted) {
		boolean allValid = true;
		Scanner scan = new Scanner(_columnsWanted);
		scan.useDelimiter(",");
		while(scan.hasNext()) {
			String col = scan.next();
			col = this.removeSpaces(col);
			if(!this.isColumnValid(_table, col)) {
				allValid = false;
			}
		}
		scan.close();
		return allValid;
	}
	private String removeSpaces(String _str) {
		String temp = "";
		boolean inTicks = false;
		int tickNum = 0;
		for(int i=0;i<_str.length();i++) {
			if(_str.charAt(i) == '\'') {
				tickNum++;
				if(tickNum ==1) {
					inTicks = true;
				}else {
					inTicks = false;
				}
			}
			if(inTicks) {
				temp+= _str.charAt(i);
			}
			if(_str.charAt(i) != ' ' && !inTicks) {
				temp += _str.charAt(i);
			}
		}
		return temp;
	}

	private String[][] fillRestrictedTable(String[][] _restrictTable, String _columnValue, Ttable _table, String _operand, String _columnName){
		int index = this.grabColumnIndex(_table, _columnName);
		String[] columns  = _table.getColArr();
		String[][] fullTable = _table.getTable();
		int row = 1;
		int col = 0;
		for(int i=0;i<columns.length;i++) {
			_restrictTable[0][i] = columns[i];
		}
		for(int r=1;r<fullTable.length;r++) {
			if(_operand.equals("=") && fullTable[r][index].equalsIgnoreCase(_columnValue)) {
				for(int c=0;c<fullTable[r].length;c++) {
					_restrictTable[row][col] = fullTable[r][c];
					col++;
				}
				col = 0;
				row++;
			}else if(_operand.equals(">=") &&(fullTable[r][index].equalsIgnoreCase(_columnValue)|| fullTable[r][index].compareTo(_columnValue)>0)) {
				for(int c=0;c<fullTable[r].length;c++) {
					_restrictTable[row][col] = fullTable[r][c];
					col++;
				}
				col = 0;
				row++;
			}else if(_operand.equals("<=") && (fullTable[r][index].equalsIgnoreCase(_columnValue) || fullTable[r][index].compareTo(_columnValue)<0)){
				for(int c=0;c<fullTable[r].length;c++) {
					_restrictTable[row][col] = fullTable[r][c];
					col++;
				}
				col = 0;
				row++;
			}else if(_operand.equals(">") && fullTable[r][index].compareTo(_columnValue)>0) {
				for(int c=0;c<fullTable[r].length;c++) {
					_restrictTable[row][col] = fullTable[r][c];
					col++;
				}
				col = 0;
				row++;
			}else if(_operand.equals("<") && fullTable[r][index].compareTo(_columnValue)<0) {
				for(int c=0;c<fullTable[r].length;c++) {
					_restrictTable[row][col] = fullTable[r][c];
					col++;
				}
				col = 0;
				row++;
			}else if(_operand.equals("!=") && !(fullTable[r][index].equalsIgnoreCase(_columnValue))){
				for(int c=0;c<fullTable[r].length;c++) {
					_restrictTable[row][col] = fullTable[r][c];
					col++;
				}
				col = 0;
				row++;
			}
		}
		return _restrictTable;
	}
	private int grabColumnIndex(Ttable _table, String _columnName) {
		int index = -1;
		String[] columns = _table.getColArr();
		for(int i=0;i<columns.length;i++) {
			if(columns[i].equalsIgnoreCase(_columnName)){
				index = i;
			}
		}
		return index;
	}
	private String[][] createTableDimensions(Ttable _table, String _columnValue, int _colIndex, String _operand){
		String[][] table;
		String[][] temp = _table.getTable();
		int column = _table.getTable()[0].length;
		int row = 1;
		for(int r=1;r<temp.length;r++) {
			for(int c=0;c<temp[r].length;c++) {
				if(_operand.equals("=") && c==_colIndex && temp[r][c].equalsIgnoreCase(_columnValue)) {
					row++;
				}else if((_operand.equals(">=") && c==_colIndex) && (temp[r][c].equalsIgnoreCase(_columnValue)|| temp[r][c].compareTo(_columnValue) > 0)) {
					row++;
				}else if((_operand.equals("<=") && c==_colIndex) && (temp[r][c].equalsIgnoreCase(_columnValue) || temp[r][c].compareTo(_columnValue) < 0)) {
					row++;
				}else if(_operand.equals(">") && c==_colIndex && (temp[r][c].compareTo(_columnValue)>0)) {
					row++;
				}else if(_operand.equals("<") && c==_colIndex && (temp[r][c].compareTo(_columnValue)<0)) {
					row++;
				}else if(_operand.equals("!=") && c==_colIndex && !(temp[r][c].equalsIgnoreCase(_columnValue))) {
					row++;
				}
			}
		}
		//System.out.println(row);
		//System.out.println(column);
		table = new String[row][column];
		return table;
	}
	private boolean isColumnValid(Ttable _table, String _column) {
		boolean isValid = _table.checkColumns(_column);
		return isValid;
	}
	private String removeTicks(String _restriction){
		String column = "";
		for(int i=0;i<_restriction.length();i++) {
			if(i>0 && i<_restriction.length()-1) {
				column += _restriction.charAt(i);
			}
		}
		return column;
	}
}

import java.io.File;
import java.io.IOException;
import java.util.*;
public class Ttable {
	private String[][] table;
	private String[] colArr;
	public String[][] getTable() {
		return table;
	}
	public void setTable(String[][] table) {
		this.table = table;
	}
	public String[] getColArr() {
		return colArr;
	}
	public void setColArr(String[] colArr) {
		this.colArr = colArr;
	}
	public Ttable(String[][] _table) {
		table = _table;
		this.fillColArr();
	}
	public Ttable(String fileName) throws IOException {
		boolean firstIteration = true;
		int rowNum = 0;
		int colNum = 0;
		int row = 0;
		int col = 0;
		Scanner scanFile = new Scanner(new File(fileName));
		while(scanFile.hasNextLine()) {
			rowNum++;
			String line = scanFile.nextLine();
			Scanner scanLine = new Scanner(line);
			scanLine.useDelimiter("~");
			while(scanLine.hasNext() && firstIteration) {
				colNum++;
				scanLine.next();
			}
			scanLine.close();
			firstIteration = false;
		}
		table = new String[rowNum][colNum];
		scanFile = new Scanner(new File(fileName));
		while(scanFile.hasNextLine()) {
			String line = scanFile.nextLine();
			Scanner scanLine = new Scanner(line);
			scanLine.useDelimiter("~");
			while(scanLine.hasNext()) {
				table[row][col] = scanLine.next();
				col++;
			}
			scanLine.close();
			col = 0;
			row++;
		}
		scanFile.close();
		this.fillColArr();
	}
	private void fillColArr() {
		colArr = new String[table[0].length];
		for(int row=0;row==0; row++) {
			for(int col=0;col<table[row].length;col++) {
				colArr[col] = table[row][col];
			}
		}
	}
	public void Display() {
		String str = "";
		for(int row = 0; row<table.length;row++ ) {
			for(int col=0;col<table[row].length;col++) {
				str += table[row][col] + " ";
			}
			str += "\n";
		}
		System.out.println(str);
	}
	public boolean checkColumns(String _column) {
		boolean isValid = false;
		for(int i=0;i<colArr.length;i++) {
			if(colArr[i].equalsIgnoreCase(_column)) {
				isValid = true;
			}
		}
		return isValid;
	}

}

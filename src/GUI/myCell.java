package GUI;

public class myCell {
    private String data;
    private int col;
    private int row;

    public myCell(String data, int col, int row) {
        this.data = data;
        this.col = col;
        this.row = row;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}

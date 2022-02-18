package model;

public class ColumnInfo {

    private int index;

    private int size;

    private String name;

    public ColumnInfo(int index, int size, String name) {
        this.index = index;
        this.size = size;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package matrix;

public class Matrix {
    public int[][] array;

    public Matrix(int rows, int cols) {
        array = new int[rows][cols];
    }
    public Matrix(int[][] array) {
        this.array = array.clone();
    }

}

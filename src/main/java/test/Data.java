package test;

import java.io.*;
import java.util.Arrays;

public class Data implements Serializable{
    int[] nums;
    String[] contents;

    private static final long serialVersionUID = 19961024l;

    public Data(int[] nums, String[] contents) {
        this.nums = nums;
        this.contents = contents;
    }
    public Data(){}

    public Data clone() {
        return toData(toBytes());
    }

    public byte[] toBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Data{" +
                "nums=" + Arrays.toString(nums) +
                ", contents=" + Arrays.toString(contents) +
                '}';
    }

    public Data toData(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(bais);
            return (Data) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

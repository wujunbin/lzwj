package cuhk.dyf.lzwj;

import java.util.Arrays;

/**
 * Created by dongyufei on 3/18/15.
 */
public class ByteArray {
    private byte[] byteArray;

    public ByteArray() {
        byteArray = new byte[0];
    }

    public ByteArray(ByteArray another) {
        byteArray = another.byteArray.clone();
    }

    public ByteArray(byte[] array) {
        byteArray = array.clone();
    }

    public ByteArray(byte b1, byte... bytes) {
        int bytesSize = (bytes != null) ? bytes.length : 0;

        byteArray = new byte[bytesSize + 1];
        byteArray[0] = b1;
        for (int i = 1; i < byteArray.length; i++) {
            byteArray[i] = bytes[i - 1];
        }
    }

    public int size() {
        return byteArray.length;
    }

    public byte get(int index) {
        return byteArray[index];
    }

    public void set(int index, byte value) {
        byteArray[index] = value;
    }

    public ByteArray append(ByteArray another) {
        int size = size();
        int anotherSize = another.size();
        int newSize = size + anotherSize;
        byte[] newBuf = new byte[newSize];

        for (int i = 0; i < size; i++) {
            newBuf[i] = get(i);
        }
        for (int i = 0; i < anotherSize; i++) {
            newBuf[i + size] = another.get(i);
        }
        byteArray = newBuf;
        return this;
    }

    public ByteArray append(byte[] array) {
        return append(new ByteArray(array));
    }

    public ByteArray append(byte b1, byte... bytes) {
        return append(new ByteArray(b1, bytes));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ByteArray other = (ByteArray) obj;
        if (!Arrays.equals(byteArray, other.byteArray)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(byteArray);
        return result;
    }

    @Override
    public String toString() {
        return "ByteArray [" + Arrays.toString(byteArray) + "]";
    }
}


package com.salk.lib.bitmap;

/**
 * @author salkli
 * @since 2023/3/29
 **/
public class IntegerBitMap {
    private int mid;
    private byte[] left;
    private byte[] right;
    private int leftSize = 8;
    private int rightSzie = 8;
    private int multiplier = 2;
    private volatile int realSize;


    public static void main(String[] args) {
        IntegerBitMap integerBitMap = new IntegerBitMap(0);
        integerBitMap.set(Integer.MAX_VALUE);
        integerBitMap.set(320102);
        integerBitMap.set(1);
        integerBitMap.set(320104);
        byte[] left = integerBitMap.getLeft();
        byte[] newLeft = new byte[left.length];
        System.arraycopy(left, 0, newLeft, 0, Math.min(left.length, newLeft.length));
        IntegerBitMap integerBitMap2 = new IntegerBitMap(0);
        integerBitMap2.set(Integer.MAX_VALUE);
        integerBitMap2.set(320102);
        integerBitMap2.set(320115);
        byte[] right = integerBitMap2.getLeft();
        for (int i = 0; i < Math.min(left.length, right.length); i++) {
            newLeft[i] = (byte)(left[i] | right[i]);
        }
        boolean result=false;
        for (int i = 0; i < left.length; i++) {
            result = (left[i] & newLeft[i]) == 0;
            if (result) {
                break;
            }
        }
        System.out.println("是否包含"+result);
    }

    public IntegerBitMap() {}

    public IntegerBitMap(int mid) {
        this.mid = mid;
        left = new byte[leftSize];
        right = new byte[rightSzie];
    }

    public IntegerBitMap(int mid, int multiplier) {
        this(mid);
        this.multiplier = multiplier;
    }

    /**
     * 推荐使用这种方式，使用首个插入的元素作为中间值，这样分布更均匀
     * 
     * @param obj
     */
    private synchronized void midByFist(Integer obj) {
        if (realSize == 0) {
            if (mid == 0) {
                this.mid = obj;
            }
        }
    }

    /** * 数组下标，正数是left的下标，负数则是right的下标 * * @param obj * @return */
    private int index(Integer obj) {
        if (obj == null) {
            throw new NullPointerException("obj is null");
        }
        int intObj = mid - obj;
        return intObj >> 3;
    }

    private synchronized void ensureSize(int index) {
        int size = Math.abs(index);
        int newSize;
        if (index >= 0 && size >= leftSize) {
            newSize = leftSize * multiplier > size ? leftSize * multiplier : (size + 1);
            byte[] newLeft = new byte[newSize];
            System.arraycopy(left, 0, newLeft, 0, Math.min(leftSize, newLeft.length));
            left = newLeft;
            leftSize = newLeft.length;
        }
        if (index < 0 && size >= rightSzie) {
            newSize = rightSzie * multiplier > size ? rightSzie * multiplier : (size + 1);
            byte[] newRight = new byte[newSize];
            System.arraycopy(right, 0, newRight, 0, Math.min(rightSzie, newRight.length));
            right = newRight;
            rightSzie = newRight.length;
        }
    }

    public synchronized boolean set(Integer obj) {
        midByFist(obj);
        int byteIndex = index(obj);
        ensureSize(byteIndex);
        byte map = byteIndex >= 0 ? left[byteIndex] : right[-byteIndex];
        int inByteIndex = Math.abs(mid - obj) % 8;
        // 0x0000 0000
        byte target = (byte)(0x01 << inByteIndex);
        boolean isExist = (map & target) == target;
        map |= target;
        if (byteIndex >= 0) {
            left[byteIndex] = map;
        } else {
            right[-byteIndex] = map;
        }
        if (!isExist) {
            realSize++;
        }
        return isExist;
    }

    public byte[] getLeft(){
        return this.left;
    }

    /** * 判断元素是否存在（时间复杂度比HashMap更优） * * @param obj * @return */

    public synchronized boolean contains(Integer obj) {
        midByFist(obj);
        int byteIndex = index(obj);
        ensureSize(byteIndex);
        byte map = byteIndex >= 0 ? left[byteIndex] : right[-byteIndex];
        int inByteIndex = Math.abs(mid - obj) % 8;
        // 0x0000 0000
        byte target = (byte)(0x01 << inByteIndex);
        return (map & target) == target;
    }

    public synchronized boolean remove(Integer obj) {
        midByFist(obj);
        int byteIndex = index(obj);
        ensureSize(byteIndex);
        byte map = byteIndex >= 0 ? left[byteIndex] : right[-byteIndex];
        int inByteIndex = Math.abs(mid - obj) % 8;
        // 0x0000 0000
        byte target = (byte)(0x01 << inByteIndex);
        boolean isExist = (map & target) == target;
        if (isExist) {
            map ^= target;
            if (byteIndex >= 0) {
                left[byteIndex] = map;
            } else {
                right[-byteIndex] = map;
            }
            realSize--;
        }
        return isExist;
    }

    public synchronized void reset() {
        leftSize = 8;
        rightSzie = 8;
        left = new byte[leftSize];
        right = new byte[rightSzie];
        mid = 0;
        realSize = 0;
    }

    /** * 将BitMap转为数组 * * @return */

    public Integer[] toArray() {
        Integer[] array = new Integer[realSize];
        int index = 0;
        index = writeToArray(right, array, index);
        writeToArray(left, array, index);
        return array;
    }

    private int writeToArray(byte[] source, Integer[] array, int posIndex) {
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < 8; j++) {
                byte r = source[i];
                byte target = (byte)(0x01 << j);
                if ((r & target) == target) {
                    array[posIndex++] = (i << 3) + j;
                }
            }
        }
        return posIndex;
    }


}

package com.salk.lib.bitmap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @func String to bitMap type
 * @author YUFUCHENG
 * @data 2019-06-15
 *
 *
 */

public class StrToBitMap {
    /* recond max long */
    private Long longMax = 0L;
    /* recond longMax to bit*/
    private ArrayList<Long> bit = new ArrayList<>();
    /* recond String to bit value */
    private HashMap<String, Long> bitMap = new HashMap<>();

    public ArrayList<Long> getBit() {
        return bit;
    }

    public Long getLongMax() {
        return longMax;
    }

    public StrToBitMap() {}

    public StrToBitMap(String str) {
        this.addBitMap(str);
    }

    public StrToBitMap(String[] strs) {
        this.addBitMap(strs);
    }

    /* add Long to bit type */
    private void addLongToBit(Long l) {
        /* get index while need to insert */
        int index = (int) (l >> 6);

        if(index == bit.size()) {
            bit.add(1l);
        }else {
            Long value = bit.get(index);
            bit.set(index, value | (1l << ((l - 1) & 0x3f)));
        }
    }

    /* delete Long to bit type*/
    private void delLongToBit(Long l) {
        /* get index while need to insert */
        int index = (int) (l >> 6);

        if(index >= bit.size()) {
            return;
        }else {
            Long value = bit.get(index);
            bit.set(index, value & (~(1L << ((l - 1) & 0x3f))));
        }
    }

    /* add str to bitMap and update longMax */
    public boolean addBitMap(String str) {
        String[] strs = {str};
        return this.addBitMap(strs);
    }
    /* add strs to bitMap and update bitMax */
    public boolean addBitMap(String[] strs) {
        if(strs.length == 0) {
            return false;
        }

        for (String str : strs) {
            if(bitMap.isEmpty()) {
                bitMap.put(str, ++longMax);
                addLongToBit(longMax);
                continue;
            }
            if(!bitMap.containsKey(str)) {
                bitMap.put(str, ++longMax);
                addLongToBit(longMax);
            }else {
                System.out.println("String: " + str + " exsist in bitMap! this value: " + getBitMap(str));
            }
        }
        return true;
    }

    /* delete str to bitMap */
    public boolean delBitMap(String str) {
        String[] strs = {str};
        return this.delBitMap(strs);
    }

    /* delete strs to bitMap */
    public boolean delBitMap(String[] strs) {
        if(strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if(bitMap.isEmpty()) {
                return false;
            }else if(bitMap.containsKey(str)) {
                Long l = bitMap.remove(str);
                delLongToBit(l);
                continue;
            }else {
                continue;
            }
        }
        return true;
    }

    /* get long in bitMap */
    private Long getBitMap(String str) {
        return bitMap.get(str);
    }

    public HashMap<String, Long> getStrBitMap(String str){
        String[] strs = new String[] {str};
        return getStrBitMap(strs);
    }

    /* get strs in bitMap, str is not exsist in bitMap if vlaue is null */
    public HashMap<String, Long> getStrBitMap(String[] strs) {
        HashMap<String, Long> strsBitMap = new HashMap<String, Long>();
        for(String str : strs) {
            if(bitMap.containsKey(str)) {
                strsBitMap.put(str, bitMap.get(str));
            }else {
                strsBitMap.put(str, null);
                System.out.println("String: " + str + " is not exsist in bitMap!");
            }
        }
        return strsBitMap;
    }

    /* get all longMap */
    public HashMap<String, Long> getAllBitMap() {
        return bitMap;
    }


}
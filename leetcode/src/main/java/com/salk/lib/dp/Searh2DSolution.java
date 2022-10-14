package com.salk.lib.dp;

class Searh2DSolution {
    public static void main(String[] args) {
        int[][] matrix=new int[][]{
                {1, 2,  3, 4,5},
                {6, 7,  8, 9,10}
        };
        int[][] matrix2=new int[][]{
                {1,4},
                {2,5}
        };
        int target=2;
        System.out.println(findNumberIn2D(matrix,target));
    }

    public static boolean findNumberIn2DArray(int[][] matrix, int target) {
        if(matrix.length==0 || matrix[0].length==0){
            return false;
        }
        int i = 0;
        int j = 0;
        int i_max = matrix.length - 1;
        int j_max = matrix[0].length - 1;
        while (i < i_max+1 || j < j_max+1) {
            if(i>=i_max+1){
                i=i_max;
            }
            if(j>=j_max+1){
                j=j_max;
            }
            int value = matrix[i][j];
            if (value == target) {
                return true;
            }
            if (value > target) {
                boolean match = false;
                //处理行
                for( int ii=i;ii<i_max+1;ii++){
                    boolean c_match=false;
                    for(int jj=0;jj<j_max+1;jj++){
                        int value2=matrix[ii][jj];
                        if(value2==target){
                            c_match=true;
                            break;
                        }
                        if(value2>target){
                            c_match=false;
                            break;
                        }
                    }
                    if(c_match){
                        match=true;
                        break;
                    }
                }
                if(match){
                    return true;
                }
                for (int ii = 0; ii < i_max+1; ii++) {
                    boolean c_match = false;
                    for (int jj = j; jj < j_max+1; jj++) {
                        int value2 = matrix[ii][jj];
                        if (value2 == target) {
                            c_match = true;
                            break;
                        }
                        if (value2 > target) {
                            c_match = false;
                            break;
                        }
                    }
                    if (c_match) {
                        match = true;
                        break;
                    }
                }
                return match;
            }
            if (value < target) {
                i++;
                j++;
            }

        }
        return false;
    }

    /**
     * 利用矩阵
     * @param matrix
     * @param target
     * @return
     */
    public static boolean findNumberIn2D(int[][] matrix, int target) {
        int i = matrix.length - 1, j = 0;
        while (i >= 0 && j < matrix[0].length) {
            if (matrix[i][j] > target) {
                i--;
            } else if (matrix[i][j] < target) {
                j++;
            } else {
                return true;
            }
        }
        return false;
    }
}
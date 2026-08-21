package br.com.judgebeecrowd;

import java.io.*;

class Solution {
    static int solution(int N, String S, String[] comments) {
        int result = 0;
        String keyword = S.toLowerCase();
        
        for (String comment : comments) {
            if (comment.toLowerCase().contains(keyword)) {
                result++;
            }
        }
        
        return result;
    }
}

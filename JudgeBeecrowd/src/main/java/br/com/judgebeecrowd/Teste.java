package br.com.judgebeecrowd;

import java.io.IOException;

public class Teste {
	public static void main(String[] args) throws IOException {
		System.out.println(solve(5, new int[]{9, 5, 1, 4, 9}, 2));
		
	}
	
	static long solve(int n, int[] a, int k) {
	    if (k == 1) return 0;
	    
	    long minDiff = Long.MAX_VALUE;
	    
	    if (k == 2) {
	        for (int i = 0; i < n - 1; i++) {
	            for (int j = i + 1; j < n; j++) {
	                minDiff = Math.min(minDiff, Math.abs(a[i] - a[j]));
	            }
	        }
	        return minDiff;
	    }
	    
	    // Para k > 2, usar DP
	    long[][] dp = new long[k + 1][n];
	    final long INF = Long.MAX_VALUE / 2;
	    
	    for (int i = 0; i < n; i++) {
	        dp[1][i] = 0;
	    }
	    
	    for (int j = 2; j <= k; j++) {
	        for (int i = j - 1; i < n; i++) {
	            dp[j][i] = INF;
	            for (int prev = j - 2; prev < i; prev++) {
	                long cost = dp[j - 1][prev] + Math.abs(a[i] - a[prev]);
	                if (cost < dp[j][i]) {
	                    dp[j][i] = cost;
	                }
	            }
	        }
	    }
	    
	    long result = INF;
	    for (int i = k - 1; i < n; i++) {
	        result = Math.min(result, dp[k][i]);
	    }
	    
	    return result;
	}
}

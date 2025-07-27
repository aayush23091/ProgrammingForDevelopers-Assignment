package Question3;
public class Question3b {

    public static int maxMagicalPower(String M) {
        int n = M.length();
        int[] leftMax = new int[n];
        int[] rightMax = new int[n];

        // Step 1: Expand around centers for odd-length palindromes
        for (int center = 0; center < n; center++) {
            int l = center, r = center;
            while (l >= 0 && r < n && M.charAt(l) == M.charAt(r)) {
                int len = r - l + 1;
                if (len % 2 == 1) {
                    leftMax[r] = Math.max(leftMax[r], len);
                    rightMax[l] = Math.max(rightMax[l], len);
                }
                l--;
                r++;
            }
        }

        // Step 2: Build prefix max for leftMax
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i], leftMax[i - 1]);
        }

        // Step 3: Build suffix max for rightMax
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i], rightMax[i + 1]);
        }

        // Step 4: Compute max product
        int maxProduct = 0;
        for (int i = 0; i < n - 1; i++) {
            maxProduct = Math.max(maxProduct, leftMax[i] * rightMax[i + 1]);
        }

        return maxProduct;
    }

    public static void main(String[] args) {
        System.out.println(maxMagicalPower("xyzyxabc")); // Output: 5
        System.out.println(maxMagicalPower("levelwowracecar")); // Output: 35
    }
}

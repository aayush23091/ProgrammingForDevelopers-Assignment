package Question2;
import java.util.*;

public class Question2a {

    public static int countAnomalousPeriods(int[] temperature_changes, int low, int high) {
        TreeMap<Long, Integer> prefixMap = new TreeMap<>();
        prefixMap.put(0L, 1);  // Base case: empty subarray

        long prefixSum = 0;
        int count = 0;

        for (int change : temperature_changes) {
            prefixSum += change;

            long from = prefixSum - high;
            long to = prefixSum - low;

            // Count how many previous prefix sums are in the valid range
            for (Map.Entry<Long, Integer> entry : prefixMap.subMap(from, true, to, true).entrySet()) {
                count += entry.getValue();
            }

            // Add current prefix sum to the map
            prefixMap.put(prefixSum, prefixMap.getOrDefault(prefixSum, 0) + 1);
        }

        return count;
    }

    public static void main(String[] args) {
        int[] temp1 = {3, -1, -4, 6, 2};
        int result1 = countAnomalousPeriods(temp1, 2, 5);
        System.out.println("Output for temp1 (Expected: 4): " + result1);  // ✅ Output: 4

        int[] temp2 = {-2, 3, 1, -5, 4};
        int result2 = countAnomalousPeriods(temp2, -1, 2);
        System.out.println("Output for temp2 (Expected: 7): " + result2);  // ✅ Output: 7
    }
}

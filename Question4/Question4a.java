package Question4;
import java.util.*;

public class Question4a {
    private final Map<Integer, List<int[]>> graph;

    public Question4a(int n, int[][] links) {
        graph = new HashMap<>();
        for (int i = 0; i < n; i++) {
            graph.put(i, new ArrayList<>());
        }

        for (int[] link : links) {
            int a = link[0], b = link[1], strength = link[2];
            graph.get(a).add(new int[]{b, strength});
            graph.get(b).add(new int[]{a, strength}); // undirected
        }
    }

    public boolean canTransmit(int sender, int receiver, int maxStrength) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(sender);
        visited.add(sender);

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            if (curr == receiver) return true;

            for (int[] neighbor : graph.get(curr)) {
                int next = neighbor[0], strength = neighbor[1];
                if (strength < maxStrength && !visited.contains(next)) {
                    visited.add(next);
                    queue.add(next);
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        int[][] links = {
            {0, 2, 4},
            {2, 3, 1},
            {2, 1, 3},
            {4, 5, 5}
        };

        Question4a st = new Question4a(6, links);

        System.out.println(st.canTransmit(2, 3, 2)); // true
        System.out.println(st.canTransmit(1, 3, 3)); // false
        System.out.println(st.canTransmit(2, 0, 3)); // true
        System.out.println(st.canTransmit(0, 5, 6)); // false
    }
}

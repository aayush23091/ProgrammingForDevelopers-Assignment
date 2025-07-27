public class Question1b {

    public static int strongPINChanges(String pin_code) {
        int n = pin_code.length();

        // 1. Check for missing character types
        boolean hasLower = false, hasUpper = false, hasDigit = false;
        for (char ch : pin_code.toCharArray()) {
            if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isDigit(ch)) hasDigit = true;
        }
        int missingTypes = 0;
        if (!hasLower) missingTypes++;
        if (!hasUpper) missingTypes++;
        if (!hasDigit) missingTypes++;

        // 2. Count repeating sequences
        int replace = 0;
        int i = 2;
        while (i < n) {
            if (pin_code.charAt(i) == pin_code.charAt(i - 1) && pin_code.charAt(i) == pin_code.charAt(i - 2)) {
                int length = 2;
                while (i < n && pin_code.charAt(i) == pin_code.charAt(i - 1)) {
                    length++;
                    i++;
                }
                replace += length / 3;
            } else {
                i++;
            }
        }

        // 3. Handle length adjustments
        if (n < 6) {
            return Math.max(missingTypes, 6 - n);
        } else if (n <= 20) {
            return Math.max(missingTypes, replace);
        } else {
            int delete = n - 20;
            int remainingDelete = delete;

            // Optimize replacements using deletions
            i = 2;
            while (i < n && remainingDelete > 0) {
                if (pin_code.charAt(i) == pin_code.charAt(i - 1) && pin_code.charAt(i) == pin_code.charAt(i - 2)) {
                    int length = 3;
                    i++;
                    while (i < n && pin_code.charAt(i) == pin_code.charAt(i - 1)) {
                        length++;
                        i++;
                    }
                    int reduce = Math.min(remainingDelete, length - 2);
                    replace -= reduce / 3;
                    remainingDelete -= reduce;
                } else {
                    i++;
                }
            }

            return delete + Math.max(missingTypes, replace);
        }
    }

    public static void main(String[] args) {
        System.out.println(strongPINChanges("X1!"));         // Output: 3
        System.out.println(strongPINChanges("123456"));      // Output: 2
        System.out.println(strongPINChanges("Aa1234!"));     // Output: 0
        System.out.println(strongPINChanges("aaaa1111AAAA"));// Output depends on length and structure
    }
}

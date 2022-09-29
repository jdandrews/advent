package advent.y2015;

public class Day10 {

    // Returns n'th term in  
    // look-and-say sequence 
    static String countAndSay(String seed, int n) {
        String str = seed;
        for (int i = 1; i <= n; i++) {
            // In counting loop below, the previous 
            // character is processed in the current iteration.
            // Add a dummy character to terminate the loop. 
            str += '$';
            int len = str.length();

            int cnt = 1; // Count of matching chars 
            String tmp = ""; // new result
            char[] arr = str.toCharArray();

            for (int j = 1; j < len; j++) {
                if (arr[j] != arr[j - 1]) {
                    // Append count of str[j-1] to temp 
                    tmp += cnt;
                    tmp += arr[j - 1];

                    // Reset count 
                    cnt = 1;
                }
                else {
                    ++cnt;
                }
            }

            // Update str 
            str = tmp;
            System.out.println(i+": "+str.length());
        }

        return str;
    }

    // Driver Code 
    public static void main(String[] args) {
        int N = 50;
        String s = countAndSay("1113122113", N);
        System.out.println(s.length());
    }
}

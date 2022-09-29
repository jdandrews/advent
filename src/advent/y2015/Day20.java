package advent.y2015;

public class Day20 {
    static int sumDivisors(int n) {
        int sum = 0;

        // Note that this loop runs till square root 
        int maxI = 1+(int)Math.sqrt(n);
        for (int i=1; i <= maxI; i++) 
        { 
            if (n%i==0) 
            {
                sum += i;

                int div2 = n/i;
                if (div2 != i) {
                    sum += div2;
                }
            } 
        }
        return sum;
    } 

    static int sumDivisors(int n, int maxUses) {
        // if (n>=637555) System.out.print("sumDivisors("+n+","+maxUses+"): ");
        int sum = 0;

        // Note that this loop runs till square root 
        int maxI = (int)Math.sqrt(n);
        for (int i=1; i <= maxI; i++) 
        { 
            if (n%i==0) 
            {
                if (n < maxUses*i) {
                    sum += i;
                    // if (n>=637555) System.out.print(i+" ");
                }

                int div2 = n/i;
                if (div2 != i && n<maxUses*div2) {
                    sum += div2;
                    // if (n>=637555) System.out.print(div2+" ");
                }
            } 
        }
        // if (n>=637555) System.out.println(" = "+sum);
        return sum;
    } 

    // Driver method 
    public static void main(String args[]) 
    {
        int target = 29000000/10;
        int n = 1;
        while (sumDivisors(n) < target) { ++n; }

        System.out.println("The sum of divisors of "+ n + " is: "+sumDivisors(n)+", which is >= "+target); 

        int target2 = 29000000/11+1;
        n = 1;
        while (sumDivisors(n, 50) < target2) { ++n; }

        System.out.println("The sum of divisors of "+ n + " is: "+sumDivisors(n,50)+", which is >= "+target2); 
        
        int nr = 29000000;
        int[] houses = new int[1000000];
        for(int i = 1; i < houses.length; i++) {
            int count = 0;
            for(int j = i; j < houses.length; j+=i) {
                houses[j] += i*11;
                if(++count == 50) 
                    break;
            }
        }
        for(int i = 0; i < houses.length;i++) {
            if(houses[i] >= nr) {
                System.out.println(i);
                break;
            }
        }
    } 

}

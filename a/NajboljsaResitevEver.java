import java.util.*;

public class HashCodeB {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = 80000;

        int stevec = n;
        boolean[] tabela = new boolean[n]; // ce je false se ni bilo uporabljeno

        Random rand = new Random();

        System.out.println(n);

        while (stevec > 0) {
            int st = rand.nextInt(n);

            if (!tabela[st]) {
                System.out.println(st);
                tabela[st] = true;
                stevec--;
            }

        }

    }
}
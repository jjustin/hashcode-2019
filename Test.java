/**
 * Test
 */
public class Test {

    public static void main(String[] args) {
        boolean[] a = new boolean[]{false, false, false};
        if(!Pizza.jeValidenKos(0, 0, 2, 2, new boolean[][]{a,a,a})){
            System.out.println("Error 1");
        }

        a = new boolean[]{false, true, false};
        if(!Pizza.jeValidenKos(0, 0, 0, 0, new boolean[][]{a,a,a})){
            System.out.println("Error 2");
        }

        a = new boolean[]{false, true, false};
        if(Pizza.jeValidenKos(0, 0, 1, 1, new boolean[][]{a,a,a})){
            System.out.println("Error 3");
        }

    }
}
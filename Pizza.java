import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Pizza
 */
public class Pizza {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // m*n matrika
        // m vrstic
        // n stolpcev

        int m = sc.nextInt();
        int n = sc.nextInt();
        int max = sc.nextInt(); // H v navodilih
        int min = sc.nextInt(); // L v navodilih

        // tabela sestavin (True=tomato, False=mushroom)
        boolean[][] pizza = new boolean[m][n];
        boolean[][] izrezanaPizza = new boolean[m][n]; //false če je še tam pizza (true če smo že izrezali)

        int stGob = m*n;
        int stPar = 0;

        // input tabele v program
        for (int i = 0; i < m; i++) {
            String vrstica = sc.next();
            for (int j = 0; j < n; j++) {
                pizza[i][j] = vrstica.charAt(j) == 'T';
                if (pizza[i][j]) {
                    stPar++;
                }
            }
        }

        stGob -= stPar;
        
        //true = manj je paradiznikov
        //false = manj je gob
        boolean minEle = false;
        if (stPar < stGob) {
            minEle = true;
        }

        // test vhoda
        System.out.println(Arrays.deepToString(pizza));


        // =====================
        // algoritem za rezanje pice
        // =====================

        // stejemo elemente
        
        // arrayList s kosi pice
        ArrayList<String> kosi = new ArrayList<String>();

        boolean jeSePizze = true;

        
        while (jeSePizze) {            
            //poiscemo najbolj zgornji levi minimalni element
            for (int i=0; i<m; i++) {
                for (int j=0; j<n; j++) {
                    if (pizza[i][j] == minEle) {
                        // gledas ce se lahko raztegnemo gor
                        if (mozenRaztegGor(pizza, i, j)) {
                            // se raztegnemo gor dokler ne hitnemo limita
                            if()
                        }
                        // se raztegnemo levo dokler lahko
                        // desno dokler lahko
                        // dol dokler lahko
                        // koncamo, ko je prvic zadoscen pogoj da je dovolj elementov notr
                        // ce se ne moremo gledamo ce se da it na levo
                        // desno
                        // dol

                        // desno
                        // desno
                        // dol
                    }
                }
            }

        }

        // vsa polja raztegnemo na max

        
        
        // =====================
        // izhod
        // =====================
        


        // izpis števila kosov
        System.out.println(kosi.size());

        // izpis za posamezne kose
        for (int i = 0; i < kosi.size(); i++) {
            // izpis koordinat kosov  
            System.out.println(kosi.get(i)); 
        }
    }

    public static boolean jeValidenKos(int x1, int y1, int x2, int y2, boolean[][] polja) {
        if(x2 >= polja[0].length){
            System.out.println("x2 out of bounds");
        } else if(y1 >= polja.length){
            System.out.println("y2 out of bounds");
        }
        
        for(int i = x1; i <= x2; i++){
            for (int j = y1; j <= y2; j++) {
                if(polja[j][i] == true) return false;
            }
        } 
        return true;
    }

    public static boolean mozenRaztegGor(int x1, int y1, int x2, int y2, boolean[][] pizza, boolean[][] izrezanaPizza) {
        //preveri za celo sirino navzgor
            //ce so se neizrezane vrne true        
        return false;
    }

    public static boolean dovoljGobInParadiznika(int x1, int y1, int x2, int y2, int min, int max, boolean[][] pica){
        if(x2 >= pica[0].length){
            System.out.println("x2 out of bounds");
        } else if(y1 >= pica.length){
            System.out.println("y2 out of bounds");
        }
        
        for(int i = x1; i <= x2; i++){
            for (int j = y1; j <= y2; j++) {
                
            }
        }
    }
}
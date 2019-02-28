import java.io.*;
import java.util.*;

import javax.management.RuntimeErrorException;

/**
 * InnerMain
 */
public class Hash {

    public static final int LIMITA_PODOBNOSTI = 5;
    public static final int VERTIKALNA_LIMITA = 4;
    public static final int POSPESEK = 1;
    
    public static Map<String, ArrayList<Photo>> slike;
    public static ArrayList<Photo> vseSlike;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt(); //stevilo vrstic/fotografij

        slike = new HashMap<String, ArrayList<Photo>>();
        vseSlike = new ArrayList<Photo>();
        
        for (int i=0; i<n; i++) {
            ArrayList<String> tags = new ArrayList<String>();
            Photo photo = new Photo(i, tags, sc.next().equals("V"));
            
            vseSlike.add(photo);

            int nTags = sc.nextInt();
            
            // Handle the tags
            for(int j = 0; j < nTags; j++){
                String tag = sc.next();
                tags.add(tag); //dodaj v list pri fotki

                // ce ta tag se ne obstaja dodaj novega
                if(slike.get(tag)==null){
                    slike.put(tag, new ArrayList<Photo>());
                }

                slike.get(tag).add(photo);
            }
        }

        Photo photo;
        ArrayList<String> tags = new ArrayList<String>();

        while(true) {
            //System.out.println(Arrays.deepToString(vseSlike.toArray()));
            photo = findNextPhoto(tags);
            if(photo.isVertical()){
                Photo photo2 = poisciNajboljsoVertikalko(photo);
                SlideShow.add(photo,photo2);
                tags = photo.tags;
                tags.addAll(photo2.tags);
            }else{
                SlideShow.add(photo);
                tags = photo.tags;
            }
        }
    }

    public static ArrayList<String> neskupniTagi(Photo p, ArrayList<String> tags){
        ArrayList<String> l = new ArrayList<String>(slike.keySet());
        ArrayList<String> a = p.tags;
        a.retainAll(tags);
        for()

        return a;
    }

    //metoda za primerjanje tagov med slikama
    //naceloma za rabo nad V slikami
    public static int podobnostMedSlikama(Photo slika1, Photo slika2) {
    
        int stTagS1 = slika1.tags.size();
        int stTagS2 = slika2.tags.size();
        int podobnost = 0;
        
        for (int i=0; i<stTagS1; i++) {
            for (int j=0; j<stTagS2; j++) {
                if (slika1.tags.get(i).equals(slika2.tags.get(j))) {
                    podobnost++;
                    break;
                }
                //if (podobnost >= LIMITA_PODOBNOSTI) return podobnost;
            }
        }
        return podobnost;
    }

    public static Photo findNextPhoto(ArrayList<String> tags) {
        for(String tag : tags){
            ArrayList<Photo> photos = slike.get(tag);
            for(Photo s : photos){
                if(neskupniTagi(s, tags).size() != 0 && !s.isUsed) return s;
            }
        }

        for(Photo s : vseSlike){
            if(!s.isUsed()) return s;
        }

        SlideShow.print();
        System.exit(0);
        return null;
    }

    //metoda za iskanje vertikalne slike ki ima podobnost pod LIMITA_PODOBNOSTI
    //ce najde sliko (ce ne najde taksne pa taprvo) jih zdruzi v objekt
    
    public static Photo poisciNajboljsoVertikalko(Photo slika1) {
        int podobnost = 0;
        int min = 1000; //neka konstanta
        int ix = 0; //zaporedna vertikalna slika
        for (int i=0; i<vseSlike.size() * POSPESEK; i++) {
            if (!vseSlike.get(i).isUsed && vseSlike.get(i).vertical) {
                podobnost = podobnostMedSlikama(slika1, vseSlike.get(i));
                if (podobnost < min) {
                    min = podobnost;
                    ix = i;
                }
                // if (podobnost <= VERTIKALNA_LIMITA) {
                //     return vseSlike.get(ix);
                // }  
            }
        }
        return vseSlike.get(ix);  
    }
}
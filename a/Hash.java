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
                photo.isUsed = true;
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

    public static int skupniTagi(Photo p, ArrayList<String> tags){
        ArrayList<String> a = new ArrayList<>(p.tags);
        a.retainAll(tags);
        // System.out.println(tags.size() + " " + a.size() + " " + p.tags.size());
        return a.size();
    }

    public static Photo findNextPhoto(ArrayList<String> tags) {
        Photo maxP = null;
        int maxTock = 0;
        
        int stTagov = tags.size();

        for(String tag : tags){
            ArrayList<Photo> photos = slike.get(tag);
            for(Photo s : photos){
                if(s.isUsed){ 
                    continue;
                }
                int presek = skupniTagi(s, tags);
                if(presek == 0) continue;
                int slikaStTagov = s.tags.size();
                int samoSlika = slikaStTagov - presek;
                int samoOriginal = stTagov - presek;
                int tocke = Math.min(samoSlika, Math.min(samoOriginal, presek));
                // System.out.println(samoOriginal + " " + presek + " "+ slikaStTagov);
                if(tocke > maxTock){
                    maxP = s;
                    maxTock = tocke;
                }
            }
        }
        if(maxP != null) {
            return maxP;
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
        int minSize = slika1.tags.size();
        Photo maxP = null;
        for(Photo p : vseSlike){
            if(p.isUsed || !p.vertical) continue;
            int size = skupniTagi(p, slika1.tags);
            if(size == 0) return p;
            if(minSize > size) {
                minSize = size;
                maxP = p;
            }
        }
        if(maxP != null) return maxP;

        for(Photo p : vseSlike) {
            if(!p.isUsed && p.vertical) return p;
        }  
        return null;
    }
}
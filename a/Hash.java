import java.io.*;
import java.util.*;

import javax.management.RuntimeErrorException;

/**
 * InnerMain
 */
public class Hash {

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
                else return s;
                // int slikaStTagov = s.tags.size();
                // int samoSlika = slikaStTagov - presek;
                // int samoOriginal = stTagov - presek;
                // int tocke = Math.min(samoSlika, Math.min(samoOriginal, presek));

                // if(tocke > maxTock){
                //     maxP = s;
                //     maxTock = tocke;
                // }
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
    static public class SlideShow {
        private static StringBuilder sb = new StringBuilder("");
        private static int stVrstic = 0;
    
       public static void add(Photo p) {
           p.isUsed = true;
           sb.append(Integer.toString(p.id) + "\n");
        //    System.out.println(p.id );
           stVrstic++;
       }
       
       public static void add(Photo p1, Photo p2) {
           p1.isUsed = true;
           p2.isUsed = true;
        //    System.out.println(p1.id + " " + p2.id);
    
           sb.append(Integer.toString(p1.id) + " ");
           sb.append(Integer.toString(p2.id) + "\n");
           stVrstic++;
       }
    
        public static void print() {
           System.out.print(Integer.toString(stVrstic) + "\n" + sb.toString());
        }
       
    
       /* @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if(idOne != null && idTwo != null) {
                sb.append(one.id);
                sb.append(" ");
                sb.append(two.id);
            } else if(idOne != null) {
                sb.append(one.id);
            } else if(idTwo != null) {
                sb.append(two.id);
            }
            return sb.toString();
        }*/
    }

    static public class Photo {

        public int id;
        public ArrayList<String> tags;
        public boolean vertical;
        public boolean isUsed;
    
        Photo(int id, ArrayList<String> tags, boolean vertical){
            this.id = id;
            this.tags = tags;
            this.vertical = vertical;
            isUsed = false;
        }
    
        public boolean isVertical() {
            return vertical;
        }
    
        public boolean isUsed() {
            return isUsed;
        }
    
        public boolean contains(String a){
            for(String s : tags){
                if (s.equals(a)) return true;
            }
            return false;
        }
    
        public String toString(){
            return id + " " + Arrays.deepToString(tags.toArray()) + " " + vertical + " " + isUsed;
        }
    }
}


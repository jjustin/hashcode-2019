import java.util.*;

/**
 * InnerMain
 */
public class Hash {

    public static Map<String, Set<Photo>> slike;
    public static Set<Photo> vseSlike;
    public static int numberOfFlags;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt(); // stevilo vrstic/fotografij
        slike = new HashMap<String, Set<Photo>>();
        vseSlike = new HashSet<Photo>();

        for (int i = 0; i < n; i++) {
            Set<String> tags = new HashSet<String>();
            Photo photo = new Photo(i, tags, sc.next().equals("V"));

            vseSlike.add(photo);

            int nTags = sc.nextInt();

            // Handle the tags
            for (int j = 0; j < nTags; j++) {
                String tag = sc.next();
                tags.add(tag); // dodaj v list pri fotki

                // ce ta tag se ne obstaja dodaj novega
                if (slike.get(tag) == null) {
                    slike.put(tag, new HashSet<Photo>());
                }

                slike.get(tag).add(photo);
            }
        }

        numberOfFlags = slike.keySet().size();

        Photo photo;
        Set<String> tags = new HashSet<String>();

        while (true) {
            // System.out.println(Arrays.deepToString(vseSlike.toArray()));
            photo = findNextPhoto(tags);
            if (photo.isVertical()) {
                photo.isUsed = true;
                Photo photo2 = poisciNajboljsoVertikalko(photo, tags);
                SlideShow.add(photo, photo2);
                tags = photo.tags;
                tags.addAll(photo2.tags);
            } else {
                SlideShow.add(photo);
                tags = photo.tags;
            }
        }
    }

    public static int skupniTagi(Set<String> tags1, Set<String> tags2) {
        Set<String> a = new HashSet<>(tags1);
        a.retainAll(tags2);

        return a.size();
    }

    public static Photo findNextPhoto(Set<String> tags) {
        // Photo maxP = null;
        // int maxTock = 0;

        // int stTagov = tags.size();

        for (String tag : tags) {
            Set<Photo> photos = slike.get(tag);
            for (Photo s : photos) {
                if (s.isUsed) {
                    continue;
                }
                int presek = skupniTagi(s.tags, tags);
                if(presek != 0) return s;

                // if (presek == 0)
                //     continue;
                // int slikaStTagov = s.tags.size();
                // int samoSlika = slikaStTagov - presek;
                // int samoOriginal = stTagov - presek;
                // int tocke = Math.min(samoSlika, Math.min(samoOriginal, presek));
                // if(tocke >= TOCKE_LIMIT) return s;
                // if(tocke > maxTock){
                //     maxP = s;
                //     maxTock = tocke;
                // }
            }
        }
        // if(maxP != null) {
        //     return maxP;
        // }

        for (Photo s : vseSlike) {
            if (!s.isUsed())
                return s;
        }

        SlideShow.print();
        System.exit(0);
        return null;
    }

    public static int skupniTagi(Set<String> tags1, Set<String> tags2, Set<String> tags3) {
        Set<String> a = new HashSet<String>(tags1);
        a.retainAll(tags2);
        a.retainAll(tags3);

        return a.size();
    }

    public static Photo poisciNajboljsoVertikalko(Photo slika1, Set<String> lastPictureTags) {
        for (Photo p : vseSlike) {
            if (p.isUsed || !p.vertical)
                continue;

            int presekSize = skupniTagi(p.tags, lastPictureTags, slika1.tags);
            int pSize = p.tags.size();
            int sSize = p.tags.size();
            if(presekSize == pSize || presekSize == sSize) continue;
            if (presekSize != 0) return p;
        }

        for (Photo p : vseSlike) {
            if (!p.isUsed && p.vertical)
                return p;
        }
        return null;
    }

    static public class SlideShow {
        private static StringBuilder sb = new StringBuilder("");
        private static int stVrstic = 0;

        public static void add(Photo p) {
            p.isUsed = true;
            sb.append(Integer.toString(p.id) + "\n");
            // System.out.println(p.id );
            stVrstic++;
        }

        public static void add(Photo p1, Photo p2) {
            p1.isUsed = true;
            p2.isUsed = true;
            // System.out.println(p1.id + " " + p2.id);

            sb.append(Integer.toString(p1.id) + " ");
            sb.append(Integer.toString(p2.id) + "\n");
            stVrstic++;
        }

        public static void print() {
            System.out.print(Integer.toString(stVrstic) + "\n" + sb.toString());
        }

        /*
         * @Override public String toString() { StringBuilder sb = new StringBuilder();
         * if(idOne != null && idTwo != null) { sb.append(one.id); sb.append(" ");
         * sb.append(two.id); } else if(idOne != null) { sb.append(one.id); } else
         * if(idTwo != null) { sb.append(two.id); } return sb.toString(); }
         */
    }

    static public class Photo {

        public int id;
        public Set<String> tags;
        public boolean vertical;
        public boolean isUsed;

        Photo(int id, Set<String> tags, boolean vertical) {
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

        public boolean contains(String a) {
            for (String s : tags) {
                if (s.equals(a))
                    return true;
            }
            return false;
        }

        public String toString() {
            return id + " " + Arrays.deepToString(tags.toArray()) + " " + vertical + " " + isUsed;
        }
    }
}

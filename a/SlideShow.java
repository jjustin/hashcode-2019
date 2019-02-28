/**
 * Slide
 */
public class SlideShow {
    private static StringBuilder sb = new StringBuilder("");
    private static int stVrstic = 0;

   public static void add(Photo p) {
       p.isUsed = true;
       sb.append(Integer.toString(p.id) + "\n");
       stVrstic++;
   }
   
   public static void add(Photo p1, Photo p2) {
       p1.isUsed = true;
       p2.isUsed = true;

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
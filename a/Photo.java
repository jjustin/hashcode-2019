import java.util.ArrayList;
import java.util.Arrays;

/**
 * Photo
 */
public class Photo {

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
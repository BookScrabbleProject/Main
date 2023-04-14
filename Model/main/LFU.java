package SecondMileStone;


import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class LFU implements CacheReplacementPolicy {

    private LinkedHashMap<String, Integer> cache;
    //private final int size=256;
    LFU(){
        cache = new LinkedHashMap<String, Integer>();
    }

    public void add(String word) {
        // TODO Auto-generated method stub
        if(cache.containsKey(word)) {
            int key = cache.get(word);
            cache.remove(word);
            cache.put(word, key+1);
        }
        /*else if(cache.size() == size) {
            cache.remove(remove());
        }*/
        else {
            cache.put(word, 1);
        }
    }

    public String remove(){
        // TODO Auto-generated method stub
        int min = Integer.MAX_VALUE;
        String toReturn = "";
        for(String s : cache.keySet()) {
            if(cache.get(s) < min) {
                min = cache.get(s);
                toReturn = s;
            }
        }
        cache.remove(toReturn);
        return toReturn;

    }


}

package Model.gameClasses;


import java.util.LinkedHashMap;

public class LFU implements CacheReplacementPolicy {

    private LinkedHashMap<String, Integer> cache;
    //private final int size=256;
    public LFU(){
        cache = new LinkedHashMap<String, Integer>();
    }


    /**
     *
     * @param word add a word to the LFU cache
     */
    public void add(String word) {
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

    /**
     *
     * @return the string that has been removed from the cache
     */
    public String remove(){
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

package Model.gameClasses;

import java.util.LinkedHashSet;
import java.util.Set;

public class LRU implements CacheReplacementPolicy {

    private Set<String> cache;
    //private final int size = 256;
    public LRU(){
        cache = new LinkedHashSet<String>();
    }

    /**
     *
     * @param word a word that has been asked
     * @return whether the word exists or not
     */
    public boolean check(String word){
        if(!cache.contains(word)){
            return false;
        }
        cache.remove(word);
        cache.add(word);
        return true;
    }

    /**
     *
     * @param word a word to be added to LRU cache
     */
    public void add(String word) {
        if(cache.contains(word)) {
            cache.remove(word);
        }
        /*else if(cache.size() == size) {
            cache.remove(remove());
        }*/
        cache.add(word);

    }

    /**
     *
     * @return the word that has been removed from the cache
     */
    public String remove() {
        String toReturn = cache.iterator().next();
        cache.remove(toReturn);
        return toReturn;
    }

}

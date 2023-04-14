package main;


import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class LRU implements CacheReplacementPolicy {

    private Set<String> cache;
    //private final int size = 256;
    LRU(){
        cache = new LinkedHashSet<String>();
    }
    public boolean check(String word){
        if(!cache.contains(word)){
            return false;
        }
        cache.remove(word);
        cache.add(word);
        return true;
    }
    public void add(String word) {
        // TODO Auto-generated method stub
        if(cache.contains(word)) {
            cache.remove(word);
        }
        /*else if(cache.size() == size) {
            cache.remove(remove());
        }*/
        cache.add(word);

    }
    public String remove() {
        // TODO Auto-generated method stub
        String toReturn = cache.iterator().next();
        cache.remove(toReturn);
        return toReturn;
    }

}

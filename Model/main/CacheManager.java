package main;


import java.util.HashSet;

public class CacheManager {
	HashSet<String> cache;
    CacheReplacementPolicy policy;
    int size;
	CacheManager(int size, CacheReplacementPolicy crp){
        cache = new HashSet<>();
        policy = crp;
        this.size = size;
    }

    public boolean query(String word){
        return cache.contains(word);
    }
    void add(String word){
        if(!query(word)){
            cache.add(word);
            policy.add(word);
            if(cache.size() > size){
                cache.remove(policy.remove());
            }
        }
        else{
            policy.add(word);
        }

    }


}

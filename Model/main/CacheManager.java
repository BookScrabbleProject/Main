package main;


import java.util.HashSet;

public class CacheManager {
	HashSet<String> cache;
    CacheReplacementPolicy policy;
    int size;

    /**
     * the CacheManager constructor gets the size of the cache structure and the cacheReplacementPolicy
     * @param size the size of the cache policy
     * @param crp the cache replacement policy (LFU, LRU...)
     */
	CacheManager(int size, CacheReplacementPolicy crp){
        cache = new HashSet<>();
        policy = crp;
        this.size = size;
    }

    /**
     *
     * @param word a word to check if it exists in cache
     * @return whether the given word exists or not
     */
    public boolean query(String word){
        return cache.contains(word);
    }

    /**
     *
     * @param word add the word to the cache and if the cache is full - remove a word using the policy of the cache.
     */
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

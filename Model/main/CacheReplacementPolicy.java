package main;

public interface CacheReplacementPolicy{
	void add(String word);
	String remove(); 
}

package Model.gameClasses;

public interface CacheReplacementPolicy{
	void add(String word);
	String remove(); 
}

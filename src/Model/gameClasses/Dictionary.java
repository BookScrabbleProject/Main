package Model.gameClasses;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Dictionary {

    private CacheManager exists;
    private CacheManager notExists;
    private BloomFilter bloomFilter;
    String[] fileNames;

    /**
     * The Dictionary constructor gets string(s) of file(s) name(s) and create a LRU cache for placing the exist words and LFU for the notExists
     * @param fileNames array of given name(s) of file(s)
     */
    public Dictionary(String...fileNames){
        this.fileNames = fileNames.clone();
        exists = new CacheManager(400, new LRU());
        notExists = new CacheManager(100, new LFU());
        bloomFilter = new BloomFilter(256, "MD5", "SHA1");
        /*for(String file : fileNames){
            bloomFilter.add(file);
        }*/
        for(String file : fileNames){
            try {
                Scanner s = new Scanner(new BufferedReader(new FileReader(file)));
                while(s.hasNext()) {
                    bloomFilter.add(s.next());
                }
            }
            catch(FileNotFoundException e){
                System.out.println("File not found");
            }
        }
    }

    /**
     *
     * @param word check if the word has been asken recently, if not - searching it line by line
     * @return
     */
    public boolean query(String word){
        if(exists.query(word)){
            return true;
        }
        else if(notExists.query(word)){
            return false;
        }
        else if(bloomFilter.contains(word)){
            exists.add(word);
            return true;
        }

        return challenge(word);
    }

    /**
     *
     * @param word checks if the word is in the "BOOKS" by searching line by line - and add the word to the relevant cache.
     * @return
     */
    public boolean challenge(String word){
        if(IOSearcher.search(word, fileNames)){
            exists.add(word);
            return true;
        }
        notExists.add(word);
        return false;

    }

}

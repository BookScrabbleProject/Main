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
        bloomFilter = new BloomFilter(32768*4, "MD5","SHA1","MD2","SHA256","SHA512", "SHA-384", "SHA-224" );
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
        for(int i=0;i<bloomFilter.bitSet.length();i++){
            if(bloomFilter.bitSet.get(i)){
                System.out.print("1");
            }
            else {
                System.out.print("0");
            }
        }
    }

    /**
     * check if the word has been asked recently, if not - searching it line by line (and add to the relevant cache)
     * @param word the word we want to ask for.
     * @return true if the word is valid, false if invalid
     */
    public boolean query(String word){
        if(exists.query(word)){
            return true;
        }
        if(notExists.query(word)){
            return false;
        }
        if(bloomFilter.contains(word)){
            exists.add(word);
            return true;
        }
        notExists.add(word);
            return false;

    }

    /**
     *
     * @param word to check with IO searching
     * @return if the word is in the "BOOKS" by searching it line by line - and add the word to the relevant cache.
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

package main;


import main.Dictionary;

import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {
    private static DictionaryManager instance = null;
    private Map<String, Dictionary> books;

    /**
     * the DictionaryManager constructor creates a HashMap.
     */
    public DictionaryManager(){
        books = new HashMap<>();
    }

    /**
     * a Signelton pattern
     * @return the dictionary manager
     */
    public static DictionaryManager get(){
        if(instance==null){
            instance = new DictionaryManager();
        }
        return instance;
    }

    /**
     *
     * @param args array of strings that represent book names, and the last string is the word to search
     * @return if the word exists or not
     */
    public boolean query(String...args){
        boolean ok = false;
        for(int i=0;i<args.length-1;i++){
            if(!books.containsKey(args[i]))
                books.put(args[i], new Dictionary(args[i]));
            if(books.get(args[i]).query(args[args.length-1]))
                ok=true;
        }
        return ok;
    }

    /**
     *
     * @param args an array of book names when the last string is the word to challenge
     * @return if the word is valid or not
     */
    public boolean challenge(String...args){
        boolean ok = false;
        for(int i=0;i<args.length-1;i++){
            if(!books.containsKey(args[i]))
                books.put(args[i], new Dictionary(args[i]));
            if(books.get(args[i]).challenge(args[args.length-1]))
                ok=true;

        }
        return ok;
    }

    /**
     *
     * @return how many dictionaries exist
     */
    public int getSize(){
        return books.size();
    }
}

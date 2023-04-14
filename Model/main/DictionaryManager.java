package ThirdMileStone;


import SecondMileStone.Dictionary;

import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {
    private static DictionaryManager instance = null;
    private Map<String, Dictionary> books;
    public DictionaryManager(){
        books = new HashMap<>();
    }

    public static DictionaryManager get(){
        if(instance==null){
            instance = new DictionaryManager();
        }
        return instance;
    }
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
    public int getSize(){
        return books.size();
    }
}

package Model.gameClasses;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class IOSearcher {

    /**
     *
     * @param word a given string to search if it exists in the dictionary
     * @param fileNames the dictionaries names to search in
     * @return returns if the word exists
     */
    public static boolean search(String word, String... fileNames){
        for(String fileName : fileNames){
            try {
                File file = new File(fileName);
                if (file.exists() && file.canRead()) {
                    Scanner scanner = new Scanner(file);
                    while(scanner.hasNextLine()){
                        String line = scanner.nextLine();
                        if(line.contains(word)){
                            return true;
                        }
                    }
                }
            } catch(FileNotFoundException e){
                System.out.println("File not found");
            }
        }
        return false;

    }

}

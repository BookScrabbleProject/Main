package main;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class IOSearcher {

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

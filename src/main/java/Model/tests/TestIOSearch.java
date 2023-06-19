package Model.tests;
import Model.gameClasses.IOSearcher;
import java.io.FileWriter;
import java.io.PrintWriter;

public class TestIOSearch {
    public static void testIOSearch() throws Exception{
        String words1 = "the quick brown fox \n jumps over the lazy dog";
        String words2 = "A Bloom filter is a space efficient probabilistic data structure, \n conceived by Burton Howard Bloom in 1970";
        PrintWriter out = new PrintWriter(new FileWriter("text1.txt"));
        out.println(words1);
        out.close();
        out = new PrintWriter(new FileWriter("text2.txt"));
        out.println(words2);
        out.close();

        if(!IOSearcher.search("A", "text1.txt","text2.txt"))
            System.out.println("your IOsearch1 did not found a word (-5)");
        if(!IOSearcher.search("Bloom", "text1.txt","text2.txt"))
            System.out.println("your IOsearch2 did not found a word (-5)");
        if(!IOSearcher.search("filter", "text1.txt","text2.txt"))
            System.out.println("your IOsearch3 did not found a word (-5)");
        if(!IOSearcher.search("is", "text1.txt","text2.txt"))
            System.out.println("your IOsearch4 did not found a word (-5)");
        if(!IOSearcher.search("a", "text1.txt","text2.txt"))
            System.out.println("your IOsearch5 did not found a word (-5)");
        if(!IOSearcher.search("space", "text1.txt","text2.txt"))
            System.out.println("your IOsearch6 did not found a word (-5)");
        if(!IOSearcher.search("efficient", "text1.txt","text2.txt"))
            System.out.println("your IOsearch7 did not found a word (-5)");
        if(!IOSearcher.search("probabilistic", "text1.txt","text2.txt"))
            System.out.println("your IOsearch8 did not found a word (-5)");
        if(!IOSearcher.search("data", "text1.txt","text2.txt"))
            System.out.println("your IOsearch9 did not found a word (-5)");
        if(!IOSearcher.search("structure", "text1.txt","text2.txt"))
            System.out.println("your IOsearch10 did not found a word (-5)");
        if(!IOSearcher.search("conceived", "text1.txt","text2.txt"))
            System.out.println("your IOsearch11 did not found a word (-5)");
        if(!IOSearcher.search("by", "text1.txt","text2.txt"))
            System.out.println("your IOsearch12 did not found a word (-5)");
        if(!IOSearcher.search("Burton", "text1.txt","text2.txt"))
            System.out.println("your IOsearch13 did not found a word (-5)");
        if(!IOSearcher.search("Howard", "text1.txt","text2.txt"))
            System.out.println("your IOsearch14 did not found a word (-5)");
        if(!IOSearcher.search("Bloom", "text1.txt","text2.txt"))
            System.out.println("your IOsearch15 did not found a word (-5)");
        if(!IOSearcher.search("in", "text1.txt","text2.txt"))
            System.out.println("your IOsearch16 did not found a word (-5)");
        if(!IOSearcher.search("1970", "text1.txt","text2.txt"))
            System.out.println("your IOsearch17 did not found a word (-5)");

        if(!IOSearcher.search("the", "text1.txt","text2.txt"))
            System.out.println("your IOsearch18 did not found a word (-5)");
        if(!IOSearcher.search("quick", "text1.txt","text2.txt"))
            System.out.println("your IOsearch19 did not found a word (-5)");
        if(!IOSearcher.search("brown", "text1.txt","text2.txt"))
            System.out.println("your IOsearch20 did not found a word (-5)");
        if(!IOSearcher.search("fox", "text1.txt","text2.txt"))
            System.out.println("your IOsearch21 did not found a word (-5)");
        if(!IOSearcher.search("jumps", "text1.txt","text2.txt"))
            System.out.println("your IOsearch22 did not found a word (-5)");
        if(!IOSearcher.search("over", "text1.txt","text2.txt"))
            System.out.println("your IOsearch23 did not found a word (-5)");
        if(!IOSearcher.search("the", "text1.txt","text2.txt"))
            System.out.println("your IOsearch24 did not found a word (-5)");
        if(!IOSearcher.search("lazy", "text1.txt","text2.txt"))
            System.out.println("your IOsearch25 did not found a word (-5)");
        if(!IOSearcher.search("dog", "text1.txt","text2.txt"))
            System.out.println("your IOsearch26 did not found a word (-5)");


        if(IOSearcher.search("cat", "text1.txt","text2.txt"))
            System.out.println("your IOsearch found a word that does not exist (-5)");

        System.out.println("IOSearch works");
    }
    public static void main(String[] args) {
        try {
            testIOSearch();
        } catch(Exception e) {
            System.out.println("you got some exception (-10)");
        }
    }
}

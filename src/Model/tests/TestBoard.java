package Model.tests;

import Model.gameClasses.Tile.Bag;
import Model.gameClasses.Tile;
import Model.gameClasses.Word;
import Model.gameClasses.Board;



public class TestBoard {


    public static void testBoard() {
        Board b = Board.getBoard();
        if(b!=Board.getBoard())
            System.out.println("board should be a Singleton (-5)");


        Bag bag = Bag.getBag();
        Tile[] ts=new Tile[10];
        for(int i=0;i<ts.length;i++)
            ts[i]=bag.getRand();

        Word w0=new Word(ts,0,6,true);
        Word w1=new Word(ts,7,6,false);
        Word w2=new Word(ts,6,7,true);
        Word w3=new Word(ts,-1,7,true);
        Word w4=new Word(ts,7,-1,false);
        Word w5=new Word(ts,0,7,true);
        Word w6=new Word(ts,7,0,false);

        if(b.boardLegal(w0) || b.boardLegal(w1) || b.boardLegal(w2) || b.boardLegal(w3) || b.boardLegal(w4) || !b.boardLegal(w5) || !b.boardLegal(w6))
            System.out.println("your boardLegal function is wrong (-10)");

        for(Tile t : ts)
            bag.put(t);

        Word horn=new Word(TestBag.get("HORN"), 7, 5, false);
        if(b.tryPlaceWord(horn)!=14)
            System.out.println("problem in placeWord for 1st word (-10)");

        Word farm=new Word(TestBag.get("FA_M"), 5, 7, true);
        if(b.tryPlaceWord(farm)!=9)
            System.out.println("problem in placeWord for 2ed word (-10)");

        Word paste=new Word(TestBag.get("PASTE"), 9, 5, false);
        if(b.tryPlaceWord(paste)!=25)
            System.out.println("problem in placeWord for 3ed word (-10)");

        Word mob=new Word(TestBag.get("_OB"), 8, 7, false);
        if(b.tryPlaceWord(mob)!=18)
            System.out.println("problem in placeWord for 4th word (-10)");

        Word bit=new Word(TestBag.get("BIT"), 10, 4, false);
        if(b.tryPlaceWord(bit)!=22)
            System.out.println("problem in placeWord for 5th word (-15)");
        Word ahi = new Word(TestBag.get("A_I"), 6, 5, true);
        if(b.tryPlaceWord(ahi)!=16)
            System.out.println("problem in placeWord for AHI word (-15)");
        Word al = new Word(TestBag.get("AL"), 6, 9, true);
        if(b.tryPlaceWord(al)!=16)
            System.out.println("problem in placeWord for AL word (-15)");
        Word ran = new Word(TestBag.get("R_N"), 6, 6, false);
        if(b.tryPlaceWord(ran)!=16)
            System.out.println("problem in placeWord for RAN word (-15)");
    }

    public static void main(String[] args) {
        testBoard();
        System.out.println("TestBoard done!");
    }

}

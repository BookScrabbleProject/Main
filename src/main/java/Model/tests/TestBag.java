package Model.tests;

import Model.gameClasses.Tile.Bag;
import Model.gameClasses.Tile;

public class TestBag {

    public static void testBag() {
        Bag b=Tile.Bag.getBag();
        Bag b1=Tile.Bag.getBag();
        if(b1!=b)
            System.out.println("your Bag in not a Singleton (-5)");

        int[] q0 = b.getQuantities();
        q0[0]+=1;
        int[] q1 = b.getQuantities();
        if(q0[0]!=q1[0] + 1)
            System.out.println("getQuantities did not return a clone (-5)");

        for(int k=0;k<9;k++) {
            int[] qs = b.getQuantities();
            Tile t = b.getRand();
            int i=t.letter-'A';
            int[] qs1 = b.getQuantities();
            if(qs1[i]!=qs[i]-1)
                System.out.println("problem with getRand (-1)");

            b.put(t);
            b.put(t);
            b.put(t);

            if(b.getQuantities()[i]!=qs[i])
                System.out.println("problem with put (-1)");
        }

        if(b.getTile('a')!=null || b.getTile('$')!=null || b.getTile('A')==null)
            System.out.println("your getTile is wrong (-2)");

    }



    public static Tile[] get(String s) {
        Tile[] ts=new Tile[s.length()];
        int i=0;
        for(char c: s.toCharArray()) {
            ts[i]=Bag.getBag().getTile(c);
            i++;
        }
        return ts;
    }


    public static void main(String[] args) {
        testBag();
        System.out.println("TestBag done!");
    }

}

package Model.tests;
import Model.gameClasses.LFU;
import Model.gameClasses.CacheReplacementPolicy;

public class TestLFU {

    public static void testLFU() {
        CacheReplacementPolicy lfu=new LFU();
        lfu.add("a");
        lfu.add("b");
        lfu.add("b");
        lfu.add("c");
        lfu.add("a");
        // 1 : c
        // 2 : a - b
        if(!lfu.remove().equals("c"))
            System.out.println("wrong implementation for LFU (-10)");
        // 2 : a - b
        if(!lfu.remove().equals("b"))
            System.out.println("wrong implementation for LFU (-10)");
        // 2: a
        lfu.add("d");
        lfu.add("d");
        lfu.add("d");
        lfu.add("d");
        // 2 : a
        // 4 : d
        if(!lfu.remove().equals("a"))
            System.out.println("wrong implementation for LFU (-10)");
        // 4 : d
        lfu.add("a");
        lfu.add("c");
        lfu.add("d");
        // 1 : c - a
        // 5 : d
        if(!lfu.remove().equals("a"))
            System.out.println("wrong implementation for LFU (-10)");
        // 1 : c
        // 5 : d
        lfu.add("c");
        // 2 : c
        // 5 : d
        if(!lfu.remove().equals("c"))
            System.out.println("wrong implementation for LFU (-10)");
        // 5 : d
        if(!lfu.remove().equals("d"))
            System.out.println("wrong implementation for LFU (-10)");

        lfu.add("a");
        lfu.add("b");
        // 1: b - a
        if(!lfu.remove().equals("a"))
            System.out.println("wrong implementation for LFU (-10)");
        lfu.add("a");
        // 1 : a - b
        lfu.add("b");
        lfu.add("a");
        // 2 : b - a
        if(!lfu.remove().equals("b"))
            System.out.println("wrong implementation for LFU (-10)");

        System.out.println("LFU works");
    }
    public static void main(String[] args) {
        testLFU();
    }

}

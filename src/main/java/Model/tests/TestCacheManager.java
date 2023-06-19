package Model.tests;
import Model.gameClasses.CacheManager;
import Model.gameClasses.LRU;

public class TestCacheManager {
    public static void testCacheManager() {
        CacheManager exists=new CacheManager(3, new LRU());
        boolean b = exists.query("a");
        b|=exists.query("b");
        b|=exists.query("c");

        if(b)
            System.out.println("wrong result for CacheManager first queries (-5)");

        exists.add("a");
        exists.add("b");
        exists.add("c");

        b=exists.query("a");
        b&=exists.query("b");
        b&=exists.query("c");

        if(!b)
            System.out.println("wrong result for CacheManager second queries (-5)");

        boolean bf = exists.query("d"); // false, LRU is "a"
        exists.add("d");
        boolean bt = exists.query("d"); // true
        bf|= exists.query("a"); // false
        exists.add("a");
        bt &= exists.query("a"); // true, LRU is "b"

        if(bf || ! bt)
            System.out.println("wrong result for CacheManager last queries (-10)");

        System.out.println("CacheManager works");

    }
    public static void main(String[] args) {
        testCacheManager();
    }
}

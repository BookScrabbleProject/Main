package Model.tests;
import Model.gameClasses.BloomFilter;
public class TestBloomFilter {
    public static void testBloomFilter() {
        BloomFilter bf = new BloomFilter(256,"MD5","SHA1");
        String[] words = "the quick brown fox jumps over the lazy dog".split(" ");
        for(String w : words)
            bf.add(w);
        if(!bf.toString().equals("0010010000000000000000000000000000000000000100000000001000000000000000000000010000000001000000000000000100000010100000000010000000000000000000000000000000110000100000000000000000000000000010000000001000000000000000000000000000000000000000000000000000001"))
            System.out.println("problem in the bit vector of the bloom filter (-10)");

        boolean found=true;
        for(String w : words)
            found &= bf.contains(w);

        if(!found)
            System.out.println("problem finding words that should exist in the bloom filter (-15)");

        found=false;
        for(String w : words)
            found |= bf.contains(w+"!");

        if(found)
            System.out.println("problem finding words that should not exist in the bloom filter (-15)");

        System.out.println("BloomFilter works");
    }

    public static void main(String[] args) {
        testBloomFilter();
    }
}

package main;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

import static java.util.Objects.hash;

public class BloomFilter {
	public BitSet bitSet;
    int size;
    public String[] hashFuncs;
    BigInteger bigInt;
    MessageDigest md;


    /**
     * gets a string and hashes it using the hashFunctions that are passed in the constructor,
     * then turn all these bits on to 1.
     * @param bits the length of the bit vector
     * @param algs the name of the HashFunctions we will use
     */
    public BloomFilter(int bits, String... algs){
        bitSet = new BitSet(bits);
        size = bits;
        hashFuncs = new String[algs.length];
        System.arraycopy(algs, 0, hashFuncs, 0, algs.length);
    }

    /**
     * put a given word in the bloom filter
     * @param word the word we will put in the bloom filter
     */
    public void add(String word) {
        byte[] bytes = null;
        for(String s: hashFuncs){
            /*try {
                md = MessageDigest.getInstance(s);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            byte[] bts = md.digest(word.getBytes());
            bigInt = new BigInteger(bts);
            int bigVal = bigInt.intValue();
            bitSet.set(Math.abs(bigVal)%256);*/
            try{
                md = MessageDigest.getInstance(s);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            bytes = md.digest(word.getBytes());
            bigInt = new BigInteger(bytes);
            int bigVal = bigInt.intValue();
            bitSet.set(Math.abs(bigVal)%size);

        }

    }

    /**
     * Check if a given word exists in the bloom filter
     * @param word the word we will check if it exists in the bloom filter
     * @return true if the word exists (may be false positive), false if the word doesn't exist.
     */
    public boolean contains(String word){
        byte[] bytes = null;
        for(String s: hashFuncs){
            try{
                md = MessageDigest.getInstance(s);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            bytes = md.digest(word.getBytes());
            bigInt = new BigInteger(bytes);
            int bigVal = bigInt.intValue();
            if(!bitSet.get(Math.abs(bigVal)%256)){
                return false;
            }
        }
        return true;
    }


    /**
     * converts the bitset into a string
     * @return a string of the bitset vector
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(bitSet.length());
        for(int i = 0; i < bitSet.length(); i++){
            if(bitSet.get(i)){
                sb.append("1");
            }
            else{
                sb.append("0");
            }
        }
        return sb.toString();
    }


}

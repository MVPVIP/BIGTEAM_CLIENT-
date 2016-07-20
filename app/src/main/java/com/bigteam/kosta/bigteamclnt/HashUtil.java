package com.bigteam.kosta.bigteamclnt;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class HashUtil
{
    public static String calculateHash(MessageDigest algorithm, String fileName) throws Exception
    {
        FileInputStream fis = new FileInputStream(new File(fileName));
        BufferedInputStream bis = new BufferedInputStream(fis);
        DigestInputStream dis = new DigestInputStream(bis, algorithm);

        // read the file and update the hash calculation
        while(dis.read() != -1)
            ;

        // get the hash value as byte array
        byte[] hash = algorithm.digest();

        return byteArray2Hex(hash);
    }

    private static String byteArray2Hex(byte[] hash)
    {
        Formatter formatter = new Formatter();
        for(byte b : hash)
        {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static String getSHA256Code(String filePath) throws Exception{
        String SHA = "";
        try
        {
            MessageDigest SHA256 = MessageDigest.getInstance("SHA-256");
            System.out.println(calculateHash(SHA256, filePath));
            SHA = HashUtil.calculateHash(SHA256, filePath);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            SHA = null;
        }
        return SHA;
    }

    public static String getMD5Code(String filePath) throws Exception{
        String md5 = "";
        try
        {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            System.out.println(calculateHash(MD5, filePath));
            md5 = HashUtil.calculateHash(MD5, filePath);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            md5 = null;
        }
        return md5;
    }

}
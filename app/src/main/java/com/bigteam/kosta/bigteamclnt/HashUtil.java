package com.bigteam.kosta.bigteamclnt;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
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
}
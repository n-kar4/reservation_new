package com.nk.reservation.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeyGeneratorUtility {

    public static KeyPair generateRsaKey(){

        KeyPair keyPair;

        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }catch(Exception e){
            System.out.println("Error while generating RSA key pair                                                now");
            throw new IllegalStateException(e);
        }

        return keyPair;
    }
}

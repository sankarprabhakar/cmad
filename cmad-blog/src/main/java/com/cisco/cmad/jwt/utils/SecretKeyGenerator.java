package com.cisco.cmad.jwt.utils;

import java.security.Key;
import java.util.logging.Logger;

import javax.crypto.spec.SecretKeySpec;

public class SecretKeyGenerator implements KeyGenerator {

    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public Key generateKey() {
        // todo: Expect secret stored in a DB with strict view access control.
        String keyString = "cmad-blog-secret";
        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
        logger.info("#### generate a key : " + key);
        return key;
    }

}

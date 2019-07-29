package org.orienteer.logger.impl;

import org.orienteer.logger.IOCorrelationIdGenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

public class DefaultCorrelationIdGenerator implements IOCorrelationIdGenerator {

    protected final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();


    @Override
    public String generate(Object object) {
        MessageDigest md = createMessageDigest();

        if (object instanceof Throwable) {
            generateDigestForThrowable(md, (Throwable)object);
        } else if (object instanceof Collection) {
            generateDigestFromCollection(md, (Collection<?>) object);
        } else if (object != null) {
            generateDigestFromObject(md, object);
        } else {
            md.update((byte) 0);
        }

        return bytesToHex(md.digest());
    }

    protected void generateDigestForThrowable(MessageDigest md, Throwable thr) {
        Throwable cause = thr;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        StackTraceElement[] stacktrace = cause.getStackTrace();
        for (StackTraceElement ste : stacktrace) {
            md.update(ste.toString().getBytes());
        }
    }

    protected void generateDigestFromCollection(MessageDigest md, Collection<?> collection) {
        collection.forEach(obj -> {
            if (obj == null) {
                md.update((byte) 0);
            } else {
                md.update(obj.getClass().getName().getBytes());
                md.update(obj.toString().getBytes());
            }
        });
    }

    protected void generateDigestFromObject(MessageDigest md, Object object) {
        md.update(object.getClass().getName().getBytes());
        md.update(object.toString().getBytes());
    }

    protected String getAlgorithm() {
        return "SHA-256";
    }

    private MessageDigest createMessageDigest() {
        String algorithm = getAlgorithm();
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError(algorithm + " not supported", nsae);
        }
    }


    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}

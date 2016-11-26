package org.orienteer.logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

/**
 * Internal class for different utilities.
 * Yes - there are bunch of libraries which can do that, but we don't want to have extra dependencies 
 */
public class OLoggerUtils {
	
	protected final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static String getCorrelationId(Object object) {
		if(object==null) return "0000";
		else if(object instanceof Throwable) return getCorrelationIdForThrowable((Throwable)object);
		else {
			MessageDigest md;
	        try {
	            md = MessageDigest.getInstance("MD5");
	        } catch (NoSuchAlgorithmException nsae) {
	            throw new InternalError("MD5 not supported", nsae);
	        }
			if(object instanceof Collection) {
				for(Object subObject : (Collection<?>)object) {
					md.update(subObject.getClass().getName().getBytes());
					md.update(subObject.toString().getBytes());
				}
			} else {
				md.update(object.getClass().getName().getBytes());
				md.update(object.toString().getBytes());
			}
			return bytesToHex(md.digest());
		}
	}
	
	public static String getCorrelationIdForThrowable(Throwable thr) {
		Throwable cause = thr;
		while(cause.getCause()!=null) cause = cause.getCause();
		StackTraceElement[] stacktrace = cause.getStackTrace();
		MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("MD5 not supported", nsae);
        }
		for (StackTraceElement ste : stacktrace) {
			md.update(ste.toString().getBytes());
		}
		return bytesToHex(md.digest());
	}
	
	public static StringBuilder appendJson(StringBuilder sb, String fieldName, Object value) {
		sb.append('"');
		escapeForJson(sb, fieldName);
		sb.append("\" : \"");
		escapeForJson(sb, value);
		sb.append('\"');
		return sb;
	}
	
	public static String escapeForJson(Object value) {
		StringBuilder sb = new StringBuilder();
		return escapeForJson(sb, value).toString();
	}
	
	public static StringBuilder escapeForJson(StringBuilder sb, Object value) {
		String string = value!=null?value.toString():null;
        if (string != null && string.length() > 0) {
	        char         c = 0;
	        int          i;
	        int          len = string.length();
	        String       t;
	
	        for (i = 0; i < len; i += 1) {
	            c = string.charAt(i);
	            switch (c) {
	            case '\\':
	            case '"':
	            case '/':
                    sb.append('\\');
	                sb.append(c);
	                break;
	            case '\b':
	                sb.append("\\b");
	                break;
	            case '\t':
	                sb.append("\\t");
	                break;
	            case '\n':
	                sb.append("\\n");
	                break;
	            case '\f':
	                sb.append("\\f");
	                break;
	            case '\r':
	               sb.append("\\r");
	               break;
	            default:
	                if (c < ' ') {
	                    t = "000" + Integer.toHexString(c);
	                    sb.append("\\u" + t.substring(t.length() - 4));
	                } else {
	                    sb.append(c);
	                }
	            }
	        }
        }
        return sb;
    }
	
	public static int copy(final InputStream input, final OutputStream output) throws IOException
	{
		byte[] buffer = new byte[1024];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer)))
		{
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
}

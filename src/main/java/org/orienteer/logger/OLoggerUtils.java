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
public final class OLoggerUtils {

	private OLoggerUtils() {}
	
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

package org.orienteer.logger.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.orienteer.logger.IOLoggerConfigurable;
import org.orienteer.logger.IOLoggerConfiguration;
import org.orienteer.logger.IOLoggerEventDispatcher;
import org.orienteer.logger.OLoggerEvent;
import org.orienteer.logger.OLoggerUtils;

public class DefaultOLoggerEventDispatcher implements IOLoggerEventDispatcher, IOLoggerConfigurable {

	protected String collectorUrl;
	@Override
	public void dispatch(OLoggerEvent event) {
		if(collectorUrl!=null) {
			try {
				URL url = new URL(collectorUrl);
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				con.setDoInput(true);
				con.setDoOutput(true);
				con.setUseCaches(false);
				con.setRequestMethod("POST");
				OutputStream os = con.getOutputStream();
				os.write(event.toJson().getBytes());
				os.flush();
				int responseCode = con.getResponseCode();
				if(responseCode!=HttpsURLConnection.HTTP_OK) {
					InputStream is = con.getInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					OLoggerUtils.copy(is, baos);
					syslog(new String(baos.toByteArray()), null);
				}
				return;
			} catch (IOException e) {
				syslog(null, e);
			}
		}
		syslog(event);
	}
	
	protected void syslog(OLoggerEvent event) {
		syslog("OLogger Event: "+event, null);
	}
	
	protected void syslog(String message, Exception exc) {
		if(message!=null) System.err.println(message);
		if(exc!=null) exc.printStackTrace();
	}

	@Override
	public void configure(IOLoggerConfiguration configuration) {
		this.collectorUrl = configuration.getCollectorUrl();
	}

}

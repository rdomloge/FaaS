package com.sqlite;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

@Component
public class FileHelper {

	public static byte[] readFile(String filename) throws IOException {
		ByteArrayOutputStream bos = null;
		FileInputStream fis = null;
		try {
			File f = new File(filename);
			fis = new FileInputStream(f);
			byte[] buffer = new byte[1024];
			bos = new ByteArrayOutputStream();
			for (int len; (len = fis.read(buffer)) != -1;) {
				bos.write(buffer, 0, len);
			}
		}
		finally {
			try {
				if (fis != null) fis.close();
			} catch (IOException ioe) { }
			try {
				if (bos != null) bos.close();
			} catch (IOException ioe) { }
		}

		return bos != null ? bos.toByteArray() : null;
	}

	public static String inputStreamToString(InputStream is) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    String line;
	    BufferedReader br = new BufferedReader(new InputStreamReader(is));
	    while ((line = br.readLine()) != null) {
	        sb.append(line);
	    }
	    br.close();
	    return sb.toString();
	}
}

package utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class File {
	
	private static final String PATH_SEPERATOR = "/";
	
	private String path;
	private String name;
	
	public File(String path) {
		this.path = PATH_SEPERATOR + path;
		String[] dirs = path.split(PATH_SEPERATOR);
		this.name = dirs[dirs.length - 1];
	}

	public File(String... paths) {
		this.path = "";
		for (String part : paths) {
			this.path += (PATH_SEPERATOR + part);
		}
		String[] dirs = path.split(PATH_SEPERATOR);
		this.name = dirs[dirs.length - 1];
	}
	
	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return getPath();
	}

	public InputStream getInputStream() {
		return Class.class.getResourceAsStream(path);
	}

	public BufferedReader getReader() throws Exception {
		try {
			InputStreamReader isr = new InputStreamReader(getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			return reader;
		} catch (Exception e) {
			System.err.println("Couldn't get reader for " + path);
			throw e;
		}
	}

	public String getName() {
		return name;
	}
}

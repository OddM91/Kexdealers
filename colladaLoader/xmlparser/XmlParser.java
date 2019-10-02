package xmlparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utility.File;

/**
 * Reads an XML file and stores all the data in {@link XmlNode} objects,
 * allowing for easy access to the data contained in the XML file.
 * 
 * From https://github.com/TheThinMatrix/OpenGL-Animation/blob/master/ColladaParser/xmlParser/XmlParser.java
 * 
 * @author Karl
 *
 */
public class XmlParser {

	private static final Pattern DATA = Pattern.compile(">(.+?)<");
	private static final Pattern START_TAG = Pattern.compile("<(.+?)>");
	private static final Pattern ATTR_NAME = Pattern.compile("(.+?)=");
	private static final Pattern ATTR_VAL = Pattern.compile("\"(.+?)\"");
	private static final Pattern CLOSED = Pattern.compile("(</|/>)");

	/**
	 * Reads an XML file and stores all the data in {@link XmlNode} objects,
	 * allowing for easy access to the data contained in the XML file.
	 * 
	 * @param file - the XML file
	 * @return The root node of the XML structure.
	 */
	public static XmlNode loadXmlFile(File file) {
		try (BufferedReader br = file.getReader()) {
			br.readLine();
			final XmlNode node = loadNode(br);
			return node;
		} catch (IOException ex) {
			System.err.println("Error finding the XML file: " + file.getPath());
			ex.printStackTrace();
			return null;
		} catch (Exception e) {
			System.err.println("Error with XML file format for: " + file.getPath());
			e.printStackTrace();
			return null;
		}
	}

	// Not sure what this is supposed to throw
	private static XmlNode loadNode(BufferedReader reader) throws Exception {
		final String line = reader.readLine().trim();
		if (line.startsWith("</")) {
			return null;
		}
		final String[] startTagParts = getStartTag(line).split(" ");
		final XmlNode node = new XmlNode(startTagParts[0].replace("/", ""));
		addAttributes(startTagParts, node);
		addData(line, node);
		if (CLOSED.matcher(line).find()) {
			return node;
		}
		XmlNode child = null;
		while ((child = loadNode(reader)) != null) {
			node.addChild(child);
		}
		return node;
	}

	private static void addData(String line, XmlNode node) {
		final Matcher matcher = DATA.matcher(line);
		if (matcher.find()) {
			node.setData(matcher.group(1));
		}
	}

	private static void addAttributes(String[] titleParts, XmlNode node) {
		for (int i = 1; i < titleParts.length; i++) {
			if (titleParts[i].contains("=")) {
				addAttribute(titleParts[i], node);
			}
		}
	}

	private static void addAttribute(String attributeLine, XmlNode node) {
		final Matcher nameMatch = ATTR_NAME.matcher(attributeLine);
		nameMatch.find();
		final Matcher valMatch = ATTR_VAL.matcher(attributeLine);
		valMatch.find();
		node.addAttribute(nameMatch.group(1), valMatch.group(1));
	}

	private static String getStartTag(String line) {
		final Matcher match = START_TAG.matcher(line);
		match.find();
		return match.group(1);
	}

}
package menueditor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A .desktop entry.
 * 
 * @author toriscope
 * 
 */
public class Entry implements Comparable<Entry> {

	/**
	 * The file this entry is linked to.
	 */
	private final File file;

	/**
	 * The necessary first line.
	 */
	private final String HEADER = "[Desktop Entry]";

	/**
	 * The name of the application.
	 */
	public String name = "";

	/**
	 * A crafty little comment.
	 */
	public String comment = "";

	/**
	 * Absolute link to the application icon.
	 */
	public String icon = "";

	/**
	 * Absolute link to executable.
	 */
	public String exec = "";

	/**
	 * if true, run in terminal.
	 */
	public boolean terminal = false;

	/**
	 * The type, usually 'Application'.
	 */
	public String type = "Application";

	/**
	 * Game;TimeWaster;etc..
	 */
	public String categories = "";

	/**
	 * if true, notify on startup.
	 */
	public boolean startupNotify = false;

	public Entry(final File file) throws FileNotFoundException {
		this.file = file;
		loadFromFile();
	}

	/**
	 * Load the data fields from the file.
	 * 
	 * @throws FileNotFoundException
	 */
	private void loadFromFile() throws FileNotFoundException {
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (!line.contains("="))
				continue;
			String payload = line.substring(line.indexOf("=") + 1).trim();

			// Categorize
			if (line.contains("Name"))
				name = payload;
			else if (line.contains("Comment"))
				comment = payload;
			else if (line.contains("Icon"))
				icon = payload;
			else if (line.contains("Exec"))
				exec = payload;
			else if (line.contains("Type"))
				type = payload;
			else if (line.contains("Categories"))
				categories = payload;
			else if (line.contains("Terminal"))
				terminal = payload.contains("true");
			else if (line.contains("StartupNotify"))
				startupNotify = payload.contains("true");
		}
	}

	@Override
	public String toString() {
		return name.length() > 0 ? name + "  (" + file.getName() + ")": file.getName();
	}

	/**
	 * Get the file content string. This is exactly what should be deposited
	 * into the desktop file.
	 * 
	 * @return the exact file content string.
	 */
	public String toContentString() {
		StringBuffer s = new StringBuffer();
		s.append(HEADER).append("\n");
		s.append("Name=").append(name).append("\n");
		s.append("Comment=").append(comment).append("\n");
		s.append("Icon=").append(icon).append("\n");
		s.append("Exec=").append(exec).append("\n");
		s.append("Type=").append(type).append("\n");
		s.append("Categories=").append(categories).append("\n");
		s.append("Terminal=").append(terminal ? "true" : "false").append("\n");
		s.append("StartupNotify=").append(startupNotify ? "true" : "false")
				.append("\n");
		return s.toString();
	}

	/*
	 * Getters and setters! YES I LOVE ENCAPSULATION.
	 */

	public File getFile() {
		return file;
	}

	@Override
	public int compareTo(Entry o) {
		return name.compareTo(o.name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entry other = (Entry) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}

}

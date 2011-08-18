import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A .desktop entry.
 * 
 * @author toriscope
 * 
 */
public class Entry {

	private final File file;

	/*
	 * ENTRY STATE. I have violated standard java lowerCamelCase because I want
	 * the fields to match their file representations.
	 */

	/**
	 * The necessary first line.
	 */
	private final String HEADER = "[Desktop Entry]";

	/**
	 * The name of the application.
	 */
	private String Name = "";

	/**
	 * A crafty little comment.
	 */
	private String Comment = "";

	/**
	 * Absolute link to the application icon.
	 */
	private String Icon = "";

	/**
	 * Abs. link to executable.
	 */
	private String Exec = "";

	/**
	 * if true, run in terminal.
	 */
	private boolean Terminal = false;

	/**
	 * The type, usually 'Application'.
	 */
	private String Type = "Application";

	/**
	 * Game;TimeWaster;etc..
	 */
	private String Categories = "";

	/**
	 * if true, notify on startup.
	 */
	private boolean StartupNotify = false;

	public Entry(final File file) {
		this.file = file;
		try {
			loadFromFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load the data fields from the file.
	 * 
	 * @throws FileNotFoundException
	 */
	public void loadFromFile() throws FileNotFoundException {
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (!line.contains("="))
				continue;
			String payload = line.substring(line.indexOf("=") + 1).trim();

			// Categorize
			if (line.contains("Name"))
				Name = payload;
			else if (line.contains("Comment"))
				Comment = payload;
			else if (line.contains("Icon"))
				Icon = payload;
			else if (line.contains("Exec"))
				Exec = payload;
			else if (line.contains("Type"))
				Type = payload;
			else if (line.contains("Categories"))
				Categories = payload;
			else if (line.contains("Terminal"))
				Terminal = payload.contains("true");
			else if (line.contains("StartupNotify"))
				StartupNotify = payload.contains("true");
		}
	}

	@Override
	public String toString() {
		return file.getName();
	}

	/**
	 * Get the file content string.
	 * 
	 * @return the exact file content string.
	 */
	public String toContentString() {
		StringBuffer s = new StringBuffer();
		s.append(HEADER).append("\n");
		s.append("Name=").append(Name).append("\n");
		s.append("Comment=").append(Comment).append("\n");
		s.append("Icon=").append(Icon).append("\n");
		s.append("Exec=").append(Exec).append("\n");
		s.append("Type=").append(Type).append("\n");
		s.append("Categories=").append(Categories).append("\n");
		s.append("Terminal=").append(Terminal ? "true" : "false").append("\n");
		s.append("StartupNotify=").append(StartupNotify ? "true" : "false")
				.append("\n");
		return s.toString();
	}

	/*
	 * Getters and setters! YES I LOVE ENCAPSULATION.
	 */

	public String getName() {
		return Name;
	}

	public String getComment() {
		return Comment;
	}

	public String getIcon() {
		return Icon;
	}

	public String getExec() {
		return Exec;
	}

	public boolean isTerminal() {
		return Terminal;
	}

	public String getType() {
		return Type;
	}

	public String getCategories() {
		return Categories;
	}

	public boolean isStartupNotify() {
		return StartupNotify;
	}

	public void setName(String name) {
		Name = name;
	}

	public void setComment(String comment) {
		Comment = comment;
	}

	public void setIcon(String icon) {
		Icon = icon;
	}

	public void setExec(String exec) {
		Exec = exec;
	}

	public void setTerminal(boolean terminal) {
		Terminal = terminal;
	}

	public void setType(String type) {
		Type = type;
	}

	public void setCategories(String categories) {
		Categories = categories;
	}

	public void setStartupNotify(boolean startupNotify) {
		StartupNotify = startupNotify;
	}

	public File getFile() {
		return file;
	}
}

package menueditor;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This Program will display and allow the editing of .desktop entries. In Gnome
 * 3. Poorly put together, I know! :)
 * 
 * @author toriscope
 * 
 */
public class Gnome3MenuEditor {

	/**
	 * The usual path from within HOME to get to the application entries.
	 */
	public static final String ENTRY_DIR_FROM_HOME = "/.local/share/applications/";

	/**
	 * The entry directory.
	 */
	private static String dir = ".";

	public static void main(final String[] args) {
		if (args.length == 0) {
			String username = null;
			try {
				username = System.getProperty("user.name");
			} catch (final SecurityException e) {
				System.err
						.println("Unable to access user.name value! Please supply a directory with -d");
				System.exit(0);
			}
			if (username == null) {
				System.err
						.println("No value for the system property user.name! Please supply a directory with -d");
				System.exit(0);
			}
			dir = "/home/" + username + ENTRY_DIR_FROM_HOME;
		} else if (args[0].contains("-h") || args[0].contains("-help")) {
			System.out
					.println("This editor allows you to edit the entries in /usr/share/applications/, "
							+ "which controls the Gnome 3 Applications Menu. "
							+ "To specify a different directory to parse for entries, "
							+ "pass it in through command line with '-d'. ex: 'sudo java -jar gnome3-menu-editor -d /home/username/Desktop'.");
			System.exit(0);
		} else if (args[0].contains("-d")) {
			if (args.length == 1) {
				System.out.println("Please supply a directory along with -d");
				System.exit(0);
			}
			dir = args[1];
		}

		File entryDir = new File(dir);
		if (!entryDir.isDirectory()) {
			System.err
					.println(dir
							+ " is not a valid directory! Please specify a proper one with -d");
			System.exit(0);
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFrame frame = new JFrame("Gnome 3 Application Menu Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"images/icon.png"));
		frame.setResizable(true);
		frame.add(new ApplicationPanel(entryDir));
		frame.pack();
		frame.setVisible(true);
		frame.setMinimumSize(frame.getPreferredSize());
	}
}

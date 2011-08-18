import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;

/**
 * This Program will display and allow the editing of .desktop entries. In Gnome
 * 3.
 * 
 * @author toriscope
 * 
 */
public class Main {

	public static String ENTRY_DIRECTORY;

	public static void main(final String[] args) {

		if (args.length != 0) {
			ENTRY_DIRECTORY = args[0];
		} else {
			ENTRY_DIRECTORY = "/usr/share/applications/";
		}

		final JFrame frame = new JFrame("Gnome 3 Application Menu Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"images/icon.png"));
		frame.setResizable(false);
		frame.add(new FileListPanel(new File(ENTRY_DIRECTORY)));
		frame.pack();
		frame.setVisible(true);
	}
}

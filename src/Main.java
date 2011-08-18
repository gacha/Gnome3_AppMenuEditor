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
	
	public final static String ENTRY_DIRECTORY = "/usr/share/applications/";
	
	public static void main(final String[] args) {
		final JFrame frame = new JFrame("Gnome 3 Application Menu Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new FileListPanel(new File(ENTRY_DIRECTORY)));
		frame.pack();
		frame.setVisible(true);
	}
}

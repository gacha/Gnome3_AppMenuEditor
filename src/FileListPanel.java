import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Panel that lists the entry files and lets you edit them.
 * 
 * @author toriscope
 * 
 */
public class FileListPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * The .desktop entries.
	 */
	private List<Entry> entries;

	/*
	 * The editor components.
	 */

	private final JTextField nameField;
	private final JTextField commentField;
	private final JTextField iconField;
	private final JTextField execField;
	private final JTextField typeField;
	private final JTextField categoryField;
	private final JCheckBox terminalBox;
	private final JCheckBox startupNotifyBox;

	/**
	 * Create a panel that uses the given directory.
	 * 
	 * @param DIRECTORY
	 *            the directory containing the .desktop entries.
	 */
	public FileListPanel(final File DIRECTORY) {
		setLayout(new BorderLayout());

		entries = new ArrayList<Entry>();
		for (File file : DIRECTORY.listFiles()) {
			if (file.getName().contains(".desktop"))
				entries.add(new Entry(file));
		}

		JList<Entry> entryList = new JList<Entry>(
				entries.toArray(new Entry[] {}));
		entryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		entryList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				selectEntry(e.getFirstIndex());
			}
		});

		nameField = new JTextField(30);
		commentField = new JTextField(30);
		iconField = new JTextField(30);
		execField = new JTextField(30);
		typeField = new JTextField(30);
		categoryField = new JTextField(30);
		terminalBox = new JCheckBox("Run in Terminal");
		startupNotifyBox = new JCheckBox("Startup Notify");

		add(new JScrollPane(entryList), BorderLayout.WEST);
		add(createEditorPanel(), BorderLayout.EAST);
	}

	/**
	 * Refresh the edit box with the data at entry.
	 * 
	 * @param entry
	 *            the entry index in the list.
	 */
	public void selectEntry(final int entry) {
		Entry e = entries.get(entry);
		nameField.setText(e.getName());
		commentField.setText(e.getComment());
		iconField.setText(e.getIcon());
		execField.setText(e.getExec());
		typeField.setText(e.getType());
		categoryField.setText(e.getCategories());
		terminalBox.setSelected(e.isTerminal());
		startupNotifyBox.setSelected(e.isStartupNotify());
	}

	/**
	 * This builds the editor panel, assigns proper labels.
	 * 
	 * @return the formed editor panel.
	 */
	private JPanel createEditorPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(new JLabel("Edit Application Entry"));

		panel.add(new JSeparator(SwingConstants.HORIZONTAL));

		panel.add(new JLabel("Name:"));
		panel.add(nameField);

		panel.add(new JLabel("Comments:"));
		panel.add(commentField);

		panel.add(new JLabel("Icon Path:"));
		panel.add(iconField);

		panel.add(new JLabel("Executable Path:"));
		panel.add(execField);

		panel.add(new JLabel("Type:"));
		panel.add(typeField);

		panel.add(new JLabel("Category:"));
		panel.add(categoryField);

		panel.add(terminalBox);

		panel.add(startupNotifyBox);

		return panel;
	}
}

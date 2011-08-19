import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Panel that lists the entry files and lets you edit them.
 * 
 * @author toriscope
 * 
 */
@SuppressWarnings("serial")
public class FileListPanel extends JPanel {

	/**
	 * The directory where the entries are stored.
	 */
	private final File DIRECTORY;

	/**
	 * The .desktop entries.
	 */
	private List<Entry> entries;

	/**
	 * The JList of all the current entries.
	 */
	private JList<Entry> entryList;

	/**
	 * The current edited entry.
	 */
	private Entry current;

	/*
	 * The editor components.
	 */
	private final JLabel fileLabel;
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

		this.DIRECTORY = DIRECTORY;

		setLayout(new BorderLayout());

		fileLabel = new JLabel("Click a file to the left to edit!");
		nameField = new JTextField(30);
		commentField = new JTextField(30);
		iconField = new JTextField(30);
		execField = new JTextField(30);
		typeField = new JTextField(30);
		categoryField = new JTextField(30);
		terminalBox = new JCheckBox("Run in Terminal");
		startupNotifyBox = new JCheckBox("Startup Notify");

		add(createEditorPanel(), BorderLayout.EAST);

		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

		entryList = new JList<Entry>();
		entryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		entryList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (entryList.getSelectedIndex() >= 0) {
					loadEntry(entryList.getSelectedIndex());
				}
			}
		});
		listPanel.add(new JScrollPane(entryList), BorderLayout.WEST);

		JButton addNewEntryButton = new JButton("Add Entry");
		addNewEntryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					createEntry();
				} catch (IOException e) {
					JOptionPane
							.showMessageDialog(null,
									"There has been an IO error! Try running this editor as root");
				}
			}
		});

		listPanel.add(addNewEntryButton);

		add(listPanel, BorderLayout.WEST);

		refreshEntries();

		new Timer(5000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refreshEntries();
			}
		}).start();
	}

	/**
	 * Create an entry in the DIRECTORY, and loadEntry().
	 * 
	 * @throws IOException
	 */
	public void createEntry() throws IOException {
		String s = JOptionPane
				.showInputDialog("Name of new entry? (omit the extension)");
		if (s != null && !s.isEmpty()) {
			File f = new File(DIRECTORY + "/" + s + ".desktop");
			new FileWriter(f).close();
			loadEntry(new Entry(f));
		}
	}

	/**
	 * Refresh the JList with he latest entries in the directory.
	 */
	public void refreshEntries() {
		entries = new ArrayList<Entry>();
		for (File file : DIRECTORY.listFiles()) {
			if (file.getName().contains(".desktop"))
				entries.add(new Entry(file));
		}

		Entry[] newEntries = entries.toArray(new Entry[] {});
		entryList.setListData(newEntries);
		Main.frame.pack();
	}

	/*
	 * Entry I/O.
	 */

	/**
	 * Refresh the edit box with the data at entry. The current entry pointer is
	 * set to the selected entry.
	 * 
	 * @param entryIndex
	 *            the entry index in the list.
	 */
	public void loadEntry(final int entryIndex) {
		loadEntry(entries.get(entryIndex));
	}

	/**
	 * Refresh the edit box with the data at entry. The current entry pointer is
	 * set to the selected entry.
	 * 
	 * @param e
	 *            the entry to load.
	 */
	public void loadEntry(final Entry e) {
		current = e;
		fileLabel.setText("Editing " + e.getFile().getName());
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
	 * Save the current editor content to the given entry and write to disk.
	 * 
	 * @param entry
	 *            entry object to save.
	 * @throws IOException
	 */
	public void saveEntry(final Entry entry) throws IOException {
		if (entry == null)
			return;
		entry.setName(nameField.getText());
		entry.setComment(commentField.getText());
		entry.setIcon(iconField.getText());
		entry.setExec(execField.getText());
		entry.setType(typeField.getText());
		entry.setCategories(categoryField.getText());
		entry.setTerminal(terminalBox.isSelected());
		entry.setStartupNotify(startupNotifyBox.isSelected());

		FileWriter writer = new FileWriter(entry.getFile());
		writer.write(entry.toContentString());
		writer.close();

		JOptionPane.showMessageDialog(null, entry.getFile().getName()
				+ " has been saved!");
	}

	/*
	 * Support Functions.
	 */

	/**
	 * This builds the editor panel, assigns proper labels.
	 * 
	 * @return the formed editor panel.
	 */
	private JPanel createEditorPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(fileLabel);

		panel.add(new JSeparator(SwingConstants.HORIZONTAL));

		panel.add(new JLabel("Name:"));
		panel.add(nameField);

		panel.add(new JSeparator(SwingConstants.HORIZONTAL));

		panel.add(new JLabel("Comments:"));
		panel.add(commentField);

		panel.add(new JSeparator(SwingConstants.HORIZONTAL));

		panel.add(new JLabel("Icon Path:"));
		panel.add(iconField);
		JButton fileButton = new JButton("Find File");
		fileButton.addActionListener(fileChooseActionListener(iconField));
		panel.add(fileButton);

		panel.add(new JSeparator(SwingConstants.HORIZONTAL));

		panel.add(new JLabel("Executable Path:"));
		panel.add(execField);
		fileButton = new JButton("Find File");
		fileButton.addActionListener(fileChooseActionListener(execField));
		panel.add(fileButton);

		panel.add(new JSeparator(SwingConstants.HORIZONTAL));

		panel.add(new JLabel("Type:"));
		panel.add(typeField);

		panel.add(new JSeparator(SwingConstants.HORIZONTAL));

		panel.add(new JLabel("Categories (';' seperated):"));
		panel.add(categoryField);

		panel.add(new JSeparator(SwingConstants.HORIZONTAL));

		panel.add(terminalBox);

		panel.add(new JSeparator(SwingConstants.HORIZONTAL));

		panel.add(startupNotifyBox);

		panel.add(new JSeparator(SwingConstants.HORIZONTAL));

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					saveEntry(current);
				} catch (IOException e) {
					JOptionPane
							.showMessageDialog(null,
									"There has been an IO error! Try running this editor as root");
				}
			}
		});
		panel.add(saveButton);

		return panel;
	}

	/**
	 * This returns an ActionListener that spawns a file chooser and dumps the
	 * resulting file into a given text field.
	 * 
	 * @param field
	 *            the field to dump the value into.
	 * @return the manufactured actionListener.
	 */
	private ActionListener fileChooseActionListener(final JTextField field) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					field.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		};
	}
}

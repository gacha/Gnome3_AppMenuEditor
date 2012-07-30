package menueditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Panel that lists the entry files and lets you edit them. Changes are made to
 * the Entries first, and then the the new entry text is generated by the entry
 * and written to disc. TODO: Split up view and logic.
 * 
 * @author toriscope
 * 
 */
@SuppressWarnings("serial")
public class ApplicationPanel extends JPanel {

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
	private JList entryList;

	/**
	 * The current edited entry.
	 */
	private Entry current;

	/**
	 * Last time directory was edited.
	 */
	private long lastEdit;

	/*
	 * The editor components.
	 */
	private JButton saveButton;
	private final JLabel fileLabel;
	private final JTextField nameField, commentField, iconField, execField,typeField, categoryField;
	private final JCheckBox terminalBox, startupNotifyBox;

	/**
	 * Create a panel that uses the given directory. This is where the button
	 * actions are assigned and the initial refresh is performed.
	 * 
	 * @param DIRECTORY
	 *            the directory containing the .desktop entries.
	 */
	public ApplicationPanel(final File DIRECTORY) {

		this.DIRECTORY = DIRECTORY;

		lastEdit = DIRECTORY.lastModified();

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

		/*
		 * Instantiate the editor components.
		 */

		fileLabel = new JLabel("Click a file to the left to edit!");
		nameField = new JTextField(30);
		commentField = new JTextField(30);
		iconField = new JTextField(30);
		execField = new JTextField(30);
		typeField = new JTextField(30);
		categoryField = new JTextField(30);
		terminalBox = new JCheckBox("Run in Terminal");
		startupNotifyBox = new JCheckBox("Startup Notify");
		add(createListPanel(), BorderLayout.LINE_START);
		add(createEditorPanel(), BorderLayout.CENTER);

		refreshEntries(true);

		new Timer(5000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refreshEntries(false);
			}
		}).start();
	}

	/**
	 * Create an entry in the DIRECTORY, and loadEntry().
	 * 
	 * @throws IOException
	 */
	public Entry createEntry() throws IOException {
		String s = JOptionPane
				.showInputDialog("Filename of new entry? (omit the extension)\nIt should then appear in the list.");
		if (s != null && !s.isEmpty()) {
			File f = new File(DIRECTORY + "/" + s + ".desktop");
			new FileWriter(f).close();
			return new Entry(f);
		}
		return null;
	}

	/**
	 * Delete the file component of the given entry.
	 * 
	 * @param entry
	 *            the entry to remove from disk.
	 */
	public void deleteEntry(final Entry entry) {
		File f = entry.getFile();
		int result = JOptionPane.showConfirmDialog(null,
				"Delete the file " + f.getName() + "?", "Confirm Deletion!",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			f.delete();
		}
	}

	/**
	 * Refresh the JList with he latest entries in the directory.
	 */
	public void refreshEntries(boolean force) {
		if (DIRECTORY.lastModified() > lastEdit || force) {
			entries = new ArrayList<Entry>();
			try {
				for (File file : DIRECTORY.listFiles()) {
					if (file.getName().endsWith(".desktop"))
						entries.add(new Entry(file));
				}
			} catch (IOException ex) {
				ioWarning();
			}
			Collections.sort(entries);
			entryList.setListData(entries.toArray(new Entry[] {}));
			lastEdit = DIRECTORY.lastModified();
		}
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
		fileLabel.setText("Editing '" + e.getFile().getName() + "'");
		nameField.setText(e.name);
		commentField.setText(e.comment);
		iconField.setText(e.icon);
		execField.setText(e.exec);
		typeField.setText(e.type);
		categoryField.setText(e.categories);
		terminalBox.setSelected(e.terminal);
		startupNotifyBox.setSelected(e.startupNotify);
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
		entry.name = nameField.getText();
		entry.comment = commentField.getText();
		entry.icon = iconField.getText();
		entry.exec = execField.getText();
		entry.type = typeField.getText();
		entry.categories = categoryField.getText();
		entry.terminal = terminalBox.isSelected();
		entry.startupNotify = startupNotifyBox.isSelected();

		FileWriter writer = new FileWriter(entry.getFile());
		writer.write(entry.toContentString());
		writer.close();
		refreshEntries(true);
		entryList.setSelectedValue(entry, true);
		JOptionPane.showMessageDialog(null, entry.getFile().getName()
				+ " has been saved!");
	}

	/*
	 * Support Functions.
	 */

	private JPanel createListPanel() {
		JPanel titlePanel = new JPanel();
		titlePanel.add(new JLabel(DIRECTORY.getAbsolutePath()));
		entryList = new JList();
		entryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		entryList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (entryList.getSelectedIndex() >= 0) {
					loadEntry(entryList.getSelectedIndex());
					saveButton.setEnabled(true);
				} else {
					saveButton.setEnabled(false);
				}
			}
		});

		JToolBar buttonPanel = new JToolBar(JToolBar.HORIZONTAL);
		buttonPanel.setFloatable(false);

		JButton addNewEntryButton = new JButton("+");
		addNewEntryButton.setToolTipText("Create Blank Entry");
		addNewEntryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Entry e = createEntry();
					refreshEntries(true);
					entryList.setSelectedValue(e, true);
				} catch (IOException e) {
					ioWarning();
				}
			}
		});
		buttonPanel.add(addNewEntryButton);
		JButton removeEntryButton = new JButton("-");
		removeEntryButton.setToolTipText("Delete Selected Entry");
		removeEntryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selection = entryList.getSelectedIndex();
				if (selection < 0)
					return;
				Entry e = entries.get(selection);
				if (e.getFile().canWrite()) {
					deleteEntry(e);
					refreshEntries(true);
				} else {
					ioWarning();
				}
			}
		});
		buttonPanel.add(removeEntryButton);

		JPanel listPanel = new JPanel();
		listPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		listPanel.setLayout(new BorderLayout());
		listPanel.add(titlePanel, BorderLayout.PAGE_START);
		listPanel.add(new JScrollPane(entryList), BorderLayout.CENTER);
		listPanel.add(buttonPanel, BorderLayout.PAGE_END);

		return listPanel;
	}

	/**
	 * This builds the editor panel, assigns proper labels.
	 * 
	 * @return the formed editor panel.
	 */
	private JPanel createEditorPanel() {
		JPanel titlePanel = new JPanel();
		titlePanel.add(fileLabel);

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		contentPanel.add(new JLabel("Name:"));
		contentPanel.add(nameField);

		contentPanel.add(new JLabel("Comments:"));
		contentPanel.add(commentField);

		contentPanel.add(new JLabel("Icon Path:"));
		JPanel iconcontentPanel = new JPanel(new BorderLayout());
		iconcontentPanel.add(iconField, BorderLayout.CENTER);
		JButton iconButton = new JButton("...");
		iconButton.addActionListener(fileChooseActionListener(iconField));
		iconcontentPanel.add(iconButton, BorderLayout.LINE_END);
		contentPanel.add(iconcontentPanel);

		contentPanel.add(new JLabel("Executable Path:"));
		JButton fileButton = new JButton("...");
		fileButton.addActionListener(fileChooseActionListener(execField));
		JPanel filePanel = new JPanel(new BorderLayout());
		filePanel.add(execField, BorderLayout.CENTER);
		filePanel.add(fileButton, BorderLayout.LINE_END);
		contentPanel.add(filePanel);

		contentPanel.add(new JLabel("Type:"));
		contentPanel.add(typeField);

		contentPanel.add(new JLabel("Categories (';' seperated):"));
		contentPanel.add(categoryField);

		contentPanel.add(terminalBox);

		contentPanel.add(startupNotifyBox);

		for (Component c : contentPanel.getComponents()) {
			if (c instanceof JComponent) {
				((JComponent) c).setAlignmentX(LEFT_ALIGNMENT);
			}
		}

		JPanel buttonPanel = new JPanel();
		saveButton = new JButton("Save Edits");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					saveEntry(current);
				} catch (IOException e) {
					ioWarning();
				}
			}
		});
		saveButton.setEnabled(false);
		buttonPanel.add(saveButton);

		JPanel noResizePanel = new JPanel();
		noResizePanel.setLayout(new BorderLayout());
		noResizePanel.add(titlePanel, BorderLayout.PAGE_START);
		noResizePanel.add(contentPanel, BorderLayout.CENTER);
		JPanel editorPanel = new JPanel();
		editorPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		editorPanel.setLayout(new BorderLayout());
		editorPanel.add(noResizePanel, BorderLayout.PAGE_START);
		editorPanel.add(buttonPanel, BorderLayout.PAGE_END);
		return editorPanel;
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
					field.setText(sanitizePath(fc.getSelectedFile().getAbsolutePath()));
				}
			}
		};
	}

	/**
	 * Adds path into quotes, to allow filenames with spaces
	 *
	 * @param patrh
	 *
	 * @return sanitized path
	 */
	private String sanitizePath(String path){
		return ('"' + path.replaceAll("\"", "\\\\\"") + '"');
	}

	/**
	 * Display an adorable little reminder to run the application as root.
	 */
	private void ioWarning() {
		JOptionPane.showMessageDialog(null, "There has been an I/O error! Try running this editor as root");
	}
}

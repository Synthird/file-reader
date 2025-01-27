import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainFrame extends JFrame implements ActionListener {
	// Default variables
	String defaultTitle = "File reader";
	Desktop desktop = Desktop.getDesktop();
	File defafultLocation = new File(System.getProperty("user.home"));
	JFileChooser fileChooser = new JFileChooser();

	Font textFont = new Font(Font.MONOSPACED, Font.PLAIN, 15);
	float fontSize = (float) textFont.getSize();

	// Dark mode colours
	Color darkBackgroundColour = new Color(30, 30, 30);
	Color darkTextBackgroundColour = new Color(30, 30, 30);

	Color whiteText = new Color(215, 215, 215);

	// Light mode colours
	Color lightBackgroundColour = new Color(255, 255, 255);
	Color lightTextBackgroundColour = new Color(255, 255, 255);

	Color blackText = new Color(51, 51, 51);

	// Blank variables
	FileReader fileReader;
	File locationPath;

	JScrollPane scrollPane;
	JScrollBar verticalScrollBar, horizontalScrollBar;
	JTextArea textArea;

	JMenuBar menuBar;
	JMenuItem open, openLocation, findText, copyText, clearText, increaseSize, decreaseSize, darkChoice, lightChoice;

	Boolean darkMode;

	public MainFrame() {
		fileChooser.setDialogTitle("Open file");
		fileChooser.setCurrentDirectory(defafultLocation);
		darkMode = false;

		// Textbox/textfield
		textArea = new JTextArea("Open a file to view its contents.");
		textArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		textArea.setFont(textFont);
		textArea.setEditable(false);

		scrollPane = new JScrollPane(textArea);
		verticalScrollBar = scrollPane.getVerticalScrollBar();
		horizontalScrollBar = scrollPane.getHorizontalScrollBar();
		scrollPane.setOpaque(false);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 0, 3));
		this.add(scrollPane, BorderLayout.CENTER);

		// Menubar
		menuBar = new JMenuBar();
		menuBar.setBorder(BorderFactory.createEmptyBorder());
		menuBar.setOpaque(false);

		// File menu
		JMenu fileMenu = new JMenu("File");

		open = new JMenuItem("Open file");
		open.addActionListener(this);
		fileMenu.add(open);

		openLocation = new JMenuItem("Open file location");
		openLocation.addActionListener(this);
		fileMenu.add(openLocation);

		menuBar.add(fileMenu);

		// Text menu
		JMenu textMenu = new JMenu("Text");

		findText = new JMenuItem("Find text");
		findText.addActionListener(this);
		textMenu.add(findText);

		copyText = new JMenuItem("Copy text");
		copyText.addActionListener(this);
		textMenu.add(copyText);

		clearText = new JMenuItem("Clear text");
		clearText.addActionListener(this);
		textMenu.add(clearText);

		increaseSize = new JMenuItem("+ Increase size");
		increaseSize.addActionListener(this);
		textMenu.add(increaseSize);

		decreaseSize = new JMenuItem("\u2014 Decrease size");
		decreaseSize.addActionListener(this);
		textMenu.add(decreaseSize);

		menuBar.add(textMenu);

		// Theme menu
		JMenu themeMenu = new JMenu("Theme");

		darkChoice = new JMenuItem("\u263d Dark");
		darkChoice.addActionListener(this);
		themeMenu.add(darkChoice);

		lightChoice = new JMenuItem("\u263c Light");
		lightChoice.addActionListener(this);
		themeMenu.add(lightChoice);

		menuBar.add(themeMenu);

		// Window setup
		setWindowTitle(defaultTitle);
		this.setMinimumSize(new Dimension(600, 400));
		this.setJMenuBar(menuBar);
		this.getContentPane().setBackground(lightBackgroundColour);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == open) {
			// Open the choose file dialog
			if (locationPath == null || !locationPath.exists()) {
				fileChooser.setCurrentDirectory(defafultLocation);
			} else {
				fileChooser.setCurrentDirectory(locationPath);
			}

			int fileChosen = fileChooser.showOpenDialog(this);

			if (fileChosen == JFileChooser.APPROVE_OPTION) {
				try {
					// Read a file
					String filePath = fileChooser.getSelectedFile().getAbsolutePath();

					File file = new File(filePath);
					fileReader = new FileReader(file, StandardCharsets.ISO_8859_1);

					textArea.read(fileReader, null);
					locationPath = file.getParentFile();
					fileChooser.setCurrentDirectory(locationPath);

					setWindowTitle(String.format("%s (%s)", defaultTitle, filePath));
				} catch (IOException | SecurityException | NullPointerException exception) {
					showErrorDialog("This file cannot be read or found....", "Unable to read!");
				} finally {
					// Close the file reader and destroy it
					if (fileReader != null) {
						try {
							fileReader.close();
						} catch (IOException iOException) {
							showErrorDialog(
									"This file can no longer be renamed or deleted when the file reader is opened....",
									"File reader prevents this file to be deleted or renamed!");
						} finally {
							fileReader = null;
						}
					}
				}
			}
		} else if (e.getSource() == copyText) {
			// Copy text
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(textArea.getText()), null);
			JOptionPane.showMessageDialog(this, "Text copied to clipboard!", "Text copied!",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (e.getSource() == clearText) {
			// Clear text
			textArea.setText("");
			setWindowTitle(defaultTitle);
			locationPath = null;
		} else if (e.getSource() == findText) {
			// Find text
			findText.setEnabled(false);
			new FindText(textArea, findText);
		} else if (e.getSource() == openLocation) {
			// Open file location
			try {
				desktop.open(locationPath);
			} catch (IOException | IllegalArgumentException | SecurityException openLocationException) {
				showCannotOpenLocationDialog("Cannot open file location! A folder has been deleted or renamed.....");
			} catch (UnsupportedOperationException unsupportedOperationException) {
				showCannotOpenLocationDialog("Opening a file location is not supported on this platform :(");
			} catch (NullPointerException nullPointerException) {
				showCannotOpenLocationDialog("A file isn't opened.....");
			}
		} else if (e.getSource() == decreaseSize) {
			// Decrease font size
			textArea.setFont(textFont.deriveFont(--fontSize));
		} else if (e.getSource() == increaseSize) {
			// Increase font size
			textArea.setFont(textFont.deriveFont(++fontSize));
		} else if (e.getSource() == darkChoice) {
			setTheme(darkBackgroundColour, darkTextBackgroundColour, whiteText);
		} else if (e.getSource() == lightChoice) {
			setTheme(lightBackgroundColour, lightTextBackgroundColour, blackText);
		}
	}

	// GUI setup

	private void setTheme(Color backgroundColour, Color textBackgroundColour, Color textColour) {
		this.getContentPane().setBackground(backgroundColour);
		// Changing the scrollbar colours
		verticalScrollBar.setBackground(backgroundColour);
		horizontalScrollBar.setBackground(backgroundColour);
		// Changing the textbox's colours
		textArea.setBackground(textBackgroundColour);
		textArea.setForeground(textColour);
		textArea.setCaretColor(textColour);
	}

	private void setWindowTitle(String text) {
		this.setTitle(text);
	}

	private void showErrorDialog(String message, String title) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}

	private void showCannotOpenLocationDialog(String message) {
		showErrorDialog(message, "Unable to open file explorer!");
	}
}

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainFrame extends JFrame implements ActionListener {
	String defaultTitle = "File reader";
	Desktop desktop = Desktop.getDesktop();

	Scanner scanner;
	File locationPath;

	JScrollPane scrollPane;
	JScrollBar verticalScrollBar;
	JScrollBar horizontalScrollBar;
	JTextArea textArea;

	JPanel buttonPanel;
	JButton openFile;
	JButton copyButton;
	JButton openFileLocationButton;
	JButton clearButton;
	JButton findButton;
	JButton changeThemeButton;

	Boolean darkMode;

	// Dark mode colours
	Color darkBackgroundColour = new Color(40, 40, 40);
	Color darkSecondaryBackgroundColour = new Color(30, 30, 30);

	Color whiteText = new Color(215, 215, 215);

	// Light mode colours
	Color lightBackgroundColour = this.getContentPane().getBackground();
	Color lightSecondaryBackgroundColour = new Color(243, 243, 243);

	Color blackText = new Color(51, 51, 51);

	public MainFrame() {
		darkMode = false;

		// Textbox/textfield
		textArea = new JTextArea("Open a file to view its contents.");
		textArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
		textArea.setEditable(false);

		scrollPane = new JScrollPane(textArea);
		verticalScrollBar = scrollPane.getVerticalScrollBar();
		horizontalScrollBar = scrollPane.getHorizontalScrollBar();
		scrollPane.setOpaque(false);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 0, 3));
		this.add(scrollPane, BorderLayout.CENTER);

		// Buttons
		buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		this.add(buttonPanel, BorderLayout.SOUTH);

		openFile = setUpButton("Open file");
		openFileLocationButton = setUpButton("Open file location");
		findButton = setUpButton("Find text");
		copyButton = setUpButton("Copy text");
		clearButton = setUpButton("Clear text");
		changeThemeButton = setUpButton("Toggle dark mode");

		// Window setup
		setWindowTitle(defaultTitle);
		this.setSize(675, 400);
		this.setLocationRelativeTo(null);
		this.setMinimumSize(this.getSize());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openFile) {
			JFileChooser fileChooser = new JFileChooser();
			int fileChosen = fileChooser.showOpenDialog(this);

			if (fileChosen == JFileChooser.APPROVE_OPTION) {
				String filePath = fileChooser.getSelectedFile().getAbsolutePath();
				File file = new File(filePath);

				try {
					scanner = new Scanner(new FileInputStream(file), StandardCharsets.ISO_8859_1);
				} catch (FileNotFoundException | SecurityException exception) {
					showErrorDialog("This file cannot be read or found....", "Unable to read!");
				} finally {
					if (scanner != null) {
						locationPath = file.getParentFile();
						textArea.setText("");

						while (scanner.hasNextLine()) {
							textArea.append(String.format("%s\n", scanner.nextLine()));
						}

						textArea.setCaretPosition(0);
						setWindowTitle(String.format("%s (%s)", defaultTitle, filePath));
						scanner.close();
						scanner = null;
					}
				}
			}
		} else if (e.getSource() == changeThemeButton) {
			darkMode = !darkMode;

			if (darkMode) {
				setTheme(darkBackgroundColour, darkSecondaryBackgroundColour, whiteText, "light");
			} else {
				setTheme(lightBackgroundColour, lightSecondaryBackgroundColour, blackText, "dark");
			}
		} else if (e.getSource() == copyButton) {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(textArea.getText()), null);
			JOptionPane.showMessageDialog(this, "Text copied to clipboard!", "Text copied!",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (e.getSource() == clearButton) {
			textArea.setText("");
			setWindowTitle(defaultTitle);
			locationPath = null;
		} else if (e.getSource() == findButton) {
			findButton.setEnabled(false);
			new FindText(textArea, findButton);
		} else if (e.getSource() == openFileLocationButton) {
			if (locationPath != null && locationPath.exists()) {
				try {
					desktop.open(locationPath);
				} catch (IOException e1) {
					showCannotFindLocationDialog();
				}
			} else {
				showCannotFindLocationDialog();
			}
		}
	}

	// GUI setup

	private JButton setUpButton(String buttonText) {
		JButton button = new JButton(buttonText);
		button.setFocusable(false);
		button.addActionListener(this);
		buttonPanel.add(button);
		return button;
	}

	private void setTheme(Color backgroundColour, Color textBackgroundColour, Color textColour, String themeName) {
		changeThemeButton.setText(String.format("Toggle %s mode", themeName));
		this.getContentPane().setBackground(backgroundColour);
		// Changing the scrollbar colours
		verticalScrollBar.setBackground(backgroundColour);
		horizontalScrollBar.setBackground(backgroundColour);
		// Changing the textbox's colours
		textArea.setBackground(textBackgroundColour);
		textArea.setForeground(textColour);
		textArea.setCaretColor(textColour);
		// Changing the colours of the buttons
		for (Component component : buttonPanel.getComponents()) {
			component.setBackground(backgroundColour);
			component.setForeground(textColour);
		}
	}

	private void setWindowTitle(String text) {
		this.setTitle(text);
	}

	private void showErrorDialog(String message, String title) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}

	private void showCannotFindLocationDialog() {
		showErrorDialog("Cannot open file location! It doesn't exist, or it has been renamed.....",
				"Unable to open file explorer!");
	}
}

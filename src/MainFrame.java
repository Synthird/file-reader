import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	Scanner scanner;

	JScrollPane scrollPane;
	JScrollBar verticalScrollBar;
	JScrollBar horizontalScrollBar;
	JTextArea textArea;

	JPanel buttonPanel;
	JButton openFile;
	JButton copyButton;
	JButton clearButton;
	JButton changeThemeButton;

	Boolean darkMode = false;

	// Dark mode colours
	Color darkBackgroundColour = new Color(40, 40, 40);
	Color darkSecondaryBackgroundColour = new Color(30, 30, 30);

	Color whiteText = new Color(215, 215, 215);

	// Light mode colours
	Color lightBackgroundColour = this.getContentPane().getBackground();
	Color lightSecondaryBackgroundColour = new Color(243, 243, 243);

	Color blackText = new Color(51, 51, 51);

	public MainFrame() {
		// Textbox/textfield
		textArea = new JTextArea("Open a file to view its contents.");
		textArea.setBackground(lightSecondaryBackgroundColour);
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
		copyButton = setUpButton("Copy entire text");
		clearButton = setUpButton("Clear text");
		changeThemeButton = setUpButton("Toggle dark mode");

		// Window setup
		this.setTitle("File reader");
		this.setSize(512, 400);
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
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

				try {
					scanner = new Scanner(new FileInputStream(file));
				} catch (FileNotFoundException | SecurityException exception) {
					JOptionPane.showMessageDialog(null, "Either the file cannot be read or found....",
							"Unable to read!", JOptionPane.ERROR_MESSAGE);
				} finally {
					if (scanner != null) {
						textArea.setText("");

						while (scanner.hasNextLine()) {
							textArea.append(String.format("%s \n", scanner.nextLine()));
						}

						textArea.setCaretPosition(0);
						scanner.close();
						scanner = null;
					}
				}
			}
		} else if (e.getSource() == changeThemeButton) {
			darkMode = !darkMode;

			if (darkMode) {
				setUpTheme(darkBackgroundColour, darkSecondaryBackgroundColour, whiteText, "light");
			} else {
				setUpTheme(lightBackgroundColour, lightSecondaryBackgroundColour, blackText, "dark");
			}
		} else if (e.getSource() == copyButton) {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(textArea.getText()), null);
			JOptionPane.showMessageDialog(null, "Text copied to clipboard!", "Text copied!", JOptionPane.PLAIN_MESSAGE);
		} else if (e.getSource() == clearButton) {
			textArea.setText("");
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

	private void setUpTheme(Color backgroundColour, Color textBackgroundColour, Color textColour, String themeName) {
		changeThemeButton.setText(String.format("Toggle %s mode", themeName));
		this.getContentPane().setBackground(backgroundColour);
		verticalScrollBar.setBackground(backgroundColour);
		horizontalScrollBar.setBackground(backgroundColour);
		textArea.setBackground(textBackgroundColour);
		textArea.setForeground(textColour);
		textArea.setCaretColor(textColour);
		for (Component component : buttonPanel.getComponents()) {
			component.setBackground(backgroundColour);
			component.setForeground(textColour);
		}
	}
}

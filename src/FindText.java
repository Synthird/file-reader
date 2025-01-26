import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;

public class FindText extends JFrame implements ActionListener, WindowListener {
	// Default variables
	DefaultHighlightPainter highlightColour = new DefaultHighlightPainter(Color.RED);

	// Blank variables
	JTextField textField;
	JTextArea textArea;
	Highlighter highlighter;

	JPanel buttonPanel;
	JMenuItem clickedButton;
	JButton clearHighlights, findButton;

	public FindText(JTextArea chosenTextArea, JMenuItem openFindGUI) {
		textArea = chosenTextArea;
		clickedButton = openFindGUI;

		highlighter = textArea.getHighlighter();

		// TextField setup
		textField = new JTextField();
		textField.setFont(new Font("Monospaced", Font.PLAIN, 13));
		textField.addActionListener(this);
		this.add(textField);

		// Button setup
		buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		this.add(buttonPanel, BorderLayout.SOUTH);

		findButton = setUpButton("Find");
		clearHighlights = setUpButton("Clear highlighted text");

		// Window setup
		this.setTitle("Find text (Case sensitive)");
		this.setSize(324, 110);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == textField || e.getSource() == findButton) {
			// Highlight text to be looked for
			Pattern pattern = Pattern.compile(textField.getText());
			Matcher matcher = pattern.matcher(textArea.getText());

			highlighter.removeAllHighlights();

			while (matcher.find()) {
				try {
					highlighter.addHighlight(matcher.start(), matcher.end(), highlightColour);
				} catch (IllegalStateException | BadLocationException exception) {
					JOptionPane.showMessageDialog(this, "Unable to perform the find text feature....",
							"Finding text failed!", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (e.getSource() == clearHighlights) {
			highlighter.removeAllHighlights();
		}
	}

	private JButton setUpButton(String buttonText) {
		JButton button = new JButton(buttonText);
		button.setFocusable(false);
		button.addActionListener(this);
		buttonPanel.add(button);
		return button;
	}

	@Override
	public void windowClosed(WindowEvent e) {
		highlighter.removeAllHighlights();
		clickedButton.setEnabled(true);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}

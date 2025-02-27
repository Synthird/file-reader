package TextAreaManipulation;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ResizeText extends JFrame implements ChangeListener, WindowListener {
	// MainFrame components
	JFrame mainFrame;
	JTextArea textArea;
	Font textFont;

	// Unique components
	JLabel sizeLabel;
	JSlider slider;
	JMenuItem sizeButton;

	String textTemplate = "Text size: %d";

	public ResizeText(JTextArea textArea, JFrame mainFrame, JMenuItem sizeButton) {
		sizeButton.setEnabled(false);

		this.mainFrame = mainFrame;
		this.sizeButton = sizeButton;

		this.textArea = textArea;
		this.textFont = textArea.getFont();

		// Slider
		slider = new JSlider(0, 90, textFont.getSize());
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(5);
		slider.setMajorTickSpacing(10);
		slider.setPaintLabels(true);
		slider.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
		slider.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
		slider.addChangeListener(this);
		this.add(slider);

		// Text size label
		sizeLabel = new JLabel(String.format(textTemplate, slider.getValue()), JLabel.CENTER);
		sizeLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		this.add(sizeLabel, BorderLayout.NORTH);

		// Window setup
		this.setTitle("Resize text");
		this.pack();
		this.setSize(this.getWidth() + 40, this.getHeight());
		this.setLocationRelativeTo(mainFrame);
		this.setResizable(false);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		int sliderValue = slider.getValue();
		textArea.setFont(textFont.deriveFont((float) sliderValue));
		sizeLabel.setText(String.format(textTemplate, sliderValue));
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		sizeButton.setEnabled(true);
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

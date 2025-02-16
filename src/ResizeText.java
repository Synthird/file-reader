import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
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

	String textTemplate = "Size: %d";

	public ResizeText(JTextArea textArea, JFrame mainFrame, JMenuItem sizeButton) {
		sizeButton.setEnabled(false);

		this.mainFrame = mainFrame;
		this.sizeButton = sizeButton;

		this.textArea = textArea;
		this.textFont = textArea.getFont();

		slider = new JSlider(0, 90, textFont.getSize());
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(5);
		slider.setMajorTickSpacing(10);
		slider.setPaintLabels(true);
		slider.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
		slider.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
		slider.addChangeListener(this);
		this.add(slider);

		JPanel sizeLabelPanel = new JPanel();
		sizeLabelPanel.setOpaque(false);

		sizeLabel = new JLabel(String.format(textTemplate, slider.getValue()));
		sizeLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		sizeLabelPanel.add(sizeLabel);

		this.add(sizeLabelPanel, BorderLayout.NORTH);

		this.setTitle("Change text size");
		this.pack();
		this.setSize(this.getWidth() + 73, this.getHeight());
		this.setLocationRelativeTo(mainFrame);
		this.setResizable(false);
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

import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
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
	JSlider slider;
	JMenuItem sizeButton;

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
		slider.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		slider.addChangeListener(this);
		this.add(slider);

		this.setTitle("Change text size");
		this.setSize(310, 90);
		this.setLocationRelativeTo(mainFrame);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		textArea.setFont(textFont.deriveFont((float) slider.getValue()));
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

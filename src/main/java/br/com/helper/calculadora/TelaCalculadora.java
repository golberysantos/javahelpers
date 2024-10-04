package br.com.helper.calculadora;

import javax.swing.JFrame;

public class TelaCalculadora {

	private JFrame frame;	

	/**
	 * Create the application.
	 */
	public TelaCalculadora() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}

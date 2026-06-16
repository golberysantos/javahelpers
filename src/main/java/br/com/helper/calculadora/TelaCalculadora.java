package br.com.helper.calculadora;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class TelaCalculadora extends Calculadora implements ActionListener {
	private JFrame frame;
	private JTextField txtNum1;
	private JTextField txtNum2;
	private JPanel panel;
	private JLabel lblNewLabel;
	private float num1;
	private float num2;

	public TelaCalculadora() {

		frame = new JFrame();
		txtNum1 = new JTextField();
		txtNum1.setHorizontalAlignment(SwingConstants.CENTER);
		txtNum2 = new JTextField();
		panel = new JPanel();
		lblNewLabel = new JLabel("CALCULADORA GOLBERY");

		frame.setBounds(100, 100, 502, 502);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Courier New", Font.BOLD, 24));
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(19, Short.MAX_VALUE)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 457, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 250, Short.MAX_VALUE)
					.addContainerGap())
		);

		txtNum1.setFont(new Font("Courier New", Font.BOLD, 32));
		txtNum1.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Entre com o primeiro número");
		lblNewLabel_1.setFont(new Font("Courier New", Font.BOLD, 24));

		txtNum2 = new JTextField();
		txtNum2.setHorizontalAlignment(SwingConstants.CENTER);
		txtNum2.setFont(new Font("Courier New", Font.BOLD, 32));
		txtNum2.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Entre com o segundo número");
		lblNewLabel_2.setFont(new Font("Courier New", Font.BOLD, 24));

		JButton btnSoma = new JButton("+");
		btnSoma.setFont(new Font("Arial Black", Font.BOLD, 32));

		JButton btnSubtracao = new JButton("-");
		btnSubtracao.setFont(new Font("Arial Black", Font.BOLD, 32));

		JButton btnMult = new JButton("x");
		btnMult.setFont(new Font("Arial Black", Font.BOLD, 32));

		JButton btnDiv = new JButton("/");
		btnDiv.setFont(new Font("Arial Black", Font.BOLD, 32));

		JLabel lblNewLabel_3 = new JLabel("Resultado:  ");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 32));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(31)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblNewLabel_3)
							.addContainerGap())
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
								.addComponent(txtNum1)
								.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblNewLabel_2)
								.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
									.addComponent(btnSoma, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
									.addGap(43)
									.addComponent(btnSubtracao, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
									.addGap(40)
									.addComponent(btnMult, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
									.addComponent(btnDiv, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
								.addComponent(txtNum2))
							.addGap(26))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(18)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtNum1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblNewLabel_2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtNum2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(44)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSoma)
						.addComponent(btnDiv)
						.addComponent(btnSubtracao)
						.addComponent(btnMult))
					.addGap(41)
					.addComponent(lblNewLabel_3)
					.addContainerGap(44, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		frame.getContentPane().setLayout(groupLayout);

		// Manipular o clique
		btnSoma.addActionListener(this);
		btnSubtracao.addActionListener(this);

		frame.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String operador = e.getActionCommand();
		System.out.println("num1: "+getNum1()+" "+operador+" num2: "+getNum2());
		calcular();


	}

	public float getNum1() {
		num1 = Float.parseFloat(txtNum1.getText());
		return num1;
	}

	public float getNum2() {
		num2 = Float.parseFloat(txtNum2.getText());
		return num2;
	}
}

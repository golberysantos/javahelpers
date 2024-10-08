package br.com.helper.calculadora;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;

public class TelaCalculadora {
	private JFrame frame;
	private JTextField txtNum1;	
	private JTextField txtNum2;
	private JPanel panel;
	private JLabel lblNewLabel;
	private float num1;
	private float num2;
	
	

	
	public float getNum1() {
		num1 = txtNum1.getText();
		return num1;
	}




	public float getNum2() {
		return num2;
	}




	public TelaCalculadora() {			
		
		frame = new JFrame();
		txtNum1 = new JTextField();
		txtNum2 = new JTextField();;
		panel = new JPanel();
		lblNewLabel = new JLabel("CALCULADORA");
		
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 434, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		 
		txtNum1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtNum1.setColumns(10);		
		
		
		JLabel lblNewLabel_1 = new JLabel("Entre com o primeiro número");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		txtNum2 = new JTextField();
		txtNum2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtNum2.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Entre com o segundo número");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(90)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblNewLabel_2)
							.addContainerGap())
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
								.addComponent(lblNewLabel_1)
								.addContainerGap(112, Short.MAX_VALUE))
							.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
								.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
									.addComponent(txtNum2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
									.addComponent(txtNum1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
									.addGroup(gl_panel.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
										.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)))
								.addGap(103)))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtNum1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(42)
					.addComponent(lblNewLabel_2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtNum2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(53, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		frame.getContentPane().setLayout(groupLayout);
		frame.setVisible(true);
	}
	
	
	
	
}

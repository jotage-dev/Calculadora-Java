package br.com.jotadev.calc.visao;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.jotadev.calc.modelo.Memoria;
import br.com.jotadev.calc.modelo.MemoriaObservador;

@SuppressWarnings("serial")
public class Display extends JPanel implements MemoriaObservador {
	private JLabel label;
	
	public Display() {
		Memoria.getInstacia().adcionarObservador(this);
		
	setBackground(new Color(46, 49, 50));
		label = new JLabel(Memoria.getInstacia().getTextoAtual());
		label.setForeground(Color.WHITE);
		label.setFont(new Font("courier",Font.PLAIN , 30));
		
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		add(label);
		
		
	}
	@Override
	public void valorAlterado(String novoValor) {
		label.setText(novoValor);
		
	}
}

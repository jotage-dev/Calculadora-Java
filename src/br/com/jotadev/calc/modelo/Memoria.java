package br.com.jotadev.calc.modelo;

import java.util.ArrayList;
import java.util.List;

public class Memoria {

	private enum TipoComando {
		ZERAR, NUMERO, DIV, MULT, SUB, SOMA, IGUAL, VIRGULA
	};

	private static final Memoria instacia = new Memoria();

	private final List<MemoriaObservador> observadores = new ArrayList<>();
	
	private TipoComando ultimaOpreacao = null;
	private boolean substituir = false;
	private String textoAtual = "";
	private String textoBuffer = "";

	private Memoria() {

	}

	public static Memoria getInstacia() {
		return instacia;
	}

	public void adcionarObservador(MemoriaObservador o) {
		observadores.add(o);
	}

	public String getTextoAtual() {
		return textoAtual.isEmpty() ? "0" : textoAtual;
	}

	public void processarComando(String texto) {
		TipoComando tipoComando = detectarTipoComando(texto);
		
		if(tipoComando == null) {
			return;
		}else if(tipoComando == TipoComando.ZERAR){
			textoAtual = "";
			textoBuffer = "";
			substituir = false;
			ultimaOpreacao = null;
		}else if(tipoComando == TipoComando.NUMERO || tipoComando == TipoComando.VIRGULA) {
			textoAtual = substituir ? texto : textoAtual + texto;
			substituir=false;
		}else {
			substituir = true;
			textoAtual = obterResultadoOperacao();
			
			textoBuffer = textoAtual;
			ultimaOpreacao = tipoComando;
			
		}
		
		
		
		
		observadores.forEach(o -> o.valorAlterado(getTextoAtual()));

	}

	private String obterResultadoOperacao() {
		if(ultimaOpreacao == null) {
			return textoAtual;
		}
		double numeroBuffer =
				Double.parseDouble(textoBuffer.replace(",", "."));
		double numeroAtual =
				Double.parseDouble(textoAtual.replace(",", "."));
		double resultado = 0;
		
		if(ultimaOpreacao == TipoComando.SOMA) {
			resultado = numeroBuffer + numeroAtual;
		}else if (ultimaOpreacao == TipoComando.SUB) {
			resultado = numeroBuffer - numeroAtual;
		}else if (ultimaOpreacao == TipoComando.MULT) {
			resultado = numeroBuffer * numeroAtual;
		}else if (ultimaOpreacao == TipoComando.DIV) {
			resultado = numeroBuffer / numeroAtual;
		}
		String texto = Double.toString(resultado).replace(".", ",");
		boolean inteiro = texto.endsWith(",0");
		return inteiro ? texto.replace(",0", "")
				: texto;
	}

	private TipoComando detectarTipoComando(String texto) {
		if (textoAtual.isEmpty() && texto == "0") {
			return null;
		}

		try {
			Integer.parseInt(texto);
			return TipoComando.NUMERO;
		} catch (NumberFormatException e) {
			// QUANDO NAO FOR NUMERO
			if ("AC".equals(texto)) {
				return TipoComando.ZERAR;
			}else if("/".equals(texto)) {
				return TipoComando.DIV;
			}else if("*".equals(texto)) {
				return TipoComando.MULT;
			}else if("+".equals(texto)) {
				return TipoComando.SOMA;
			}else if("-".equals(texto)) {
				return TipoComando.SUB;
			}else if("=".equals(texto)) {
				return TipoComando.IGUAL;
			}else if(",".equals(texto) && !textoAtual.contains(",")) {
				return TipoComando.VIRGULA;
			}
		}

		return null;
	}

}
package br.com.helper.javaapi.modelos;

public class Serie extends Titulo {
	private int temporadas;
	private boolean ativa;
	private int episodiosPorTemporada;
	private int minutosPorEpisodio;

	public Serie(String nome, int anoDeLancamento) {
		super(nome, anoDeLancamento);
	}

	@Override
	public int getDuracaoEmMinutos() {
		return temporadas * episodiosPorTemporada * minutosPorEpisodio;
	}

	public int getEpisodiosPorTemporada() {
		return episodiosPorTemporada;
	}

	public int getMinutosPorEpisodio() {
		return minutosPorEpisodio;
	}

	public int getTemporadas() {
		return temporadas;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	public void setEpisodiosPorTemporada(int episodiosPorTemporada) {
		this.episodiosPorTemporada = episodiosPorTemporada;
	}

	public void setMinutosPorEpisodio(int minutosPorEpisodio) {
		this.minutosPorEpisodio = minutosPorEpisodio;
	}

	public void setTemporadas(int temporadas) {
		this.temporadas = temporadas;
	}

	@Override
	public String toString() {
		return "Série: " + this.getNome() + "(" + this.getAnoDeLancamento() + ")";
	}
}

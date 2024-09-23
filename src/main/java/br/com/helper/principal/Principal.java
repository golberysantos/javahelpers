package br.com.helper.principal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import br.com.helper.env.ConexaoGibenDB;
import br.com.helper.javaapi.BuscarEnderecoViaCep;
import br.com.helper.javaapi.ExchangeRateAPITela;
import br.com.helper.javaapi.modelos.Episodio;
import br.com.helper.javaapi.modelos.Filme;
import br.com.helper.javaapi.modelos.Serie;
import br.com.helper.javaapi.openaigptapi.ConsultaChatGPTCtrl;
import br.com.javaapi.calculos.CalculadoraDeTempo;
import br.com.javaapi.calculos.FiltroRecomendacao;

public class Principal {

	public static void main(String[] args) throws IOException, InterruptedException {

		ConsultaChatGPTCtrl ccgptctrl = new ConsultaChatGPTCtrl();
		ccgptctrl.consultar();

		System.exit(0);


		ConexaoGibenDB Cgdb = new ConexaoGibenDB();
		try {
			Cgdb.conectar();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("foi");

		System.exit(0);





		Scanner ler = new Scanner(System.in);
		System.out.println("\n");
		System.out.println("╔═════════════════════════╗");
		System.out.println("║ JAVA HELPER. BY GOLBERY ║");
		System.out.println("╚═════════════════════════╝");
		System.out.println("╔═════════════════════════╗");
		System.out.println("║ OPÇÕES:                 ║");
		System.out.println("║ 0. SAIR                 ║");
		System.out.println("║ 1. Exchange Rate API    ║");
		System.out.println("╚═════════════════════════╝");
		System.out.println("");

		String opcao = ler.next();

		System.out.println(opcao);
		switch (opcao) {
		case "0":

			break;
		case "1":
			ExchangeRateAPITela exch = new ExchangeRateAPITela();
			exch.exibir();
			break;

		default:
			break;
		}

		ler.close();
		System.exit(0);



		BuscarEnderecoViaCep Bevc = new BuscarEnderecoViaCep();
		System.out.println(Bevc.exibir("69086051"));

		Filme meuFilme = new Filme("O poderoso chefão", 1970);
		meuFilme.setDuracaoEmMinutos(180);
		System.out.println("Duração do filme: " + meuFilme.getDuracaoEmMinutos());

		meuFilme.exibeFichaTecnica();
		meuFilme.avalia(8);
		meuFilme.avalia(5);
		meuFilme.avalia(10);
		System.out.println("Total de avaliações: " + meuFilme.getTotalDeAvaliacoes());
		System.out.println(meuFilme.pegaMedia());
		// meuFilme.somaDasAvaliacoes = 10;
		// meuFilme.totalDeAvaliacoes = 1;
		// System.out.println(meuFilme.pegaMedia());

		Serie lost = new Serie("Lost", 2000);
		lost.exibeFichaTecnica();
		lost.setTemporadas(10);
		lost.setEpisodiosPorTemporada(10);
		lost.setMinutosPorEpisodio(50);
		System.out.println("Duração para maratonar Lost: " + lost.getDuracaoEmMinutos());

		Filme outroFilme = new Filme("Avatar", 2023);
		outroFilme.setDuracaoEmMinutos(200);

		CalculadoraDeTempo calculadora = new CalculadoraDeTempo();
		calculadora.inclui(meuFilme);
		calculadora.inclui(outroFilme);
		calculadora.inclui(lost);
		System.out.println(calculadora.getTempoTotal());

		FiltroRecomendacao filtro = new FiltroRecomendacao();
		filtro.filtra(meuFilme);

		Episodio episodio = new Episodio();
		episodio.setNumero(1);
		episodio.setSerie(lost);
		episodio.setTotalVisualizacoes(300);
		filtro.filtra(episodio);

		var filmeDoPaulo = new Filme("Dogville", 2003);
		filmeDoPaulo.setDuracaoEmMinutos(200);
		filmeDoPaulo.avalia(10);

		ArrayList<Filme> listaDeFilmes = new ArrayList<>();
		listaDeFilmes.add(filmeDoPaulo);
		listaDeFilmes.add(meuFilme);
		listaDeFilmes.add(outroFilme);
		System.out.println("Tamanho da lista " + listaDeFilmes.size());
		System.out.println("Primeiro filme " + listaDeFilmes.get(0).getNome());
		System.out.println(listaDeFilmes);
		System.out.println("toString do filme " + listaDeFilmes.get(0).toString());

	}
}
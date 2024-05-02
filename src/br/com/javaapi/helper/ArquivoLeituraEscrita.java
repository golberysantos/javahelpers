package br.com.javaapi.helper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * As classes FileReader e FileWriter são usadas para ler e escrever dados em
 * arquivos de texto, sendo que a classe FileReader lê os caracteres de um
 * arquivo de texto, enquanto a classe FileWriter escreve os caracteres.
 */
public class ArquivoLeituraEscrita {
	/**
	 * Exemplo path: "C:\\Users\\user\\meuArquivo.txt"
	 *
	 * @param path
	 * @throws IOException
	 */
	public void lerComFileReader(String path) throws IOException {
		File file = new File(path);
		FileReader reader = new FileReader(file);

		try {
			int data = reader.read();
			while (data != -1) {
				System.out.print((char) data);
				data = reader.read();
			}
			reader.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Exemplo path: "C:\\Users\\user\\meuArquivo.txt"
	 *
	 * @param path
	 * @param texto
	 */
	public void criarComFileWriter(String path, String texto) {
		File file = new File(path);
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			writer.write(texto);
			writer.write("dsfafdsa");
			writer.close();
			System.out.println("Arquivo criado com sucesso!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

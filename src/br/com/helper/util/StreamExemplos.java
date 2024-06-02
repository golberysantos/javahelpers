package br.com.helper.util;

import java.util.Arrays;
import java.util.List;

public class StreamExemplos {
	public static void arrayDeString() {
		List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Golbery", "Patrícia Brasil");
		nomes.stream()
        .sorted()
        .limit(3)
        .filter(n -> n.startsWith("N"))
        .map(n -> n.toUpperCase())
        .forEach(System.out::println);		
	}
}

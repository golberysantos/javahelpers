package br.com.javaapi.helper;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import br.com.javaapi.screenmatch.modelos.Titulo;
import br.com.javaapi.screenmatch.modelos.TituloOmdb;

/**
 * Gson é uma biblioteca Java que pode ser usada para converter objetos Java em
 * sua representação JSON (serializar). Também pode ser usado para converter uma string JSON
 * em um objeto Java equivalente (desserializa). 
 * 
 * Here is an example of how Gson is used for a simple Class:
 * Gson gson = new Gson(); // Or use new GsonBuilder().create();
 * MyType target = new MyType();
 * String json = gson.toJson(target); // serializes target to Json
 * MyType target2 = gson.fromJson(json, MyType.class); // deserializes json into target2
 * 
 * Fonte:
 * https://www.javadoc.io/doc/com.google.code.gson/gson/2.8.5/com/google/gson/Gson.html
 * https://mvnrepository.com/artifact/com.google.code.gson/gson/2.10
 */
public class Gson {
	
	private String json;
	private Object obj = null; 

	public void serializar() {

	}

	/**
	 * Transforma json em um objeto java. 
	 * Retorna um objeto java.
	 * @param Json
	 */
	public Object desserializar() {
		

		try {			
			
			com.google.gson.Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
					.create();			
			obj= gson.fromJson(json, obj.getClass());
		} catch (NumberFormatException e) {
			System.out.println("Aconteceu um erro: ");
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("Algum erro de argumento na busca");
		} catch (Exception e) {
			System.out.println("Aconteceu algo, não sei o que");
		}

		System.out.println("Resposta desserializada "+obj);
		return obj;

	}

	

}

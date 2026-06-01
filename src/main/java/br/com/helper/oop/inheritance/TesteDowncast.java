package br.com.helper.oop.inheritance;

public class TesteDowncast {
	
   public void show() {

	Animal animal = new Cachorro();
   
    System.out.println("--- Acessando método herdado ---");
    animal.respirar();  //  Funciona - Animal tem respirar()
    
    System.out.println("\n--- Tentativa sem downcast (daria erro) ---");
    // animal.latir();  // Descomentar daria erro de compilação
    
    System.out.println("\n--- Downcast seguro com instanceof ---");
    if (animal instanceof Cachorro) {
        Cachorro cachorro = (Cachorro) animal;
        cachorro.latir();  // Agora funciona!
        System.out.println("Downcast realizado com sucesso!");
    }
    
    System.out.println("\n--- Por que a verificação é importante? ---");
    System.out.println("animal instanceof Cachorro? " + (animal instanceof Cachorro));
    // Retorna true, pois o objeto REAL é um Cachorro

   }
}

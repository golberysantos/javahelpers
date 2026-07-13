package br.com.javahelperai.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CalculadoraTest {

    @Test
    void deveSomarDoisNumeros() {

        // Arrange
        Calculadora calculadora = new Calculadora();

        // Act
        int resultado = calculadora.somar(2, 2);

        // Assert
        assertEquals(4, resultado);
    }

}
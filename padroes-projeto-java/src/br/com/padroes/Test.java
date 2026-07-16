package br.com.padroes;

import br.com.padroes.facade.Facade;
import br.com.padroes.singleton.SingletonEager;
import br.com.padroes.singleton.SingletonLazy;
import br.com.padroes.singleton.SingletonLazyHolder;
import br.com.padroes.strategy.Comportamento;
import br.com.padroes.strategy.ComportamentoAgressivo;
import br.com.padroes.strategy.ComportamentoDefensivo;
import br.com.padroes.strategy.ComportamentoNormal;
import br.com.padroes.strategy.Robo;

public class Test {

    public static void main(String[] args) {

        // -------------------------------------------------------
        // SINGLETON
        // -------------------------------------------------------
        System.out.println("=== Singleton Lazy ===");
        SingletonLazy lazy1 = SingletonLazy.getInstancia();
        SingletonLazy lazy2 = SingletonLazy.getInstancia();
        System.out.println("Mesma instância? " + (lazy1 == lazy2));

        System.out.println("\n=== Singleton Eager ===");
        SingletonEager eager1 = SingletonEager.getInstancia();
        SingletonEager eager2 = SingletonEager.getInstancia();
        System.out.println("Mesma instância? " + (eager1 == eager2));

        System.out.println("\n=== Singleton LazyHolder ===");
        SingletonLazyHolder holder1 = SingletonLazyHolder.getInstancia();
        SingletonLazyHolder holder2 = SingletonLazyHolder.getInstancia();
        System.out.println("Mesma instância? " + (holder1 == holder2));

        // -------------------------------------------------------
        // STRATEGY
        // -------------------------------------------------------
        System.out.println("\n=== Strategy — Robô ===");
        Comportamento normal     = new ComportamentoNormal();
        Comportamento defensivo  = new ComportamentoDefensivo();
        Comportamento agressivo  = new ComportamentoAgressivo();

        Robo robo = new Robo();
        robo.setComportamento(normal);
        robo.mover();
        robo.mover();

        robo.setComportamento(defensivo);
        robo.mover();

        robo.setComportamento(agressivo);
        robo.mover();
        robo.mover();
        robo.mover();

        // -------------------------------------------------------
        // FACADE
        // -------------------------------------------------------
        System.out.println("\n=== Facade — Migração de Cliente ===");
        Facade facade = new Facade();
        facade.migrarCliente("Venilton", "01310-100");
    }
}

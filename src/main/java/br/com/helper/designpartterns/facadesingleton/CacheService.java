package br.com.helper.designpartterns.facadesingleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



//===== SERVIÇO DE CACHE SIMULADO =====

public class CacheService {
	private static final Logger logger = LoggerFactory.getLogger(CacheService.class);
	private final Map<String, CacheEntry> cache;
	private final ScheduledExecutorService cleaner;

	public CacheService() {
		this.cache = new ConcurrentHashMap<>();
		// Cleaner thread para remover itens expirados
		this.cleaner = Executors.newSingleThreadScheduledExecutor();
		this.cleaner.scheduleAtFixedRate(this::limparCacheExpirado, 10, 10, TimeUnit.MINUTES);
		logger.info("CacheService inicializado");
	}

	public <T> T obter(String chave) {
		CacheEntry entry = cache.get(chave);
		if (entry != null && !entry.isExpirado()) {
			return (T) entry.getValor();
		}
		return null;
	}

	public void armazenar(String chave, Object valor) {
		cache.put(chave, new CacheEntry(valor, System.currentTimeMillis() + 300000)); // 5 minutos
		logger.debug("Item armazenado em cache: {}", chave);
	}

	private void limparCacheExpirado() {
		long agora = System.currentTimeMillis();
		cache.entrySet().removeIf(entry -> entry.getValue().isExpirado(agora));
		logger.debug("Cache limpo. Tamanho atual: {}", cache.size());
	}

// Classe interna para controle de expiração
	private static class CacheEntry {
		private final Object valor;
		private final long expiracao;

		public CacheEntry(Object valor, long expiracao) {
			this.valor = valor;
			this.expiracao = expiracao;
		}

		public boolean isExpirado() {
			return isExpirado(System.currentTimeMillis());
		}

		public boolean isExpirado(long agora) {
			return agora > expiracao;
		}

		public Object getValor() {
			return valor;
		}
	}
}

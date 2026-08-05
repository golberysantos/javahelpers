# Guia Completo sobre Web Scraping

**Web Scraping** (ou raspagem de dados web) é uma técnica automatizada para **extrair informações de sites** e transformá-las em dados estruturados (como tabelas, planilhas CSV ou bancos de dados).

Em vez de copiar e colar manualmente textos e preços de várias páginas, um script ou robô (conhecido como *scraper* ou *spider*) acessa as páginas web, lê o código-fonte (HTML) e extrai apenas os dados específicos que você precisa.

---

## Como Funciona o Processo

1. **Requisição HTTP (Acessando a página)**
   - O script envia uma solicitação HTTP (GET) para a URL do site desejado, simulando o comportamento de um navegador web. O servidor do site responde enviando o código HTML da página.

2. **Parsing (Análise do HTML)**
   - O robô lê a estrutura HTML da página. Ele utiliza identificadores como **tags HTML**, **classes CSS** ou caminhos **XPath** para localizar exatamente onde os dados de interesse estão inseridos.

3. **Extração dos Dados (Filtrando o conteúdo)**
   - O conteúdo relevante (preços, nomes de produtos, comentários, datas) é isolado e separado das tags de formatação e de elementos desnecessários da página (como menus e anúncios).

4. **Armazenamento Estruturado (Salvando para análise)**
   - Os dados coletados são salvos em um formato pronto para uso, como arquivos `.csv`, `.json`, planilhas do Excel ou diretamente em um banco de dados.

---

## Principais Casos de Uso

| Área | Aplicação Prática |
| :--- | :--- |
| **E-commerce & Vendas** | Monitoramento de preços de concorrentes e histórico de promoções em tempo real. |
| **Inteligência de Mercado** | Coleta de avaliações de clientes e análises de sentimento sobre produtos. |
| **Ciência de Dados & IA** | Criação de grandes conjuntos de dados (*datasets*) para treinar modelos de machine learning. |
| **Imobiliário** | Coleta de anúncios de imóveis em diferentes portais para comparar preços por região. |
| **Finanças** | Agregação de dados do mercado financeiro, relatórios e cotações de ativos. |

---

## Principais Ferramentas e Bibliotecas (Python)

Se você pretende criar scripts de raspagem, a linguagem mais popular é o **Python**:

- **BeautifulSoup / lxml:** Ideais para raspagens simples e páginas estáticas (HTML puro).
- **Requests / HTTPX:** Para fazer requisições HTTP rápidas.
- **Selenium / Playwright:** Usados quando a página precisa renderizar JavaScript (ex: clicar em botões, rolar a tela, fazer login).
- **Scrapy:** Framework robusto para projetos de raspagem em grande escala.

---

## ⚠️ Aspectos Éticos e Legais

O web scraping deve ser feito com responsabilidade para evitar problemas legais e técnicos:

- **Respeite o `robots.txt`:** Arquivo presente nos sites (ex: `site.com/robots.txt`) que indica quais páginas podem ou não ser raspadas por robôs.
- **Evite sobrecarregar o servidor:** Enviar requisições demais em poucos segundos pode derrubar o site alvo ou caracterizar um ataque de negação de serviço (DDoS). Use intervalos (*delays*) entre as requisições.
- **Dados Pessoais (LGPD/GDPR):** Coletar informações pessoais não públicas pode violar leis de privacidade de dados.
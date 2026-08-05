O ciclo de vida de uma requisição no **Spring MVC** (e Spring Boot) descreve o caminho exato que um protocolo HTTP percorre desde o momento em que chega ao servidor até a devolução da resposta ao cliente.

O coração desse processo é o padrão **Front Controller**, implementado no Spring pela classe central `DispatcherServlet`.

---

## O Ciclo de Vida Passo a Passo

1. **Filtros Servlet (Servlet Filters):** Antes de entrar no contexto do Spring MVC.
A requisição HTTP chega ao container Servlet (como Tomcat). Ela passa pela cadeia de **Servlet Filters** (`FilterChain`), como `CorsFilter` ou `Spring Security Filter Chain`. Aqui ocorrem validações globais, como autenticação inicial e verificação de cabeçalhos.


2. **Chegada ao DispatcherServlet:** Entrada no Spring Web MVC.
Se os filtros permitirem a passagem, a requisição é entregue ao `DispatcherServlet` (o Front Controller). Ele orquestra todos os componentes subsequentes.


3. **Mapeamento de Handlers (HandlerMapping):**
O `DispatcherServlet` consulta o `HandlerMapping` para descobrir qual Controller e qual método específico deve processar a requisição (com base na URL e no verbo HTTP, como `@GetMapping`). O `HandlerMapping` retorna um objeto chamado `HandlerExecutionChain`.


4. **Execução de Interceptadores (Pre-Handle):** HandlerInterceptor.
Antes de chamar o Controller, o Spring executa o método `preHandle()` dos **HandlerInterceptors** registrados. Se algum interceptador retornar `false`, a execução é abortada imediatamente.


5. **Adaptador de Handler (HandlerAdapter):**
O `DispatcherServlet` envia o handler para o `HandlerAdapter`. O adaptador é responsável por invocar o método do seu `@RestController` ou `@Controller`, resolvendo parâmetros de entrada (como `@RequestBody`, `@PathVariable`, `@RequestParam`).


6. **Execução do Controller:** Sua regra de negócio.
O método do Controller executa a lógica necessária, geralmente chamando serviços (`@Service`) e repositórios (`@Repository`).


7. **Processamento do Retorno (HttpMessageConverter ou ViewResolver):**
* **Em APIs REST (`@RestController` / `@ResponseBody`):** O `HttpMessageConverter` (como Jackson) serializa o objeto retornado diretamente para JSON/XML na resposta HTTP.
* **Em Aplicações MVC tradicionais:** O Controller retorna o nome de uma View (ModelAndView), e o `ViewResolver` traduz esse nome para um template HTML (como Thymeleaf).


8. **Pós-processamento dos Interceptadores (Post-Handle e After-Completion):**
O método `postHandle()` é chamado após a execução do Controller, e o `afterCompletion()` é executado após a renderização final/envio da resposta, ideal para limpeza de recursos e logs de auditoria.


9. **Resposta HTTP entregue ao Cliente:**
A resposta formatada retorna pela cadeia de Servlet Filters até ser enviada de volta ao navegador ou cliente HTTP.


---

## Componentes Principais

| Componente | Papel no Ciclo de Vida |
| --- | --- |
| **DispatcherServlet** | Orquestrador central que recebe e direciona as requisições. |
| **HandlerMapping** | Associa a URL recebida ao Controller correto. |
| **HandlerAdapter** | Executa o método do Controller e injeta os argumentos necessários. |
| **HttpMessageConverter** | Converte objetos Java em JSON/XML (e vice-versa). |
| **ViewResolver** | Resolve nomes de visões em arquivos HTML/Templates. |

---

## Filtros (Filters) vs Interceptadores (Interceptors)

Uma dúvida clássica no ciclo de vida é a diferença entre **Filters** e **Interceptors**:

* **Servlet Filters:** Operam no nível do Container Servlet (Tomcat/Jetty), **antes** que o Spring MVC assuma o controle. Ideais para segurança, compactação (gzip) e CORS.
* **HandlerInterceptors:** Operam **dentro** do Spring MVC, entre o `DispatcherServlet` e o Controller. Têm acesso ao contexto do Spring e aos metadados do handler (anotações, métodos executados).
O Fluxo Real de Entrega de uma Feature

Com a equipe estruturada, o ciclo de entrega opera com governança técnica clara e sem burocracia desnecessária:

[1. Discovery] ──> [2. Refinamento & Gatilho] ──> [3. Build & CI] ──> [4. QA] ──> [5. Release] ──> [6. Telemetria]
 (PM + UX + TL)        (Squad + Consultas)        (Devs + TL)       (QA)       (DevOps+PM)      (TL + EM + Arq)

1. Descoberta e Validação de Produto (Discovery)

    Quem atua: PM, Product Designer e Tech Lead.

    O que acontece: O PM define o objetivo de negócio e o Designer prototipa a experiência do usuário. O Tech Lead participa pontualmente para garantir que a proposta seja tecnicamente viável e não viole restrições óbvias do sistema.

2. Refinamento Técnico e Checagem de Impacto

    Quem atua: Tech Lead, Engenheiros (Back/Front) + Especialistas acionados sob demanda.

    O que acontece: O squad quebra a feature em tarefas técnicas. Aqui entra a regra do gatilho de complexidade:

        Feature padrão: O Tech Lead e o time desenham a solução usando os padrões já consolidados.

        Feature de alto impacto (novo banco, integração crítica, salto de volumetria): O Tech Lead aciona o Arquiteto de Software, o DBA ou o Cloud Architect para desenhar os contratos e aprovar um ADR (Architecture Decision Record).

        Risco de conformidade/dados sensíveis: O AppSec valida o fluxo de segurança.

3. Desenvolvimento e Integração Contínua (Build)

    Quem atua: Engenheiros de Software (Front-end e Back-end Java) e Tech Lead.

    O que acontece:

        Os desenvolvedores escrevem o código e testes unitários.

        O Tech Lead e os pares realizam os Code Reviews.

        A esteira de CI/CD (mantida pelo DevOps/SRE) roda automaticamente linters, testes de unidade e varreduras estáticas de segurança (AppSec).

4. Garantia da Qualidade (QA)

    Quem atua: Engenheiro de QA (dentro do squad).

    O que acontece: Valida os critérios de aceitação em ambiente de testes (Staging), roda testes automatizados de regressão e testa cenários extremos (edge cases).

5. Lançamento Controlado (Release)

    Quem atua: Squad, PM e DevOps/SRE.

    O que acontece: O deploy é executado de forma automatizada via Feature Flags ou Canary Release (liberação para uma fatia pequena de usuários primeiro).

6. Observabilidade e Governança Pós-Lançamento

    Quem atua: Tech Lead, DevOps/SRE, PM, EM e Arquiteto de Software.

    O que acontece:

        Tech Lead e DevOps: Monitoram latência de API, erros e consumo de infraestrutura no curto prazo.

        PM: Avalia se as métricas de negócio foram atingidas.

        EM: Mede métricas de entrega e saúde do time (throughput, tempo de ciclo, sobrecarga da equipe).

        Arquiteto de Software: Avalia se o acúmulo dessa e de outras entregas gerou acoplamento indesejado ou débitos técnicos que precisam entrar no roadmap de arquitetura corporativa.
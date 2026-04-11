# Principais mudanças

1. **Desmembramento da classe App**: O maior problema de design do projeto era a alta quantidade de responsabilidades da classe main, que precisou ser modularizada ao máximo usando os conceitos de encapsulamento. Agora, a `App` é apenas o ponto de entrada da aplicação.

2. **Criação da classe Menu**: A classe `App` ainda gerenciava o menu e chamadas de console. Toda lógica de interface (o `Scanner` e os `System.out.println`) foi migrada para a classe `Menu`, assumindo a interação I/O.

3. **Inserção do Padrão de Armazenamento**: Agora todas as entidades (Provas, Questoes, Tentativas e Participantes) guardam a lógica de gerar os id's dentro de si e possuem uma classe de lista separada para cada entidade, responsável por gerenciar elas.

4. **Implementação da classe Tabuleiro**: A classe Tabuleiro foi implementada para auxiliar na visualização do tabuleiro de xadrez, recebendo uma string FEN e imprimindo o tabuleiro no console, tirando essa responsabilidade da classe App.

5. **Adição do método calcularNota() na classe Tentativa**: Tendo em vista que a classe Tentativa é a verdadeira detentora das respostas fornecidas na prova, o método calcularNota() foi adicionado à classe Tentativa, responsável por calcular a nota do participante na prova, tirando essa responsabilidade da classe App.

6. **Polimorfismo com a Classe Questao**: A classe `Questao` original foi convertida em uma classe Abstrata genérica, induzindo a criação da nova subclasse `QuestaoFechada` para alocar perguntas com alternativas de "A até E".

---

# Onde cada princípio foi aplicado

## S - Single Responsibility Principle

1. **Cálculo de Notas**: Antigamente a classe `App.java` acessava os dados da `Tentativa` e resolvia o cálculo. Como a `Tentativa` é a verdadeira detentora das repostas fornecidas na prova, foi adicionado o método `calcularNota()` para dentro dela. Ela sabe sua própria nota e o encapsulamento foi preservado.
2. **Impressão do FEN (Xadrez)**: O projeto exige a transformação da notação de texto bruta de xadrez em um tabuleiro visual no console. Para tirar a carga visual de formatação de um jogo da classe main da aplicação, foi criada a classe utilitária `Tabuleiro.java`, extraindo essa responsabilidade visual por completo.
3. **Persistência e Indexação**: Foram removidas as coleções (arrays de armazenamento) e a geração incremental de id's manuais de dentro da classe `App.java` e distribuídas em arquivos de listas separados, cada um possuindo a única responsabilidade de gerenciar as entidades as quais são nomeadas (ex: `ListaProvas`, `ListaParticipantes`).
4. **Interface de Terminal (I/O)**: A manipulação feita pelo `Scanner` e as strings do console que estavam em `App.java` foram delegadas para a classe `Menu.java`.

## O - Open-Closed Principle

- **O Padrão Repository**: A principal vulnerabilidade ao escalonamento de software é manter a classe principal lidando com manipulação de dados concretos. Ao introduzir as chamadas para a interface genérica (`Repositorio`), garante-se que o desenvolvedor possa criar implementações em banco de dados (seja com uma classe `BancoDeDadosProvas` ou conexões a banco de dados externos, por exemplo) no modelo sem precisar alterar o núcleo do código, já que todos usam a porta da mesma interface genérica. O software ficou "aberto" para novos métodos de armazenamento, porém "fechado" no núcleo.
- **Herança de Tipos de Questões**: A classe `Questao` se encontrava rígida, focada apenas em questões de múltipla escolha. Ela se tornou Abstrata possuindo uma função flexível de análise (`isRespostaCorreta(String)`). Desse modo a aplicação permanece fechada no seu núcleo e aberta para a adição de novos tipos de questões, como por exemplo a classe `QuestaoFechada`, deixando o código pronto para futuras implementações. Para esta adaptação a `Resposta` também englobou propriedades unicamente `String` retirando limitações para char primitivos.

## L - Liskov Substitution Principle

- Na declaração do `App.java`, as listas foram convertidas sem danos estruturais ao código-fonte ou a execução. Todas as 4 estruturas listais que foram instanciadas, mesmo possuindo funcionamentos internos diferentes, retornam instâncias compatíveis ao serem chamadas (como `Armazenamento<Questao> questoes = new ListaQuestoes()`, por exemplo). Isso garante que a lógica de negócio tenha o mesmo resultado, independentemente da implementação.

## I - Interface Segregation Principle

- **Interface Enxuta**: A interface `Repositorio<T>` foi elaborada contendo apenas os 3 métodos básicos do CRUD do projeto (`adicionar()`, `listarTodos()`, e `buscarPorId()`). O princípio foi implementado, visto que as classes simples não são obrigadas a implementar métodos complexos que fugissem do seu escopo.

## D - Dependency Inversion Principle

- Antes, a classe `App.java` estava engessada com dependência às coleções de listas diretas para gravar todos os comportamentos e dados da aplicação.
- Com a adição do conceito de repositório de domínio em forma de abstração (`Repositorio<T>`), o fluxo de controle foi invertido. As requisições apontam para algo universal em vez de algo mais específico, dependendo unicamente do que se injeta na parte superior. O mesmo vale para a classe de interação agora, a `Menu` recebe a injeção diretamente da Main por parâmetro sem depender de injeção dos dados da classe controladora.
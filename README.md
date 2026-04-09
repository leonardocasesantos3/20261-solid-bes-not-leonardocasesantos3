# Principais mudanças

1. **Desacoplamento da classe App**: A maior problemática de design do projeto era o sobrepeso da classe main, que precisou ser modularizada ao máximo possível usando os conceitos do encapsulamento base.

2. **Inserção do Padrão de Armazenamento**: Agora todas as identidades (Provas, Questoes, Tentativas e Participantes) guardam as lógicas de gerar os id's dentro de si e possuem forte coesão com sua camada de banco de lista.

3. **Implementação da classe Tabuleiro**: A classe Tabuleiro foi implementada para auxiliar na visualização do tabuleiro de xadrez, recebendo uma string FEN e imprimindo o tabuleiro no console, tirando essa responsabilidade da classe App.

4. **Adição do método calcularNota() na classe Tentativa**: Tendo em vista que a classe Tentativa é a verdadeira detentora das respostas fornecidas na prova, o método calcularNota() foi adicionado à classe Tentativa, responsável por calcular a nota do participante na prova, tirando essa responsabilidade da classe App.

---

# Onde cada princípio foi aplicado

## S - Single Responsibility Principle

1. **Cálculo de Notas**: Antigamente a classe `App.java` acessava os dados da `Tentativa` e resolvia o cálculo. Como a `Tentativa` é a verdadeira detentora das repostas fornecidas na prova, foi adicionado o método `calcularNota()` para dentro dela. Ela sabe sua própria nota e o encapsulamento foi preservado.
2. **Impressão do FEN (Xadrez)**: O projeto exige a transformação da notação de texto bruta de xadrez em um tabuleiro visual no console. Para tirar a carga visual de formatação de um jogo da classe main da aplicação, foi criada a classe utilitária `Tabuleiro.java`, extraindo essa responsabilidade visual por completo.
3. **Persistência e Indexação**: Foram removidas as coleções (arrays de armazenamento) e a geração incremental de id's manuais de dentro da classe `App.java` e distribuídas em arquivos de listas separados, cada um possuindo a única responsabilidade de gerenciar as entidades as quais são nomeadas (ex: `ListaProvas`, `ListaParticipantes`).

## O - Open-Closed Principle

- **O Padrão Repository**: A principal vulnerabilidade ao escalonamento de software é manter a classe principal lidando com manipulação de dados concretos. Ao introduzir as chamadas para a interface genérica (`Armazenamento`), garante-se que o desenvolvedor possa criar implementações em banco de dados (seja com uma classe `BancoDeDadosProvas` ou conexões a banco de dados externos, por exemplo) no modelo sem precisar alterar o núcleo do código, já que todos usam a porta da mesma interface genérica. O software ficou "aberto" para novos métodos de armazenamento, porém "fechado" no núcleo.

## L - Liskov Substitution Principle

- Na declaração do `App.java`, as listas foram convertidas sem danos estruturais ao código-fonte ou a execução. Todas as 4 estruturas listais que foram instanciadas, mesmo possuindo funcionamentos internos diferentes, retornam instâncias compatíveis ao serem chamadas (como `Armazenamento<Questao> questoes = new ListaQuestoes()`, por exemplo). Isso garante que a lógica de negócio tenha o mesmo resultado, independentemente da implementação.

## I - Interface Segregation Principle

- **Interface Enxuta**: A interface `Armazenamento<T>` foi elaborada contendo apenas os 3 métodos básicos do CRUD do projeto (`adicionar()`, `listarTodos()`, e `buscarPorId()`). O princípio foi implementado, visto que as classes simples não são obrigadas a implementar métodos complexos que fugissem do escopo de seu armazenamento.

## D - Dependency Inversion Principle

- Antes, a classe `App.java` estava engessada com dependência às coleções de listas diretas para gravar todos os comportamentos e dados da aplicação.
- Com a adição do conceito de repositório de domínio em forma de abstração (`Armazenamento<T>`), o fluxo de controle foi invertido. Agora, a classe `App` sabe o que ela quer buscar, com base em preenchimentos polimórficos na sua parte superior (ao invés de como era antes, onde o `ArrayList` era o responsável por buscar os dados).
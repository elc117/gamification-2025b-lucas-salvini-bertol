# Code Breaker

---

## Identificação

- Aluno: Lucas Salvini Bertol
- Curso: Sistemas de informação  
- Disciplina: [Paradigmas de Programação](https://github.com/AndreaInfUFSM/elc117-2025b/tree/main)

---

## Proposta 

- Criação de um jogo utilizando o framework [LibGDX](https://libgdx.com/) onde você precisa resolver desafios de lógica e códigos de 
programação de para aprimorar seus conhecimentos na área, e avançar de nível. Cada nível (1-5) tem um novo desafio de código, com opção 
inicial de escolha de dificuldade. 
Dependendo do tempo que o player  finalizar a fase, ele ganhará uma nota (A+, A, B, C, D) e um *score*, 
cada nota valerá uma pontuação específica para esse *score* final. 
    
---

## Processo de desenvolvimento

### _Parte 1_: Entendendo o funcionamento do LIBGDX e criação do projeto

- O primeiro passo foi ler a [documentação oficial WIKI LibGDX](https://libgdx.com/wiki/). Inicialmente, a estrutura de um projeto LibGDX (com os módulos `core`, `lwjgl3`, `html`) não fazia muito sentido para mim. Através do [gerador de projetos](https://libgdx.com/dev/project-generation/), tutoriais e perguntas ao ChatGPT, entendi que o código principal do jogo fica no `core`, enquanto os outros módulos são apenas "lançadores" para cada plataforma específica que vai rodar o projeto.
- O primeiro desafio foi entender o os métodos `create()`, `render()` e `dispose()` do LibGDX. Descobri que `create()` é chamado uma vez para configurar o jogo, `render()` exibe tudo na tela, e `dispose()` libera recursos e evita vazar memória.

### _Parte 2_: Estruturando o jogo com Telas

- No começo, a tendência era colocar toda a lógica na classe principal `MainGame`. Rapidamente, percebi que não iria daa certo. Pesquisando por "Padrões em LibGDX" e analisando os [trabalhos anteriores](www.google.com) disponibilizados pela professora, descobri o padrão `Screen`. Adotar essa abordagem me permitiu isolar os elementos de cada tela (Menu, Jogo, Seleção de Dificuldade), tornando o código mais organizado e tendo um ponto de partida.
- A comunicação entre as telas foi um desafio. Como fazer para o botão "Jogar" no `MenuScreen` abrir a `DifficultSelectScreen`? A solução, que encontrei em exemplos e tutoriais, foi passar a instância principal da classe `Game` para o construtor de cada `Screen`. Isso permitiu que cada tela pudesse chamar `game.setScreen(new OutraScreen(game))` para navegar entre elas.
- Conforme o jogo crescia, precisei compartilhar dados entre as telas (como as perguntas já respondidas). A primeira ideia foi passar múltiplos parâmetros nos construtores, mas isso rapidamente se tornou confuso e insustentável. Pesquisando sobre "gerenciamento de estado em jogos", descobri a ideia de ter uma classe central para isso. Assim, criei o `GameStateManager` para armazenar o estado do jogo, simplificando a lógica e a comunicação entre as telas.

### _Parte 2.5_: A Criação das Telas

- **`MenuScreen`**: Comecei com a tela mais simples para testar os conceitos básicos. A ideia era apenas mostrar uma imagem e responder a um toque. A descoberta foi a simplicidade do `Gdx.input.justTouched()`, que me permitiu criar uma primeira tela interativa sem complexidade, servindo como um "Olá, Mundo!" funcional para o projeto.
- **`UserInputScreen`**: Tentar posicionar botões e textos manualmente, calculando coordenadas `x` e `y` no método `render()`, foi um dos meus erros iniciais. Era trabalhoso e não se adaptava a diferentes resoluções de tela. Pesquisando por uma solução melhor, encontrei o `Scene2D.ui` para "cuidar do layout". Com algumas análises dos projetos, pesquisas e lendo a documentação, descobri a classe `Table`, que organiza tudo automaticamente. Outro problema foi que os botões e textos tinham tamanhos inconsistentes. A solução foi (mais pra frente) criar a classe `Constants` para definir valores padronizados (`BUTTON_WIDTH`, `FONT_SCALE`, etc.), o que tornou a UI muito mais organizada e facil de editar algo.
- **`DifficultSelectScreen`**: Foi bem similar à tela anterior, mas com dois botões (`Fácil` e `Difícil`). A novidade aqui foi implementar uma lógica de contagem regressiva. Após o clique, uma mensagem aparece e um contador é iniciado. Apenas quando o contador chega a zero, o jogo avança para a `QuestionScreen`. Isso foi feito para dar ao jogador um momento para se preparar.
- **`QuestionScreen`**: Foi a tela mais complexa do projeto. Ela combina múltiplos `Labels` para o enunciado e a pergunta, uma lista dinâmica de `TextFields` para as respostas e um botão de verificação. O principal desafio foi lidar com conteúdo que poderia ser maior que a tela. A solução foi usar um `ScrollPane`, que permite que o usuário role o conteúdo verticalmente. A lógica de validação, como mencionado antes, foi movida para a classe `AnswerValidator` para manter esta tela focada apenas na apresentação.
- Um outro problemas com as telas foram as `skins`, pois ficava dando erro de 'not found'. Descobri que a `skin` (que são o "design" dos botões, inputs etc...) depende de um `.json`, que referencia um `.atlas` que mapeia um `.png`. Baixando os 'defaults' desses do repositório oficial do LibGDX e colocando no lugar certo resolveu o problema.
- Seguindo dicas recebidas pela professora por email, fui pesquisar sobre `Viewport`, para poder evitar erros de formatação e bugs em diferentes tamanhos de telas. Acabei usando o `FitViewport`, pois achei o melhor pra o projeto, não esticando a tela quando redimensionado, mantendo sempre a proporção 16:9.

---

## Diagrama de classes: 

---

## Orientações para execução: 

---

## Resultado final: 

---

## Referências:

- [Slides da disciplina (Paradigmas de programação | ELC117 - 2025b)](https://github.com/AndreaInfUFSM/elc117-2025b/tree/main)  
- [Wiki LibGDX, contendo tudo que foi baixado e estudado para o projeto](https://libgdx.com/wiki/)
- [Software de criação dos diagramas de classse (Astah)](https://astah.net/downloads/)  
- [IA que gerou os assets do jogo (gemini)](https://gemini.google.com/app)

---

## I.A. / Prompts: 

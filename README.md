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

### _Parte 2.1_: A Criação das Telas

A criação das telas do jogo envolveu diversos desafios e aprendizados. Inicialmente, na `MenuScreen`, utilizei `Gdx.input.justTouched()` para responder a toques simples, mas evoluí a implementação para incluir dois botões ("JOGAR" e "RANK") na parte inferior usando `Scene2D.ui` com `Stage`, `Table` e `TextButton`, tornando a interface mais profissional e preparando o terreno para funcionalidades futuras como o sistema de ranking. Um erro comum foi tentar posicionar elementos manualmente calculando coordenadas `x` e `y`, o que era trabalhoso e não se adaptava a diferentes resoluções. A solução veio com o uso da classe `Table` do `Scene2D.ui`, que organiza layouts automaticamente, e posteriormente com a criação da classe `Constants` para padronizar valores como `BUTTON_WIDTH` e `FONT_SCALE`, facilitando ajustes e mantendo consistência visual. Na `DifficultSelectScreen`, implementei uma lógica de contagem regressiva que dá ao jogador um momento para se preparar antes de avançar para a `QuestionScreen`, que acabou sendo a tela mais complexa por combinar múltiplos `Labels`, `TextFields` dinâmicos e um `ScrollPane` para lidar com conteúdo que excede o tamanho da tela. Outro desafio foi resolver erros relacionados às `skins`, descobrindo que elas dependem de uma estrutura de arquivos (`.json`, `.atlas` e `.png`) que precisavam estar corretamente posicionados. Por fim, seguindo dicas da professora, adotei o `FitViewport` para evitar problemas de formatação em diferentes resoluções, mantendo a proporção 16:9 sem distorcer a tela ao redimensionar.

### _Parte 2.2_: A Criação da lógica das telas

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

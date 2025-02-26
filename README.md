# 🎰 Jogo da Roleta (Roulette Game)

## 📌 Introdução
Este projeto consiste na implementação de um **Jogo da Roleta**, desenvolvido no contexto da disciplina de **Laboratório de Informática e Computadores**. O jogo permite que os jogadores realizem apostas num teclado, utilizando créditos adquiridos através da introdução de moedas num moedeiro virtual. A arquitetura do sistema combina hardware e software para simular uma máquina de roleta funcional.

## ⚙️ Arquitetura do Sistema
O sistema é composto pelos seguintes módulos principais:

1. **Keyboard Reader** – Lê as teclas pressionadas pelo jogador.
2. **Coin Acceptor** – Simula a introdução de moedas e a atribuição de créditos.
3. **Serial LCD Controller (SLCDC)** – Controla a exibição de informações no LCD.
4. **Serial Roulette Controller (SRC)** – Controla a exibição do número sorteado na roleta.
5. **Control** – Implementado em software (Kotlin), gerencia toda a lógica do jogo.

A comunicação entre os módulos ocorre através de protocolos série, garantindo um funcionamento coordenado do jogo.

## 🛠️ Instalação
### Requisitos
- Kotlin instalado
- Ambiente de desenvolvimento compatível (IntelliJ IDEA, VS Code com plugin Kotlin, etc.)
- Hardware de simulação (opcional, dependendo da implementação)

### Como compilar e executar
#### 🖥️ Windows
```sh
# Clonar o repositório
$ git clone https://github.com/seu-repositorio/jogo-roleta.git
$ cd jogo-roleta

# Compilar o projeto
$ kotlinc src -include-runtime -d jogo-roleta.jar

# Executar o jogo
$ java -jar jogo-roleta.jar
```

#### 🐧 Linux
```sh
# Clonar o repositório
git clone https://github.com/seu-repositorio/jogo-roleta.git
cd jogo-roleta

# Compilar o projeto
kotlinc src -include-runtime -d jogo-roleta.jar

# Executar o jogo
java -jar jogo-roleta.jar
```

## 🎮 Como Jogar
1. **Iniciar o jogo** pressionando a tecla `*`, desde que haja créditos disponíveis.
2. **Realizar apostas** pressionando teclas numéricas (0-9) ou alfabéticas (A-D).
3. Cada aposta consome 1 crédito do saldo do jogador.
4. **Finalizar as apostas** pressionando `#`, iniciando assim o sorteio.
5. **A roleta gira e sorteia um número/letra**, e os ganhos são atualizados no saldo do jogador.
6. **Modo Manutenção**:
   - `A`: Consultar contadores de moedas e jogos.
   - `C`: Consultar a lista de números sorteados.
   - `D`: Desligar o sistema.

## 📝 Licença
Este projeto é de uso educacional e não possui uma licença específica. Caso pretenda utilizá-lo ou modificá-lo, cite a fonte original.

---
🚀 Desenvolvido para o **Laboratório de Informática e Computadores - 2024/2025** 
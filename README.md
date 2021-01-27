# GameQuizDB
Criei esse projeto utilizando a IDE Eclipse, Banco de Dados MariaDB com o software HeidiSQL. o .jar mysql-connector-java-5.1.48 JAVA SE 1.8 Após o Download coloque o projeto na sua Workspace Acesse o Eclipse, clique em FILE > Import > General > Existing Projects into Workspace > Finish

Para inicializar o Projeto basta criar um Banco de Dados e modificar o nome dentro da "Classe BancoDB" Um detalhe importante nessa classe está na linha 18, nessa parte:

("jdbc:mysql://localhost:3306/quiz_bd", "root", "root"); como o software do HeidiSQL

exige uma senha, tive que modificar essa parte onde geralmente a senha fica em branco, e colocar a senha usada, caso o software de vocês utilizem senha em branco, apaguem essa parte do "root", e mude o nome do Banco de Dados o resultado seria esse:

("jdbc:mysql://localhost:3306/NomeDoBancoDeDadosAqui", "root", "");

após esses procedimentos basta fazer um Run As na aplicação, as tabelas serão criadas automaticamente.

Existe a possibilidade de adicionar perguntas e respostas para o jogo, ao inserir seu nome o usuario é levado para a tela principal onde aparecera perguntas a serem respondidas, as respostas são lançacas com o acionamento da tecla ENTER. se o campo estiver em Branco a tecla ENTER ignora o comando. O uso de letrais maiusculas e minusculas não importam.

Colocando o nome como (admin), sem os parenteses, o jogador entrara em um painel para cadastro de novas perguntas, após cadastralas basta finalizar e iniciar o jogo com um novo nome. Eu coloquei as perguntas iniciais de maneira fixa, assim sempre é possivel testar o jogo sem a necessidade de cadastrar uma pergunta nova.


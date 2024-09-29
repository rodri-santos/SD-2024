Projeto SD
Desenvolvido por:

Rodrigo Santos - 2021236556

Mafalda Duarte - 2021236492


######################### Instalações #########################
* Começamos por instalar o Jsoup (https://jsoup.org/packages/jsoup-1.17.2.jar), abrindo o link começa a instalação;
* De seguida, instalar o Gson (https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.8/gson-2.8.8.jar)**, abrindo o o link começa a instalação;
* Descarregar o maven-wrapper (https://www.dropbox.com/scl/fo/ax7ju2nlnj35fuhojd0cj/ADSTpsY6KOltYnLfDcf9Fa4?rlkey=gj0vgzmarm11x89h99au7j0gp&st=v27ev064&dl=0), dar unzip, colocar o mvn na diretoria da webapp-demo (onde está este readme) e alterar o nome da pasta para .mvn;

######################## Como correr ########################
* 1º Passo:
    * Abrir um Command Prompt com Windows + R, escrever cmd e dar Enter;
    * Dar cd para a diretoria onde está o backend (Diretoria do projeto\webapp-demo\src\main\java\com\example\backend);
    * Executar o comando javac -cp "Copiar o JAR do Jsoup como caminho;Copiar o JAR do Gson como caminho" *.java (ter em atenção que quando se copia o caminho fica com as 2 aspas, tem que se eliminar as aspas antes e depois do ;);
* 2º Passo:
    * Abrir um Command Prompt;
    * Dar cd para a diretoria java (Diretoria do projeto\webapp-demo\src\main\java);
    * Executar o comando start rmiregistry 7000 ;
    * Executar o comanado java -cp .;"Copiar o JAR do Jsoup como caminho;Copiar o JAR do Gson como caminho;Diretoria do projeto\webapp-demo\src\main\java" com.example.backend.RMISearchModule_proj ;
    * Deverá aparecer a mensagem: RMI Server Ready.;
* 3º Passo:
    * Abrir um Command Prompt;
    * Dar cd para a diretoria webapp-demo (Diretoria do projeto\webapp-demo);
    * Executar o comando mvnw spring-boot:run
* 4º Passo:
    * Abrir o browser;
    * Abrir o link (http://localhost:8080) caso esteja a utilizar localhost;
    * Caso queira utilizar o seu IP, http://Substituir Ip:8080
    * Aproveitar as funcionalidades, SD rules!
* 5º Passo (opcional):
    * Caso queira experimentar com amigos, há algumas alterações a fazer no código, sendo estas:
        * No computador onde vai estar o server (terá que efetuar todos os 4 passos), tem que mudar todas os sítios onde está localhost para o seu IP (exemplo 123.456.7.89) em RMISearchModule_proj.java, GreetingController.java, ThymeleafServlet.java
        * No computador que só vai aceder, fazer as mesmas alterações de localhost para o mesmo IP (neste caso 123.456.7.89) do computador onde está a correr o server e executar todos os passos menos o 2º Passo;

Faltou implementar API do ChatGPT e ver todos os urls indexados a um url na meta 2, na meta 1 está feito o código para tal.


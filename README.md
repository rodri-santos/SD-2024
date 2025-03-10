# Projeto SD
### **Desenvolvido por:**

Rodrigo Santos - _2021236556_

[Mafalda Duarte](https://github.com/mafs27) - _2021236492_


~~#########################~~ **Instalações** ~~########################~~
* Começamos por instalar o **[Jsoup](https://jsoup.org/packages/jsoup-1.17.2.jar)**, clicando no link começa a instalação;
* De seguida, instalar o **[Gson](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.8/gson-2.8.8.jar)**, clicando no link começa a instalação;
* Descarregar o **[maven-wrapper](https://www.dropbox.com/scl/fo/ax7ju2nlnj35fuhojd0cj/ADSTpsY6KOltYnLfDcf9Fa4?rlkey=gj0vgzmarm11x89h99au7j0gp&st=v27ev064&dl=0)**, dar unzip, colocar o mvn na diretoria da webapp-demo (onde está este readme) e alterar o nome da pasta para .mvn;

~~########################~~ **Como correr** ~~########################~~
* 1º Passo:
    * Abrir um Command Prompt com _Windows + R_, escrever _cmd_ e dar _Enter_;
    * Dar _cd_ para a diretoria onde está o **backend** (_Diretoria do projeto_\webapp-demo\src\main\java\com\example\backend);
    * Executar o comando **_javac -cp "Copiar o JAR do Jsoup como caminho;Copiar o JAR do Gson como caminho" *.java_** (ter em atenção que quando se copia o caminho fica com as 2 aspas, tem que se eliminar as aspas antes e depois do ;);
* 2º Passo:
    * Abrir um Command Prompt;
    * Dar _cd_ para a diretoria **java** (_Diretoria do projeto_\webapp-demo\src\main\java);
    * Executar o comando **_start rmiregistry 7000_** ;
    * Executar o comanado **_java -cp .;"Copiar o JAR do Jsoup como caminho;Copiar o JAR do Gson como caminho;_Diretoria do projeto_\webapp-demo\src\main\java" com.example.backend.RMISearchModule_proj_** ;
    * Deverá aparecer a mensagem: **RMI Server Ready.**;
* 3º Passo:
    * Abrir um Command Prompt;
    * Dar _cd_ para a diretoria **webapp-demo** (_Diretoria do projeto_\webapp-demo);
    * Executar o comando **_mvnw spring-boot:run_**
* 4º Passo:
    * Abrir o browser;
    * Clicar **[aqui](http://localhost:8080)** caso esteja a utilizar localhost;
    * Caso queira utilizar o seu IP, http://_Substituir Ip_:8080
    * Aproveitar as funcionalidades, SD rules!
* 5º Passo (opcional):
    * Caso queira experimentar com amigos, há algumas alterações a fazer no código, sendo estas:
        * No computador onde vai estar o server (terá que efetuar todos os 4 passos), tem que mudar todas os sítios onde está _localhost_ para o seu _IP (exemplo 123.456.7.89)_ em  **_RMISearchModule_proj.java_**, **_GreetingController.java_**, **_ThymeleafServlet.java_**
        * No computador que só vai aceder, fazer as mesmas alterações de _localhost_ para o mesmo _IP_ (neste caso _123.456.7.89_) do computador onde está a correr o server e executar todos os passos menos o **2º Passo**;

> Faltou implementar API do ChatGPT e ver todos os urls indexados a um url na meta 2, na meta 1 está feito o código para tal, pelo que só faltou a sua implementação no frontend.

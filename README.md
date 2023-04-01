# -> GerenciadorTarefas <-

 <p><b>você pode me encontrar em:</b></p>
  <a href="https://www.linkedin.com/in/brunno-danyel-739a90231/">
  <img src="https://img.shields.io/badge/linkedin-%230077B5.svg?style=for-the-badge&logo=linkedin&logoColor=white">
  </a>

# Funcionalidade Usuário:

1. Cadastro de usuário informando os seguintes atributos: nome, login e senha.
2.  Após a validação de todos os dados informados anteriormente, o sistema vai retornar as informações do Usuário (id, login, nome e senha), a senha ser apresentada de forma criptografada tal como também para o banco de dados.
3.  Para acessar o sistema e seus endpoints o usuário em questão vai precisar fazer a autenticação informando o login e a senha.
4.  Após a autenticação concluída, o sistema vai retornar um token de acesso onde será usado nos endpoints para a permissão dos serviços.

# Funcionalidade Tarefa:

1. Criar tarefa informando o seguintes atributos: titulo, descricao, idResponsavel, dataPrevistaConclusao(dd-MM-yyyy) e prioridade.
2. Após a validação de todos os dados informados anteriormente, o sistema registra a tarefa com no Banco de dados e realiza o envio de email para o responsável que foi registrado na tarefa

* Buscar tarefa por id: usuário informa o ID da tarefa em questão.
* Buscar todas as tarefas registradas.
* Buscar tarefa por Descrição: usuário informa a descrição da tarefa para a busca.
* Buscar tarefa por titulo: usuário informa o titulo da tarefa para a busca.
* Buscar tarefa por status: usuário informa o status da tarefa (EM_ANDAMENTO, CONCLUIDA, ATRASADA).
* Buscar tarefas de um responsável específico: usuário informa o respectivo número do responsável em questão.

3. Para remover uma tarefa, o usuário deve informar o ID da tarefa.
4. Para atualizar uma tarefa, o usuário deve informar o ID da tarefa que deseja realizar a atualização e informar os atributos para serem atualizados:  titulo, descricao, idResponsavel, prazoParaConclusaoEmDias e prioridade.
5. Para concluir a tarefa o usuário informar o ID da tarefa que deseja concluir, sendo assim atualizando o status da tarefa para CONCLUIDA, o método já realiza o envio de email notificando ao usuário que a tarefa foi concluida.

# Funcionalidade E-mail:

1. Envio de e-mail unitário, envio que pede que o usuário informe o ID do usuário que deseja enviar a tarefa e o ID da tarefa que deseja enviar.

# Outras funcionalidades:
  
  * Nessa aplicação é existente um Agendamento de tarefa, onde ele é responsável por atualizar tarefas que já passaram da sua data prevista para a conclusão, atualizando assim seus status para ATRASADA
  * Foi implementado também um outro Agendamento de tarefa para a realização de envio de e-mail para aquelas tarefas que estão atrasadas

OBS: Ambos agendamentos podem ser programados da maneira que o usuário desejar.

# Fluxo de funcionalidade:
![Gerenciador-de-Tarefa-fluxo](https://github.com/Brunno-Danyel/Projeto-Gerenciador-de-tarefas/blob/main/Documentos/Gerenciador-de-Tarefa-fluxo.png)



# OBSERVAÇÕES: 
1. Para o envio de e-mail precisei configurar uma conta do gmail e fazer algumas autorizações de segurança.
2. O atributo prazoParaConclusãoEmDias após informado ele realiza um retorno de data, onde esse retorno fica armazenado no atributo de dataPrevistaParaConclusao e antes de ser armazenado é realizado uma condição para que nenhuma data cai em dias não úteis.



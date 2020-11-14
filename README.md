# projeto-back-end-sistema-de-gestao-prefeitura

Etapas:
1° Criar Entity's dos tres objetos: Concluído!
2° Criar Crud's dos tres objetos: Concluído!
3° Implementar Regras de negócio dos tres objetos: Concluído!
4° Testes unitários: Concluído!

Processo:
Na etapa 1 eu optei por criar as clases com os atributos NOT NULL (garantir a uniformidade), 
nada de muito novo durante a modelagem!

Na etapa 2 não houve grandes surpresas optei pelo padrão dos endPoints! 

Etapa 3:

SECRETARIA:
Método de Busca: verificá a não existência, se não existir retorno Optional.empty(),
caso contrario retorno objeto;

Método de Listagem: Optei por retornar uma lista, estando vazia ou não;

Método Create: verifico a existencia da secretaria pela área. 
Após, crio um objeto das informações recebidas e salvo o objeto;

Método Update: verifico a existencia da secretaria e se foi alterada a área, 
após, dou um set nas informações alteradas e salvo o objeto!

Metod Remove: verifico a não existencia da secretaria, se existir eu a apago!

'~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~'

FUNCIONARIO:
Método de Busca: verificá a não existência, se não existir retorno Optional.empty(),
caso contrario retorno objeto;

Método de Listagem: Optei por retornar uma lista, estando vazia ou não;

Método Create: verifico a existência do funcionario pelo CPF(UNIQUE), e da secretaria pelo id.
Após instâncio um objeto secretaria recebido pelo idSecretaria, e verifico o salario do funcionaio
em relação ao orçamento, e por fim salvo a instância do objeto, criada!

Método Update: verifico a existência do funcionario, e da secretaria, pelos respectivos id's.
Instancio um objeto a partir do id passado como parametro, para comparar as informações no banco de dados, 
com as informações alteradas!
Verifico o salario se diminuio, se sim retorno erro.
Criei uma abstração para comparar quando for alterado a secretaria do funcionario,
duas instancias de Secretaria uma vinda pelo funcionario ainda não alterado, outra vindo das informações do DTO.
A partir dai verifico se a nova secretariapode suportar o novo funcionario.
Utilizo as instancias das secretarias, a antiga e a nova (em caso de alteração), 
para setar o valor do orçamento de acordo com a transferencia dpo funcionario!
Por fim crio uma instância de Funcionario e, após popular ela, eu a salvo!

Método Remove: Verifico a não existencia do funcionario, se existir instâncio ele 
numa instância, e a partir dele instÂncio sua secretaria.
Com essas informações retorno o valor do orçamento ao normal, e deleto o funcionario.

'~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~'

PROJETO:
Método de Busca: verificá a não existência, se não existir retorno Optional.empty(),
caso contrario retorno objeto;

Método de Listagem: Optei por retornar uma lista, estando vazia ou não;

Método Create: Verifico se a secretaria existe, se sim verifico se o orçamento é suficiente,
se sim crio uma instância de Projeto, a populo, e salvo setando dataFinal como nulo e concluído como false!

Método Update: verifico a existencia do projeto, após, crio uma instância, altero as informações e a salvo.!

Método Finaliza Projeto: Um método put que altera a data final do projeto de nulo pra data passada pelo usúario,
verifico se a data final é maior que a data final com uma instancia, altero as informações e salvo!

'~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~''~'

Etapa 4°:
Testes unitários:
Foram Criado Três testes, eu optei por utilizar o teste de cadastro de cada classe Service!
(Se for testes a mais do que os três, é por que sobrou algum tempo).


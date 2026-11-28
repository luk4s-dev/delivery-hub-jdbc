# 🛵 DeliveryHUB — Sistema de Gerenciamento de Delivery

Sistema de gerenciamento de pedidos, produtos, pagamentos e entregas desenvolvido em **Java (JDBC)** integrado a um banco de dados relacional **MySQL**. Projeto acadêmico desenvolvido para a disciplina de Banco de Dados do curso de Engenharia de Software (UCSAL).

---

## 📋 Sobre o Projeto

O **DeliveryHUB** centraliza o fluxo operacional de estabelecimentos que trabalham com entregas, substituindo controles manuais por uma aplicação integrada. O sistema gerencia o ciclo completo de um pedido: cadastro de clientes e múltiplos endereços, seleção de itens com preservação de preço histórico, atribuição de entregadores disponíveis, registro de pagamento único por pedido e fechamento automatizado via rotinas no banco de dados.

---

## 🗄️ Modelagem do Banco de Dados

O banco relacional (`deliveryhub`) é composto por **7 tabelas** normalizadas:

* **`cliente`**: Dados cadastrais dos clientes.
* **`endereco`**: Múltiplos endereços por cliente (`1:N`), com indicação de endereço principal.
* **`produto`**: Catálogo de itens disponíveis para venda.
* **`entregador`**: Cadastro de entregadores e controle de disponibilidade (`DISPONIVEL` / `INDISPONIVEL`).
* **`pedido`**: Registro central da compra, vinculando cliente, endereço de entrega e entregador (anulável até a atribuição).
* **`produto_pedido`**: Tabela associativa (`N:N`) com chave primária composta (`id_pedido`, `id_produto`), armazenando quantidade e `preco_unitario` praticado no momento da compra.
* **`pagamento`**: Registro financeiro vinculado ao pedido (`1:1` via restrição `UNIQUE` em `id_pedido`).

### Recursos SQL Implementados
* **Consultas Analíticas:** Uso de `INNER JOIN`, `LEFT JOIN`, `GROUP BY`, `HAVING` e `ORDER BY` para relatórios de itens por pedido e produtos mais vendidos.
* **View (`vw_pedidos_detalhados`):** Consolida dados de pedidos, clientes, endereços, entregadores e status de pagamento em uma única consulta.
* **Function (`calcular_total_pedido`):** Calcula o valor total do pedido somando `quantidade * preco_unitario` na tabela associativa.
* **Procedure (`finalizar_pedido`):** Valida as regras de negócio (existência de itens e pagamento registrado), atualiza o valor total e o status para `FINALIZADO` e libera o entregador.

---

## 🏗️ Arquitetura do Projeto Java

O projeto segue o padrão de **Arquitetura em Camadas** utilizando Java SE puro e JDBC nativo (`java.sql`):

```text
src/main/java/br/ucsal/deliveryhub/
├── Main.java                  # Ponto de entrada da aplicação
├── database/
│   └── ConnectionFactory.java # Fábrica de conexões JDBC com MySQL
├── model/                     # Entidades POJO (Cliente, Endereco, Produto, Entregador, Pedido, ProdutoPedido, Pagamento)
├── dao/                       # Operações CRUD com PreparedStatement, ResultSet e CallableStatement
├── service/
│   └── DeliveryService.java   # Validação das regras de negócio
└── view/
    └── MenuView.java          # Interface de interação via console

⚙️ Regras de Negócio Garantidas
Todo pedido deve conter ao menos 1 item com quantidade maior que zero.

O endereço selecionado no pedido deve pertencer ao cliente que realizou a compra.

O preço unitário registrado no item preserva o valor do produto no instante da compra.

Entregadores com status INDISPONIVEL não podem ser associados a novos pedidos.

Um pedido permite apenas um registro de pagamento e só pode ser finalizado após o pagamento ser registrado.

🚀 Como Executar
Pré-requisitos
Java JDK 17+

Maven

MySQL Server 8.0+ (e MySQL Workbench)

Passo a passo
Clone o repositório:

Bash
git clone [https://github.com/seu-usuario/deliveryhub.git](https://github.com/seu-usuario/deliveryhub.git)
cd deliveryhub
Configure o Banco de Dados:

Abra o MySQL Workbench e execute os scripts SQL da pasta /sql na seguinte ordem:

01_ddl_criacao_tabelas.sql

02_views_functions_procedures.sql

03_dml_dados_teste.sql (opcional, para popular o banco)

Configure a Conexão JDBC:

No arquivo src/main/java/br/ucsal/deliveryhub/database/ConnectionFactory.java, atualize USER e PASSWORD com as credenciais do seu MySQL.

Execute a aplicação:

Compile as dependências Maven e execute a classe br.ucsal.deliveryhub.Main.

👥 Autores
Lucas Guimarães Santana

Bruno Silva Barreto de Souza

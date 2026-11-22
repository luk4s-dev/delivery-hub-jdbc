package br.ucsal.deliveryhub.view;

import br.ucsal.deliveryhub.dao.*;
import br.ucsal.deliveryhub.model.*;
import br.ucsal.deliveryhub.service.DeliveryService;

import java.math.BigDecimal;
import java.util.Scanner;

public class MenuView {

    private final Scanner scanner = new Scanner(System.in);
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final EnderecoDAO enderecoDAO = new EnderecoDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final EntregadorDAO entregadorDAO = new EntregadorDAO();
    private final RelatorioDAO relatorioDAO = new RelatorioDAO();
    private final DeliveryService service = new DeliveryService();

    public void exibirMenu() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n========= DELIVERYHUB =========");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Cadastrar Endereço para Cliente");
            System.out.println("3. Cadastrar Produto");
            System.out.println("4. Cadastrar Entregador");
            System.out.println("5. Criar Novo Pedido");
            System.out.println("6. Adicionar Item ao Pedido");
            System.out.println("7. Atribuir Entregador ao Pedido");
            System.out.println("8. Registrar Pagamento do Pedido");
            System.out.println("9. Finalizar Pedido (Procedure + Function)");
            System.out.println("10. Listar Pedidos Detalhados (View)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
                processarOpcao(opcao);
            } catch (Exception e) {
                System.out.println("Aviso/Erro: " + e.getMessage());
            }
        }
    }

    private void processarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> {
                System.out.print("Nome: ");
                String nome = scanner.nextLine();
                System.out.print("Telefone: ");
                String tel = scanner.nextLine();
                System.out.print("Email: ");
                String email = scanner.nextLine();
                Cliente c = new Cliente(nome, tel, email);
                clienteDAO.inserir(c);
                System.out.println("Cliente cadastrado com ID: " + c.getIdCliente());
            }
            case 2 -> {
                Endereco e = new Endereco();
                System.out.print("ID do Cliente: ");
                e.setIdCliente(Integer.parseInt(scanner.nextLine()));
                System.out.print("CEP: ");
                e.setCep(scanner.nextLine());
                System.out.print("Logradouro: ");
                e.setLogradouro(scanner.nextLine());
                System.out.print("Número: ");
                e.setNumero(scanner.nextLine());
                System.out.print("Complemento: ");
                e.setComplemento(scanner.nextLine());
                System.out.print("Bairro: ");
                e.setBairro(scanner.nextLine());
                System.out.print("Cidade: ");
                e.setCidade(scanner.nextLine());
                System.out.print("Estado (UF): ");
                e.setEstado(scanner.nextLine());
                System.out.print("Referência: ");
                e.setReferencia(scanner.nextLine());
                System.out.print("Tipo (Casa/Trabalho): ");
                e.setTipo(scanner.nextLine());
                e.setPrincipal(true);
                enderecoDAO.inserir(e);
                System.out.println("Endereço cadastrado com ID: " + e.getIdEndereco());
            }
            case 3 -> {
                System.out.print("Nome do Produto: ");
                String nome = scanner.nextLine();
                System.out.print("Descrição: ");
                String desc = scanner.nextLine();
                System.out.print("Preço (ex: 29.90): ");
                BigDecimal preco = new BigDecimal(scanner.nextLine());
                System.out.print("Categoria: ");
                String cat = scanner.nextLine();
                Produto p = new Produto(nome, desc, preco, cat);
                produtoDAO.inserir(p);
                System.out.println("Produto cadastrado com ID: " + p.getIdProduto());
            }
            case 4 -> {
                System.out.print("Nome do Entregador: ");
                String nome = scanner.nextLine();
                System.out.print("Telefone: ");
                String tel = scanner.nextLine();
                System.out.print("Veículo (Moto/Bike): ");
                String veiculo = scanner.nextLine();
                Entregador ent = new Entregador(nome, tel, veiculo, "DISPONIVEL");
                entregadorDAO.inserir(ent);
                System.out.println("Entregador cadastrado com ID: " + ent.getIdEntregador());
            }
            case 5 -> {
                System.out.print("ID do Cliente: ");
                int idCliente = Integer.parseInt(scanner.nextLine());
                System.out.print("ID do Endereço de entrega: ");
                int idEndereco = Integer.parseInt(scanner.nextLine());
                Pedido pedido = service.criarPedido(idCliente, idEndereco);
                System.out.println("Pedido criado com sucesso! ID: " + pedido.getIdPedido());
            }
            case 6 -> {
                System.out.print("ID do Pedido: ");
                int idPedido = Integer.parseInt(scanner.nextLine());
                System.out.print("ID do Produto: ");
                int idProduto = Integer.parseInt(scanner.nextLine());
                System.out.print("Quantidade: ");
                int qtd = Integer.parseInt(scanner.nextLine());
                service.adicionarItemAoPedido(idPedido, idProduto, qtd);
                System.out.println("Item adicionado ao pedido!");
            }
            case 7 -> {
                System.out.print("ID do Pedido: ");
                int idPedido = Integer.parseInt(scanner.nextLine());
                System.out.print("ID do Entregador: ");
                int idEntregador = Integer.parseInt(scanner.nextLine());
                service.atribuirEntregador(idPedido, idEntregador);
                System.out.println("Entregador atribuído ao pedido!");
            }
            case 8 -> {
                System.out.print("ID do Pedido: ");
                int idPedido = Integer.parseInt(scanner.nextLine());
                BigDecimal totalCalculado = relatorioDAO.calcularTotalPedidoFunction(idPedido);
                System.out.println("Total calculado pela Function: R$ " + totalCalculado);
                System.out.print("Forma de Pagamento (PIX/CARTAO/DINHEIRO): ");
                String forma = scanner.nextLine();
                service.registrarPagamento(idPedido, forma, totalCalculado);
                System.out.println("Pagamento registrado com sucesso!");
            }
            case 9 -> {
                System.out.print("ID do Pedido para finalizar: ");
                int idPedido = Integer.parseInt(scanner.nextLine());
                service.validarAntesDeFinalizar(idPedido);
                relatorioDAO.finalizarPedidoProcedure(idPedido);
                System.out.println("Pedido finalizado com sucesso via Procedure!");
            }
            case 10 -> relatorioDAO.listarPedidosDetalhadosView();
            case 0 -> System.out.println("Encerrando DeliveryHUB...");
            default -> System.out.println("Opção inválida.");
        }
    }
}
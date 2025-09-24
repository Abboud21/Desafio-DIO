package br.com.dio;

import br.com.dio.expcetion.AccountNotFoundException;
import br.com.dio.expcetion.NoFundsEnoughException;
import br.com.dio.expcetion.WalletNotFoundException;
import br.com.dio.model.AccountWallet;
import br.com.dio.model.Investment;
import br.com.dio.repository.AccountRepository;
import br.com.dio.repository.InvestmentRepository;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.time.temporal.ChronoUnit.SECONDS;


public class Main {

    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Ola bem vindo ao DIO BANK");

        while (true){
            System.out.println("Selecione a operação desejada: ");
            System.out.println("1 - Criar uma conta");
            System.out.println("2 - Criar um investimento");
            System.out.println("3 - Criar uma carteira de investimento");
            System.out.println("4 - Depositar na conta");
            System.out.println("5 - Sacar da conta");
            System.out.println("6 - Transferencia entre contas");
            System.out.println("7 - Investir");
            System.out.println("8 - Sacar investmento");
            System.out.println("9 - Listar contas");
            System.out.println("10 - Listar investimentos");
            System.out.println("11 - Listar carteiras de investimentos");
            System.out.println("12 - Atualizar investimentos");
            System.out.println("13 - Historico de conta");
            System.out.println("14 - Sair");

            var option = scanner.nextInt();

            switch (option){
                case 1 -> creatAccont();
                case 2 -> creatInvestment();
                case 3 -> creatWalletInvestment();
                case 4 -> deposit();
                case 5 -> withdraw();
                case 6 -> transferToAccount();
                case 7 -> incInvestment();
                case 8 -> rescueInvestment();
                case 9 -> accountRepository.list().forEach(System.out::println);
                case 10 -> investmentRepository.list().forEach(System.out::println);
                case 11 -> investmentRepository.listWallets().forEach(System.out::println);
                case 12 -> {
                    investmentRepository.updateAmount();
                    System.out.println("Investmentos reajustados");
                }
                case 13 -> checkHistory();
                case 14 -> System.exit(0);
                default -> System.out.println("Opção invalida");
            }

        }
    }

    private static void creatAccont(){
        System.out.println("Informe as chaves pix (separadas por ';')");
        var pix = Arrays.stream(scanner.next().split(";")).toList();
        System.out.println("Informe o valor inicial do deposito");
        var amount = scanner.nextLong();
        var wallet = accountRepository.create(pix,amount);
        System.out.println("Conta criada: " + wallet);
    }

    private static void creatInvestment(){
        System.out.println("Informe a taxa do investmento");
        var tax = scanner.nextInt();
        System.out.println("Informe o valor inicial do deposito");
        var initialFunds = scanner.nextLong();
        var investment = investmentRepository.create(tax,initialFunds);
        System.out.println("Investimento criado: " + investment );
    }

    private static void withdraw(){
        System.out.println("Informe a chave pix da conta para o saque: ");
        var pix = scanner.next();
        System.out.println("Informe o valor que sera sacado: ");
        var amount = scanner.nextLong();
        try {
            accountRepository.withdraw(pix, amount);
        } catch (NoFundsEnoughException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void deposit(){
        System.out.println("Informe a chave pix da conta para o deposito: ");
        var pix = scanner.next();
        System.out.println("Informe o valor que sera depositado: ");
        var amount = scanner.nextLong();
        try {
            accountRepository.deposit(pix, amount);
        } catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void transferToAccount(){
        System.out.println("Informe a chave pix da conta de origem: ");
        var source = scanner.next();
        System.out.println("Informe a chave pix da conta de destino: ");
        var target = scanner.next();
        System.out.println("Informe o valor que sera depositado: ");
        var amount = scanner.nextLong();
        try {
            accountRepository.transferMoney(source, target, amount);
        } catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void creatWalletInvestment(){
        System.out.println("Informe a chave pix da conta: ");
        var pix = scanner.next();
        var account = accountRepository.findByPix(pix);
        System.out.println("Informe o indentificador do investimento");
        var investmentId = scanner.nextInt();
        var investmentWaller = investmentRepository.initInvestment(account, investmentId);
        System.out.println("Conta de investimento criada: " + investmentWaller);
    }

    private static void incInvestment(){
        System.out.println("Informe a chave pix da conta para o investmento: ");
        var pix = scanner.next();
        System.out.println("Informe o valor que sera investido: ");
        var amount = scanner.nextLong();
        try {
            investmentRepository.deposit(pix, amount);
        } catch (WalletNotFoundException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void rescueInvestment(){
        System.out.println("Informe a chave pix da conta para o resguate do investimento: ");
        var pix = scanner.next();
        System.out.println("Informe o valor que sera sacado: ");
        var amount = scanner.nextLong();
        try {
            investmentRepository.withdraw(pix, amount);
        } catch (NoFundsEnoughException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void checkHistory(){
        System.out.println("Informe a chave pix da conta para verificar o extrato: ");
        var pix = scanner.next();
        AccountWallet wallet;
        try {
            var sortedHistory = accountRepository.getHistory(pix);
            sortedHistory.forEach((k,v) -> {
                System.out.println(k.format(ISO_DATE_TIME));
                System.out.println(v.get(0).transactionId());
                System.out.println(v.get(0).description());
                System.out.println("R$" + (v.size() / 100) + ",1" + (v.size() % 100));
            });
        } catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }

    }

}
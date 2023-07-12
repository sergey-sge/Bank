package com.gmail.sge.serejka;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class App {
    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("JPABank");
        em = emf.createEntityManager();
        Scanner scanner = new Scanner(System.in);
        addCurrencies();
        try {
            while (true) {
                System.out.println("Bank System");
                System.out.println("1 -- Add new user");
                System.out.println("2 -- Add new account");
                System.out.println("3 -- Deposit money into account");
                System.out.println("4 -- Transfer money between 2 accounts");
                System.out.println("5 -- Show total amount of user's money");
                String s = scanner.nextLine();
                switch (s) {
                    case "1":
                        addUser(scanner);
                        break;
                    case "2":
                        addAccount(scanner);
                        break;
                    case "3":
                        depositMoneyIntoAccount(scanner);
                        break;
                    case "4":
                        transferBetweenAccounts(scanner);
                        break;
                    case "5":
                        showTotalMoneyOfUser(scanner);
                        break;
                    default:
                        return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            em.close();
            emf.close();
            scanner.close();
        }
    }

    private static String checkCurrencyName(Scanner scanner){
        System.out.println("Select the currency: 1 - USD, 2 - EUR, 3 - UAH");
        String s = scanner.nextLine();
        String currencyName = "";
        switch (s) {
            case "1":
                currencyName = "USD";
                break;
            case "2":
                currencyName = "EUR";
                break;
            default:
                currencyName = "UAH";
        }
        return currencyName;
    }

    public static void addUser(Scanner scanner) {
        System.out.println("Enter the user name");
        String name = scanner.nextLine();
        System.out.println("Enter the phone number");
        long phone = Long.parseLong(scanner.nextLine());
        em.getTransaction().begin();
        try {
            User user = new User(name, phone);
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public static void addAccount(Scanner scanner) {
        System.out.println("Enter the user phone number");
        long phone = Long.parseLong(scanner.nextLine());
        try {
            Query query = em.createQuery("Select c from User c where phone = " + phone);
            User user = (User) query.getSingleResult();
            String currencyName = checkCurrencyName(scanner);
            try {
                em.getTransaction().begin();
                Account account = new Account(user, currencyName);
                if (user.addAccount(account)) {
                    em.persist(account);
                    em.getTransaction().commit();
                } else
                    return;
            } catch (Exception e) {
                em.getTransaction().rollback();
            }
        } catch (NoResultException e) {
            System.out.println("User not found!");
            return;
        }
    }


    public static void depositMoneyIntoAccount(Scanner scanner){
        System.out.println("Enter the account ID");
        long id = Long.parseLong(scanner.nextLine());
        Account account = em.find(Account.class,id);
        String currencyName = checkCurrencyName(scanner);
        System.out.println("Enter amount of money for deposit");
        double money = Double.parseDouble(scanner.nextLine());
        em.getTransaction().begin();
        try {
            account.incomingTransaction(convert(currencyName,account.getCurrencyName(),money));
            em.getTransaction().commit();
            System.out.println("User " + account.getUser().getName() + " received " +
                    convert(currencyName,account.getCurrencyName(),money) + " " + account.getCurrencyName());
        } catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    public static void transferBetweenAccounts(Scanner scanner){
        System.out.println("Enter the account id of the sender");
        Account out = em.find(Account.class,Long.parseLong(scanner.nextLine()));
        System.out.println("Enter the account id of the receiver");
        Account in = em.find(Account.class,Long.parseLong(scanner.nextLine()));
        System.out.println("Enter the amount of money you want to send");
        double amount = Double.parseDouble(scanner.nextLine());
        transfer(in,out,amount);
    }

    public static void transfer(Account incoming, Account outcoming, double amount){
        em.getTransaction().begin();
        try {
            outcoming.outgoingTransaction(amount);
            double converted = convert(incoming.getCurrencyName(), outcoming.getCurrencyName(),amount);
            incoming.incomingTransaction(converted);
            em.persist(incoming);
            em.persist(outcoming);
            Transaction transaction = new Transaction(incoming,outcoming,amount,converted);
            em.persist(transaction);
            em.getTransaction().commit();
        } catch (NotEnoughMoneyException e){
            System.out.println("Not enough money for operation");
        }
    }


    public static double getCurrency(String currencyName){
        int id;
        switch (currencyName){
            case "USD" :
                id = 1;
                break;
            case "EUR" :
                id = 2;
                break;
            default:
                id = 3;
        }
        CurrencyRate currencyRate = em.find(CurrencyRate.class,id);
        return currencyRate.getAmount();
    }

    public static void addCurrencies(){
        em.getTransaction().begin();
        try {
            CurrencyRate currencyUSD = new CurrencyRate("USD",36.58);
            CurrencyRate currencyEUR = new CurrencyRate("EUR",40.74);
            CurrencyRate currencyUAH = new CurrencyRate("UAH",1);
            em.persist(currencyUSD);
            em.persist(currencyEUR);
            em.persist(currencyUAH);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    public static double convert(String currencyIn, String currencyOut, double in){
        return in * getCurrency(currencyIn)/getCurrency(currencyOut);
    }



    public static void showTotalMoneyOfUser(Scanner scanner){
        System.out.println("Input the user's ID");
        long id = Long.parseLong(scanner.nextLine());
        User user = em.find(User.class,id);
        List<Account> list = user.getAccounts();
        int totalMoney = 0;
        for (Account account : list){
            totalMoney += convert(account.getCurrencyName(),"UAH",account.getAmount());
        }
        System.out.println("User " + user.getName() + " has totally :" + totalMoney + " UAH");
    }
}
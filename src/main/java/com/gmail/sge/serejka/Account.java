package com.gmail.sge.serejka;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.text.DecimalFormat;

@Entity
@Table(name = "accounts")
public class Account {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String currencyName;
    @Column(columnDefinition = "DECIMAL(10,2)")
    private double amount;

    public Account() {

    }

    public Account(User user, String currencyName) {
        this.user = user;
        this.currencyName = currencyName;
        amount = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public synchronized void incomingTransaction(double amount){
        this.amount += amount;
    }

    public synchronized void outgoingTransaction(double amount) throws NotEnoughMoneyException {
        if (amount <= this.amount){
            this.amount -= amount;
        } else {
            throw new NotEnoughMoneyException();
        }
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", currencyName='" + currencyName + '\'' +
                ", amount=" + amount +
                '}';
    }
}

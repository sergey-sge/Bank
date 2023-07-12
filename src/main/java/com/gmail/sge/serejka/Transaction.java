package com.gmail.sge.serejka;

import javax.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "incoming_account_id")
    private Account incomingAccount;
    @ManyToOne
    @JoinColumn(name = "outgoing_account_id")
    private Account outgoingAccount;
    private double amountSend;
    private double amountReceived;

    public Transaction() {

    }

    public Transaction(Account incomingAccount, Account outgoingAccount, double amountSend, double amountReceived){
        this.incomingAccount = incomingAccount;
        this.outgoingAccount = outgoingAccount;
        this.amountSend = amountSend;
        this.amountReceived = amountReceived;

    }

    public Account getIncomingAccount() {
        return incomingAccount;
    }

    public void setIncomingAccount(Account incomingAccount) {
        this.incomingAccount = incomingAccount;
    }

    public Account getOutgoingAccount() {
        return outgoingAccount;
    }

    public void setOutgoingAccount(Account outgoingAccount) {
        this.outgoingAccount = outgoingAccount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmountSend() {
        return amountSend;
    }

    public void setAmountSend(double amountSend) {
        this.amountSend = amountSend;
    }

    public double getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(double amountReceived) {
        this.amountReceived = amountReceived;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", incomingAccount=" + incomingAccount +
                ", outgoingAccount=" + outgoingAccount +
                ", amountSend=" + amountSend +
                ", amountReceived=" + amountReceived +
                '}';
    }
}

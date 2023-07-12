package com.gmail.sge.serejka;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private long phone;
    @OneToMany(mappedBy = "user")
    private List<Account> accounts = new ArrayList<>();
    public User(){
        super();
    }
    public User(String name, long phone){
        this.name = name;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
    public boolean addAccount(Account account){
        if (checkAccount(account)) {
            accounts.add(account);
            return true;
        } else {
            System.out.println("User already have account with this currency");
            return false;
        }
    }

    public boolean checkAccount(Account account){
        for (Account a : accounts){
            if (a.getCurrencyName().equals(account.getCurrencyName())){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                '}';
    }
}
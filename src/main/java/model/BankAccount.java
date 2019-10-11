package model;

import java.math.BigDecimal;
import java.util.Objects;

public class BankAccount {
    long accountId;
    String accountOwnerName;
    BigDecimal balance;
    Currency currency;

    public BankAccount(){

    }
    public BankAccount(long accountId, String accountOwnerName, BigDecimal balance, Currency currency){
        this.accountId = accountId;
        this.accountOwnerName=accountOwnerName;
        this.balance=balance;
        this.currency=currency;
    }
    public String getAccountOwnerName(){
        return accountOwnerName;
    }
    public BigDecimal getBalance(){
        return  balance;
    }

    public void setBalance(BigDecimal newBalance){
        this.balance=newBalance;
    }
    public long getAccountId(){
        return accountId;
    }

    public Currency getCurrency(){return currency;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(getAccountId(), that.getAccountId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountId());
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public void setAccountOwnerName(String accountOwnerName) {
        this.accountOwnerName = accountOwnerName;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}

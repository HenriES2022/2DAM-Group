/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author 2dam
 */
public class Account implements Serializable {

    private Long id;
    private String description;
    private Double balance;
    private Double creditLine;
    private Double beginBalance;
    private Timestamp beginBalanceTimestamp;
    private Type type;
    private Set<Movement> accountMovements;

    public Account() {
        this.accountMovements = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Double getBalance() {
        return balance;
    }

    public Double getCreditLine() {
        return creditLine;
    }

    public Double getBeginBalance() {
        return beginBalance;
    }

    public Timestamp getBeginBalanceTimestamp() {
        return beginBalanceTimestamp;
    }

    public Type getType() {
        return type;
    }

    public Set<Movement> getAccountMovements() {
        return accountMovements;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setCreditLine(Double creditLine) {
        this.creditLine = creditLine;
    }

    public void setBeginBalance(Double beginBalance) {
        this.beginBalance = beginBalance;
    }

    public void setBeginBalanceTimestamp(Timestamp beginBalanceTimestamp) {
        this.beginBalanceTimestamp = beginBalanceTimestamp;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setAccountMovements(Set<Movement> accountMovements) {
        this.accountMovements = accountMovements;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.description);
        hash = 59 * hash + Objects.hashCode(this.balance);
        hash = 59 * hash + Objects.hashCode(this.creditLine);
        hash = 59 * hash + Objects.hashCode(this.beginBalance);
        hash = 59 * hash + Objects.hashCode(this.beginBalanceTimestamp);
        hash = 59 * hash + Objects.hashCode(this.type);
        hash = 59 * hash + Objects.hashCode(this.accountMovements);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.balance, other.balance)) {
            return false;
        }
        if (!Objects.equals(this.creditLine, other.creditLine)) {
            return false;
        }
        if (!Objects.equals(this.beginBalance, other.beginBalance)) {
            return false;
        }
        if (!Objects.equals(this.beginBalanceTimestamp, other.beginBalanceTimestamp)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return Objects.equals(this.accountMovements, other.accountMovements);
    }




    public void getDatos(){
        System.out.println("\tID: " +this.getId());
        System.out.println("\tDescription: " +this.getDescription());
        System.out.println("\tBalance: " +this.getBalance());
        System.out.println("\tCredit Line: " +this.getCreditLine());
        System.out.println("\tBegin Balance: " +this.getBeginBalance());
        System.out.println("\tBegin Balance Timestamp: " +this.getBeginBalanceTimestamp());
        System.out.println("\tType: " + this.getType());
    }
    
    public void showMovements() {
        System.out.println("---Movimientos---");
        for (Movement am : accountMovements) {
            am.getDatos();
        }
    }
}

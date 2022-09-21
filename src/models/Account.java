/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author 2dam
 */
public class Account implements Serializable {

    private Integer id;
    private String description;
    private float balance;
    private float creditLine;
    private float beginBalance;
    private float beginBalanceTimestamp;
    private Type type;
    private List<Movement> accountMovements;

    public Account() {
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public float getBalance() {
        return balance;
    }

    public float getCreditLine() {
        return creditLine;
    }

    public float getBeginBalance() {
        return beginBalance;
    }

    public float getBeginBalanceTimestamp() {
        return beginBalanceTimestamp;
    }

    public Type getType() {
        return type;
    }

    public List<Movement> getAccountMovements() {
        return accountMovements;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setCreditLine(float creditLine) {
        this.creditLine = creditLine;
    }

    public void setBeginBalance(float beginBalance) {
        this.beginBalance = beginBalance;
    }

    public void setBeginBalanceTimestamp(float beginBalanceTimestamp) {
        this.beginBalanceTimestamp = beginBalanceTimestamp;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setAccountMovements(List<Movement> accountMovements) {
        this.accountMovements = accountMovements;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.description);
        hash = 59 * hash + Float.floatToIntBits(this.balance);
        hash = 59 * hash + Float.floatToIntBits(this.creditLine);
        hash = 59 * hash + Float.floatToIntBits(this.beginBalance);
        hash = 59 * hash + Float.floatToIntBits(this.beginBalanceTimestamp);
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
        if (Float.floatToIntBits(this.balance) != Float.floatToIntBits(other.balance)) {
            return false;
        }
        if (Float.floatToIntBits(this.creditLine) != Float.floatToIntBits(other.creditLine)) {
            return false;
        }
        if (Float.floatToIntBits(this.beginBalance) != Float.floatToIntBits(other.beginBalance)) {
            return false;
        }
        if (Float.floatToIntBits(this.beginBalanceTimestamp) != Float.floatToIntBits(other.beginBalanceTimestamp)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.accountMovements, other.accountMovements)) {
            return false;
        }
        return true;
    }

    public void getDatos(){
        System.out.println("ID: " +this.getId());
        System.out.println("Description: " +this.getDescription());
        System.out.println("Balance: " +this.getBalance());
        System.out.println("Credit Line: " +this.getCreditLine());
        System.out.println("Begin Balance: " +this.getBeginBalance());
        System.out.println("Begin Balance Timestamp: " +this.getBeginBalanceTimestamp());
        System.out.println("Type: " + this.getType());
    }
    
    public void showMovements() {
        System.out.println("---Movimientos---");
        for (Movement am : accountMovements) {
            am.getDatos();
        }
    }
}

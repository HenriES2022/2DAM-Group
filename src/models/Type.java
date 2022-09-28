/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author 2dam
 */
public enum Type {
    STANDAR(0), 
    CREDIT(1);
    
    private final Integer type;
    
    Type(Integer value){
        this.type = value;
    }
    
    public Integer getType(){
        return type;
    }
}

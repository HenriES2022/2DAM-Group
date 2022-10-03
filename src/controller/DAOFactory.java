/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;


import java.util.ResourceBundle;
import models.DAO;
import models.daoImplementacion.DAOImplementacionBD;
import models.daoImplementacion.DAOImplementacionFich;
/**
 *
 * @author yeguo
 */
public class DAOFactory {
    
    public static DAO getDAO(){
        try {
            String dbType = ResourceBundle.getBundle("controller.config").getString("db");
            if(dbType.equalsIgnoreCase("sql")){
                return new DAOImplementacionBD();
            }
        } catch (Exception e) {
            System.err.print(e);
        }
        return new DAOImplementacionFich();
    }
    
    
    
    
}

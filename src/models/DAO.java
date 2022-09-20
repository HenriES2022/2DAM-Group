/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;

/**
 *
 * @author iorit
 */
public interface DAO {
   public void createMovement(Movement mov);
   public List<Movement> checkMovement(Account ac);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.Controller;
import controller.utilidades.Util;
import java.util.Iterator;
import java.util.Set;
import models.Account;
import models.Customer;

/**
 *
 * @author yeguo
 */
public class ViewClass {

    public static void run(Controller controller) {
        int opc;

        do {
            opc = menu();
            switch (opc) {
                case 1:
                    controller.createCustomer();
                    break;
                case 2:
                    controller.checkCustomer();
                    break;
                case 3:
                    controller.checkCustomerAccounts();
                    break;
                case 4:
                    controller.createAccount();
                    break;
                case 5:
                    controller.addAccountToCustomer();
                    break;
                case 6:
                    controller.checkAccountData();
                    break;
                case 7:
                    controller.createMovement();
                    break;
                case 8:
                    controller.checkMovements();
                    break;
                case 9:
                    System.out.println("Saliendo del programa....");
                    break;
                default:
                    System.out.println();
            }

        } while (opc != 9);

    }

    private static int menu() {
        System.out.println("-----------Menú-----------"
                + "\n 1. Crear cliente"
                + "\n 2. Consultar datos de un cliente"
                + "\n 3. Consultar cuentas de un cliente"
                + "\n 4. Crear cuenta para cliente"
                + "\n 5. Agregar cliente a cuenta"
                + "\n 6. Consultar datos de una cuenta"
                + "\n 7. Realizar movimiento sobre una cuenta"
                + "\n 8. Consultar movimientos de cuenta"
                + "\n 9. Salir");
        return Util.leerInt("Seleccione una opción: ");
    }

}

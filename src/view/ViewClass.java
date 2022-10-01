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
        Customer cus;
        Set<Account> accCus;
        do {
            opc = menu();
            switch (opc) {
                case 1:
                    System.out.println(controller.createCustomer() == true
                            ? "Se ha creado correctamente" : "Error. No se ha podido crear el cliente.");
                    break;
                case 2:
                    cus = controller.checkCustomer();
                    if (cus != null) {
                        System.out.println(cus.toString());
                        System.out.print("\n¿Quieres ver las cuentas del cliente?");
                        if (Util.esBoolean()) {
                            accCus = controller.checkCustomerAccounts(cus);
                            if (accCus != null) {
                                iterateSet(accCus);
                            }
                        }
                    }
                    break;
                case 3:
                    iterateSet(controller.checkCustomerAccounts(controller.checkCustomer()));
                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 7:
                    controller.createMovement();
                    break;
                case 8:
                    controller.checkMovements();
                    break;
                case 9:

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

    private static void iterateSet(Set<Account> accList) {
        if (accList != null) {
            Iterator<Account> iter = accList.iterator();
            int i = 1;
            while (iter.hasNext()) {
                System.out.printf("---Cuenta %d---\n", i);
                iter.next().getDatos();
                i++;
            }
        }
    }

}

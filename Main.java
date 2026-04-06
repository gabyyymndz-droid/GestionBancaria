import java.util.Scanner;

public class Main {

    static Cuenta[] cuentas = new Cuenta[50];
    static int totalCuentas = 0;
    static Scanner sc = new Scanner(System.in);


    public static void main(String[] args){
        cargarCuentasIniciales();

        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║   POOmerica - Sistema Bancario   ║");
        System.out.println("╚══════════════════════════════════╝");

        boolean salir = false;
        while (!salir){
            mostrarMenu();
            String opcion = sc.nextLine().trim();
            switch (opcion){
                case "1": menuDeposito();      break;
                case "2": menuRetiro();        break;
                case "3": menuTransferencia(); break;
                case "4": menuHistorial();     break;
                case "5": menuCuentas();       break;
                case "6": menuDashboard();     break;
                case "0":
                    System.out.println("Gracias por usarnos");
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        }
    }
    static void mostrarMenu(){
        System.out.println("\n┌──────────────────────────────┐");
        System.out.println("│        MENU PRINCIPAL        │");
        System.out.println("├──────────────────────────────┤");
        System.out.println("│  1. Depositar                │");
        System.out.println("│  2. Retirar                  │");
        System.out.println("│  3. Transferir               │");
        System.out.println("│  4. Ver historial            │");
        System.out.println("│  5. Gestionar cuentas        │");
        System.out.println("│  6. Dashboard / Resumen      │");
        System.out.println("│  0. Salir                    │");
        System.out.println("└──────────────────────────────┘");
        System.out.print("  Opcion: ");
    }

    static void menuDeposito(){
        System.out.println("-- DEPOSITAR --");
        Cuenta cuenta = pedirCuenta();
        if (cuenta == null) return;

        double monto = pedirMonto();
        if (monto <= 0) return;

        cuenta.depositar(monto);
        System.out.printf("Deposito exitoso. Nuevo saldo: $%.2f%n", cuenta.getSaldo()); // %n es un salto de linea "Enter"
    }


    static void menuRetiro(){
        System.out.println("--RETIRAR--");
        Cuenta cuenta = pedirCuenta();
        if (cuenta == null) return;

        double monto = pedirMonto();
        if (monto <= 0) return;

        if (cuenta.retirar(monto)){
            System.out.printf("Retiro exitoso. Nuevo saldo: $%.2f%n", cuenta.getSaldo());
        } else{
            System.out.println("Saldo insuficiente o monto no permitido");
        }
    }


    static void menuTransferencia(){
        System.out.println("--TRANSFERIR--");

        System.out.println("  Cuenta origen: ");
        Cuenta origen = buscarCuenta(sc.nextLine().trim());
        if (origen == null){
            System.out.println("Cuenta origen no encontrada");
            return;
        }

        System.out.println("  Cuenta destino: ");
        Cuenta destino = buscarCuenta(sc.nextLine().trim());
        if (destino == null){
            System.out.println("Cuenta destino no encontrada");
            return;
        }

        if (origen == destino) {
            System.out.println("La cuenta destino debe ser unica");
            return;
        }

        double monto = pedirMonto();
        if (monto <= 0) return;

        if (origen.transferir(destino, monto)){
            System.out.printf("Transferencia exitosa: $%.2f de %s a %s%n", monto, origen.getCodigo(), destino.getCodigo());
        } else {
            System.out.println("Saldo insuficiente");
        }
    }



    static void menuHistorial(){
        System.out.println("--HISTORIAL--");

        Cuenta cuenta = pedirCuenta();
        if (cuenta == null) return;

        System.out.printf("%n Cliente: %s%n", cuenta.getCliente()); //%s: String
        System.out.printf("  Tipo    : %s%n", cuenta.getTipo());
        System.out.printf("  Saldo   : $%.2f%n", cuenta.getSaldo());
        System.out.println(" " + "-".repeat(55)); //Agregacion estetica -------------...

        if (cuenta.totalMovimientos == 0){
            System.out.println(" Sin movimientos registrados");
            return;
        }
        for (int i = 0; i < cuenta.totalMovimientos; i++){
            System.out.println(" "+ (i + 1) + " " + cuenta.historial[i]);
        }
    }


    static void menuCuentas(){
        System.out.println("\n-- GESTIONAR CUENTAS --");
        System.out.println("  1. Crear cuenta");
        System.out.println("  2. Listar todas las cuentas");
        System.out.println("  3. Buscar una cuenta");
        System.out.print("  Opcion: ");

        switch (sc.nextLine().trim()){
            case "1": crearCuenta(); break;
            case "2": listarCuentas(); break;
            case "3":
                Cuenta c = pedirCuenta();
                if (c != null) mostrarDetalleCuenta(c);
                break;
            default:
                System.out.println("Opcion no valida");
        }
    }

    static void crearCuenta(){
        System.out.println("  Codigo: ");
        String codigo = sc.nextLine().trim();

        if (codigo.isEmpty() || !codigo.matches("[A-Za-z0-9]+")){
            System.out.println("Codigo no valido, solo letras y numeros");
            return;
        }
        if (buscarCuenta(codigo) != null){
            System.out.println("Ya existe cuenta con ese numero");
            return;
        }
        if (totalCuentas >= cuentas.length){
            System.out.println("Limite de cuentas alcanzado"); //Porque al inicio definimos que "cuentas" tenia maximo 50
            return;
        }

        System.out.print(" Cliente: "); //El print LN es un Enter, el print se queda ahi mismo
        String cliente = sc.nextLine().trim();
        if (cliente.isEmpty()){
            System.out.println("El nombre no puede estar vacio");
            return;
        }

        System.out.println("  Tipo de cuenta:");
        System.out.println("    1. Estandar");
        System.out.println("    2. Ahorros  (limite de retiro $5,000)");
        System.out.println("    3. Corriente (permite sobregiro de $1,000)");
        System.out.print("  Opcion: ");

        Cuenta nueva;
        switch (sc.nextLine().trim()){
            case "2": nueva = new CuentaAhorros(codigo, cliente); break;
            case "3": nueva = new CuentaCorriente(codigo, cliente); break;
            default: nueva = new Cuenta(codigo, cliente); break; // Ya usa por defecto "Estandar" --> En Cuenta
        }

        cuentas [totalCuentas++] = nueva;
        System.out.printf(" Cuenta %s creada (%s) para %s.%n", codigo, nueva.getTipo(), cliente);
    }



    static void listarCuentas(){
        System.out.println("");
        System.out.printf( "  %-8s %-12s %-20s %12s%n", "Codigo", "Tipo", "Cliente", "Saldo"); //Solo son los titulos de la tablita
        System.out.println("  " + "-".repeat(56));
        for (int i = 0; i < totalCuentas; i++){
            Cuenta c = cuentas[i];
            System.out.printf("  %-8s %-12s %-20s %12s%n", c.getCodigo(), c.getTipo(), c.getCliente(), "$" + String.format("%.2f", c.getSaldo()));


        }
    }
    static void mostrarDetalleCuenta(Cuenta c){
        System.out.printf("%n  Codigo      : %s%n", c.getCodigo());
        System.out.printf("  Tipo        : %s%n", c.getTipo());
        System.out.printf("  Cliente     : %s%n", c.getCliente());
        System.out.printf("  Saldo       : $%.2f%n", c.getSaldo());
        System.out.printf("  Movimientos : %d%n", c.totalMovimientos);
    }

    static void menuDashboard(){
        double saldoTotal = 0;
        int totalMovs = 0;
        for (int i = 0; i < totalCuentas; i++){
            saldoTotal += cuentas[i].getSaldo();
            totalMovs += cuentas[i].totalMovimientos;
        }

        System.out.println("\n-- DASHBOARD --");
        System.out.printf("  Total de cuentas  : %d%n", totalCuentas);
        System.out.printf("  Saldo total       : $%.2f%n", saldoTotal);
        System.out.printf("  Total movimientos : %d%n", totalMovs);
        System.out.println();
        listarCuentas();
    }

    static Cuenta pedirCuenta(){
        System.out.println("  Codigo cuenta: ");
        String cod = sc.nextLine().trim();
        Cuenta c = buscarCuenta(cod);
        if (c == null)System.out.println("Cuenta no encontrada");
        return c;
    }

    static double pedirMonto(){
        System.out.print("  Monto: $");
        try{
            double monto = Double.parseDouble(sc.nextLine().trim()); //Double.parseDouble: Agarra un String y lo convierte en double
            if (monto <= 0) System.out.println(" El monto debe ser mayor a 0");
            return monto;
        } catch (NumberFormatException e){ //Cuando le doy letra embes de numero me lanza un error, con NumberFormatException lo omito
            System.out.println(" Monto invalido. Ingresa solo numeros");
            return -1;
        }
    }


    static Cuenta buscarCuenta(String codigo){
        for (int i = 0; i < totalCuentas; i++)
            if (cuentas[i].getCodigo().equals(codigo)) return cuentas[i];
        return null;
    }


    static void cargarCuentasIniciales(){
        Cuenta c1          = new Cuenta("001", "Juan Perez");
        CuentaAhorros c2   = new CuentaAhorros("002", "Maria Garcia");
        CuentaCorriente c3 = new CuentaCorriente("003", "Carlos Lopez");
        Cuenta c4          = new Cuenta("004", "Ana Martinez");

        c1.depositar(1000); c1.depositar(500); c1.retirar(200);
        c2.depositar(2000);
        c1.transferir(c2, 300);
        c3.depositar(5000); c3.retirar(1500);
        c4.depositar(750);

        cuentas[0] = c1;
        cuentas[1] = c2;
        cuentas[2] = c3;
        cuentas[3] = c4;
        totalCuentas = 4;
    }
}
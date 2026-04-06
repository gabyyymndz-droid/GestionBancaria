import java.util.Scanner;

public class Main {

    static Banco banco = new Banco();
    static Scanner sc  = new Scanner(System.in);

    public static void main(String[] args) {
        banco.cargar();
        if (banco.totalCuentas == 0) cargarDatosEjemplo();

        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║     NovaBanca - Sistema Bancario ║");
        System.out.println("╚══════════════════════════════════╝");

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            String opcion = sc.nextLine().trim();
            switch (opcion) {
                case "1": menuDeposito();       break;
                case "2": menuRetiro();         break;
                case "3": menuTransferencia();  break;
                case "4": menuHistorial();      break;
                case "5": menuCuentas();        break;
                case "6": menuDashboard();      break;
                case "0":
                    banco.guardar();
                    System.out.println("\n  Datos guardados. ¡Hasta luego!");
                    salir = true;
                    break;
                default:
                    System.out.println("  [!] Opción no válida.");
            }
        }
    }

    // ── Menú principal ─────────────────────────────────────────────────────
    static void mostrarMenu() {
        System.out.println("\n┌─────────────────────────────┐");
        System.out.println("│         MENÚ PRINCIPAL       │");
        System.out.println("├─────────────────────────────┤");
        System.out.println("│  1. Depositar                │");
        System.out.println("│  2. Retirar                  │");
        System.out.println("│  3. Transferir               │");
        System.out.println("│  4. Ver historial            │");
        System.out.println("│  5. Gestionar cuentas        │");
        System.out.println("│  6. Dashboard / Resumen      │");
        System.out.println("│  0. Salir                    │");
        System.out.println("└─────────────────────────────┘");
        System.out.print("  Opción: ");
    }

    // ── Depósito ──────────────────────────────────────────────────────────
    static void menuDeposito() {
        System.out.println("\n── DEPOSITAR ──");
        Cuenta cuenta = pedirCuenta();
        if (cuenta == null) return;

        double monto = pedirMonto();
        if (monto <= 0) return;

        cuenta.depositar(monto);
        banco.guardar();
        System.out.printf("  [✓] Depósito exitoso. Nuevo saldo: $%.2f%n", cuenta.getSaldo());
    }

    // ── Retiro ────────────────────────────────────────────────────────────
    static void menuRetiro() {
        System.out.println("\n── RETIRAR ──");
        Cuenta cuenta = pedirCuenta();
        if (cuenta == null) return;

        double monto = pedirMonto();
        if (monto <= 0) return;

        // Polimorfismo: retirar() se comporta diferente según el tipo de cuenta
        if (cuenta.retirar(monto)) {
            banco.guardar();
            System.out.printf("  [✓] Retiro exitoso. Nuevo saldo: $%.2f%n", cuenta.getSaldo());
        } else {
            System.out.println("  [✗] Saldo insuficiente o monto no permitido.");
        }
    }

    // ── Transferencia ─────────────────────────────────────────────────────
    static void menuTransferencia() {
        System.out.println("\n── TRANSFERIR ──");
        System.out.print("  Cuenta origen:  ");
        Cuenta origen = banco.buscarCuenta(sc.nextLine().trim());
        if (origen == null) { System.out.println("  [!] Cuenta origen no encontrada."); return; }

        System.out.print("  Cuenta destino: ");
        Cuenta destino = banco.buscarCuenta(sc.nextLine().trim());
        if (destino == null) { System.out.println("  [!] Cuenta destino no encontrada."); return; }

        if (origen == destino) { System.out.println("  [!] Las cuentas deben ser diferentes."); return; }

        double monto = pedirMonto();
        if (monto <= 0) return;

        if (origen.transferir(destino, monto)) {
            banco.guardar();
            System.out.printf("  [✓] Transferencia exitosa: $%.2f de %s → %s%n",
                    monto, origen.getCodigo(), destino.getCodigo());
        } else {
            System.out.println("  [✗] Saldo insuficiente.");
        }
    }

    // ── Historial ─────────────────────────────────────────────────────────
    static void menuHistorial() {
        System.out.println("\n── HISTORIAL ──");
        Cuenta cuenta = pedirCuenta();
        if (cuenta == null) return;

        System.out.printf("%n  Cliente : %s%n", cuenta.getCliente());
        System.out.printf("  Tipo    : %s%n", cuenta.getTipo());
        System.out.printf("  Saldo   : $%.2f%n", cuenta.getSaldo());
        System.out.println("  " + "─".repeat(60));

        if (cuenta.totalMovimientos == 0) {
            System.out.println("  Sin movimientos registrados.");
            return;
        }
        for (int i = 0; i < cuenta.totalMovimientos; i++) {
            System.out.println("  " + (i + 1) + ". " + cuenta.historial[i]);
        }
    }

    // ── Gestión de cuentas ────────────────────────────────────────────────
    static void menuCuentas() {
        System.out.println("\n── GESTIONAR CUENTAS ──");
        System.out.println("  1. Crear cuenta");
        System.out.println("  2. Listar cuentas");
        System.out.println("  3. Buscar cuenta");
        System.out.print("  Opción: ");

        switch (sc.nextLine().trim()) {
            case "1": crearCuenta(); break;
            case "2": listarCuentas(); break;
            case "3": {
                Cuenta c = pedirCuenta();
                if (c != null) mostrarCuenta(c);
                break;
            }
            default: System.out.println("  [!] Opción no válida.");
        }
    }

    static void crearCuenta() {
        System.out.print("  Código:  ");
        String codigo = sc.nextLine().trim();
        if (codigo.isEmpty() || !codigo.matches("[A-Za-z0-9]+")) {
            System.out.println("  [!] Código inválido (solo letras y números)."); return;
        }
        if (banco.buscarCuenta(codigo) != null) {
            System.out.println("  [!] El código ya existe."); return;
        }

        System.out.print("  Cliente: ");
        String cliente = sc.nextLine().trim();
        if (cliente.isEmpty()) { System.out.println("  [!] Nombre no puede estar vacío."); return; }

        System.out.println("  Tipo de cuenta:");
        System.out.println("    1. Estándar");
        System.out.println("    2. Ahorros (límite retiro $5,000)");
        System.out.println("    3. Corriente (sobregiro $1,000)");
        System.out.print("  Opción: ");

        // Polimorfismo: se instancia la subclase correcta
        Cuenta nueva;
        switch (sc.nextLine().trim()) {
            case "2": nueva = new CuentaAhorros(codigo, cliente);   break;
            case "3": nueva = new CuentaCorriente(codigo, cliente); break;
            default:  nueva = new Cuenta(codigo, cliente);          break;
        }

        banco.crearCuenta(nueva);
        banco.guardar();
        System.out.printf("  [✓] Cuenta %s creada (%s) para %s.%n", codigo, nueva.getTipo(), cliente);
    }

    static void listarCuentas() {
        if (banco.totalCuentas == 0) { System.out.println("  Sin cuentas registradas."); return; }
        System.out.println();
        System.out.printf("  %-8s %-12s %-20s %12s%n", "Código", "Tipo", "Cliente", "Saldo");
        System.out.println("  " + "─".repeat(56));
        for (int i = 0; i < banco.totalCuentas; i++) {
            Cuenta c = banco.cuentas[i];
            // Polimorfismo: getTipo() retorna el tipo real en tiempo de ejecución
            System.out.printf("  %-8s %-12s %-20s %12s%n",
                    c.getCodigo(), c.getTipo(), c.getCliente(),
                    "$" + String.format("%.2f", c.getSaldo()));
        }
    }

    static void mostrarCuenta(Cuenta c) {
        System.out.printf("%n  Código  : %s%n", c.getCodigo());
        System.out.printf("  Tipo    : %s%n", c.getTipo());
        System.out.printf("  Cliente : %s%n", c.getCliente());
        System.out.printf("  Saldo   : $%.2f%n", c.getSaldo());
        System.out.printf("  Movims. : %d%n", c.totalMovimientos);
    }

    // ── Dashboard ─────────────────────────────────────────────────────────
    static void menuDashboard() {
        System.out.println("\n── DASHBOARD ──");
        System.out.printf("  Total de cuentas : %d%n", banco.totalCuentas);
        System.out.printf("  Saldo total      : $%.2f%n", banco.getSaldoTotal());

        int totalMovs = 0;
        for (int i = 0; i < banco.totalCuentas; i++)
            totalMovs += banco.cuentas[i].totalMovimientos;
        System.out.printf("  Total movimientos: %d%n", totalMovs);

        System.out.println("\n  Resumen por tipo:");
        int est = 0, aho = 0, cor = 0;
        for (int i = 0; i < banco.totalCuentas; i++) {
            String tipo = banco.cuentas[i].getTipo();
            if (tipo.equals("Ahorros"))   aho++;
            else if (tipo.equals("Corriente")) cor++;
            else est++;
        }
        System.out.printf("    Estándar  : %d%n", est);
        System.out.printf("    Ahorros   : %d%n", aho);
        System.out.printf("    Corriente : %d%n", cor);
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    static Cuenta pedirCuenta() {
        System.out.print("  Código de cuenta: ");
        String cod = sc.nextLine().trim();
        Cuenta c = banco.buscarCuenta(cod);
        if (c == null) System.out.println("  [!] Cuenta no encontrada.");
        return c;
    }

    static double pedirMonto() {
        System.out.print("  Monto: $");
        try {
            double monto = Double.parseDouble(sc.nextLine().trim());
            if (monto <= 0) System.out.println("  [!] El monto debe ser positivo.");
            return monto;
        } catch (NumberFormatException e) {
            System.out.println("  [!] Monto inválido. Ingresa un número.");
            return -1;
        }
    }

    // ── Datos de ejemplo ──────────────────────────────────────────────────
    static void cargarDatosEjemplo() {
        Cuenta c1          = new Cuenta("001", "Juan Pérez");
        CuentaAhorros c2   = new CuentaAhorros("002", "María García");
        CuentaCorriente c3 = new CuentaCorriente("003", "Carlos López");

        c1.depositar(1000); c1.depositar(500); c1.retirar(200);
        c2.depositar(2000);
        c1.transferir(c2, 300);
        c3.depositar(5000); c3.retirar(1500);

        banco.crearCuenta(c1);
        banco.crearCuenta(c2);
        banco.crearCuenta(c3);
        banco.guardar();
    }
}

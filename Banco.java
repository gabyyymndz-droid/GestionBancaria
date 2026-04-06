import java.io.*;

public class Banco {
    private static final String ARCHIVO_CUENTAS   = "cuentas.txt";
    private static final String ARCHIVO_HISTORIAL = "historial.txt";

    Cuenta[] cuentas = new Cuenta[200];
    int totalCuentas = 0;

    public Cuenta buscarCuenta(String codigo) {
        for (int i = 0; i < totalCuentas; i++)
            if (cuentas[i].getCodigo().equals(codigo)) return cuentas[i];
        return null;
    }

    public boolean crearCuenta(Cuenta cuenta) {
        if (totalCuentas >= cuentas.length) return false;
        if (buscarCuenta(cuenta.getCodigo()) != null) return false;
        cuentas[totalCuentas++] = cuenta;
        return true;
    }

    public double getSaldoTotal() {
        double total = 0;
        for (int i = 0; i < totalCuentas; i++) total += cuentas[i].getSaldo();
        return total;
    }

    // ── Persistencia: guardar ──────────────────────────────────────────────
    public void guardar() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_CUENTAS))) {
            for (int i = 0; i < totalCuentas; i++)
                pw.println(cuentas[i].toCSV());
        } catch (IOException e) {
            System.out.println("  [!] Error al guardar cuentas: " + e.getMessage());
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_HISTORIAL))) {
            for (int i = 0; i < totalCuentas; i++) {
                Cuenta c = cuentas[i];
                for (int j = 0; j < c.totalMovimientos; j++)
                    pw.println(c.getCodigo() + ";" + c.historial[j].toCSV());
            }
        } catch (IOException e) {
            System.out.println("  [!] Error al guardar historial: " + e.getMessage());
        }
    }

    // ── Persistencia: cargar ───────────────────────────────────────────────
    public void cargar() {
        File f = new File(ARCHIVO_CUENTAS);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_CUENTAS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                Cuenta c = parsearCuenta(linea);
                if (c != null) cuentas[totalCuentas++] = c;
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("  [!] Error al cargar cuentas: " + e.getMessage());
        }

        File fh = new File(ARCHIVO_HISTORIAL);
        if (!fh.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_HISTORIAL))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                int idx = linea.indexOf(';');
                if (idx < 0) continue;
                Cuenta c = buscarCuenta(linea.substring(0, idx));
                if (c != null) c.registrar(Transaccion.fromCSV(linea.substring(idx + 1)));
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("  [!] Error al cargar historial: " + e.getMessage());
        }
    }

    private Cuenta parsearCuenta(String linea) {
        try {
            String[] p = linea.split(";");
            String tipo = p[0], codigo = p[1], cliente = p[2];
            double saldo = Double.parseDouble(p[3]);
            Cuenta c;
            if (tipo.equals("Corriente")) {
                double limite = p.length > 4 ? Double.parseDouble(p[4]) : 1000.0;
                c = new CuentaCorriente(codigo, cliente, limite);
            } else if (tipo.equals("Ahorros")) {
                c = new CuentaAhorros(codigo, cliente);
            } else {
                c = new Cuenta(codigo, cliente);
            }
            c.setSaldo(saldo);
            return c;
        } catch (Exception e) {
            System.out.println("  [!] Línea inválida ignorada: " + linea);
            return null;
        }
    }
}

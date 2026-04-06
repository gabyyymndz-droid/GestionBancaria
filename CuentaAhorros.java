// Herencia: CuentaAhorros extiende Cuenta
public class CuentaAhorros extends Cuenta {

    private static final double LIMITE_RETIRO = 5000.0;

    public CuentaAhorros(String codigo, String cliente) {
        super(codigo, cliente);
    }

    // Polimorfismo: retirar con límite diario
    @Override
    public boolean retirar(double monto) {
        if (monto > LIMITE_RETIRO) {
            System.out.println("  [!] Cuenta Ahorros: límite de retiro es $" + String.format("%.2f", LIMITE_RETIRO));
            return false;
        }
        return super.retirar(monto);
    }

    @Override
    public String getTipo() { return "Ahorros"; }
}

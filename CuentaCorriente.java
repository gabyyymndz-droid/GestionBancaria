// Herencia: CuentaCorriente extiende Cuenta
public class CuentaCorriente extends Cuenta {

    private double limiteCredito;

    public CuentaCorriente(String codigo, String cliente, double limiteCredito) {
        super(codigo, cliente);
        this.limiteCredito = limiteCredito;
    }

    public CuentaCorriente(String codigo, String cliente) {
        this(codigo, cliente, 1000.0);
    }

    // Polimorfismo: permite sobregiro hasta el límite de crédito
    @Override
    public boolean retirar(double monto) {
        if (monto <= 0 || monto > getSaldo() + limiteCredito) return false;
        setSaldo(getSaldo() - monto);
        registrar(new Transaccion("RETIRO", monto,
                getSaldo() < 0 ? "Retiro con sobregiro" : "Retiro de efectivo"));
        return true;
    }

    @Override
    public String getTipo() { return "Corriente"; }

    public double getLimiteCredito() { return limiteCredito; }

    @Override
    public String toCSV() {
        return getTipo() + ";" + getCodigo() + ";" + getCliente() + ";" + getSaldo() + ";" + limiteCredito;
    }
}

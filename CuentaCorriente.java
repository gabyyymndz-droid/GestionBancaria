public class CuentaCorriente extends Cuenta {

    private double limiteCredito;

    public CuentaCorriente(String codigo, String cliente, double limiteCredito) {
        super(codigo, cliente);
        this.limiteCredito = limiteCredito;
    }

    public CuentaCorriente(String codigo, String cliente) {
        this(codigo, cliente, 1000.0);
    }

    @Override
    public boolean retirar(double monto) {
        if (monto <= 0 || monto > saldo + limiteCredito) return false;
        saldo -= monto;
        String desc = saldo < 0 ? "Retiro con sobregiro" : "Retiro de efectivo";
        registrar(new Transaccion("RETIRO", monto, desc));
        return true;
    }

    @Override
    public String getTipo() { return "Corriente"; }

    public double getLimiteCredito() { return limiteCredito; }
}

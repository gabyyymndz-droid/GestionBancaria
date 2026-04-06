public class CuentaCorriente extends Cuenta{

    private double limiteCredito;

    public CuentaCorriente(String codigo, String cliente, double limiteCredito){
        super(codigo, cliente);
        this.limiteCredito = limiteCredito;
    }

    public CuentaCorriente(String codigo, String cliente){
        this(codigo,cliente, 100.0);
    }

    @Override
    public boolean retirar(double monto) {
        if (monto <= 0 || monto > saldo + limiteCredito) {
            return false;
        }
        saldo = saldo - monto;
        String descripcion;
        if (saldo < 0){
            descripcion = "Retiro con sobregiro";
        }else {
            descripcion = "Retiro de efectivo";
        }
        Transaccion nuevaTransaccion = new Transaccion("RETIRO", monto, descripcion);
        registrar(nuevaTransaccion);
        return true;
    }

    @Override
    public String getTipo() {
        return "Corriente";
    }

    public double getLimiteCredito(){
        return limiteCredito;
    }
}

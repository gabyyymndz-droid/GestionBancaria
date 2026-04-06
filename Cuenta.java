public class Cuenta {

    private  String codigo;
    private  String cliente;
    protected double saldo;
    protected Transaccion[] historial;
    protected int totalMovimientos;

    public Cuenta(String codigo, String cliente) {
        this.codigo            = codigo;
        this.cliente           = cliente;
        this.saldo             = 0.0;
        this.historial         = new Transaccion[200];
        this.totalMovimientos  = 0;
    }

    public String getCodigo()  { return codigo;  }
    public String getCliente() { return cliente; }
    public double getSaldo()   { return saldo;   }

    public void depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
            registrar(new Transaccion("DEPOSITO", monto, "Deposito en efectivo"));
        }
    }

    public boolean retirar(double monto) {
        if (monto <= 0 || monto > saldo) return false;
        saldo -= monto;
        registrar(new Transaccion("RETIRO", monto, "Retiro de efectivo"));
        return true;
    }

    public boolean transferir(Cuenta destino, double monto) {
        if (monto <= 0 || monto > saldo) return false;
        saldo -= monto;
        registrar(new Transaccion("TRANSFERENCIA", monto, "Enviada a: " + destino.getCodigo()));
        destino.saldo += monto;
        destino.registrar(new Transaccion("TRANSFERENCIA", monto, "Recibida de: " + this.codigo));
        return true;
    }

    protected void registrar(Transaccion t) {
        if (totalMovimientos < historial.length)
            historial[totalMovimientos++] = t;
    }

    public String getTipo() { return "Estandar"; }
}

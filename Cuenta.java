public class Cuenta {

    private String codigo;
    private String cliente;
    protected double saldo;                 //protected: La variable no es pública para el programa, sino que solo puede ser leída o modificada por la clase donde fue creada
    protected Transaccion[] historial;
    protected int totalMovimientos;

    public Cuenta(String codigo, String cliente){
        this.codigo = codigo;
        this.cliente = cliente;
        this.saldo = 0.0;
        this.historial = new Transaccion[200];
        this.totalMovimientos = 0;
    }

    public String getCodigo() {return codigo;}
    public String getCliente() {return cliente;}
    public double getSaldo() {return saldo;}

    public void depositar(double monto){
        if(monto > 0) {
            saldo += monto;
            registrar(new Transaccion("Deposito", monto, "Retiro en efectivo"));
        }
    }

    public boolean retirar(double monto){
        if (monto <= 0 || monto > saldo) return false;
        saldo -= monto;
        registrar(new Transaccion("RETIRO", monto, "Retiro en efectivo"));
        return true;
    }

    public boolean transferir(Cuenta destino, double monto){
        if (monto <= 0 || monto > saldo) return false;
        saldo -= monto;
        registrar(new Transaccion("TRANSFERENCIA", monto, "Enviada a: " + destino.getCodigo()));
        destino.saldo += monto;
        destino.registrar(new Transaccion("TRANFERENCIA", monto, "Recibida de: " + this.codigo));
        return true;
    }

    protected void registrar(Transaccion t){
        if (totalMovimientos < historial.length)
            historial[totalMovimientos++] = t;
    }

    public String getTipo(){
        return "Estandar";
    } //Funcionaria como un tipo de cuenta por defecto, en caso de crearse una cuenta y no definir su tipo, funciona como un constructor
}

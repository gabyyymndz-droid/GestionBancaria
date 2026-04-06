public class CuentaAhorros extends Cuenta{

    private static double LIMITE_RETIRO = 5000.0;

    public CuentaAhorros(String codigo, String cliente){
        super(codigo, cliente);
    }

    @Override
    public boolean retirar(double monto){
        if (monto > LIMITE_RETIRO){
            System.out.println("Cuenta Ahorro: El limite de retiro es de: $" + String.format("%.2f", LIMITE_RETIRO));
            return false;
        }
        return super.retirar(monto);
    }

    @Override
    public String getTipo(){
        return "Ahorros";
    }
}

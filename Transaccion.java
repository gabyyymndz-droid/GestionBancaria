import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaccion {

    String tipo;
    double monto;
    String descripcion;
    LocalDateTime fecha;

    public Transaccion(String tipo, double monto, String descripcion) {
        this.tipo        = tipo;
        this.monto       = monto;
        this.descripcion = descripcion;
        this.fecha       = LocalDateTime.now();
    }

    public String getFechaFormateada() {
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String toString() {
        String signo = tipo.equals("DEPOSITO") ? "+" : "-";
        return "[" + tipo + "] " + signo + "$" + String.format("%.2f", monto)
                + " | " + descripcion + " | " + getFechaFormateada();
    }
}

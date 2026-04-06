import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaccion {
    String tipo;        // "DEPOSITO", "RETIRO", "TRANSFERENCIA"
    double monto;
    String descripcion;
    LocalDateTime fecha;

    public Transaccion(String tipo, double monto, String descripcion) {
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
    }

    // Constructor para cargar desde archivo
    public Transaccion(String tipo, double monto, String descripcion, LocalDateTime fecha) {
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public String getFechaFormateada() {
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String toString() {
        String signo = tipo.equals("DEPOSITO") ? "+" : "-";
        return "[" + tipo + "] " + signo + "$" + String.format("%.2f", monto)
                + " | " + descripcion + " | " + getFechaFormateada();
    }

    // Serializar para guardar en archivo
    public String toCSV() {
        return tipo + ";" + monto + ";" + descripcion + ";"
                + fecha.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    // Deserializar desde archivo
    public static Transaccion fromCSV(String linea) {
        String[] p = linea.split(";", 4);
        return new Transaccion(p[0], Double.parseDouble(p[1]), p[2],
                LocalDateTime.parse(p[3], DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}

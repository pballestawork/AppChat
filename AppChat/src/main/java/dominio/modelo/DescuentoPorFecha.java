package dominio.modelo;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Date-based discount that is only valid on specific dates such as Black Friday or seasonal sales.
 */
public class DescuentoPorFecha implements Descuento {
    private String nombre;
    private double porcentaje;
    private MonthDay fechaInicio;
    private MonthDay fechaFin;
    
    /**
     * Creates a date-based discount valid for a specific day.
     * 
     * @param nombre The name of the discount
     * @param porcentaje The percentage of the discount (0.0 to 1.0)
     * @param dia The day of the month (1-31)
     * @param mes The month (1-12)
     */
    public DescuentoPorFecha(String nombre, double porcentaje, int dia, Month mes) {
        this(nombre, porcentaje, MonthDay.of(mes, dia), MonthDay.of(mes, dia));
    }
    
    /**
     * Creates a date-based discount valid for a range of dates.
     * 
     * @param nombre The name of the discount
     * @param porcentaje The percentage of the discount (0.0 to 1.0)
     * @param diaInicio The starting day
     * @param mesInicio The starting month
     * @param diaFin The ending day
     * @param mesFin The ending month
     */
    public DescuentoPorFecha(String nombre, double porcentaje, int diaInicio, Month mesInicio, int diaFin, Month mesFin) {
        this(nombre, porcentaje, MonthDay.of(mesInicio, diaInicio), MonthDay.of(mesFin, diaFin));
    }
    
    /**
     * Creates a date-based discount valid for a range of dates using MonthDay.
     * 
     * @param nombre The name of the discount
     * @param porcentaje The percentage of the discount (0.0 to 1.0)
     * @param fechaInicio The starting date
     * @param fechaFin The ending date
     */
    public DescuentoPorFecha(String nombre, double porcentaje, MonthDay fechaInicio, MonthDay fechaFin) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    
    @Override
    public String getNombre() {
        return nombre;
    }
    
    @Override
    public double getPorcentaje() {
        return porcentaje;
    }
    
    @Override
    public double calcularDescuento(double precioOriginal) {
        return precioOriginal * porcentaje;
    }
    
    @Override
    public double calcularPrecioFinal(double precioOriginal) {
        return precioOriginal - calcularDescuento(precioOriginal);
    }
    
    @Override
    public boolean esAplicable() {
        LocalDate hoy = LocalDate.now();
        MonthDay diaActual = MonthDay.from(hoy);
        
        // Si fechaInicio es antes que fechaFin (mismo año)
        if (fechaInicio.isBefore(fechaFin) || fechaInicio.equals(fechaFin)) {
            return !diaActual.isBefore(fechaInicio) && !diaActual.isAfter(fechaFin);
        } 
        // Si fechaInicio es después de fechaFin (por ejemplo, del 20 de diciembre al 10 de enero)
        else {
            return !diaActual.isBefore(fechaInicio) || !diaActual.isAfter(fechaFin);
        }
    }
    
    @Override
    public String getDescripcionRequisitos() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM").withLocale(new Locale("es", "ES"));
        
        if (fechaInicio.equals(fechaFin)) {
            return "Válido solo el " + formatter.format(fechaInicio.atYear(LocalDate.now().getYear()));
        } else {
            return "Válido desde el " + formatter.format(fechaInicio.atYear(LocalDate.now().getYear())) + 
                   " hasta el " + formatter.format(fechaFin.atYear(LocalDate.now().getYear()));
        }
    }
    
    @Override
    public String toString() {
        return nombre + " (" + (int)(porcentaje * 100) + "%)";
    }
    
    /**
     * Factory method for creating a Black Friday discount.
     * 
     * @param porcentaje The percentage of the discount (0.0 to 1.0)
     * @return A new Black Friday discount instance
     */
    public static DescuentoPorFecha crearDescuentoBlackFriday(double porcentaje) {
        // Black Friday is the 4th Friday of November
        return new DescuentoPorFecha("Black Friday", porcentaje, 24, Month.NOVEMBER, 26, Month.NOVEMBER);
    }
    
    /**
     * Factory method for creating a seasonal sales discount.
     * 
     * @param nombre The name of the seasonal sale
     * @param porcentaje The percentage of the discount (0.0 to 1.0)
     * @param mesInicio The starting month
     * @param mesFin The ending month
     * @return A new seasonal discount instance
     */
    public static DescuentoPorFecha crearDescuentoTemporada(String nombre, double porcentaje, Month mesInicio, Month mesFin) {
        return new DescuentoPorFecha(
            nombre + " " + mesInicio.getDisplayName(TextStyle.FULL, new Locale("es", "ES")), 
            porcentaje, 
            1, mesInicio, 
            mesFin.length(LocalDate.now().isLeapYear()), mesFin
        );
    }
}
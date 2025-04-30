package dominio.modelo;

/**
 * Basic implementation of a discount with a fixed percentage.
 */
public class DescuentoBasico implements Descuento {
    private String nombre;
    private double porcentaje;
    
    /**
     * Creates a basic discount with a name and percentage.
     * 
     * @param nombre The name of the discount
     * @param porcentaje The percentage of the discount (0.0 to 1.0)
     */
    public DescuentoBasico(String nombre, double porcentaje) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
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
        // Basic discounts are always applicable
        return true;
    }
    
    @Override
    public String getDescripcionRequisitos() {
        return "No tiene requisitos especiales";
    }
    
    @Override
    public String toString() {
        return nombre + " (" + (int)(porcentaje * 100) + "%)";
    }
}
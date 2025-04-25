package utils;

/**
 * Class representing a discount with a name and percentage.
 */
public class Descuento {
    private String nombre;
    private double porcentaje;
    
    public Descuento(String nombre, double porcentaje) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public double getPorcentaje() {
        return porcentaje;
    }
    
    /**
     * Calculates the discount amount based on the original price.
     * 
     * @param precioOriginal The original price
     * @return The discount amount
     */
    public double calcularDescuento(double precioOriginal) {
        return precioOriginal * porcentaje;
    }
    
    /**
     * Calculates the final price after applying the discount.
     * 
     * @param precioOriginal The original price
     * @return The final price after discount
     */
    public double calcularPrecioFinal(double precioOriginal) {
        return precioOriginal - calcularDescuento(precioOriginal);
    }
    
    @Override
    public String toString() {
        return nombre + " (" + (int)(porcentaje * 100) + "%)";
    }
}
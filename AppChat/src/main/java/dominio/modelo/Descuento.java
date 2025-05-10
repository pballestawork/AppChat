package dominio.modelo;

/**
 * Interface representing a discount with common methods.
 */
public interface Descuento {
    /**
     * Gets the name of the discount.
     * 
     * @return The name of the discount
     */
    String getNombre();
    
    /**
     * Gets the percentage of the discount.
     * 
     * @return The percentage of the discount (0.0 to 1.0)
     */
    double getPorcentaje();
    
    /**
     * Calculates the discount amount based on the original price.
     * 
     * @param precioOriginal The original price
     * @return The discount amount
     */
    double calcularDescuento(double precioOriginal);
    
    /**
     * Calculates the final price after applying the discount.
     * 
     * @param precioOriginal The original price
     * @return The final price after discount
     */
    double calcularPrecioFinal(double precioOriginal);
    
    /**
     * Validates if the discount is applicable.
     * 
     * @return true if the discount is applicable, false otherwise
     */
    boolean esAplicable();
    
    /**
     * Get a description of the requirements for this discount.
     * 
     * @return A human-readable description of the requirements
     */
    String getDescripcionRequisitos();
}
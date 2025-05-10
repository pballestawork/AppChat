package dominio.modelo;

import java.time.LocalDate;
import java.time.Period;

/**
 * Age-based discount implementation that requires the user to be within a certain age range.
 */
public class DescuentoPorEdad implements Descuento {
    private String nombre;
    private double porcentaje;
    private int edadMinima;
    private int edadMaxima;
    private LocalDate fechaNacimientoUsuario;
    
    /**
     * Creates an age-based discount.
     * 
     * @param nombre The name of the discount
     * @param porcentaje The percentage of the discount (0.0 to 1.0)
     * @param edadMinima The minimum age required
     * @param edadMaxima The maximum age allowed
     */
    public DescuentoPorEdad(String nombre, double porcentaje, int edadMinima, int edadMaxima) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
    }
    
    /**
     * Sets the user's birth date to validate discount applicability.
     * 
     * @param fechaNacimiento The user's birth date
     */
    public void setFechaNacimientoUsuario(LocalDate fechaNacimiento) {
        this.fechaNacimientoUsuario = fechaNacimiento;
    }
    
    @Override
    public String getNombre() {
        return nombre;
    }
    
    @Override
    public double getPorcentaje() {
        return porcentaje;
    }
    
    /**
     * Gets the minimum age required for this discount.
     * 
     * @return The minimum age
     */
    public int getEdadMinima() {
        return edadMinima;
    }
    
    /**
     * Gets the maximum age allowed for this discount.
     * 
     * @return The maximum age
     */
    public int getEdadMaxima() {
        return edadMaxima;
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
        if (fechaNacimientoUsuario == null) {
            return false;
        }
        
        int edad = Period.between(fechaNacimientoUsuario, LocalDate.now()).getYears();
        return esValidoParaEdad(edad);
    }
    
    /**
     * Checks if the given age is valid for this discount.
     * 
     * @param edad The age to check
     * @return true if the age is within the valid range
     */
    public boolean esValidoParaEdad(int edad) {
        return edad >= edadMinima && edad <= edadMaxima;
    }
    
    @Override
    public String getDescripcionRequisitos() {
        return "Requiere edad entre " + edadMinima + " y " + edadMaxima + " años";
    }
    
    @Override
    public String toString() {
        return nombre + " (" + (int)(porcentaje * 100) + "%) - Edad: " + edadMinima + "-" + edadMaxima;
    }
}
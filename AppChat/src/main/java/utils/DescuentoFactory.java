package utils;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import dominio.modelo.Descuento;
import dominio.modelo.DescuentoBasico;
import dominio.modelo.DescuentoPorEdad;
import dominio.modelo.DescuentoPorFecha;

/**
 * Factory class for creating different types of discounts.
 */
public class DescuentoFactory {
    
    private static final Map<String, Supplier<Descuento>> descuentoProviders = new HashMap<>();
    
    static {
        // Basic discounts without special requirements
        registerDescuento("Sin descuento", () -> new DescuentoBasico("Sin descuento", 0.0));
        
        // Age-based discounts
        registerDescuento("Descuento por estudiante", () -> new DescuentoPorEdad("Descuento estudiante", 0.25, 18, 25));
        registerDescuento("Descuento por jubilación", () -> new DescuentoPorEdad("Descuento familiar", 0.15, 65, 120));
        
        // Date-based discounts
        registerDescuento("Black Friday", () -> DescuentoPorFecha.crearDescuentoBlackFriday(0.30));
        registerDescuento("Descuento primavera", () -> DescuentoPorFecha.crearDescuentoTemporada("Descuento primavera", 0.20, Month.MARCH, Month.MAY));
        registerDescuento("Descuento verano", () -> DescuentoPorFecha.crearDescuentoTemporada("Descuento verano", 0.20, Month.JUNE, Month.AUGUST));
    }
    
    /**
     * Register a new discount type with the factory.
     * 
     * @param nombre The name of the discount type
     * @param supplier A supplier that creates new instances of the discount
     */
    public static void registerDescuento(String nombre, Supplier<Descuento> supplier) {
        descuentoProviders.put(nombre, supplier);
    }
    
    /**
     * Create a new discount instance of the specified type.
     * 
     * @param nombre The name of the discount type to create
     * @return A new discount instance
     * @throws IllegalArgumentException if the discount type is not found
     */
    public static Descuento createDescuento(String nombre) {
        Supplier<Descuento> supplier = descuentoProviders.get(nombre);
        if (supplier == null) {
            throw new IllegalArgumentException("Descuento no encontrado: " + nombre);
        }
        return supplier.get();
    }
    
    /**
     * Get an array of all available discount type names.
     * 
     * @return An array of discount type names
     */
    public static String[] getDescuentosDisponibles() {
        return descuentoProviders.keySet().toArray(new String[0]);
    }
    
    /**
     * Apply user-specific context to a discount to determine if it's applicable.
     * This should be called before using age or date-based discounts.
     * 
     * @param descuento The discount to configure
     * @param fechaNacimientoUsuario The user's birth date for age validation
     * @return The configured discount
     */
    public static Descuento configurarContextoUsuario(Descuento descuento, LocalDate fechaNacimientoUsuario) {
        if (descuento instanceof DescuentoPorEdad) {
            ((DescuentoPorEdad) descuento).setFechaNacimientoUsuario(fechaNacimientoUsuario);
        }
        return descuento;
    }
}
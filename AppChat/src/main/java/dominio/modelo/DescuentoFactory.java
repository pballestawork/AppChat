package dominio.modelo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Factory for creating discount objects using lambda expressions and suppliers.
 * This factory follows the Factory Method Pattern with a functional approach.
 */
public class DescuentoFactory {
    
    // Map of discount providers using Supplier functional interface
    private static final Map<String, Supplier<Descuento>> descuentoProviders = new HashMap<>();
    
    // Static initialization block to register all available discounts
    static {
        registerDescuento("Sin descuento", () -> new Descuento("Sin descuento", 0.0));
        registerDescuento("Descuento estudiante", () -> new Descuento("Descuento estudiante", 0.25));
        registerDescuento("Descuento familiar", () -> new Descuento("Descuento familiar", 0.15));
        registerDescuento("Código promocional", () -> new Descuento("Código promocional", 0.10));
        registerDescuento("Black Friday", () -> new Descuento("Black Friday", 0.30));
    }
    
    /**
     * Registers a new discount provider with the factory.
     * 
     * @param nombre The name of the discount
     * @param supplier The supplier function that creates the discount object
     */
    public static void registerDescuento(String nombre, Supplier<Descuento> supplier) {
        descuentoProviders.put(nombre, supplier);
    }
    
    /**
     * Creates a new discount object based on the given name.
     * 
     * @param nombre The name of the discount to create
     * @return A new discount object
     * @throws IllegalArgumentException If the discount name is not registered
     */
    public static Descuento createDescuento(String nombre) {
        Supplier<Descuento> supplier = descuentoProviders.get(nombre);
        if (supplier == null) {
            throw new IllegalArgumentException("Descuento no encontrado: " + nombre);
        }
        return supplier.get();
    }
    
    /**
     * Returns all available discount names.
     * 
     * @return Array of discount names
     */
    public static String[] getDescuentosDisponibles() {
        return descuentoProviders.keySet().toArray(new String[0]);
    }
}
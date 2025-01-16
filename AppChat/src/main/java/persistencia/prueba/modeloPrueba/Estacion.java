package persistencia.prueba.modeloPrueba;

import java.time.LocalDateTime;

public class Estacion {
	String id;
	String nombre;
	int plazas;
	LocalDateTime fechaApertura;
	
	
	//POJO
	public Estacion() {
		
	}
	
	public Estacion(String id, String nombre, int plazas, LocalDateTime fechaApertura) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.plazas = plazas;
		this.fechaApertura = fechaApertura;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getPlazas() {
		return plazas;
	}
	public void setPlazas(int plazas) {
		this.plazas = plazas;
	}
	public LocalDateTime getFechaApertura() {
		return fechaApertura;
	}
	public void setFechaApertura(LocalDateTime fechaApertura) {
		this.fechaApertura = fechaApertura;
	}
	
	
	
	
}

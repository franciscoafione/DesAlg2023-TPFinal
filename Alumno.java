
package TPFinal;

/**
 *
 * @author Francisco Afione
 */
public class Alumno {
    private String apellido;
    private String nombre;
    private int legajo;
    private int grado;
    private double promedio;
    
    // constructores
    public Alumno (String apell, String nomb, int leg, int grad, double prom) {
        this.apellido = apell;
        this.nombre = nomb;
        this.legajo = leg;
        this.grado = grad;
        this.promedio = prom;
}
    
    // observadores
    public String getApellido () {
        return this.apellido;
    }
    
    public String getNombre () {
        return this.nombre;
    }
    
    public int getGrado () {
        return this.grado;
    }
    
    public int getLegajo () {
        return this.legajo;
    }
    
    public double getPromedio (){
        return this.promedio;
    }
    
    public String toString () {
        return this.apellido + ", "+ this.nombre + ", Legajo: " + this.legajo;
    }
    
    // modificadores
    public void setGrado (int grad) {
        this.grado = grad;
    }
    
    public void setProm (double prom){
        this.promedio = prom;
    }
    
    public void pasarDeGrado (){
        grado = this.grado +1;
    }
    
}

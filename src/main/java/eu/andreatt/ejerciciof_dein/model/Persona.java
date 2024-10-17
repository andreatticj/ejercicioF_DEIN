package eu.andreatt.ejerciciof_dein.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

/**
 * La clase {@code Persona} representa una persona con las propiedades nombre, apellido y edad.
 * Utiliza propiedades observables de JavaFX para permitir la vinculación de datos.
 */
public class Persona {

    private StringProperty nombre;
    private StringProperty apellido;
    private IntegerProperty edad;

    /**
     * Constructor que inicializa una instancia de {@code Persona} con nombre, apellido y edad.
     *
     * @param nombre   El nombre de la persona.
     * @param apellido El apellido de la persona.
     * @param edad     La edad de la persona.
     */
    public Persona(String nombre, String apellido, int edad) {
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.edad = new SimpleIntegerProperty(edad);
    }

    /**
     * Obtiene la propiedad {@code nombre} para permitir la vinculación de datos.
     *
     * @return La propiedad {@code nombre}.
     */
    public StringProperty nombreProperty() {
        return nombre;
    }

    /**
     * Obtiene el valor actual de la propiedad {@code nombre}.
     *
     * @return El valor de {@code nombre}.
     */
    public String getNombre() {
        return nombre.get();
    }

    /**
     * Establece un nuevo valor para la propiedad {@code nombre}.
     *
     * @param nombre El nuevo valor para {@code nombre}.
     */
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    /**
     * Obtiene la propiedad {@code apellido} para permitir la vinculación de datos.
     *
     * @return La propiedad {@code apellido}.
     */
    public StringProperty apellidoProperty() {
        return apellido;
    }

    /**
     * Obtiene el valor actual de la propiedad {@code apellido}.
     *
     * @return El valor de {@code apellido}.
     */
    public String getApellido() {
        return apellido.get();
    }

    /**
     * Establece un nuevo valor para la propiedad {@code apellido}.
     *
     * @param apellido El nuevo valor para {@code apellido}.
     */
    public void setApellido(String apellido) {
        this.apellido.set(apellido);
    }

    /**
     * Obtiene la propiedad {@code edad} para permitir la vinculación de datos.
     *
     * @return La propiedad {@code edad}.
     */
    public IntegerProperty edadProperty() {
        return edad;
    }

    /**
     * Obtiene el valor actual de la propiedad {@code edad}.
     *
     * @return El valor de {@code edad}.
     */
    public int getEdad() {
        return edad.get();
    }

    /**
     * Establece un nuevo valor para la propiedad {@code edad}.
     *
     * @param edad El nuevo valor para {@code edad}.
     */
    public void setEdad(int edad) {
        this.edad.set(edad);
    }

    /**
     * Compara este objeto {@code Persona} con otro objeto para determinar si son iguales.
     * La comparación se realiza en base a los valores de las propiedades {@code nombre}, {@code apellido} y {@code edad}.
     *
     * @param o El objeto con el que se compara.
     * @return {@code true} si los objetos son iguales, {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Comprobación de referencia
        if (o == null || getClass() != o.getClass()) return false; // Comprobación de tipo
        Persona persona = (Persona) o;

        // Comparar los valores de las propiedades, no las propiedades en sí
        return Objects.equals(getNombre(), persona.getNombre()) &&
                Objects.equals(getApellido(), persona.getApellido()) &&
                getEdad() == persona.getEdad(); // Comparar la edad directamente
    }

    /**
     * Devuelve un valor hash basado en los valores de las propiedades {@code nombre}, {@code apellido} y {@code edad}.
     *
     * @return El valor hash de esta instancia de {@code Persona}.
     */
    @Override
    public int hashCode() {
        // Utilizar los valores de las propiedades en hashCode
        return Objects.hash(getNombre(), getApellido(), getEdad());
    }

}

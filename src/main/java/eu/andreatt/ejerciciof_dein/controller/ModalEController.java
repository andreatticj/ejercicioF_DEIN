package eu.andreatt.ejerciciof_dein.controller;

import eu.andreatt.ejerciciof_dein.model.Persona;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Controlador de la ventana modal para agregar o modificar una persona.
 * Permite gestionar la información ingresada y agregarla a una lista de personas,
 * o cancelar la operación y cerrar la ventana modal.
 */
public class ModalEController {

    @FXML
    private Button btnCancelar;  // Botón para cancelar la operación

    @FXML
    private Button btnGuardar;  // Botón para guardar una nueva persona

    @FXML
    private TextField txtApellido;  // Campo de texto para ingresar el apellido

    @FXML
    private TextField txtEdad;  // Campo de texto para ingresar la edad

    @FXML
    private TextField txtNombre;  // Campo de texto para ingresar el nombre

    private Persona persona;  // Objeto Persona que se creará a partir de los datos ingresados
    private ObservableList<Persona> personas;  // Lista observable que contiene las personas

    /**
     * Establece la lista de personas donde se añadirá o editara la persona.
     *
     * @param personas Lista observable de personas.
     */
    public void setPersonas(ObservableList<Persona> personas) {
        this.personas = personas;
    }

    /**
     * Maneja el evento de cancelar y no añadir persona a la lista.
     * Cierra la ventana.
     *
     * @param event El evento que desencadena la acción.
     */
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();  // Obtiene la ventana actual
        stage.close();  // Cierra la ventana
    }

    /**
     * Carga los datos de la persona en los campos de texto de la ventana modal para su modificación.
     *
     * @param persona La persona cuyos datos serán cargados en los campos.
     */
    public void cargarPersona(Persona persona) {
        this.persona = persona;  // Asigna la persona a modificar
        txtNombre.setText(persona.getNombre());  // Carga el nombre
        txtApellido.setText(persona.getApellido());  // Carga el apellido
        txtEdad.setText(String.valueOf(persona.getEdad()));  // Carga la edad
    }

    /**
     * Maneja el evento de guardar o editar una persona.
     * Verifica que los datos ingresados sean válidos antes de agregar o editar a la persona en la lista.
     * Si los datos son válidos, agrega o edita la persona y cierra la ventana.
     *
     * @param event El evento que desencadena la acción.
     */
    @FXML
    void guardar(ActionEvent event) {
        Window win = ((Button) event.getSource()).getScene().getWindow();  // Obtiene la ventana actual
        String errores = verificarInfo();  // Verifica la validez de la información ingresada

        if (errores.isEmpty()) {  // Si no hay errores
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            int edad = Integer.parseInt(txtEdad.getText());

            if (persona != null) { // Si estamos editando una persona
                // Verifica que no exista otra persona con los mismos datos
                Persona personaEditada = new Persona(nombre, apellido, edad);
                if (!verificarExistePersona(win, personaEditada)) {
                    // Actualiza la persona existente
                    persona.setNombre(nombre);
                    persona.setApellido(apellido);
                    persona.setEdad(edad);
                } else {
                    cargarPersona(persona);  // Carga nuevamente la persona si ya existe
                    return; // Si ya existe, no se guarda ni se cierra la ventana
                }

            } else { // Si estamos creando una nueva persona
                Persona nuevaPersona = new Persona(nombre, apellido, edad);  // Crea una nueva persona
                if (!verificarExistePersona(win, nuevaPersona)) {
                    personas.add(nuevaPersona);  // Agrega la nueva persona a la lista
                } else {
                    return;  // Si ya existe, no hace nada
                }
            }

            limpiarCampos();  // Limpia los campos de texto
            Stage stage = (Stage) btnCancelar.getScene().getWindow();  // Cierra la ventana actual
            stage.close();
        } else {
            mostrarAlertError(win, errores);  // Muestra errores si los hay
        }
    }

    /**
     * Verifica si una persona ya existe en la lista.
     *
     * @param win La ventana sobre la que se mostrará la alerta.
     * @param p La persona que se va a verificar.
     * @return true si la persona ya existe en la lista, false de lo contrario.
     */
    private boolean verificarExistePersona(Window win, Persona p) {
        // Verificar si la persona ya existe en la lista
        if (personas.contains(p)) {
            mostrarAlertError(win, "Esa persona ya existe");  // Muestra alerta si la persona ya está en la lista
            limpiarCampos();  // Limpia los campos de texto
            return true; // Salir del método para evitar cerrar la ventana
        }
        return false;  // La persona no existe
    }

    /**
     * Muestra una alerta de error con un mensaje específico.
     *
     * @param win   La ventana sobre la que se mostrará la alerta.
     * @param error El mensaje de error a mostrar.
     */
    private void mostrarAlertError(Window win, String error) {
        mostrarAlert(win, Alert.AlertType.ERROR, "ERROR", error);  // Llama al método general para mostrar alerta de error
    }

    /**
     * Muestra una alerta de acuerdo al tipo, título y contenido especificados.
     *
     * @param win       La ventana sobre la que se mostrará la alerta.
     * @param alertType El tipo de alerta a mostrar.
     * @param title     El título de la alerta.
     * @param content   El contenido de la alerta.
     */
    private void mostrarAlert(Window win, Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);  // Crea una nueva alerta
        alert.initOwner(win);  // Establece la ventana principal
        alert.setHeaderText(null);  // Sin encabezado
        alert.setTitle(title);  // Establece el título de la alerta
        alert.setContentText(content);  // Establece el contenido de la alerta
        alert.showAndWait();  // Muestra la alerta y espera a que el usuario la cierre
    }

    /**
     * Devuelve el objeto Persona creado a partir de los datos ingresados.
     *
     * @return El objeto Persona.
     */
    public Persona getPersona() {
        return persona;  // Retorna el objeto Persona
    }

    /**
     * Verifica la validez de la información ingresada en los campos de texto.
     * Verifica que los campos no estén vacíos y que la edad sea un número válido.
     *
     * @return Una cadena con los errores encontrados, o una cadena vacía si no se encuentran errores.
     */
    private String verificarInfo() {
        StringBuilder errores = new StringBuilder();

        // Verifica si el campo Nombre está vacío
        if (txtNombre.getText().isEmpty()) {
            errores.append("El campo Nombre es obligatorio.\n");
        }

        // Verifica si el campo Apellido está vacío
        if (txtApellido.getText().isEmpty()) {
            errores.append("El campo Apellido es obligatorio.\n");
        }

        // Verifica si el campo Edad está vacío
        if (txtEdad.getText().isEmpty()) {
            errores.append("El campo Edad es obligatorio.\n");
        } else {
            // Verifica que la edad sea un número válido
            try {
                Integer.parseInt(txtEdad.getText());  // Intenta convertir el campo edad a un número
            } catch (NumberFormatException e) {
                errores.append("El campo Edad debe ser un número válido.\n");  // Agrega un error si no es un número válido
            }
        }

        return errores.toString();  // Retorna la cadena de errores
    }

    /**
     * Limpia los campos de texto del formulario.
     */
    private void limpiarCampos() {
        txtNombre.clear();  // Limpia el campo de nombre
        txtApellido.clear();  // Limpia el campo de apellido
        txtEdad.clear();  // Limpia el campo de edad
    }
}

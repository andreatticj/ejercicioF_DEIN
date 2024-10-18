package eu.andreatt.ejerciciof_dein.controller;

import eu.andreatt.ejerciciof_dein.model.Persona;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Controlador de la interfaz gráfica de la aplicación que gestiona una tabla de personas.
 * Este controlador permite agregar, modificar y eliminar personas en la tabla.
 */
public class HelloController {

    private static final Logger log = LoggerFactory.getLogger(HelloController.class);
    @FXML
    private Button btnAgregarPersona;  // Botón para agregar una nueva persona

    @FXML
    private Button btnEliminar;  // Botón para eliminar una persona seleccionada

    @FXML
    private Button btnModificar;  // Botón para modificar una persona seleccionada

    @FXML
    private Button btnExportar;

    @FXML
    private Button btnImportar;

    @FXML
    private TextField txtFiltro;

    @FXML
    private TableView<Persona> tabla;  // Tabla que muestra la lista de personas

    @FXML
    private TableColumn<Persona, String> colNombre;  // Columna para mostrar el nombre de la persona

    @FXML
    private TableColumn<Persona, String> colApellido;  // Columna para mostrar el apellido de la persona

    @FXML
    private TableColumn<Persona, Integer> colEdad;  // Columna para mostrar la edad de la persona

    private FileChooser fileChooser;

    /**
     * Inicializa las columnas de la tabla, estableciendo los valores de las propiedades de los objetos Persona.
     */
    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colApellido.setCellValueFactory(cellData -> cellData.getValue().apellidoProperty());
        colEdad.setCellValueFactory(cellData -> cellData.getValue().edadProperty().asObject());
    }

    /**
     * Evento que se dispara al hacer clic en el botón para agregar una nueva persona.
     * Abre una ventana modal para ingresar los datos de la nueva persona.
     *
     * @param event Evento de acción que ocurre al hacer clic en el botón
     */
    @FXML
    void agregarPersona(ActionEvent event) {
        ventanaModal("Nueva Persona", null);  // No pasa una persona ya que estamos creando una nueva
    }

    /**
     * Evento que se dispara al hacer clic en el botón para eliminar una persona seleccionada.
     *
     * @param event Evento de acción que ocurre al hacer clic en el botón
     */
    @FXML
    void eliminar(ActionEvent event) {
        Persona personaSeleccionada = tabla.getSelectionModel().getSelectedItem(); // Obtiene la persona seleccionada

        if (personaSeleccionada != null) {
            confirmarEliminacion(event, personaSeleccionada); // Confirma la eliminación de la persona
        } else {
            mostrarAlertError(((Button) event.getSource()).getScene().getWindow(), "Por favor, selecciona una persona para eliminar."); // Muestra error si no hay selección
        }
    }

    /**
     * Confirma la eliminación de la persona seleccionada.
     *
     * @param event             El evento que desencadena la acción.
     * @param personaSeleccionada La persona que se va a eliminar.
     */
    private void confirmarEliminacion(ActionEvent event, Persona personaSeleccionada) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // Crea una alerta de confirmación
        alert.setTitle("Confirmar eliminación"); // Título de la alerta
        alert.setHeaderText(null); // Sin encabezado
        alert.setContentText("¿Estás seguro de que deseas eliminar a " + personaSeleccionada.getNombre() + "?"); // Contenido de la alerta

        // Muestra la alerta y espera la respuesta
        if (alert.showAndWait().get() == ButtonType.OK) {
            tabla.getItems().remove(personaSeleccionada); // Elimina la persona de la lista
        }
    }

    /**
     * Muestra una alerta de error con un mensaje específico.
     *
     * @param win   La ventana sobre la que se mostrará la alerta.
     * @param error El mensaje de error a mostrar.
     */
    private void mostrarAlertError(Window win, String error) {
        mostrarAlert(win, Alert.AlertType.ERROR, "ERROR", error); // Llama al método general para mostrar alerta de error
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
        Alert alert = new Alert(alertType); // Crea una nueva alerta
        alert.initOwner(win); // Establece la ventana principal
        alert.setHeaderText(null); // Sin encabezado
        alert.setTitle(title); // Establece el título
        alert.setContentText(content); // Establece el contenido
        alert.showAndWait(); // Muestra la alerta y espera a que se cierre
    }

    /**
     * Evento que se dispara al hacer clic en el botón para modificar una persona seleccionada.
     * Abre una ventana modal para editar los datos de la persona seleccionada.
     *
     * @param event Evento de acción que ocurre al hacer clic en el botón
     */
    @FXML
    void modificar(ActionEvent event) {
        Persona personaSeleccionada = tabla.getSelectionModel().getSelectedItem(); // Obtiene la persona seleccionada

        if (personaSeleccionada != null) {
            ventanaModal("Editar Persona", personaSeleccionada); // Pasa la persona seleccionada al modal
        } else {
            mostrarAlertError(((Button) event.getSource()).getScene().getWindow(), "Por favor, selecciona una persona para modificar."); // Muestra error si no hay selección
        }
    }

    /**
     * Método que abre una ventana modal para agregar o modificar una persona.
     * La ventana se carga desde un archivo FXML, y se pasa la lista de personas
     * al controlador del modal para que pueda gestionar los datos.
     *
     * @param titulo El título de la ventana modal.
     * @param persona La persona a modificar, o null si se está agregando una nueva.
     */
    private void ventanaModal(String titulo, Persona persona) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/andreatt/ejerciciof_dein/fxml/modalF.fxml"));
            Parent root = loader.load();

            // Obtener el controlador del modal
            ModalEController modalController = loader.getController();

            // Pasar la lista de personas al controlador del modal
            modalController.setPersonas(tabla.getItems());

            if (persona != null) {
                modalController.cargarPersona(persona); // Cargar los datos de la persona en el modal
            }

            // Crear la nueva escena y la ventana modal
            Scene newScene = new Scene(root);
            Stage newStage = new Stage();
            newStage.initModality(Modality.APPLICATION_MODAL);  // Define la ventana como modal
            newStage.setResizable(false);  // No permite redimensionar la ventana
            newStage.setWidth(300);  // Establece el ancho de la ventana
            newStage.setHeight(200);  // Establece la altura de la ventana
            newStage.setScene(newScene);  // Asocia la escena con la ventana
            newStage.setTitle(titulo);  // Título de la ventana

            // Mostrar la ventana modal y esperar a que se cierre
            newStage.showAndWait();

            // Actualizar la tabla una vez que se cierra el modal
            tabla.refresh();

        } catch (IOException e) {
            // Muestra un mensaje de error si ocurre una excepción al cargar el modal
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void exportar(ActionEvent event) {
        Stage stage = new Stage();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Export File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.showSaveDialog(stage);
        String nombreGuardar = fileChooser.showSaveDialog(stage).getName();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(nombreGuardar));){

            w.write(tabla.getItems());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(nombreGuardar);


    }

    @FXML
    void importar(ActionEvent event) {
        Stage stage = new Stage();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File archSel = fileChooser.showOpenDialog(stage);
        if (archSel != null) {
            System.out.println("Archivo seleccionado: " + archSel.getAbsolutePath());
        }
    }


    @FXML
    void filtro(ActionEvent event) {

    }
}

package eu.andreatt.ejerciciof_dein.controller;

import eu.andreatt.ejerciciof_dein.model.Persona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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


import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador de la interfaz gráfica de la aplicación que gestiona una tabla de personas.
 * Este controlador permite agregar, modificar y eliminar personas en la tabla.
 */
public class HelloController {

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
    private ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();

    /**
     * Inicializa los elementos de la tabla, asignando los valores de las columnas correspondientes
     * a las propiedades de los objetos Persona. También activa el filtrado dinámico de personas
     * basado en la entrada del campo de texto.
     */
    @FXML
    public void initialize() {
        // Inicializa las columnas
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colApellido.setCellValueFactory(cellData -> cellData.getValue().apellidoProperty());
        colEdad.setCellValueFactory(cellData -> cellData.getValue().edadProperty().asObject());

        tabla.setItems(listaPersonas);

        // Filtrar personas
        txtFiltro.textProperty().addListener((observable, oldValue, newValue) -> filtrarPersonas(newValue));
    }

    /**
     * Filtra las personas de la lista en función del texto ingresado en el campo de búsqueda.
     * Si no se proporciona ningún filtro, se muestran todas las personas.
     *
     * @param filtro El texto utilizado para filtrar las personas.
     */
    private void filtrarPersonas(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tabla.setItems(listaPersonas);
        } else {
            List<Persona> filtrada = listaPersonas.stream()
                    .filter(p -> p.getNombre().toLowerCase().contains(filtro.toLowerCase()))
                    .collect(Collectors.toList());
            tabla.setItems(FXCollections.observableArrayList(filtrada));
        }
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
     * Acción que se ejecuta al hacer clic en el botón "Eliminar". Elimina la persona seleccionada
     * de la tabla después de confirmar la acción a través de una ventana emergente.
     *
     * @param event Evento que se dispara al hacer clic en el botón.
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
     * Confirma la eliminación de una persona mostrando una ventana de confirmación.
     * Si el usuario acepta, se elimina la persona seleccionada de la tabla.
     *
     * @param event Evento que dispara la acción.
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
     * Acción que se ejecuta al hacer clic en el botón "Modificar". Abre una ventana modal
     * para permitir la modificación de los datos de la persona seleccionada.
     *
     * @param event Evento que se dispara al hacer clic en el botón.
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

    /**
     * Acción que se ejecuta al hacer clic en el botón "Exportar". Permite al usuario exportar
     * los datos de las personas en la tabla a un archivo CSV.
     *
     * @param event Evento que se dispara al hacer clic en el botón.
     */
    @FXML
    void exportar(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar archivo CSV");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Mostrar el cuadro de diálogo de guardar
        File archivoGuardar = fileChooser.showSaveDialog(stage);

        if (archivoGuardar != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoGuardar))) {
                // Encabezado del archivo CSV
                writer.write("Nombre,Apellido,Edad\n");

                // Recorrer los items de la tabla y escribir cada persona en el archivo
                for (Persona persona : tabla.getItems()) {
                    writer.write(persona.getNombre() + "," + persona.getApellido() + "," + persona.getEdad() + "\n");
                }
            } catch (IOException e) {
                mostrarAlertError(stage, "Error al exportar archivo: " + e.getMessage());
            }
        }
    }

    /**
     * Acción que se ejecuta al hacer clic en el botón "Importar". Permite al usuario importar
     * datos de personas desde un archivo CSV y mostrarlos en la tabla.
     *
     * @param event Evento que se dispara al hacer clic en el botón.
     */
    @FXML
    void importar(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar archivo CSV");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File archivoSeleccionado = fileChooser.showOpenDialog(stage);
        if (archivoSeleccionado != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivoSeleccionado))) {
                String linea;
                tabla.getItems().clear(); // Limpiar la tabla antes de importar

                // Leer el archivo línea por línea (omitimos el encabezado)
                reader.readLine(); // Saltar la primera línea que contiene el encabezado

                while ((linea = reader.readLine()) != null) {
                    String[] datos = linea.split(","); // Separar los valores por coma
                    String nombre = datos[0];
                    String apellido = datos[1];
                    int edad = Integer.parseInt(datos[2]);

                    // Crear un nuevo objeto Persona y añadirlo a la tabla
                    Persona persona = new Persona(nombre, apellido, edad);
                    tabla.getItems().add(persona);
                }
            } catch (IOException e) {
                mostrarAlertError(stage, "Error al importar archivo: " + e.getMessage());
            }
        }
    }



}

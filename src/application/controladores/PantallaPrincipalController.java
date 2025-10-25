package application.controladores;

import java.awt.Toolkit;
import java.io.File;
import java.util.prefs.Preferences;

import application.Escriba;
import application.Util.Recursos;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class PantallaPrincipalController {

    @FXML private TextField txfRuta;
    @FXML private TextField txfRutaSalida;
    @FXML private TextField txfNombre;
    @FXML private TextField txfFormato;
    @FXML private TextField txfIdioma;

    @FXML private CheckBox chkAutoDetectar;
    @FXML private CheckBox chkSubCarpetas;
    @FXML private CheckBox chkComentarLineas;
    @FXML private CheckBox chkComentarClase;

    @FXML private Button btnOK;
    @FXML private Button btnElegirDirectorio;
    @FXML private Button btnElegirDirectorioSalida;

    @FXML private Label LblCompletarCampoRuta;
    @FXML private Label LblCompletarCampoRutaSalida;
    @FXML private Label LblCompletarCampoNombre;
    @FXML private Label LblCompletarCampoFormato;
    @FXML private Label LblCompletarCampoIdioma;

    @FXML private TextArea txaOutput;
    
    private String ruta, rutaSalida, nombre, formato, idioma;
    
    private Escriba escribaLocale = new Escriba();

    @FXML
    private void initialize() {
        ocultarErrores();

        btnOK.setOnAction(e -> validarCampos());
        btnElegirDirectorio.setOnAction(e -> elegirDirectorio(true));
        btnElegirDirectorioSalida.setOnAction(e -> elegirDirectorio(false));
    }

    private void elegirDirectorio(boolean esEntrada) {
        DirectoryChooser dc = new DirectoryChooser();
        Preferences prefs = Preferences.userNodeForPackage(PantallaPrincipalController.class);
        
        dc.setTitle(esEntrada ? "Seleccionar Proyecto - LibGDX" : "Seleccionar carpeta de salida");
        
        //Abre en el ultimo directorio usado
        String ultimaRuta = prefs.get(esEntrada ? "ultimaRutaEntrada" : "ultimaRutaSalida", null);
        if (ultimaRuta != null) {
            File carpetaAnterior = new File(ultimaRuta);
            if (carpetaAnterior.exists()) {
                dc.setInitialDirectory(carpetaAnterior);
            }
        }
        
        
        File dir = dc.showDialog(new Stage());

        if (dir != null) {
        	  prefs.put(esEntrada ? "ultimaRutaEntrada" : "ultimaRutaSalida", dir.getAbsolutePath());
            if (esEntrada) {
                Recursos.ruta = dir.getAbsolutePath();
                txfRuta.setText(Recursos.ruta);
                txfRutaSalida.setText(Recursos.ruta);
                Recursos.rutaSalida = Recursos.ruta+ "\\assets\\locale";
            } else {
                Recursos.rutaSalida = dir.getAbsolutePath();
                txfRutaSalida.setText(Recursos.rutaSalida);
            }
        }
    }

    private void validarCampos() {
        boolean camposValidos = true;

        if (txfRuta.getText().trim().isEmpty()) {
            LblCompletarCampoRuta.setVisible(true);
            camposValidos = false;
        } else {
            LblCompletarCampoRuta.setVisible(false);
        }

        if (txfRutaSalida.getText().trim().isEmpty()) {
            LblCompletarCampoRutaSalida.setVisible(true);
            camposValidos = false;
        } else {
            LblCompletarCampoRutaSalida.setVisible(false);
        }

        if (txfNombre.getText().trim().isEmpty()) {
            LblCompletarCampoNombre.setVisible(true);
            camposValidos = false;
        } else {
            LblCompletarCampoNombre.setVisible(false);
        }

        if (txfFormato.getText().trim().isEmpty() && !txfFormato.isDisabled()) {
            LblCompletarCampoFormato.setVisible(true);
            camposValidos = false;
        } else {
            LblCompletarCampoFormato.setVisible(false);
        }

        if (txfIdioma.getText().trim().isEmpty()) {
            LblCompletarCampoIdioma.setVisible(true);
            camposValidos = false;
        } else {
            LblCompletarCampoIdioma.setVisible(false);
        }

        if (camposValidos) {
            procesarDatos();
        }else {
        	Toolkit.getDefaultToolkit().beep();
        }
    }

    private void procesarDatos() {
        // Guardar los datos en Recursos
        Recursos.ruta = txfRuta.getText().trim();
        Recursos.rutaSalida = txfRutaSalida.getText().trim();
        Recursos.nombreBundle = txfNombre.getText().trim();
//        Recursos.formato = txfFormato.getText().trim();
        Recursos.idioma = (txfIdioma.getText().trim().startsWith("_")?txfIdioma.getText().trim():"_"+txfIdioma.getText().trim());
        
        System.out.println("Ruta entrada: " + Recursos.ruta);
        System.out.println("Ruta salida: " + Recursos.rutaSalida);
        System.out.println("Nombre del bundle: " + Recursos.nombreBundle);
//        System.out.println("Formato: " + Recursos.formato);
        System.out.println("Idioma: " + Recursos.idioma);
        System.out.println("Subcarpetas: " + chkSubCarpetas.isSelected());
        System.out.println("Auto detectar: " + chkAutoDetectar.isSelected());
        
        escribaLocale.procesarDirectorio(Recursos.ruta, txaOutput, chkComentarLineas.isSelected(), chkComentarClase.isSelected());

    }

    private void ocultarErrores() {
        LblCompletarCampoRuta.setVisible(false);
        LblCompletarCampoRutaSalida.setVisible(false);
        LblCompletarCampoNombre.setVisible(false);
        LblCompletarCampoFormato.setVisible(false);
        LblCompletarCampoIdioma.setVisible(false);
    }

    // Getters por si se necesitan
    public String getRuta() { return ruta; }
    public String getRutaSalida() { return rutaSalida; }
    public String getNombre() { return nombre; }
    public String getFormato() { return formato; }
    public String getIdioma() { return idioma; }
}

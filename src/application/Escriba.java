package application;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Util.Recursos;
import javafx.scene.control.TextArea;

public class Escriba {

    private ArrayList<String> listaNueva = new ArrayList<>();
    private ArrayList<String> listaVieja = new ArrayList<>();
    
    private TextArea out;
    private int lineasDeTraduccion = 0;
    private int archivosRevisados = 0;
    private int cantidadDirectorios = 0;
    

    public void procesarDirectorio(String rutaBase, TextArea output) {
    	this.out = output;
        if (rutaBase == null || rutaBase.trim().isEmpty()) {
            System.out.println("Ruta base invalida");
            return;
        }

        File dir = new File(rutaBase);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("La ruta no es valida: " + rutaBase);
            return;
        }
        

        recorrerCarpeta(dir);

        // Una vez procesados todos los archivos, escribir el bundle
        escribir();
    }

    private void recorrerCarpeta(File carpeta) {
        File[] archivos = carpeta.listFiles();
        if (archivos == null) return;

        for (File f : archivos) {
            if (f.isDirectory()) {
                recorrerCarpeta(f);
            } else if (f.isFile() && f.getName().endsWith(".java")) { // solo .java
                leerArchivo(f);

            }
        }
    }

    private void leerArchivo(File archivo) {
        System.out.println("Procesando: " + archivo.getAbsolutePath());
        out.appendText(archivo.getAbsolutePath());
        archivosRevisados++;
        leer(archivo);

    }

    private void leer(File f) {
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String linea;
            Pattern p = Pattern.compile("\"([^\"]*)\"");
            int numeroLinea = 0;
            boolean encabezadoEscrito = false;

            while ((linea = r.readLine()) != null) {
                numeroLinea++;
                if (linea.contains(Recursos.nombreBundle + ".get(")) {
                	lineasDeTraduccion++;
                    Matcher m = p.matcher(linea);
                    while (m.find()) {
                        String texto = m.group(1).trim();
                        if (!texto.isEmpty()) {
                            // Comentario con archivo y línea
                            if (!encabezadoEscrito) {
                                listaNueva.add("# Archivo: " + f.getName());
                                encabezadoEscrito = true;
                            }
                            listaNueva.add(texto + " = # Linea " + numeroLinea);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void escribir() {
        try {
            File carpetaSalida = new File(Recursos.rutaSalida + "/assets/locale");
            if (!carpetaSalida.exists()) carpetaSalida.mkdirs();

            File archivoSalida = new File(carpetaSalida, "locale" + Recursos.idioma + ".properties");

            // Leer archivo existente si existe
            if (archivoSalida.exists()) {
                try (BufferedReader r = new BufferedReader(new FileReader(archivoSalida))) {
                    String linea;
                    while ((linea = r.readLine()) != null) {
                        listaVieja.add(linea);
                    }
                }
            }

            ArrayList<String> listaFinal = new ArrayList<>(listaVieja); // Empezamos con lo viejo

            // Agregar solo líneas nuevas que no existan
            for (String nueva : listaNueva) {
                String clave = nueva.split(" =")[0]; // obtener clave antes del =
                boolean existe = false;
                for (String vieja : listaVieja) {
                    if (vieja.startsWith(clave + " =")) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) listaFinal.add(nueva);
            }

            // Escribir archivo final
            try (BufferedWriter w = new BufferedWriter(new FileWriter(archivoSalida))) {
                for (String f : listaFinal) {
                    w.write(f);
                    w.newLine();
                }
            }

            out.appendText("\n¡Exito!");
            out.appendText("\nRevisados " + archivosRevisados + " archivos en " + cantidadDirectorios + " directorios");
            out.appendText("\n¡Encontradas " + lineasDeTraduccion + " lineas de traduccion!");
            System.out.println("Archivo de propiedades generado en: " + archivoSalida.getAbsolutePath());

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(archivoSalida.getParentFile());
            }
            // Limpiar listas para siguiente ejecución
            listaNueva.clear();
            listaVieja.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

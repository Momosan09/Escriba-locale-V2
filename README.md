# Escriba Locale

Es una herramienta que busca facilitar la traduccion de videojueos hechos con [LibGDX](https://libgdx.com/).


# Metodo de uso

## Su codigo
  1. Cree una clase abstracta que contenga un objeto del tipo I18NBundle
  <img width="945" height="69" alt="manualCodigo1" src="https://github.com/user-attachments/assets/c2caf1d6-3d47-4a99-bbb8-ce853845aa84" />

  2. Utilize el bundle donde desea traducir, poniendo las claves
  
  <img width="808" height="666" alt="manualCodigo2" src="https://github.com/user-attachments/assets/37828577-46fa-4544-b740-0d62089725dd" />
  
  > Es recomendable crear un formato para sus claves **Ej:** `npc.misiones.viejo`

## Escriba locale
  1. Descargar el ejecutable

  2. Seleccionar el directorio principal del juego.
  <img width="656" height="488" alt="manualEscriba1" src="https://github.com/user-attachments/assets/b6606b93-3156-4d01-be28-3f0a1ec87b5e" />
  
  > El directorio principal es el que contiene a `core`, `desktop`, `android`, `...`
  
  > el directorio de salida es, por defecto, assets -> locale

  4. Especifique el nombre del objeto tipo I18NBundle
  <img width="637" height="768" alt="manualEscriba2" src="https://github.com/user-attachments/assets/677246bc-715d-451d-95a6-c9f2e3308b10" />   
     > Se refiere al objeto creado en la clase abstracta (ver primera imagen)
     > Note como tanto en el codigo como en el Escriba Locale, el nombre del objeto son iguales


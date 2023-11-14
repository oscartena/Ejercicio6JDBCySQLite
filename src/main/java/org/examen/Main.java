package org.examen;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path rutaBaseDatos = Path.of("src","main","resources","db","f12006sqlite.db");

        //OperacionesCRUDPilotos.mostrarClasificacionPiloto(rutaBaseDatos);
        OperacionesCRUDPilotos.mostrarClasificacionConstructores(rutaBaseDatos);

    }
}
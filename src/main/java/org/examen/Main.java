package org.examen;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Path rutaBaseDatos = Path.of("src","main","resources","db","f12006sqlite.db");
        LocalDate fechaNacimiento = LocalDate.of(1981, 7, 29);
        Piloto p = new Piloto("AHO", "Fernandito", "Ahonso", fechaNacimiento, "Spanish", "http://en.wikipedia.org/wiki/Fernando_Alonso");

        OperacionesCRUDPilotos.crearPiloto(p, rutaBaseDatos);

        p = OperacionesCRUDPilotos.leerPiloto(39, rutaBaseDatos);


        List<Piloto> pilotos = OperacionesCRUDPilotos.leerPilotos(rutaBaseDatos);
        pilotos.stream().forEach(System.out::println);

        p.setApellido("Diaz");
        OperacionesCRUDPilotos.actualizarPiloto(p, rutaBaseDatos);

        OperacionesCRUDPilotos.borrarPiloto(p, rutaBaseDatos);

        OperacionesCRUDPilotos.mostrarClasificacionPiloto(rutaBaseDatos);
        OperacionesCRUDPilotos.mostrarClasificacionConstructores(rutaBaseDatos);

    }
}
package org.examen;

import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OperacionesCRUDPilotos {
    public static void crearPiloto(Piloto piloto, Path rutaBaseDatos) {
        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + rutaBaseDatos.toString())) {
            String insertarSQl = "INSERT INTO drivers (code, forename, surname, dob, nationality, url) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insercion = conexion.prepareStatement(insertarSQl);
            insercion.setString(1, piloto.getCodigo());
            insercion.setString(2, piloto.getNombre());
            insercion.setString(3, piloto.getApellido());
            insercion.setString(4, piloto.getFechaNacimiento().toString());
            insercion.setString(5, piloto.getNacionalidad());
            insercion.setString(6, piloto.getUrlFoto());

            insercion.executeUpdate();
            insercion.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Piloto leerPiloto(int id, Path rutaBaseDatos) {
        Piloto p;
        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + rutaBaseDatos.toString())) {
            String consultaSQL = "SELECT * " +
                    "FROM drivers" +
                    " WHERE driverid = ?";
            PreparedStatement lectura = conexion.prepareStatement(consultaSQL);
            lectura.setInt(1, id);

            ResultSet resultado = lectura.executeQuery();

            p = new Piloto(resultado.getInt("driverid"), resultado.getString("code"), resultado.getString("forename"), resultado.getString("surname"), LocalDate.parse(resultado.getString("dob")), resultado.getString("nationality"), resultado.getString("url"));

            lectura.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    public static List<Piloto> leerPilotos(Path rutaBaseDatos) {
        List<Piloto> pilotos = new ArrayList<>();
        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + rutaBaseDatos.toString())) {
            String consultaSQL = "SELECT * FROM drivers";
            PreparedStatement consulta = conexion.prepareStatement(consultaSQL);
            ResultSet resultado = consulta.executeQuery();

            while (resultado.next()) {
                Piloto p = new Piloto(resultado.getInt("driverid"), resultado.getString("code"), resultado.getString("forename"), resultado.getString("surname"), LocalDate.parse(resultado.getString("dob")), resultado.getString("nationality"), resultado.getString("url"));
                pilotos.add(p);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pilotos;
    }

    public static void actualizarPiloto(Piloto p, Path rutaBaseDatos) {
        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + rutaBaseDatos.toString())) {
            String updateSQL = "UPDATE drivers SET code=?, forename=?, surname=?, dob=?, nationality=?, url=? WHERE driverid=?";
            PreparedStatement actualizar = conexion.prepareStatement(updateSQL);
            actualizar.setString(1, p.getCodigo());
            actualizar.setString(2, p.getNombre());
            actualizar.setString(3, p.getApellido());
            actualizar.setString(4, p.getFechaNacimiento().toString());
            actualizar.setString(5, p.getNacionalidad());
            actualizar.setString(6, p.getUrlFoto());
            actualizar.setInt(7, p.getId());

            int filasActualizadas = actualizar.executeUpdate();
            if (filasActualizadas == 1) {
                System.out.println("Piloto actualizado");
            } else {
                System.out.println("No se ha podido actualizar el piloto");
            }
            actualizar.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void borrarPiloto(Piloto p, Path rutaBaseDatos) {
        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + rutaBaseDatos.toString())) {
            String borrarSQL = "DELETE FROM drivers WHERE driverid=?";
            PreparedStatement borrar = conexion.prepareStatement(borrarSQL);
            borrar.setInt(1, p.getId());
            borrar.executeUpdate();
            borrar.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void mostrarClasificacionPiloto(Path rutaBaseDatos) {
        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + rutaBaseDatos.toString())) {
            String consultaSQl = "SELECT drivers.forename, drivers.surname, SUM(results.points) AS puntos " +
                    "FROM drivers " +
                    "INNER JOIN results ON drivers.driverid = results.driverid " +
                    "GROUP BY drivers.driverid " +
                    "ORDER BY puntos DESC";
            PreparedStatement consulta = conexion.prepareStatement(consultaSQl);
            ResultSet resultado = consulta.executeQuery();
            System.out.println("Clasificación de pilotos: ");
            while (resultado.next()){
                System.out.println(resultado.getString("forename") + " " + resultado.getString("surname") + " " + resultado.getInt("puntos"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void mostrarClasificacionConstructores(Path rutaBaseDatos){
        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + rutaBaseDatos.toString())) {
            String consultaSQl = "SELECT constructors.name, SUM(results.points) AS puntos " +
                    "FROM constructors " +
                    "INNER JOIN drivers ON constructors.constructorid = drivers.constructorid " +
                    "INNER JOIN results ON drivers.driverid = results.driverid " +
                    "GROUP BY constructors.name " +
                    "ORDER BY puntos DESC";

            PreparedStatement consulta = conexion.prepareStatement(consultaSQl);
            ResultSet resultado = consulta.executeQuery();
            System.out.println("Clasificación de escuderias: ");
            while (resultado.next()){
                System.out.println(resultado.getString("name") + " " + resultado.getInt("puntos"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
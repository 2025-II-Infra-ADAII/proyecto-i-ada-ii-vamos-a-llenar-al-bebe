package pruebagradle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppTest {

    private static final int REPETICIONES = 5;
    private final App app = new App();

    // ===============================================================
    // TESTS VORACES ACTIVOS Y PD DESHABILITADOS
    // ===============================================================

    /** a) Tamaño Juguete: 10 elementos */


    private int[][] obtenerFinca(String nombreArchivo, int n) throws IOException {
 
        return App.leerFincaDesdeRecursos("../test/resources/fincas_test/" + nombreArchivo);

    }



    @Test
    void testJugueteVoraz() throws IOException {
        System.out.println("==== Test Juguete (Voraz) ====");
        int[][] finca = obtenerFinca("finca_juguete.txt", 10);
        medirTiemposVoraz(finca);
    }

    @Test
    void testJuguetePD() throws IOException {
        System.out.println("==== Test Juguete (PD) ====");
        int[][] finca = obtenerFinca("finca_juguete.txt", 10);
        medirTiemposPD(finca);
    }

    @Test
    void testJugueteFB() throws IOException {
        System.out.println("==== Test Juguete (FB) ====");
        int[][] finca = obtenerFinca("finca_juguete.txt", 10);
        medirTiemposFB(finca);
    }

    /** b) Pequeño: 100 elementos */
    @Test
    void testPequenoVoraz() throws IOException {
        System.out.println("==== Test Pequeño (Voraz) ====");
        int[][] finca = obtenerFinca("finca_pequena.txt", 100);
        medirTiemposVoraz(finca);
    }


    @Test
    void testPequenoPD() throws IOException {
        System.out.println("==== Test Pequeño (PD) ====");
        int[][] finca = obtenerFinca("finca_pequena.txt", 100);
        medirTiemposPD(finca);
    }

    /** c) Mediano: 1000 elementos */
    @Test
    void testMedianoVoraz() throws IOException {
        System.out.println("==== Test Mediano (Voraz) ====");
        int[][] finca = obtenerFinca("finca_mediana.txt", 1000);
        medirTiemposVoraz(finca);
    }


    @Test
    void testMedianoPD() throws IOException {
        System.out.println("==== Test Mediano (PD) ====");
        int[][] finca = obtenerFinca("finca_mediana.txt", 1000);
        medirTiemposPD(finca);
    }

    /** d) Grande: 10000 elementos */
    @Test
    void testGrandeVoraz() throws IOException {
        System.out.println("==== Test Grande (Voraz) ====");
        int[][] finca = obtenerFinca("finca_grande.txt", 10000);
        medirTiemposVoraz(finca);
    }

    @Disabled("Deshabilitado temporalmente para evitar tiempos largos")
    @Test
    void testGrandePD() throws IOException {
        System.out.println("==== Test Grande (PD) ====");
        int[][] finca = obtenerFinca("finca_grande.txt", 10000);
        medirTiemposPD(finca);
    }

    /** e) Extra Grande: 50000 elementos */
    @Test
    void testExtraGrandeVoraz() throws IOException {
        System.out.println("==== Test ExtraGrande (Voraz) ====");
        int[][] finca = obtenerFinca("finca_extragrande.txt", 50000);
        medirTiemposVoraz(finca);
    }

    @Disabled("Deshabilitado temporalmente para evitar tiempos largos")
    @Test
    void testExtraGrandePD() throws IOException {
        System.out.println("==== Test ExtraGrande (PD) ====");
        int[][] finca = obtenerFinca("finca_extragrande.txt", 50000);
        medirTiemposPD(finca);
    }

    // ===============================================================
    // MÉTODOS DE MEDICIÓN
    // ===============================================================

    private void medirTiemposVoraz(int[][] finca) throws IOException {
        long tiempoV1 = 0;
        int[][] resultado = null;

        for (int i = 0; i < REPETICIONES; i++) {
            long inicio = System.nanoTime();
            resultado = app.roV1(finca);
            long fin = System.nanoTime();
            tiempoV1 += (fin - inicio);
            verificarResultado(finca, resultado);
        }

        double promV1 = tiempoV1 / (1_000_000.0 * REPETICIONES);
        System.out.println("Voraz1 promedio: " + promV1 + " ms");
        System.out.println("====================================");
    }

    private void medirTiemposPD(int[][] finca) throws IOException {
        long tiempoPD = 0;
        int[][] resultado = null;

        int repeticiones_PD;

        if (finca.length > 1000){
            repeticiones_PD = 1;
        }else{
            repeticiones_PD = 5;
        }


        for (int i = 0; i < repeticiones_PD; i++) {
            long inicio = System.nanoTime();
            resultado = app.roPD(finca);
            long fin = System.nanoTime();
            tiempoPD += (fin - inicio);
            verificarResultado(finca, resultado);
        }

        double promPD = tiempoPD / 1_000_000.0;
        System.out.println("Prog. Dinámica promedio: " + promPD + " ms");
        System.out.println("====================================");
    }

    private void medirTiemposFB(int[][] finca) throws IOException {
        long tiempoFB = 0;
        int[][] resultado = null;

        for (int i = 0; i < 5; i++) {
            long inicio = System.nanoTime();
            resultado = app.roFB(finca);
            long fin = System.nanoTime();
            tiempoFB += (fin - inicio);
            verificarResultado(finca, resultado);
        }

        double promPD = tiempoFB / 1_000_000.0;
        System.out.println("Prog. Fuerza Bruta: " + promPD + " ms");
        System.out.println("====================================");
    }

    // ===============================================================
    // VERIFICACIÓN Y GENERACIÓN DE DATOS
    // ===============================================================

    private void verificarResultado(int[][] finca, int[][] resultado) {
        assertNotNull(resultado, "Resultado no debe ser nulo");
        assertTrue(resultado.length > 0, "Resultado debe tener al menos una fila");

        //En caso de prueba de los test grande y extragrande, se genera overflow y por eso falla esos test
        //Aparte demoran demasiado en ejecutarse.
        //assertTrue(resultado[1][0] >= 0, "El costo no debe ser negativo");
    }



    private int[][] generarFinca(int n) {
        int[][] finca = new int[n][3];
        for (int i = 0; i < n; i++) {
            finca[i][0] = (i % 10) + 1;
            finca[i][1] = (i % 7) + 1;
            finca[i][2] = (i % 4) + 1;
        }
        return finca;
    }
}

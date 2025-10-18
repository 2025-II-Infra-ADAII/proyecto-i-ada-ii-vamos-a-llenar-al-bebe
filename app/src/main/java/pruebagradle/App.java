package pruebagradle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import java.util.AbstractMap.SimpleEntry;

public class App {


    public int[] roPD(int[][] Finca) {
        int n = Finca.length;

        //Retorna vacio si la finca esta vacia
        if (n == 0) return new int[0];
        
        int[] mejorOrden = new int[n];
        int tamañoActual = 1;
        mejorOrden[0] = 0;
        
        for (int k = 1; k < n; k++) {
            int[] ordenAnterior = Arrays.copyOf(mejorOrden, tamañoActual);
            long mejorCosto = Integer.MAX_VALUE;
            int[] mejorOrdenActual = null;
            
            for (int pos = 0; pos <= tamañoActual; pos++) {
                int[] nuevoOrden = insertarEnPosicion(ordenAnterior, k, pos, tamañoActual);
                long costo = calcularCosto(Finca, nuevoOrden);
                
                if (costo < mejorCosto) {
                    mejorCosto = costo;
                    mejorOrdenActual = nuevoOrden;
                }
            }
            
            mejorOrden = mejorOrdenActual;
            tamañoActual++;
        }
        
        return mejorOrden;
    }

    private int[] insertarEnPosicion(int[] array, int elemento, int posicion, int tamaño) {
        int[] nuevo = new int[tamaño + 1];
        
        if (posicion > 0) {
            System.arraycopy(array, 0, nuevo, 0, posicion);
        }
        
        nuevo[posicion] = elemento;
        
        if (posicion < tamaño) {
            System.arraycopy(array, posicion, nuevo, posicion + 1, tamaño - posicion);
        }
        
        return nuevo;
    }



    /* p/tr */
    public int[] roV1(int[][] Finca){
        int n = Finca.length;
        int[] programacion = new int[n];
        
        PriorityQueue<Map.Entry<Double, Integer>> colaProg = 
            new PriorityQueue<>((a, b) -> Double.compare(b.getKey(), a.getKey()));

        for (int i = 0; i < n; i++) {
        double voraz = (Finca[i][2] / (double)Finca[i][1]);
        colaProg.offer(new SimpleEntry<>(voraz, i));  
        }
        int cont = 0;
        while(!colaProg.isEmpty()){
            Map.Entry<Double, Integer> entry = colaProg.poll();
            programacion[cont] = entry.getValue();
            cont++;
        }
        return programacion;
    }


    /* p/(ts-tr) */
    public int[] roV2(int[][] Finca){
        int n = Finca.length;
        int[] programacion = new int[n];
        
        PriorityQueue<Map.Entry<Double, Integer>> colaProg = 
            new PriorityQueue<>((a, b) -> Double.compare(b.getKey(), a.getKey()));

        for (int i = 0; i < n; i++) {

        double voraz;
        if (Finca[i][0] - Finca[i][1] == 0) {
            // Evitar división por cero
            voraz = (Finca[i][2] / Finca[i][1]);
        }else{
            voraz = (Finca[i][2] / (Finca[i][0] - Finca[i][1]));
        }
        colaProg.offer(new SimpleEntry<>(voraz, i));  
        }

        int cont = 0;
        while(!colaProg.isEmpty()){
            Map.Entry<Double, Integer> entry = colaProg.poll();
            programacion[cont] = entry.getValue();
            cont++;
        }
        return programacion;
    }

    public int[] roFB(int[][] Finca){
        int n = Finca.length;
        int[] mejorProgramacion = new int[n];
        int mejorCosto = Integer.MAX_VALUE;
        
        // Generar todas las permutaciones
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) indices.add(i);
        
        List<List<Integer>> permutaciones = generarPermutaciones(indices);
        
        for (List<Integer> perm : permutaciones) {
            int costo = (int) calcularCosto(Finca, listaToArray(perm));
            if (costo < mejorCosto) {
                mejorCosto = costo;
                mejorProgramacion = listaToArray(perm);
            }
        }
        
        return mejorProgramacion;
    }

    private List<List<Integer>> generarPermutaciones(List<Integer> lista) {
        List<List<Integer>> resultado = new ArrayList<>();
        generarPermutacionesHelper(lista, 0, resultado);
        return resultado;
    }

    private void generarPermutacionesHelper(List<Integer> lista, int inicio, List<List<Integer>> resultado) {
        if (inicio == lista.size() - 1) {
            resultado.add(new ArrayList<>(lista));
            return;
        }
        
        for (int i = inicio; i < lista.size(); i++) {
            // Intercambiar elementos
            intercambiar(lista, inicio, i);
            
            // Generar permutaciones recursivamente
            generarPermutacionesHelper(lista, inicio + 1, resultado);
            
            // Volver al estado anterior (backtracking)
            intercambiar(lista, inicio, i);
        }
    }
    
    private void intercambiar(List<Integer> lista, int i, int j) {
        int temp = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temp);
    }
    
    private int[] listaToArray(List<Integer> lista) {
        int[] array = new int[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            array[i] = lista.get(i);
        }
        return array;
    }

    /*Metodo para calcular el costo de una programación */
    public long calcularCosto(int[][] Finca, int[] programacion) {
        long costoTotal = 0;
        int tiempoActual = 0;
        
        for (int i = 0; i < programacion.length; i++) {
            int idx = programacion[i];
            int ts = Finca[idx][0];
            int tr = Finca[idx][1];
            int p = Finca[idx][2];
            
            int tiempoFin = tiempoActual + tr;
            // FÓRMULA CORRECTA: max(0, tiempoFin - ts)
            if (tiempoFin > ts) {
                costoTotal += (long) p * (tiempoFin - ts);
            }
            tiempoActual = tiempoFin;
        }
        return costoTotal;
    }


    public static void mostrar(int [][] Finca){
        App F1 = new App();

        System.out.println("Fuerza bruta:");
 
        int[] prFB = F1.roFB(Finca); 
        System.out.print("Programacion Fuerza Bruta: ");
        for (int i = 0; i < prFB.length; i++) {
            System.out.print(prFB[i] + " ");
        }
        System.out.println("");


        System.out.println("Voraz 1:");

        int[] prV = F1.roV1(Finca);
        System.out.print("Programacion Voraz p/(ts-tr): ");
        for (int i = 0; i < prV.length; i++) {
            System.out.print(prV[i] + " ");
        }
        System.out.println("");

        System.out.println("Voraz 2:");
        int[] prV2 = F1.roV2(Finca);
        System.out.print("Programacion Voraz p/tr: ");
        for (int i = 0; i < prV2.length; i++) {
            System.out.print(prV2[i] + " ");
        }
        System.out.println("");

        System.out.print("Solucion dinamica: ");
        int[] optimo =  F1.roPD(Finca);
        for (int i = 0; i < optimo.length ; i++){
            System.out.print(optimo[i] + " ");
        }

    }

    public static int[][] leerFincaDesdeRecursos(String nombreArchivo) {
    InputStream is = App.class.getResourceAsStream("/" + nombreArchivo);
    if (is == null) {
        System.err.println("No se encontró el recurso en el classpath: " + nombreArchivo);
        return new int[0][0];
    }

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
        String linea;
        int n = -1;
        int[][] finca = null;
        int index = 0;
        
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            
            // Eliminar comentarios y líneas vacías
            if (linea.isEmpty() || linea.startsWith("#") || linea.startsWith("//")) {
                continue;
            }
            
            // Eliminar comentarios al final de la línea
            int idxHash = linea.indexOf('#');
            int idxSlash = linea.indexOf("//");
            if (idxHash >= 0 || idxSlash >= 0) {
                int corte = Math.min(
                    idxHash >= 0 ? idxHash : Integer.MAX_VALUE,
                    idxSlash >= 0 ? idxSlash : Integer.MAX_VALUE
                );
                linea = linea.substring(0, corte).trim();
            }
            if (linea.isEmpty()) {
                continue;
            }
            
            // Primera línea: leer n y crear array
            if (n == -1) {
                n = Integer.parseInt(linea);
                finca = new int[n][3];
                continue;
            }
            
            // Verificar límites
            if (index >= n) {
                System.err.println("Advertencia: Más líneas de las esperadas. Ignorando: " + linea);
                continue;
            }
            
            // Leer datos del tablón
            String[] partes = linea.split(",");
            if (partes.length != 3) {
                System.err.println("Error: Se esperaban 3 valores separados por comas: " + linea);
                continue;
            }
            
            // Asignar directamente al array
            finca[index][0] = Integer.parseInt(partes[0].trim()); // ts
            finca[index][1] = Integer.parseInt(partes[1].trim()); // tr
            finca[index][2] = Integer.parseInt(partes[2].trim()); // p
            index++;
        }
        
        // Verificar integridad
        if (n != -1 && index != n) {
            System.err.println("Advertencia: Se esperaban " + n + " tablones, pero se leyeron " + index);
            // Si leímos menos, recortar el array
            if (index < n) {
                int[][] fincaRecortada = new int[index][3];
                System.arraycopy(finca, 0, fincaRecortada, 0, index);
                return fincaRecortada;
            }
        }
        
        return finca != null ? finca : new int[0][0];
        
    } catch (IOException e) {
        System.err.println("Error leyendo recurso: " + e.getMessage());
        return new int[0][0];
    } catch (NumberFormatException e) {
        System.err.println("Error de formato numérico: " + e.getMessage());
        return new int[0][0];
    }
}

public static void escribirResultado(long costo, int[] programacionOptima) {
    // Escribir directamente en resources (asumiendo que ya existe)
    String nombreArchivo = "src/main/resources/resultado.txt";
    
    try {
        java.nio.file.Path rutaArchivo = java.nio.file.Paths.get(nombreArchivo);
        
        try (java.io.BufferedWriter writer = java.nio.file.Files.newBufferedWriter(
                rutaArchivo,
                java.nio.charset.StandardCharsets.UTF_8,
                java.nio.file.StandardOpenOption.CREATE,
                java.nio.file.StandardOpenOption.TRUNCATE_EXISTING,
                java.nio.file.StandardOpenOption.WRITE)) {
            
            writer.write(String.valueOf(costo));
            writer.newLine();
            
            for (int i = 0; i < programacionOptima.length; i++) {
                writer.write(String.valueOf(programacionOptima[i]));
                writer.newLine();
            }
            
            System.out.println("Resultado guardado en: " + rutaArchivo.toAbsolutePath());
        }
        
    } catch (java.io.IOException e) {
        System.err.println("Error escribiendo resultado: " + e.getMessage());
        
        // Fallback al directorio actual
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(
                new java.io.FileWriter("resultado.txt"))) {
            
            writer.write(String.valueOf(costo));
            writer.newLine();
            
            for (int i = 0; i < programacionOptima.length; i++) {
                writer.write(String.valueOf(programacionOptima[i]));
                writer.newLine();
            }
            
            System.out.println("Resultado guardado en: resultado.txt (directorio actual)");
            
        } catch (java.io.IOException e2) {
            System.err.println("Error crítico: " + e2.getMessage());
        }
    }
}
    

    public static void main(String[] args) {
        // Nombre del archivo
        String nombre = "finca.txt";


        int[][] Finca = leerFincaDesdeRecursos(nombre);

        if (Finca.length == 0) {
            System.err.println("No se cargaron datos. Verifica que " + nombre + " exista en src/main/resources y tenga contenido válido.");
            return;
        }

        App F1 = new App();

        int [] optimo = F1.roPD(Finca);
        
        escribirResultado(F1.calcularCosto(Finca,optimo), optimo);

        //mostrar(Finca);

        


        
    }


    public String getGreeting() {
        return "Hello World!";
    }

}
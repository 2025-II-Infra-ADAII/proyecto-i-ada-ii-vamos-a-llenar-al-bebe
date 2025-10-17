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


    /* p/tr */
    public int[] programacionVoraz1(int[][] Finca){
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
    public int[] programacionVoraz2(int[][] Finca){
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

    public int[] programacionFuerzaBruta(int[][] Finca){
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
    public long calcularCosto(int[][] Finca, int[] programacion){
        int costoTotal = 0;
        int tiempoRiego = 0;

        for (int i = 0; i < programacion.length; i++){

            int tablon = programacion[i];
            /* Cada tablon es el valor de i */
            long ts = Finca[tablon][0];
            long tr = Finca[tablon][1];
            long pi = Finca[tablon][2];

            /* Calculamos la penalizacion */
            long penalizacion = (tiempoRiego - ts + tr);

            /* Si la penalizacion es mayor que 0 se le agrega al costo total */
            if ( penalizacion > 0){
                costoTotal += penalizacion * pi;
            }else{
                costoTotal += 0;
            }
            /* Actualizamos el tiempo de riego */
            tiempoRiego += tr;
        }
        return costoTotal;
    }


    public static void mostrar(int [][] Finca){
        App F1 = new App();

        System.out.println("Fuerza bruta:");
 
        int[] prFB = F1.programacionFuerzaBruta(Finca); 
        System.out.print("Programacion Fuerza Bruta: ");
        for (int i = 0; i < prFB.length; i++) {
            System.out.print(prFB[i] + " ");
        }
        System.out.println("");


        System.out.println("Voraz 1:");

        int[] prV = F1.programacionVoraz1(Finca);
        System.out.print("Programacion Voraz p/(ts-tr): ");
        for (int i = 0; i < prV.length; i++) {
            System.out.print(prV[i] + " ");
        }
        System.out.println("");

        System.out.println("Voraz 2:");
        int[] prV2 = F1.programacionVoraz2(Finca);
        System.out.print("Programacion Voraz p/tr: ");
        for (int i = 0; i < prV2.length; i++) {
            System.out.print(prV2[i] + " ");
        }
        System.out.println("");

    }

    public static int[][] leerFincaDesdeRecursos(String nombreArchivo) {
        ArrayList<int[]> listaFinca = new ArrayList<>();

        
        InputStream is = App.class.getResourceAsStream("/" + nombreArchivo);
        if (is == null) {
            System.err.println("No se encontró el recurso en el classpath: " + nombreArchivo);
            return new int[0][0]; 
        }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    linea = linea.trim();
                    // Eliminar comentarios y líneas vacías
                    if (linea.isEmpty() || linea.startsWith("#") || linea.startsWith("//")) continue;

                    
                    int idxHash = linea.indexOf('#');
                    int idxSlash = linea.indexOf("//");
                    int corte = -1;
                    if (idxHash >= 0 && idxSlash >= 0) corte = Math.min(idxHash, idxSlash);
                    else if (idxHash >= 0) corte = idxHash;
                    else if (idxSlash >= 0) corte = idxSlash;

                    if (corte >= 0) linea = linea.substring(0, corte).trim();
                    if (linea.isEmpty()) continue;

                    String[] partes = linea.split("\\s+");
                    int[] fila = new int[partes.length];
                    for (int i = 0; i < partes.length; i++) {
                        fila[i] = Integer.parseInt(partes[i]);
                    }
                    listaFinca.add(fila);
                }
            } catch (IOException e) {
                System.err.println("Error leyendo recurso: " + e.getMessage());
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.err.println("Formato inválido en el archivo: " + e.getMessage());
                e.printStackTrace();
            }

            // Convertir ArrayList<int[]> a int[][]
            int[][] finca = new int[listaFinca.size()][];
            for (int i = 0; i < listaFinca.size(); i++) {
                finca[i] = listaFinca.get(i);
            }
            return finca;
    }

       
    

    public static void main(String[] args) {
        // Nombre del archivo
        String nombre = "finca.txt";

        int[][] Finca = leerFincaDesdeRecursos(nombre);

        if (Finca.length == 0) {
            System.err.println("No se cargaron datos. Verifica que " + nombre + " exista en src/main/resources y tenga contenido válido.");
            return;
        }
        System.out.println(Arrays.deepToString(Finca));

        mostrar(Finca);
    }


    public String getGreeting() {
        return "Hello World!";
    }

}
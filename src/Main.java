

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import java.util.AbstractMap.SimpleEntry;

public class Main {


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
        Main F1 = new Main ();

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


    

    public static void main(String[] args) {

        /*Finca = ⟨⟨10, 3, 4⟩, ⟨5, 3, 3⟩, ⟨2, 2, 1⟩, ⟨8, 1, 1⟩, ⟨6, 4, 2⟩⟩. */
        int [][] Finca = {
                {10, 3, 4},
                {5, 3, 3},
                {2, 2, 1},
                {8, 1, 1},
                {6, 4, 2}
        };
         
        mostrar(Finca);
        
        /* 
        int [] programacion = {1,4,2,0,3};
        P_Riego F1 = new P_Riego();
        System.out.println("Costo programacion dada: " + F1.calcularCosto(Finca, programacion));
        */
        



        
    }
}
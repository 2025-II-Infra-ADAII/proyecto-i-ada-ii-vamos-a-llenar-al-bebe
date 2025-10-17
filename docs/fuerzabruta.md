## Usando fuerza bruta

Para este caso se generan todas las permutaciones posibles y calcula $CRF^{\Pi}$ de cada una.

### Código

```java
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
	System.out.println("Menor costo encontrado: " + mejorCosto);
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
```


### Complejidad

La complejidad de **programacionFuerzaBruta** es: $T(n) = O(n! * n)$

Dado que:

```java
public int[] programacionFuerzaBruta(int[][] Finca){
	int n = Finca.length;
	int[] mejorProgramacion = new int[n];
	int mejorCosto = Integer.MAX_VALUE;
	
	List<Integer> indices = new ArrayList<>();
	
	
	for (int i = 0; i < n; i++) indices.add(i); // O(n)
	
	// Genera n! de permutaciones
	List<List<Integer>> permutaciones = generarPermutaciones(indices);
	
	
	// Por cada permutacion itera n que es la cantidad de tablones,verifica el costo de esta programación 
	for (List<Integer> perm : permutaciones) {
		int costo = (int) calcularCosto(Finca, listaToArray(perm));
		if (costo < mejorCosto) {
			mejorCosto = costo;
			mejorProgramacion = listaToArray(perm);
		}
	}
	System.out.println("Menor costo encontrado: " + mejorCosto);
	return mejorProgramacion;
}
```

Para empezar a determinar la complejidad temporal del método **programacionFuerzaBruta()**, esta determinada por la función  **generarPermutaciones()** que llamaremos $g(n)$ a esta complejidad se aplica un bucle **for** que evalúa el costo de cada permutación, este valor esta asociado a la función **calcularCosto()** que llamaremos $c(n)$. Entonces el valor de la complejidad es $O(g(n) * c(n))$

### $g(n) = generarPermutaciones()$

```java
private List<List<Integer>> generarPermutaciones(List<Integer> lista) {
        List<List<Integer>> resultado = new ArrayList<>();
	//La complegidad de g(n) depende entonces de generarPermutacionesHelper()
	generarPermutacionesHelper(lista, 0, resultado);
	return resultado;
}
  
private void generarPermutacionesHelper(List<Integer> lista, int inicio, List<List<Integer>> resultado) {
		
		// Guarda la permutacion final
		// EL final esta determinado por el tamaño de la lista
   if (inicio == lista.size() - 1) {
		resultado.add(new ArrayList<>(lista));
		return;
	}
	
	// Este bucle se genera desde i que empieza en 0 hasta n
	// Cada iteracion llama de nuevo a la funcion con un valor de i+1, que es lo mismo que (n-1), asi hasta llegar a n = 1.
	for (int i = inicio; i < lista.size(); i++) {
		intercambiar(lista, inicio, i);
		generarPermutacionesHelper(lista, inicio + 1, resultado);
		intercambiar(lista, inicio, i);
	}
}
private void intercambiar(List<Integer> lista, int i, int j) {
	int temp = lista.get(i);
	lista.set(i, lista.get(j));
	lista.set(j, temp);
}
```

El valor de $g(n)$ depende de la función **generarPermutacionesHelper()**, el valor de esta función esta determinado por un ciclo **for** que itera desde i = 0 hasta el valor de n, pero cada iteración de este ciclo llama recursivamente al metodo **generarPermutacionesHelper()** adicionando 1 al valor inicial de iteración siendo así el primer sub llamado desde i = 1 hasta n; que es lo mismo que iterar desde i=0 hasta (n - 1).

Terminando en una sucesión de llamados de bucles con valor de:
$$
n * (n-1) * (n-2) * \dots * (3) * (2) * (1) = n!
$$
Por lo que el valor de $g(n) = O(n!)$

### $C(n) = generarPermutaciones()$

```java
/*Metodo para calcular el costo de una programación */

 public long calcularCosto(int[][] Finca, int[] programacion){
	int costoTotal = 0;
	int tiempoRiego = 0;

	// Se itera el bucle for desde i = 0 hasta n   O(n)
	for (int i = 0; i < programacion.length; i++){

		int tablon = programacion[i];
		long ts = Finca[tablon][0];
		long tr = Finca[tablon][1];
		long pi = Finca[tablon][2];

		long penalizacion = (tiempoRiego - ts + tr);

		if ( penalizacion > 0){
			costoTotal += penalizacion * pi;
		}else{
			costoTotal += 0;
		}
		tiempoRiego += tr;
	}
	return costoTotal;

}
```

La complejidad esta determinada por el bucle **for** que itera desde i = 0 hasta n. Dando así una complejidad de $O(n)$.

Por lo que el valor de $c(n) = O(n)$

### $O(g(n) * c(n))$

Entonces la complejidad de la función principal, que es la que determina el costo es:
$$
T(n) = O(n! * n)
$$

### Corrección





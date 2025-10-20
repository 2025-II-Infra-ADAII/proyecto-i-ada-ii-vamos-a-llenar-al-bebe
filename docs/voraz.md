## Usando un algoritmo voraz 

### Describiendo el algoritmo 

#### Solución voraz 1

Para la definición de este algoritmo voraz se tomaron en cuenta 2 conceptos de optimización, el primero se llama **Minimización de tiempo de flujo** y el segundo **optimización por prioridad**.

 **Minimización de tiempo de flujo**: Este concepto se basa darle mayor prioridad a tiempos de espera bajos, para no afectar a los demás componentes que dependen del tiempo utilizado por los anteriores. Para esto vamos a colocar el  $tr_i$ en el denominador, de esta forma al tener $tr_i$ valores mayores, su prioridad disminuye, quedando la ecuación $1/tr_i$ 

**Optimización por prioridad**: Teniendo en cuenta que se tienen 3 variables clave para definir la solución voraz $(ts_i, tr_i, p_i)$, se adiciona a la ecuación anterior la prioridad $p_i$, de esta forma al la prioridad tomar valores más altos, se le dé más prioridad a los tablones $T$. Quedando de esta forma:

$$\frac{p_i}{tr_i}$$

```java
/* p/tr */
    public int[][] roV1(int[][] Finca){
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

        long costo = calcularCosto(Finca, programacion);
        return new int[][]{programacion, new int[]{(int)costo}};
    }
```

#### Solución voraz 2
**Solución Voraz alternativa: Para terminar de definir la solución voraz se puede plantear alternativamente que entre menor sea la diferencia entre $tr_i$ y $ts_i$, mas prioridad debe tener ese tablón $T$, dado que para los 3 casos:
- **Caso 1**: $(ts_i - tr_i) < 0$: El tablón inevitablemente tendrá penalización, pero debe priorizarse para minimizar el daño.
    
- **Caso 2**: $(ts_i - tr_i) = 0$: El tablón debe regarse inmediatamente para evitar penalización.
    
- **Caso 3**: $(ts_i - tr_i) > 0$: El tablón tiene holgura, pero menor holgura implica mayor urgencia.

Planteamos una segunda solución voraz priorizando la holgura, así:
$$\frac{p_i}{ts_i - tr_i} $$
Plantearla sola nos supone un error muy grande y es cuando $(ts_i - tr_i) = 0$, por tanto la vamos a volver a trozos.

$$

\left\{
\begin{array}{ll}
\frac{p_i}{ts_i - tr_i} & \text{si } (ts_i - tr_i) \neq 0 \\
\frac{p_i}{tr_i}  & \text{si } (ts_i - tr_i) = 0
\end{array}
\right.

$$


```java
/* p/(ts-tr) */
    public int[][] roV2(int[][] Finca){
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
        long costo = calcularCosto(Finca, programacion);
        return new int[][]{programacion, new int[]{(int)costo}};
    }
```

### Pruebas de las Soluciones Voraces

#### Ejemplo 1: 
$F1 = ⟨⟨10, 3, 4⟩, ⟨5, 3, 3⟩, ⟨2, 2, 1⟩, ⟨8, 1, 1⟩, ⟨6, 4, 2⟩⟩$

```java
F1 = [(10,3,4), (5,3,3), (2,2,1), (8,1,1), (6,4,2)]
```
##### Solución Voraz 1:
$$\frac{p_i}{tr_i}$$
```java
// Cálculo de métricas:
T0: 4/3 = 1.333
T1: 3/3 = 1.000
T2: 1/2 = 0.500
T3: 1/1 = 1.000
T4: 2/4 = 0.500

// Orden descendente: [0, 1, 3, 2, 4]
// Cálculo de costo:
T0: tiempo 0-3, fin=3, ts=10 → penal=0
T1: tiempo 3-6, fin=6, ts=5 → penal=3×(6-5)=3
T3: tiempo 6-7, fin=7, ts=8 → penal=0
T2: tiempo 7-9, fin=9, ts=2 → penal=1×(9-2)=7
T4: tiempo 9-13, fin=13, ts=6 → penal=2×(13-6)=14

Costo total: 24
```

##### Solución Voraz 2:
$$

\left\{
\begin{array}{ll}
\frac{p_i}{ts_i - tr_i} & \text{si } (ts_i - tr_i) \geq 0 \\
\frac{p_i}{tr_i}  & \text{en otro caso }
\end{array}
\right.

$$
```java
// Cálculo de métricas:
T0: (10-3)=7≥0 → 4/7=0.571
T1: (5-3)=2≥0 → 3/2=1.500
T2: (2-2)=0≥0 → 1/2=0.500
T3: (8-1)=7≥0 → 1/7=0.143
T4: (6-4)=2≥0 → 2/2=1.000

// Orden descendente: [1, 4, 0, 2, 3]
// Cálculo de costo:
T1: tiempo 0-3, fin=3, ts=5 → penal=0
T4: tiempo 3-7, fin=7, ts=6 → penal=2×(7-6)=2
T0: tiempo 7-10, fin=10, ts=10 → penal=0
T2: tiempo 10-12, fin=12, ts=2 → penal=1×(12-2)=10
T3: tiempo 12-13, fin=13, ts=8 → penal=1×(13-8)=5

Costo total: 17
```

##### Solución Fuerza Bruta:
```java
// Mejor orden encontrado: [2, 1, 3, 0, 4]
// Cálculo de costo:
T2: tiempo 0-2, fin=2, ts=2 → penal=0
T1: tiempo 2-5, fin=5, ts=5 → penal=0
T3: tiempo 5-6, fin=6, ts=8 → penal=0
T0: tiempo 6-9, fin=9, ts=10 → penal=0
T4: tiempo 9-13, fin=13, ts=6 → penal=2×(13-6)=14

Costo total: 14
```

#### Ejemplo 2:
$F2 = ⟨⟨9, 3, 4⟩, ⟨5, 3, 3⟩, ⟨2, 2, 1⟩, ⟨8, 1, 1⟩, ⟨6, 4, 2⟩⟩$ 

##### Solución Voraz 1:
$$\frac{p_i}{tr_i}$$
```java
// Cálculo de métricas:
T0: 4/3 = 1.333
T1: 3/3 = 1.000
T2: 1/2 = 0.500
T3: 1/1 = 1.000
T4: 2/4 = 0.500

// Orden descendente: [0, 1, 3, 2, 4]
// Cálculo de costo:
T0: tiempo 0-3, fin=3, ts=9 → penal=0
T1: tiempo 3-6, fin=6, ts=5 → penal=3×(6-5)=3
T3: tiempo 6-7, fin=7, ts=8 → penal=0
T2: tiempo 7-9, fin=9, ts=2 → penal=1×(9-2)=7
T4: tiempo 9-13, fin=13, ts=6 → penal=2×(13-6)=14

Costo total: 24
```
##### Solución Voraz 2:
$$

\left\{
\begin{array}{ll}
\frac{p_i}{ts_i - tr_i} & \text{si } (ts_i - tr_i) \geq 0 \\
\frac{p_i}{tr_i}  & \text{en otro caso }
\end{array}
\right.

$$


```java
// Cálculo de métricas:
T0: (9-3)=6≥0 → 4/6=0.667
T1: (5-3)=2≥0 → 3/2=1.500
T2: (2-2)=0≥0 → 1/2=0.500
T3: (8-1)=7≥0 → 1/7=0.143
T4: (6-4)=2≥0 → 2/2=1.000

// Orden descendente: [1, 4, 0, 2, 3]
// Cálculo de costo:
T1: tiempo 0-3, fin=3, ts=5 → penal=0
T4: tiempo 3-7, fin=7, ts=6 → penal=2×(7-6)=2
T0: tiempo 7-10, fin=10, ts=9 → penal=4×(10-9)=4
T2: tiempo 10-12, fin=12, ts=2 → penal=1×(12-2)=10
T3: tiempo 12-13, fin=13, ts=8 → penal=1×(13-8)=5
Costo total: 21

```

##### Solución Fuerza Bruta:
```java
// Mejor orden encontrado: [2, 1, 3, 0, 4]
// Cálculo de costo:
T2: tiempo 0-2, fin=2, ts=2 → penal=0
T1: tiempo 2-5, fin=5, ts=5 → penal=0
T3: tiempo 5-6, fin=6, ts=8 → penal=0
T0: tiempo 6-9, fin=9, ts=9 → penal=0
T4: tiempo 9-13, fin=13, ts=6 → penal=2×(13-6)=14

Costo total: 14
```

#### Ejemplo 3:
$F2 = ⟨⟨3, 3, 1⟩,⟨5, 3, 2⟩,⟨2, 2, 3⟩,⟨14, 7, 2⟩,⟨13, 7, 1⟩,⟨13, 8, 3⟩,⟨3, 1, 3⟩,⟨10, 3, 4⟩⟩$ 

##### Solución Voraz 1:
$$\frac{p_i}{tr_i}$$
```java
// Cálculo de métricas:
T0: 1/3 = 0,333
T1: 2/3 = 0,667
T2: 3/2 = 1,500
T3: 2/7 = 0,286
T4: 1/7 = 0,143
T5: 3/8 = 0,375
T6: 3/1 = 3,000
T7: 4/3 = 1,333

// Orden descendente: [6, 2, 7, 1, 5, 0, 3, 4]
// Cálculo de costo:
T6: tiempo 0-1, fin=1, ts=3 → penal=0
T2: tiempo 1-3, fin=3, ts=2 → penal=3×(3-2)=3
T7: tiempo 3-6, fin=6, ts=10 → penal=0
T1: tiempo 6-9, fin=9, ts=5 → penal=2×(9-5)=8
T5: tiempo 9-17, fin=17, ts=13 → penal=3×(17-13)=12
T0: tiempo 17-20, fin=20, ts=3 → penal=1×(20-3)=17
T3: tiempo 20-27, fin=27, ts=14 → penal=2×(27-14)=26
T4: tiempo 27-34, fin=34, ts=13 → penal=1×(34-13)=21

Costo total: 87
```

##### Solución Voraz 2:
$$
\left\{
\begin{array}{ll}
\frac{p_i}{ts_i - tr_i} & \text{si } (ts_i - tr_i) \geq 0 \\
\frac{p_i}{tr_i}  & \text{en otro caso }
\end{array}
\right.
$$
```java
// Cálculo de métricas:
T0: (3-3)=0≥0 → 1/3=0,333
T1: (5-3)=2≥0 → 2/2=1,000
T2: (2-2)=0≥0 → 3/2=1,500
T3: (14-7)=7≥0 → 2/7=0,286
T4: (13-7)=6≥0 → 1/6=0,167
T5: (13-8)=5≥0 → 3/5=0,600
T6: (3-1)=2≥0 → 3/2=1,500
T7: (10-3)=7≥0 → 4/7=0,571

// Orden descendente: [2, 6, 1, 5, 7, 0, 3, 4]
// Cálculo de costo:
T2: tiempo 0-2, fin=2, ts=2 → penal=0
T6: tiempo 2-3, fin=3, ts=3 → penal=0
T1: tiempo 3-6, fin=6, ts=5 → penal=2×(6-5)=2
T5: tiempo 6-14, fin=14, ts=13 → penal=3×(14-13)=3
T7: tiempo 14-17, fin=17, ts=10 → penal=4×(17-10)=28
T0: tiempo 17-20, fin=20, ts=3 → penal=1×(20-3)=17
T3: tiempo 20-27, fin=27, ts=14 → penal=2×(27-14)=26
T4: tiempo 27-34, fin=34, ts=13 → penal=1×(34-13)=21

Costo total: 97
```

##### Solución Fuerza Bruta:
```java
// Mejor orden encontrado: [2, 6, 1, 7, 5, 0, 3, 4]
// Cálculo de costo:
T2: tiempo 0-2, fin=2, ts=2 → penal=0
T6: tiempo 2-3, fin=3, ts=3 → penal=0
T1: tiempo 3-6, fin=6, ts=5 → penal=2×(6-5)=2
T7: tiempo 6-9, fin=9, ts=10 → penal=0
T5: tiempo 9-17, fin=17, ts=13 → penal=3×(17-13)=12
T0: tiempo 17-20, fin=20, ts=3 → penal=1×(20-3)=17
T3: tiempo 20-27, fin=27, ts=14 → penal=2×(27-14)=26
T4: tiempo 27-34, fin=34, ts=13 → penal=1×(34-13)=21

Costo total: 78
```

#### Ejemplo 4:
$F2 = ⟨⟨4, 7, 3⟩,⟨13, 9, 2⟩,⟨17, 8, 4⟩,⟨4, 9, 3⟩,⟨3, 2, 3⟩,⟨12, 5, 2⟩,⟨3, 1, 2⟩,⟨7, 9, 2⟩,⟨14, 9, 1⟩⟩$ 
##### Solución Voraz 1:
$$\frac{p_i}{tr_i}$$
```java
// Cálculo de métricas:
T0: 3/7 = 0,429
T1: 2/9 = 0,222
T2: 4/8 = 0,500
T3: 3/9 = 0,333
T4: 3/2 = 1,500
T5: 2/5 = 0,400
T6: 2/1 = 2,000
T7: 2/9 = 0,222
T8: 1/9 = 0,111

// Orden descendente: [6, 4, 2, 0, 5, 3, 1, 7, 8]
// Cálculo de costo:
T6: tiempo 0-1, fin=1, ts=3 → penal=0
T4: tiempo 1-3, fin=3, ts=3 → penal=0
T2: tiempo 3-11, fin=11, ts=17 → penal=0
T0: tiempo 11-18, fin=18, ts=4 → penal=3×(18-4)=42
T5: tiempo 18-23, fin=23, ts=12 → penal=2×(23-12)=22
T3: tiempo 23-32, fin=32, ts=4 → penal=3×(32-4)=84
T1: tiempo 32-41, fin=41, ts=13 → penal=2×(41-13)=56
T7: tiempo 41-50, fin=50, ts=7 → penal=2×(50-7)=86
T8: tiempo 50-59, fin=59, ts=14 → penal=1×(59-14)=45

Costo total: 335
```

##### Solución Voraz 2:
$$
\left\{
\begin{array}{ll}
\frac{p_i}{ts_i - tr_i} & \text{si } (ts_i - tr_i) \geq 0 \\
\frac{p_i}{tr_i}  & \text{en otro caso }
\end{array}
\right.
$$
```java
// Cálculo de métricas:
T0: (4-7)=-3≥0 → 3/-3=-1,000
T1: (13-9)=4≥0 → 2/4=0,500
T2: (17-8)=9≥0 → 4/9=0,444
T3: (4-9)=-5≥0 → 3/-5=-0,600
T4: (3-2)=1≥0 → 3/1=3,000
T5: (12-5)=7≥0 → 2/7=0,286
T6: (3-1)=2≥0 → 2/2=1,000
T7: (7-9)=-2≥0 → 2/-2=-1,000
T8: (14-9)=5≥0 → 1/5=0,200

// Orden descendente: [4, 6, 1, 2, 5, 8, 3, 0, 7]
// Cálculo de costo:
T4: tiempo 0-2, fin=2, ts=3 → penal=0
T6: tiempo 2-3, fin=3, ts=3 → penal=0
T1: tiempo 3-12, fin=12, ts=13 → penal=0
T2: tiempo 12-20, fin=20, ts=17 → penal=4×(20-17)=12
T5: tiempo 20-25, fin=25, ts=12 → penal=2×(25-12)=26
T8: tiempo 25-34, fin=34, ts=14 → penal=1×(34-14)=20
T3: tiempo 34-43, fin=43, ts=4 → penal=3×(43-4)=117
T0: tiempo 43-50, fin=50, ts=4 → penal=3×(50-4)=138
T7: tiempo 50-59, fin=59, ts=7 → penal=2×(59-7)=104

Costo total: 417
```

##### Solución Fuerza Bruta:

```java
// Mejor orden encontrado: [4, 6, 0, 2, 5, 3, 1, 7, 8]
// Cálculo de costo:
T4: tiempo 0-2, fin=2, ts=3 → penal=0
T6: tiempo 2-3, fin=3, ts=3 → penal=0
T0: tiempo 3-10, fin=10, ts=4 → penal=3×(10-4)=18
T2: tiempo 10-18, fin=18, ts=17 → penal=4×(18-17)=4
T5: tiempo 18-23, fin=23, ts=12 → penal=2×(23-12)=22
T3: tiempo 23-32, fin=32, ts=4 → penal=3×(32-4)=84
T1: tiempo 32-41, fin=41, ts=13 → penal=2×(41-13)=56
T7: tiempo 41-50, fin=50, ts=7 → penal=2×(50-7)=86
T8: tiempo 50-59, fin=59, ts=14 → penal=1×(59-14)=45

Costo total: 315
```

#### Ejemplo 5:
$F2 = ⟨⟨4, 6, 1⟩,⟨3, 9, 2⟩,⟨11, 5, 1⟩,⟨9, 2, 1⟩,⟨18, 9, 4⟩,⟨7, 8, 4⟩,⟨5, 4, 3⟩,⟨18, 10, 1⟩,⟨11, 2, 1⟩,⟨3, 2, 1⟩⟩$ 

##### Solución Voraz 1:
$$\frac{p_i}{tr_i}$$
```java
// Cálculo de métricas:
T0: 1/6 = 0,167
T1: 2/9 = 0,222
T2: 1/5 = 0,200
T3: 1/2 = 0,500
T4: 4/9 = 0,444
T5: 4/8 = 0,500
T6: 3/4 = 0,750
T7: 1/10 = 0,100
T8: 1/2 = 0,500
T9: 1/2 = 0,500

// Orden descendente: [6, 8, 9, 3, 5, 4, 1, 2, 0, 7]
// Cálculo de costo:
T6: tiempo 0-4, fin=4, ts=5 → penal=0
T8: tiempo 4-6, fin=6, ts=11 → penal=0
T9: tiempo 6-8, fin=8, ts=3 → penal=1×(8-3)=5
T3: tiempo 8-10, fin=10, ts=9 → penal=1×(10-9)=1
T5: tiempo 10-18, fin=18, ts=7 → penal=4×(18-7)=44
T4: tiempo 18-27, fin=27, ts=18 → penal=4×(27-18)=36
T1: tiempo 27-36, fin=36, ts=3 → penal=2×(36-3)=66
T2: tiempo 36-41, fin=41, ts=11 → penal=1×(41-11)=30
T0: tiempo 41-47, fin=47, ts=4 → penal=1×(47-4)=43
T7: tiempo 47-57, fin=57, ts=18 → penal=1×(57-18)=39

Costo total: 264
```
##### Solución Voraz 2:
$$
\left\{
\begin{array}{ll}
\frac{p_i}{ts_i - tr_i} & \text{si } (ts_i - tr_i) \geq 0 \\
\frac{p_i}{tr_i}  & \text{en otro caso }
\end{array}
\right.
$$
```java
// Cálculo de métricas:
T0: (4-6)=-2≥0 → 1/-2=-0,500
T1: (3-9)=-6≥0 → 2/-6=-0,333
T2: (11-5)=6≥0 → 1/6=0,167
T3: (9-2)=7≥0 → 1/7=0,143
T4: (18-9)=9≥0 → 4/9=0,444
T5: (7-8)=-1≥0 → 4/-1=-4,000
T6: (5-4)=1≥0 → 3/1=3,000
T7: (18-10)=8≥0 → 1/8=0,125
T8: (11-2)=9≥0 → 1/9=0,111
T9: (3-2)=1≥0 → 1/1=1,000

// Orden descendente: [6, 9, 4, 2, 3, 7, 8, 1, 0, 5]
// Cálculo de costo:
T6: tiempo 0-4, fin=4, ts=5 → penal=0
T9: tiempo 4-6, fin=6, ts=3 → penal=1×(6-3)=3
T4: tiempo 6-15, fin=15, ts=18 → penal=0
T2: tiempo 15-20, fin=20, ts=11 → penal=1×(20-11)=9
T3: tiempo 20-22, fin=22, ts=9 → penal=1×(22-9)=13
T7: tiempo 22-32, fin=32, ts=18 → penal=1×(32-18)=14
T8: tiempo 32-34, fin=34, ts=11 → penal=1×(34-11)=23
T1: tiempo 34-43, fin=43, ts=3 → penal=2×(43-3)=80
T0: tiempo 43-49, fin=49, ts=4 → penal=1×(49-4)=45
T5: tiempo 49-57, fin=57, ts=7 → penal=4×(57-7)=200

Costo total: 387
```
##### Solución Fuerza Bruta:
```java
// Mejor orden encontrado: [6, 5, 3, 8, 9, 4, 1, 2, 0, 7]
// Cálculo de costo:
T6: tiempo 0-4, fin=4, ts=5 → penal=0
T5: tiempo 4-12, fin=12, ts=7 → penal=4×(12-7)=20
T3: tiempo 12-14, fin=14, ts=9 → penal=1×(14-9)=5
T8: tiempo 14-16, fin=16, ts=11 → penal=1×(16-11)=5
T9: tiempo 16-18, fin=18, ts=3 → penal=1×(18-3)=15
T4: tiempo 18-27, fin=27, ts=18 → penal=4×(27-18)=36
T1: tiempo 27-36, fin=36, ts=3 → penal=2×(36-3)=66
T2: tiempo 36-41, fin=41, ts=11 → penal=1×(41-11)=30
T0: tiempo 41-47, fin=47, ts=4 → penal=1×(47-4)=43
T7: tiempo 47-57, fin=57, ts=18 → penal=1×(57-18)=39

Costo total: 259
```
#### Tabla de soluciones 

| Entrada | #$T$ | Voraz 1 | Voraz 2 | Mejor Voraz | Fuerza Bruta |
| :-----: | ---- | :-----: | :-----: | :---------: | :----------: |
|  $F_1$  | 5    |   24    |   17    |   Voraz 2   |      14      |
|  $F_2$  | 5    |   24    |   21    |   Voraz 2   |      14      |
|  $F_3$  | 8    |   87    |   97    |   Voraz 1   |      78      |
|  $F_4$  | 9    |   335   |   417   |   Voraz 1   |     315      |
|  $F_5$  | 10   |   264   |   387   |   Voraz 1   |     259      |

Al final para valores mas altos, es mejor la solución voraz 1. Así que esa será la que se trabajara al completo.

### Complejidad 

Solución voraz1:
$$\frac{p_i}{tr_i}$$

```java
/* p/tr */
    public int[][] roV1(int[][] Finca){
        int n = Finca.length;
        int[] programacion = new int[n];
        
        PriorityQueue<Map.Entry<Double, Integer>> colaProg = 
            new PriorityQueue<>((a, b) -> Double.compare(b.getKey(), a.getKey()));

		// O(n)
        for (int i = 0; i < n; i++) {
            double voraz = (Finca[i][2] / (double)Finca[i][1]);
            colaProg.offer(new SimpleEntry<>(voraz, i));  
        }
        int cont = 0;
        
        // O(n)
        while(!colaProg.isEmpty()){
            Map.Entry<Double, Integer> entry = colaProg.poll();
            programacion[cont] = entry.getValue();
            cont++;
        }

        long costo = calcularCosto(Finca, programacion);
        return new int[][]{programacion, new int[]{(int)costo}};
    }
```

Para calcular la complejidad de la función **roV1** se deben mirar dos momentos clave, la instanciación de la cola de prioridad con el bucle **for** y el paso de los valores al la lista en el bucle **while**.

**for**
$T(n) = \sum_{i=0}^{n} colaProg.offer()$

Una cola de prioridad se ordena con una complejidad $T(n) = O(\log n)$

$colaProg.offer =  O(\log n)$

$T(n) = \sum_{i=0}^{n} \log n$

$T(n) = \log n \sum_{i=0}^{n} 1$

$T(n) = \log n * n$

$T(n) = O(n * \log n)$

**while**
$T(n) = \sum_{i=0}^{n} colaProg.poll()$

$colaProg.Poll =  O(\log n)$

$T(n) = O(n * \log n)$

**Complejidad final:**
$$
T(n) = O(n * \log n)
$$
### Corrección

Para validar el comportamiento del algoritmo **voraz (`roV`)**, se compararon los resultados obtenidos con los del algoritmo de **fuerza bruta (`roFB`)**, considerado como referencia óptima.

#### Objetivo

Verificar que la estrategia voraz, basada en el criterio de prioridad por tiempo de riego $\frac{p_i}{tr_i}$, genera una programación de riego con un costo cercano al óptimo, reduciendo significativamente el tiempo de ejecución.

#### Caso de prueba

Usando la misma finca de referencia:

$$
F_1 = ([10,3,4],[5,3,3],[2,2,1],[8,1,1],[6,4,2])
$$

#### Ejecución del algoritmo

```java
int[][] F1 = {
    {10,3,4}, {5,3,3}, {2,2,1}, {8,1,1}, {6,4,2}
};
int[][] resultado = roV(F1);
System.out.println("Orden voraz: " + Arrays.toString(resultado[0]));
System.out.println("Costo obtenido: " + resultado[1][0]);
```

### Salida obtenida:
Orden voraz: [1, 4, 0, 3, 2]
Costo obtenido: 17

#### Analisis
Teniendo  en cuenta que la solución optima por fuerza bruta nos arrojó un costo de 14 podemos observar que la solución voraz solo nos da una diferencia de +3
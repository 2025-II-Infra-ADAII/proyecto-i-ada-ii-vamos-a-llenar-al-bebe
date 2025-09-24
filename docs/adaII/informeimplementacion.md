# Informe de Implementación — Problema de la Mochila 0/1

## 0. Descripción del problema

El **problema de la mochila 0/1** consiste en seleccionar un subconjunto de objetos con peso y valor, de manera que la suma de los pesos no exceda una capacidad máxima \(W\) y el valor total sea máximo.

### Definición formal

- Conjunto de \(n\) objetos: \(\{1, 2, \ldots, n\}\).
- Cada objeto \(i\) tiene:
  - Peso \(w_i \in \mathbb{Z}^+\).
  - Valor \(v_i \in \mathbb{Z}^+\).
- Capacidad de la mochila: \(W\).
- Objetivo:
  \[
  \max \sum*{i=1}^n v_i x_i \quad \text{sujeto a } \sum*{i=1}^n w_i x_i \leq W, \quad x_i \in \{0,1\}.
  \]

### Ejemplo ilustrativo

- Objetos:
  1. \(w_1=2, v_1=3\)
  2. \(w_2=3, v_2=4\)
  3. \(w_3=4, v_3=5\)
- Capacidad: \(W=5\).

Subconjuntos válidos:

- \(\{1,2\}\): peso \(2+3=5\), valor \(3+4=7\) → **óptimo**.
- \(\{1,3\}\): peso 6 → inválido.

La solución óptima es tomar los objetos 1 y 2 con valor total 7.

---

## 1. Lenguaje y herramientas usadas

- **Lenguaje:** Python 3.
- **Bibliotecas estándar:** `itertools`, `time`, `random`.
- **Estructuras usadas:** listas, diccionarios y arreglos bidimensionales.
- **Motivación de elección:** Python facilita la implementación de algoritmos y pruebas rápidas, sin necesidad de compilación compleja.

---

## 2. Estructura del proyecto

El proyecto se organizó en archivos principales:

```

knapsack_project/
│
├── knapsack_report.py # Implementaciones ingenua, dinámica y voraz
├── benchmark.py # Script para medición de tiempos (no usado en este informe)
├── plot_results.py # Script para graficar resultados (no usado en este informe)
├── requirements.txt # Dependencias (matplotlib opcional)
└── .github/
└── workflows/
└── ci.yml # Pipeline de compilación/ejecución

```

---

## 3. Ejecución del proyecto

La ejecución se hace desde consola.

### Ejemplo de ejecución básica:

```bash
python knapsack_report.py
```

### Parámetros técnicos

- El programa puede generar instancias aleatorias con:
  - `n`: número de objetos.
  - `max_weight`: peso máximo de un objeto.
  - `max_value`: valor máximo de un objeto.
  - `seed`: semilla para reproducibilidad.

- Ejemplo:

```python
from knapsack_report import gen_instance, knapsack_dp
items, W = gen_instance(10, 20, 100, seed=1)
print(knapsack_dp(items, W))
```

---

## 4. Ideas de solución

### a) Solución ingenua (fuerza bruta)

Generar todos los subconjuntos ($2^n$) y elegir el de mayor valor que cumpla la restricción:

$$
\text{Óptimo} = \max_{S \subseteq \{1,\dots,n\},\; \sum_{i \in S} w_i \leq W} \sum_{i \in S} v_i
$$

**Ejemplo:** con $W=5$, objetos como el caso introductorio → se recorren todos los subconjuntos hasta encontrar $\{1,2\}$.

---

### b) Solución dinámica

Definición recursiva:

$$
V[i][w] =
\begin{cases}
V[i-1][w], & w_i > w \\
\max\big(V[i-1][w],\; V[i-1][w-w_i] + v_i\big), & w_i \leq w
\end{cases}
$$

Donde $V[i][w]$ es el mejor valor con los primeros $i$ objetos y capacidad $w$.
Se construye de manera **bottom-up** llenando una tabla bidimensional.

**Ejemplo:** para $n=3, W=5$, se obtiene que $V[3][5]=7$, con los objetos 1 y 2.

---

### c) Solución voraz

Ordenar los objetos por eficiencia:

$$
\text{ratio}_i = \frac{v_i}{w_i}
$$

y agregar al conjunto mientras la capacidad lo permita.

**Ejemplo:** con objetos

1. $v/w = 1.5$,
2. $v/w = 1.33$,
3. $v/w = 1.25$.
   Se eligen primero los más eficientes hasta llenar la capacidad.

---

## 5. Partes importantes del código

- **Generación de instancias (`gen_instance`)**
  Permite crear listas de objetos aleatorios y fijar una capacidad $W$. Relaciona pesos y valores de forma controlada.

- **Fuerza bruta (`knapsack_bruteforce`)**
  Recorre subconjuntos mediante bitmasks. Se incluye **poda temprana**: si la suma de pesos supera $W$, se descarta inmediatamente.

- **Programación dinámica (`knapsack_dp`)**
  Construye una tabla `V` y aplica la recurrencia matemática. Después, reconstruye la solución verificando si un objeto fue incluido comparando `V[i][w]` con `V[i-1][w]`.

- **Voraz (`knapsack_greedy_ratio`)**
  Ordena por $\frac{v_i}{w_i}$ y selecciona mientras quepa. Refleja fielmente la heurística.

Cada módulo se ajusta a la definición matemática explicada en la sección 4.

---

## 6. Pipeline de compilación/ejecución

Se definió un pipeline simple de integración continua en **GitHub Actions** para verificar que el proyecto se ejecute sin errores (no incluye pruebas de rendimiento ni validación).

Archivo: `.github/workflows/ci.yml`

```yaml
name: Knapsack CI

on:
  push:
    branches: ["main"]
  pull_request:
    branches: ["main"]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout repository
        uses: actions/checkout@v3

      - name: Set up Python
        uses: actions/setup-python@v4
        with:
          python-version: "3.10"

      - name: Install dependencies
        run: |
          python -m pip install --upgrade pip
          if [ -f requirements.txt ]; then pip install -r requirements.txt; fi

      - name: Run knapsack main script
        run: |
          python knapsack_report.py
```

```

```

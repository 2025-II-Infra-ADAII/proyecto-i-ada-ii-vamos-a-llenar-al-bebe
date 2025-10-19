# Formalización

Sea una finca $F$ una secuencia de tablones  
$$F = \langle T_0, \ldots, T_{n-1} \rangle,$$

donde cada tablón $T_i$ es la tupla $T_i = \langle ts_i, tr_i, p_i \rangle$ con:

- $ts_i$: tiempo de supervivencia del tablón $i$ (días)
- $tr_i$: tiempo de regado del tablón $i$ (días)  
- $p_i$: prioridad del tablón $i$ (entero en $\{1, \ldots, 4\}$)

Una programación de riego es una permutación  
$$\Pi = \langle \pi_0, \pi_1, \ldots, \pi_{n-1} \rangle$$  
de $\{0, 1, \ldots, n-1\}$ que indica el orden en que se regarán los tablones.

Si se adopta la permutación $\Pi$, el tiempo de inicio de riego del tablón $T_{\pi_j}$ viene dado por:

$$
\begin{aligned}
t^*_{\Pi}(\pi_0) &= 0, \\
t^*_{\Pi}(\pi_j) &= t^*_{\Pi}(\pi_{j-1}) + tr_{\pi_{j-1}}, \quad j = 1, \ldots, n-1.
\end{aligned}
$$

Para cada tablón $T_i$, dado $\Pi$, definimos el costo por sufrimiento (penalización por regar tarde) como:

$$
CRF^{\Pi}[i] = p_i \cdot \max\left\{0, (t^*_{\Pi}(i) + tr_i) - ts_i\right\},
$$

es decir, la prioridad multiplicada por la cantidad de días en que el regado termina después del tiempo de supervivencia (si no hay retraso la penalización es $0$).

El costo total de riego de la finca $F$ con la programación $\Pi$ es:

$$
CRF^{\Pi} = \sum_{i=0}^{n-1} CRF_{\Pi}[i].
$$

# Aplicaciones

1. [[fuerzabruta]]
2. [[voraz]]
3. [[dinamica]]
4. [[Complejidades]]

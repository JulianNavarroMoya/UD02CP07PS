# Ordenación por mezcla concurrente (Caso práctico 7)

Implementación del algoritmo recursivo de **ordenación por mezcla (merge sort)** de forma concurrente, usando un **árbol binario de hebras**. Cada nodo del árbol es un `Thread` que divide su lista, delega cada mitad en dos hebras hijas y, cuando éstas terminan, mezcla los resultados.

## Estructura del proyecto

```
src/
├── Main.java                 // Punto de entrada: genera la lista y lanza la raíz
├── concurrencia/
│   └── Nodo.java             // Hebra que ordena su lista (clase del diagrama)
└── utilidades/
    ├── GeneradorDatos.java   // Genera listas de enteros aleatorios
    └── Visualizador.java     // Muestra cabecera, listas y errores
```

## Diagrama de clases

`Nodo` extiende `Thread` y tiene una composición reflexiva de **2** nodos (sus hijos):

| Elemento | Descripción |
|---|---|
| `lista : List<Integer>` | Lista que el nodo debe ordenar |
| `Nodo(lista)` | Constructor |
| `añade(l, desde, hasta)` | Añade a la lista del nodo los elementos de `l` entre `desde` (incluido) y `hasta` (excluido) |
| `mezcla(l1, l2)` | Mezcla ordenadamente `l1` y `l2`, dejando el resultado en la lista del nodo |
| `run()` | Lógica recursiva de ordenación |

## Funcionamiento

1. `Main` genera una lista aleatoria (entre 5 y 100 elementos, valores entre 1 y 100) y crea el **nodo raíz**.
2. `run()` de cada nodo:
    - **Caso base:** si la lista tiene 0 o 1 elemento, ya está ordenada y el hilo termina.
    - **Caso recursivo:**
        1. Parte la lista por la mitad, creando copias independientes (`new ArrayList<>(subList(...))`).
        2. Crea dos nodos hijos, cada uno con una mitad, y los arranca con `start()`.
        3. Espera a ambos con `join()`.
        4. Vacía su lista y la rellena con `mezcla(mitad1, mitad2)`.
3. `Main` espera a la raíz con `join()` y muestra la lista ordenada.

### Por qué no hace falta `synchronized`

Cada hilo trabaja **solo sobre sus propios datos**:

- Las mitades se copian antes de lanzar los hijos, así que cada hijo tiene su propia lista.
- El padre no toca la lista de un hijo hasta que el hijo ha terminado (`join()`).

No hay memoria compartida accedida simultáneamente, por lo que no hay condiciones de carrera. Además, `join()` establece una relación *happens-before`: todo lo que escribe el hijo es visible para el padre cuando el `join()` retorna.

### Número de hebras

Cada nodo con 2 o más elementos crea 2 hijos. Los nodos hoja (1 elemento o 0) también son hebras que terminan de inmediato. Con *n* elementos el árbol tiene **2n − 1 nodos**, es decir, 2n − 1 hebras. Por ejemplo, para 24 elementos se crean 47 hebras.

La profundidad del árbol es aproximadamente ⌈log₂ n⌉, de ahí que se hable de profundidad variable según el tamaño de la lista.

## Decisiones de diseño

- **Interpretación de `añade`:** el enunciado indica "añade a la lista l los elementos de lista", pero en `mezcla` es necesario añadir a la lista del nodo los elementos de `l1` o `l2`. Por eso el parámetro es el **origen** y el destino es siempre la lista del nodo.
- **Interrupción:** si `join()` es interrumpido, el nodo restaura el flag de interrupción y sale sin mezclar, para no producir una lista parcial.
- **Copias defensivas:** `new ArrayList<>(subList(...))` evita que los hijos modifiquen la lista del padre.

## Compilación y ejecución

Desde la carpeta `src/`:

```bash
javac Main.java concurrencia/Nodo.java utilidades/*.java
java Main
```

## Ejemplo de ejecución

```
= Ordenacion por mezcla concurrente =
Lista aleatoria repartida en hebras.

Lista original (24 elementos):
[63, 33, 28, 64, 35, 31, 74, 2, 25, 21, 12, 1, 67, 42, 46, 66, 45, 99, 76, 92, 83, 26, 14, 39]

Lista ordenada (24 elementos):
[1, 2, 12, 14, 21, 25, 26, 28, 31, 33, 35, 39, 42, 45, 46, 63, 64, 66, 67, 74, 76, 83, 92, 99]
```

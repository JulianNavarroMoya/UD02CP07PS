package utilidades;

import java.util.List;

public class Visualizador {

    public static void mostrarCabecera() {
        System.out.println("=== Ordenacion por mezcla concurrente ===");
        System.out.println("Lista aleatoria repartida en hebras.\n");
    }

    public static void mostrarEstadoLista(String mensaje, List<Integer> lista) {
        System.out.println(mensaje + " (" + lista.size() + " elementos):");
        System.out.println(lista);
        System.out.println();
    }

    public static void mostrarError(String mensaje) {
        System.err.println("[ERROR] " + mensaje);
    }
}
import concurrencia.Nodo;
import utilidades.GeneradorDatos;
import utilidades.Visualizador;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Visualizador.mostrarCabecera();

        int tamano = new Random().nextInt(96) + 5; // entre 5 y 100 elementos
        List<Integer> listaOriginal = GeneradorDatos.generarListaAleatoria(24, 1, 100);
        Visualizador.mostrarEstadoLista("Lista original", listaOriginal);

        Nodo raiz = new Nodo(listaOriginal);
        raiz.start();

        try {
            raiz.join();
            Visualizador.mostrarEstadoLista("Lista ordenada", listaOriginal);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Visualizador.mostrarError("El hilo principal fue interrumpido durante la espera.");
        }
    }
}
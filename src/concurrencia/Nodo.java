package concurrencia;

import java.util.ArrayList;
import java.util.List;

public class Nodo extends Thread {

    private List<Integer> lista;

    public Nodo(List<Integer> lista) {
        this.lista = lista;
    }

    /**
     * Añade a la lista del nodo los elementos de 'origen' entre las
     * posiciones desde (incluida) y hasta (excluida).
     */
    private void añade(List<Integer> origen, int desde, int hasta) {
        lista.addAll(origen.subList(desde, hasta));
    }

    /**
     * Mezcla ordenadamente l1 y l2 dejando el resultado en la lista del nodo.
     * Precondición: l1 y l2 están ordenadas y la lista del nodo está vacía.
     */
    private void mezcla(List<Integer> l1, List<Integer> l2) {
        int i = 0, j = 0;

        while (i < l1.size() && j < l2.size()) {
            if (l1.get(i) <= l2.get(j)) {
                lista.add(l1.get(i++));
            } else {
                lista.add(l2.get(j++));
            }
        }

        añade(l1, i, l1.size());
        añade(l2, j, l2.size());
    }

    @Override
    public void run() {
        // Caso base: 0 o 1 elemento ya está ordenado
        if (lista.size() <= 1) {
            return;
        }

        int medio = lista.size() / 2;
        List<Integer> mitad1 = new ArrayList<>(lista.subList(0, medio));
        List<Integer> mitad2 = new ArrayList<>(lista.subList(medio, lista.size()));

        Nodo hijo1 = new Nodo(mitad1);
        Nodo hijo2 = new Nodo(mitad2);
        hijo1.start();
        hijo2.start();

        try {
            hijo1.join();
            hijo2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return; // no mezclar mitades incompletas
        }

        lista.clear();
        mezcla(mitad1, mitad2);
    }
}

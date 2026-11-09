package utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradorDatos {

    public static List<Integer> generarListaAleatoria(int tamano, int min, int max) {
        List<Integer> lista = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < tamano; i++) {
            lista.add(random.nextInt((max - min) + 1) + min);
        }

        return lista;
    }
}
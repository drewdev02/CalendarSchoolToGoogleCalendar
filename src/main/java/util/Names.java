package util;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Names {
    private static final Map<String, String> names = Map.of(
            "MIII", "Calculo III",
            "EF", "Educacion Fisica",
            "EDII", "Estructura de Datos II",
            "GPN", "Gestion de Proyectos",
            "SBDI", "Sistema de Base de Datos I",
            "AC", "Arquitectura de Computadoras"
    );

    private Names() {
    }

    public static String reemplaceNames(String name) {
        var newName = new AtomicReference<>(name);
        names.forEach((key, value) -> {
            if (name.contains(key)) {
                newName.set(name.replace(key, value));
            }
        });
        return newName.get();
    }

}

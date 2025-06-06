package org.acme.services;

import org.acme.entities.Alerta;
import org.acme.entities.Usuario;

public class AlertaService {
    public boolean validateAlerta(Alerta alerta) {
        if (alerta == null)
            return false;
        if (alerta.getTitulo() == null || alerta.getTitulo().isEmpty())
            return false;

        return true;
    }
}

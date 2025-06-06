package org.acme.services;

import org.acme.entities.Missao;
import org.acme.entities.Usuario;

public class MissaoService {
    public boolean validateMissao(Missao missao) {
        if (missao == null)
            return false;
        if (missao.getTitulo() == null || missao.getTitulo().isEmpty())
            return false;

        return true;
    }
}

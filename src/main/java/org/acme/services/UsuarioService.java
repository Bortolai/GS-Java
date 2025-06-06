package org.acme.services;

import org.acme.dtos.UsuarioAtualizacaoDto;
import org.acme.dtos.UsuarioCadastroDto;
import org.acme.entities.Usuario;
import org.acme.repositories.UsuarioRepository;

import java.util.Optional;

public class UsuarioService {
    private UsuarioRepository usuarioRepository = new UsuarioRepository();

    public void cadastrarUsuario(UsuarioCadastroDto dto) {
        if (!dto.getSenha().equals(dto.getConfirmaSenha())) {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }

        if (dto.getCategoria() == null || dto.getCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a categoria do usuário: Moderador ou Voluntário.");
        }

        Optional<Usuario> usuarioExistente = usuarioRepository.buscarPorEmail(dto.getEmail());

        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        if (usuarioRepository.buscarPorTelefone(dto.getTelefone()).isPresent()) {
            throw new IllegalArgumentException("Telefone já cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCategoria(dto.getCategoria());
        usuario.setTelefone(dto.getTelefone());

        usuarioRepository.cadastrarUsuario(usuario, dto.getSenha());
    }

    public Usuario loginUsuario(String email, String senha) {
        return usuarioRepository.fazerLogin(email, senha)
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos."));
    }

    public void atualizarUsuario(int id, UsuarioAtualizacaoDto dto) {
        // Verifica se o email já existe para outro usuário
        Optional<Usuario> usuarioComMesmoEmail = usuarioRepository.buscarPorEmail(dto.getEmail());
        if (usuarioComMesmoEmail.isPresent() && usuarioComMesmoEmail.get().getId() != id) {
            throw new IllegalArgumentException("Email já cadastrado para outro usuário.");
        }

        // Verifica se o telefone já existe para outro usuário
        Optional<Usuario> usuarioComMesmoTelefone = usuarioRepository.buscarPorTelefone(dto.getTelefone());
        if (usuarioComMesmoTelefone.isPresent() && usuarioComMesmoTelefone.get().getId() != id) {
            throw new IllegalArgumentException("Telefone já cadastrado para outro usuário.");
        }

        // Validação da senha, se estiver presente
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            if (!dto.getSenha().equals(dto.getConfirmaSenha())) {
                throw new IllegalArgumentException("Senha e confirmação não coincidem.");
            }
        }

        // Atualiza dados gerais no Crud (apenas nome, email, categoria, telefone)
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCategoria(dto.getCategoria());
        usuario.setTelefone(dto.getTelefone());

        usuarioRepository.atualizar(id, usuario);

        // Atualiza senha separadamente (somente se veio no DTO)
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuarioRepository.atualizarSenhaUsuario(id, dto.getSenha());
        }
    }

    public Usuario buscarPorId(int id) {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + id));
    }

}

package org.acme.resources;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.UsuarioAtualizacaoDto;
import org.acme.dtos.UsuarioCadastroDto;
import org.acme.dtos.UsuarioLoginDto;
import org.acme.entities.JwtUtil;
import org.acme.entities.Usuario;
import org.acme.repositories.UsuarioRepository;
import org.acme.services.UsuarioService;
import org.jboss.resteasy.reactive.RestResponse;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/usuario")
public class UsuarioResource {
    private UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final UsuarioService usuarioService = new UsuarioService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Usuario> getUsuarios() {return usuarioRepository.listarTodos();}

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@PathParam("id") int id){
        var usuario = usuarioRepository.buscarPorId(id);

        return usuario.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Path("/cadastro")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cadastrarUsuario(UsuarioCadastroDto dto) {
        try {
            usuarioService.cadastrarUsuario(dto);
            return Response.status(Response.Status.CREATED).entity("Usuário cadastrado com sucesso!").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao cadastrar usuário.").build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUsuario(UsuarioLoginDto dto) {
        try {
            // Tenta autenticar o usuário (lança IllegalArgumentException se falhar)
            Usuario usuario = usuarioService.loginUsuario(dto.getEmail(), dto.getSenha());

            String token = JwtUtil.gerarToken(String.valueOf(usuario.getId()));
            return Response.ok(Collections.singletonMap("token", token)).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao realizar login.").build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarUsuario(@PathParam("id") int id, UsuarioAtualizacaoDto dto,
                                     @Context HttpHeaders headers) {
        try {
            // Pega o token do cabeçalho Authorization
            String authHeader = headers.getHeaderString("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Token JWT obrigatório").build();
            }

            // 1. Extrair e validar token JWT
            String idDoTokenStr = JwtUtil.validarToken(authHeader);
            int idDoToken = Integer.parseInt(idDoTokenStr);

            // 2. Verificar se o ID no token bate com o ID da URL
            if (idDoToken != id) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Você não tem permissão para atualizar este usuário.")
                        .build();
            }

            // Verifica se o email do token bate com o do usuário que vai atualizar
            Usuario usuarioNoBanco = usuarioService.buscarPorId(id);
            if (usuarioNoBanco == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Usuário não encontrado").build();
            }


            // Atualiza o usuário normalmente
            usuarioService.atualizarUsuario(id, dto);
            return Response.ok("Usuário atualizado com sucesso!").build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao atualizar usuário.").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUsuario(@PathParam("id") int id){
        var usuario = usuarioRepository.buscarPorId(id);

        if (usuario == null)
            return Response.status(RestResponse.Status.NOT_FOUND).build();

        usuarioRepository.deleteById(id);
        return Response.noContent().build();
    }
}

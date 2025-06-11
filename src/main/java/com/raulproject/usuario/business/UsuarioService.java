package com.raulproject.usuario.business;


import com.raulproject.usuario.business.converter.UsuarioConverter;
import com.raulproject.usuario.business.dto.UsuarioDTO;
import com.raulproject.usuario.infraestructure.entity.Usuario;
import com.raulproject.usuario.infraestructure.repository.UsuarioRepository;
import com.raulproject.usuario.infraestructure.exceptions.ResourceNotFoundException;
import com.raulproject.usuario.infraestructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não encontrado" + email));
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO usuarioDTO){
        // buscar o email do usuario atraves do token (tirar a obrigatoriedade do email)
       String email = jwtUtil.extrairEmailToken(token.substring(7));

       //Criptografia de senha
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);

       // busca os dados do usuario no banco de dados
       Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
               new ResourceNotFoundException("Email nao localizado"));

       // Mesclou os dados que recebemos na requisicao DTO com os dados no banco de dados
       Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioEntity);

       // Colocou criptografia na senha
       usuario.setSenha(passwordEncoder.encode(usuario.getPassword()));

        //Salvou os dados do usuario convertido e depois pegou o retorno e converteu para usuarioDTO
       return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

}

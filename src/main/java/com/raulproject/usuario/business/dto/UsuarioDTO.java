package com.raulproject.usuario.business.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UsuarioDTO {

    private String nome;
    private String email;
    private String senha;

    private List<EnderecoDTO> enderecos;
    private List<TelefoneDTO> telefones;

}

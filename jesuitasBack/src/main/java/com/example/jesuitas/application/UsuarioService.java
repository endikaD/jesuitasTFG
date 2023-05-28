package com.example.jesuitas.application;


import com.example.jesuitas.models.User;

import java.util.List;

public interface UsuarioService {

    //List<UsuarioOutputDto> obtenerUsuarioPorNombre(String nombre);

    User obtenerUsuarioPorId(Integer id);

    List<User> obtenerUsuarios(Integer pagina, Integer tamanio);

    User actualizarUsuario(Integer id, User user);

    void borrarUsuario(Integer id) throws Exception;
}

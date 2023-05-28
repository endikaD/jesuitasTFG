package com.example.jesuitas.application;

import com.example.jesuitas.models.User;
import com.example.jesuitas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    UserRepository usuarioRepository;


    @Override
    public List<User> obtenerUsuarios(Integer pagina, Integer tamanio) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanio);
        return usuarioRepository.findAll(pageRequest).getContent().stream().map(User::new).toList();
    }

    @Override
    public User obtenerUsuarioPorId(Integer id) {
        User usuario = usuarioRepository.findById(Long.valueOf(id)).orElseThrow();
        return new User(usuario);
    }

//    @Override
//    public List<UsuarioOutputDto> obtenerUsuarioPorNombre(String nombre) {
//        List<UsuarioOutputDto> usuarios = new ArrayList<>();
//        for (Usuario usuario: usuarioRepository.findByNombre(nombre)) {
//            usuarios.add(new UsuarioOutputDto(usuario));
//        }
//        return usuarios;
//    }


    @Override
    public User actualizarUsuario(Integer id, User usuarioInputDto) {
        User usuario = usuarioRepository.findById(Long.valueOf(id)).orElseThrow();

        usuario.setUsername(usuarioInputDto.getUsername());
        usuario.setEmail(usuarioInputDto.getEmail());
        usuario.setPassword(usuarioInputDto.getPassword());

        usuarioRepository.save(usuario);
        return new User(usuario);
    }

    @Override
    public void borrarUsuario(Integer id) throws Exception {
        usuarioRepository.delete(usuarioRepository.findById(Long.valueOf(id)).orElseThrow());
    }
}

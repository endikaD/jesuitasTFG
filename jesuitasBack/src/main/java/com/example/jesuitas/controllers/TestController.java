package com.example.jesuitas.controllers;

import com.example.jesuitas.application.UsuarioService;
import com.example.jesuitas.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
  @Autowired
  UsuarioService usuarioService;

  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('USER_ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/mod")
  //@PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/admin")
  //@PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }

  @GetMapping
  public List<User> obtenerListaPersonas(@RequestParam(defaultValue = "0", required = false) Integer pagina,
                                                     @RequestParam(defaultValue = "4", required = false) Integer tamanio) {
    return usuarioService.obtenerUsuarios(pagina, tamanio);
  }

  @GetMapping("/{id}")
  public User obtenerPersonaPorId(@PathVariable Integer id) throws Exception {
    return usuarioService.obtenerUsuarioPorId(id);
  }

  @PutMapping("/{id}")
  public User actualizarPersona(@PathVariable Integer id, @RequestBody User usuarioInputDto) throws Exception {
    return usuarioService.actualizarUsuario(id, usuarioInputDto);
  }

  @DeleteMapping("/{id}")
  public void borrarPersona(@PathVariable Integer id) throws Exception {
    usuarioService.borrarUsuario(id);
  }


}

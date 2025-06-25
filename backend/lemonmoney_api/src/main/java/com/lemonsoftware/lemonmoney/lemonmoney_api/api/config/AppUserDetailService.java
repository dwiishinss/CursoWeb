package com.lemonsoftware.lemonmoney.lemonmoney_api.api.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Usuario;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.UsuarioRepository;

@Service
public class AppUserDetailService implements UserDetailsService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(username);
        Usuario user = usuario.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));

        return new User(username, user.getSenha(), getPermissoes(user));
    }

    private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        
        usuario.getPermissoes().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getDescricao().toUpperCase())));

        return authorities;
    }
    
    
}

package com.distributivo.spring.security;

import com.distributivo.ejb.security.UsuarioFacade;
import com.distributivo.ejb.security.ViewPermisoFacade;
import com.distributivo.model.security.Usuario;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {


    @EJB(mappedName = "java:global/Distributivo/UsuarioFacade")
    private UsuarioFacade usuarioFacade;
    @EJB(mappedName = "java:global/Distributivo/ViewPermisoFacade")
    private ViewPermisoFacade viewpermisoFacade;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        Usuario useraux = this.usuarioFacade.getLogin(username, password);
        if (useraux == null) {
            throw new BadCredentialsException("Usuario o Contraseña están Incorrectos");
        }
        List<GrantedAuthority> lista = new LinkedList<>();
        for (String rol : this.viewpermisoFacade.getRolesUsuario(username)) {
            lista.add(new SimpleGrantedAuthority(rol));
        }
        if (lista.size() < 1) {
            throw new BadCredentialsException("No cuenta con los permisos Necesarios");
        }
        Authentication auth = new UsernamePasswordAuthenticationToken(username, password, lista);
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    

}

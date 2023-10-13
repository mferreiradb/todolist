package br.com.mferreiradb.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.mferreiradb.todolist.users.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository _userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks")) {
            // Pegar a autenticação (username e senha)
            var authorization = request.getHeader("Authorization"); // Código em base 64

            // Calcula o tamanho da palavra indicada e a remove da string. Após isso, remove
            // os espaços
            var authToken = authorization.substring("Basic".length()).trim();

            // Decodifica o token e cria um array de bytes
            byte[] authDecoded = Base64.getDecoder().decode(authToken);

            // Transforma em o token decodificado em uma string
            var authString = new String(authDecoded);

            String[] credentials = authString.split(":");

            String username = credentials[0];
            String password = credentials[1];

            // Validar usuário
            var userExists = this._userRepository.findByUsername(username);

            if (userExists == null) {
                response.sendError(401);
            } else {

                // Validar senha
                var verifiedPassword = BCrypt.verifyer().verify(password.toCharArray(), userExists.getPassword());
                if (verifiedPassword.verified) {

                    request.setAttribute("userId", userExists.getId());

                    // Segue com a requisição "passando request e response para frente"
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }

    }

}

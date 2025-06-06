package gob.issste.gys.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static gob.issste.gys.service.SecurityService.*;

@Configuration
public class JWTAuthenticationConfig {

    public String getJWTToken(String username) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ADMIN, SUPERVISOR, OPERATIVO");
        String token = Jwts
                .builder()
                .setId("5i6y$")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(getSigningKey(SUPER_SECRET_KEY),  SignatureAlgorithm.HS512).compact();

        return "Bearer " + token;
    }

    public String getKeyJWTToken(String sck, String encodedIv) {
        String token = Jwts
                .builder()
                .claim("sck", sck)
                .claim("iv", encodedIv)
                .signWith(getSigningKey(SUPER_SECRET_KEY),  SignatureAlgorithm.HS512).compact();
        return "Bearer " + token;
    }


}

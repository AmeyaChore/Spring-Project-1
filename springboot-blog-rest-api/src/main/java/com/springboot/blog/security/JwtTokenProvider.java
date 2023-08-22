package com.springboot.blog.security;

import com.springboot.blog.exceptons.BlogAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

//4.JWT
@Component// to make this class a spring bean
public class JwtTokenProvider {

    //we have to pass the property key so we can get Value from application.properties file
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    //we have to pass the property key so we can get Value from application.properties file
    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    //generate JWT token
    public String generateToken(Authentication authentication){
        String username=authentication.getName();

        //current date to mark login time
        Date currentDate=new Date();

        //expiration date to set login valid up to 7 days
        Date expireDate=new Date(currentDate.getTime()+jwtExpirationDate);

        String token=Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();

        return token;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    //get Username from jwp token
    public String getUsername(String token){
        Claims claims=Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJwt(token)
                .getBody();

        String username= claims.getSubject();
        return username;
    }

    //Validate JWT token
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        }  catch (MalformedJwtException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Invalid JWT token");
        }catch (ExpiredJwtException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Expired JWt token");
        }catch (UnsupportedJwtException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"JWt claims string is Empty");
        }

    }
}

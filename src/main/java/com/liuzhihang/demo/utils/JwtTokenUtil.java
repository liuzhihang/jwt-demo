package com.liuzhihang.demo.utils;

import com.liuzhihang.demo.bean.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

/**
 * 使用 java-jwt jwt类库
 *
 * @author liuzhihang
 * @date 2019-06-05 09:22
 */
@Component
public class JwtTokenUtil {

    private static final SignatureAlgorithm SIGN_TYPE = SignatureAlgorithm.HS256;

    public static final String SECRET = "jwt-secret";

    /**
     * JWT超时时间
     */
    public static final long EXPIRED_TIME = 7 * 24 * 60 * 60 * 1000L;

    /**
     * claims 为自定义的私有声明, 要放在前面
     * <p>
     * 生成token
     */
    public String generateToken(UserDetails userDetails) {

        long instantNow = Instant.now().toEpochMilli();

        Claims claims = Jwts.claims();
        claims.put(Claims.SUBJECT, userDetails.getUsername());

        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(instantNow))
                .setExpiration(new Date(instantNow + EXPIRED_TIME))
                .signWith(SIGN_TYPE, SECRET).compact();
    }

    /**
     * claims 为自定义的私有声明, 要放在前面
     * <p>
     * 生成token
     */
    public String generateToken(String userName) {

        long instantNow = Instant.now().toEpochMilli();

        Claims claims = Jwts.claims();
        claims.put(Claims.SUBJECT, userName);

        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(instantNow))
                .setExpiration(new Date(instantNow + EXPIRED_TIME))
                .signWith(SIGN_TYPE, SECRET).compact();
    }

    /**
     * 将token解析, 映射为 UserDetails
     *
     * @param jwtToken
     * @return
     */
    public UserDetails getUserDetailsFromToken(String jwtToken) {

        Claims claimsFromToken = getClaimsFromToken(jwtToken);

        String userName = claimsFromToken.get(Claims.SUBJECT, String.class);

        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUsername(userName);

        return userDetails;
    }

    /**
     * 验证token
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        UserDetailsImpl user = (UserDetailsImpl) userDetails;
        String username = getPhoneNoFromToken(token);

        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);

            long instantNow = Instant.now().toEpochMilli();

            refreshedToken = Jwts.builder().setClaims(claims).setIssuedAt(new Date(instantNow))
                    .setExpiration(new Date(instantNow + EXPIRED_TIME))
                    .signWith(SIGN_TYPE, SECRET).compact();
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 获取token是否过期
     */
    public Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 根据token获取username
     */
    public String getPhoneNoFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 获取token的过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    /**
     * 解析JWT
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

}

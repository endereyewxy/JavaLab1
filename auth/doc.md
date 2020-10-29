# API说明

### 检查登录状态

```java
import io.jsonwebtoken.*;

class Demo {
    public int checkSignInStatus(String token) {
        final Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(KEY_REFRESH.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            // 目前处于登录状态
            // 打印用户名
            System.out.println(claims.get("username"));
            // 返回用户ID
            return claims.get("id");
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            return null;
        }
    }
}
```

### 登录

重定向到`/auth/sign-in/?from=<from-url>`，其中`<from-url>`是登录成功之后跳转到的URL。

TODO 如果`<from-url>`为空怎么办？

### 登出

重定向到`/auth/sign-out/?from=<from-url>`，之后会自动跳转到`<from-url>`。

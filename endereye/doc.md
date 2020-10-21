# 资源服务器约定

 - 令牌存储在Cookie中，名字是`token`，形式是一个JWT，Secret Key自行约定。
 - 令牌包含用户ID，用户名，签发时间，过期时间，具体参考 [这篇博客](https://blog.csdn.net/CSDN906214391/article/details/98633667) 。
 - 令牌的有效时间很短。
 - 需要重新登录时，需要重定向到`/auth/sign-in/?from=/example/resource/service`，其中`from`参数是登录成功后跳转到的URL。

# 校验服务器

 - 实际上有两种令牌，`access token`和`refresh token`，使用不同的Secret Key。
 - `access token`就是资源服务器可见的令牌。
 - `refresh token`实质上是用户密码的替代品，有效时间较长，重新登录时，服务器会续签`refresh token`。

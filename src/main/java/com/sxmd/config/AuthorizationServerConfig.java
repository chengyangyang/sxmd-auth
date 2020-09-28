package com.sxmd.config;

import com.sxmd.base.BaseUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  授权服务配置
 *
 * @author cy
 * @date 2020年09月02日 9:56
 * Version 1.0
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * jwt 密钥前缀，和授权中心保持一致
     */
    private static final String PRE_SIGNING_KEY = "SXMD-";

    @Resource
    private DataSource dataSource;
    @Resource
    private AuthenticationManager authenticationManager;

    @Value("${sxmd.jwt.signing-key:}")
    private String signingKey;

    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * Description:   配置客户端信息
     *
     * @param clients:
     * @return void
     * @author cy
     * @date 2020/8/18 17:59
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //这个地方指的是从jdbc查出数据来存储
        clients.withClientDetails(jdbcClientDetailsService());
///        clients.inMemory()
///                .withClient("sxmd")
///                .secret(passwordEncoder.encode("sxmd"))
///                .scopes("service")
///                // 登录后是否自动授权
///                .autoApprove(false)
///                .authorizedGrantTypes("implicit", "refresh_token", "password", "authorization_code")
///                .redirectUris("http://localhost:22225/")
///                .accessTokenValiditySeconds(12 * 300);//5min过期
    }


    /**
     * Description:   配置授权token 的节点
     *
     * @param endpoints:
     * @return void
     * @author cy
     * @date 2020/8/18 18:38
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // token 的存储方式
        endpoints.tokenStore(jwtStore())
                // 开启密码验证 来自 WebSecurityConfigurerAdapter
                .authenticationManager(authenticationManager)
                .tokenEnhancer(jwtTokenEnhancer())
                .reuseRefreshTokens(false);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .allowFormAuthenticationForClients()
                .checkTokenAccess("permitAll()");
    }


    @Bean
    public TokenStore jwtStore() {
        TokenStore tokenStore = new JwtTokenStore(jwtTokenEnhancer());
        return tokenStore;
    }


    /**
     * Description:   jwt token 的生成
     *
     * @param :
     * @return org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
     * @author cy
     * @date 2020/8/18 18:32
     */
    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        // 重写增强token的方法，自定义返回
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                // 与登录时候放进去的UserDetail实现类一直查看link{SecurityConfiguration}
                BaseUser user = (BaseUser) authentication.getUserAuthentication().getPrincipal();
                /** 自定义一些token属性 ***/
                final Map<String, Object> additionalInformation = new HashMap<>(2);
                // 里面包含了用户的权限和用户名称
                additionalInformation.put("userId", user.getId());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
                return enhancedToken;
            }
        };
        converter.setSigningKey(PRE_SIGNING_KEY + signingKey);
        return converter;
    }

}

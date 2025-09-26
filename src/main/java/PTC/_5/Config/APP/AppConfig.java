package PTC._5.Config.APP;

import PTC._5.Utils.JWTCookieAuthFilter;
import PTC._5.Utils.JWTUtils;
import org.springframework.context.annotation.Bean;

public class AppConfig
{
    @Bean
    public JWTCookieAuthFilter jwtCookieAuthFilter(JWTUtils jwtUtils)
    {
        return new JWTCookieAuthFilter(jwtUtils);
    }
}

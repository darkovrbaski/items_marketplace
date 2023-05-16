package me.darkovrbaski.items.marketplace.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpsRedirectConfig {

  private static final String HTTP_SCHEME = "http";

  @Value("${server.http.port}")
  private int serverHttpPort;

  @Bean
  public ServletWebServerFactory servletContainer() {
    final TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
    tomcat.addAdditionalTomcatConnectors(httpConnector());
    return tomcat;
  }

  private Connector httpConnector() {
    final Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
    connector.setScheme(HTTP_SCHEME);
    connector.setPort(serverHttpPort);
    connector.setSecure(false);
    return connector;
  }
}

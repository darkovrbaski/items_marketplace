package me.darkovrbaski.items.marketplace.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpsRedirectConfig {

  private static final String REDIRECT_PATTERN = "/*";
  private static final String HTTP_SCHEME = "http";
  private static final String CONFIDENTIAL = "CONFIDENTIAL";

  @Value("${server.port}")
  private int serverPort;

  @Value("${server.http.port}")
  private int serverHttpPort;

  @Bean
  public ServletWebServerFactory servletContainer() {
    final TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
      @Override
      protected void postProcessContext(final Context context) {
        final SecurityConstraint securityConstraint = new SecurityConstraint();
        securityConstraint.setUserConstraint(CONFIDENTIAL);
        final SecurityCollection collection = new SecurityCollection();
        collection.addPattern(REDIRECT_PATTERN);
        securityConstraint.addCollection(collection);
        context.addConstraint(securityConstraint);
      }
    };

    tomcat.addAdditionalTomcatConnectors(redirectConnector());
    return tomcat;
  }

  private Connector redirectConnector() {
    final Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
    connector.setScheme(HTTP_SCHEME);
    connector.setPort(serverHttpPort);
    connector.setSecure(false);
    connector.setRedirectPort(serverPort);

    return connector;
  }
}

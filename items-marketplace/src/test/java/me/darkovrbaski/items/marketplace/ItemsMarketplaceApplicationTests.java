package me.darkovrbaski.items.marketplace;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ItemsMarketplaceApplicationTests {

  @Test
  void contextLoads(final ApplicationContext context) {
    assertThat(context).isNotNull();
  }

}

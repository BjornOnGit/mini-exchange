package dev.minix.common.money;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;

class MoneyStringPropertyTest {

  @Property
  void roundTripsThroughPlainString(
      @ForAll @IntRange(min = 0, max = 18) int scale, @ForAll long unscaled) {
    Asset asset = new Asset("TEST", scale);
    Money money = new Money(asset, BigDecimal.valueOf(unscaled, scale));

    assertThat(Money.parse(asset, money.toPlainString())).isEqualTo(money);
  }
}

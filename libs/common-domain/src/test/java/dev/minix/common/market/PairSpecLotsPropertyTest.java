package dev.minix.common.market;

import static org.assertj.core.api.Assertions.assertThat;

import dev.minix.common.money.Asset;
import java.math.BigDecimal;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.LongRange;

class PairSpecLotsPropertyTest {

  private static final Pair PAIR = new Pair(new Asset("BTC", 8), new Asset("USDT", 6));

  @Property
  void lotsRoundTripThroughQuantity(
      @ForAll @LongRange(min = 1) long lots,
      @ForAll @LongRange(min = 1, max = 1_000_000) long lotUnscaled,
      @ForAll @IntRange(min = 0, max = 8) int lotScale) {
    PairSpec spec =
        new PairSpec(
            PAIR, BigDecimal.ONE, BigDecimal.valueOf(lotUnscaled, lotScale), BigDecimal.ONE);

    assertThat(spec.qtyToLots(spec.lotsToQty(lots))).isEqualTo(lots);
  }
}

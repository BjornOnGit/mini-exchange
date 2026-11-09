package dev.minix.common.market;

import static org.assertj.core.api.Assertions.assertThat;

import dev.minix.common.money.Asset;
import java.math.BigDecimal;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.LongRange;

class PairSpecTicksPropertyTest {

  private static final Pair PAIR = new Pair(new Asset("BTC", 8), new Asset("USDT", 6));

  @Property
  void ticksRoundTripThroughPrice(
      @ForAll @LongRange(min = 1) long ticks,
      @ForAll @LongRange(min = 1, max = 1_000_000) long tickUnscaled,
      @ForAll @IntRange(min = 0, max = 8) int tickScale) {
    PairSpec spec =
        new PairSpec(
            PAIR, BigDecimal.valueOf(tickUnscaled, tickScale), BigDecimal.ONE, BigDecimal.ONE);

    assertThat(spec.priceToTicks(spec.ticksToPrice(ticks))).isEqualTo(ticks);
  }
}

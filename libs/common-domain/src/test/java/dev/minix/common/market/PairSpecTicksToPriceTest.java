package dev.minix.common.market;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import dev.minix.common.money.Asset;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PairSpecTicksToPriceTest {

  private static final Pair PAIR = new Pair(new Asset("BTC", 8), new Asset("USDT", 6));
  private static final PairSpec SPEC =
      new PairSpec(PAIR, new BigDecimal("0.01"), new BigDecimal("0.0001"), BigDecimal.TEN);

  @Test
  void convertsTicksToExactPrice() {
    assertThat(SPEC.ticksToPrice(10001L)).isEqualByComparingTo("100.01");
    assertThat(SPEC.ticksToPrice(1L)).isEqualByComparingTo("0.01");
  }

  @ParameterizedTest
  @ValueSource(longs = {0L, -1L, Long.MIN_VALUE})
  void rejectsZeroAndNegativeTicks(long ticks) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> SPEC.ticksToPrice(ticks))
        .withMessageContaining("positive");
  }
}

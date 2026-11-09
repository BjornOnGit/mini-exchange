package dev.minix.common.market;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import dev.minix.common.money.Asset;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PairSpecTest {

  private static final Pair PAIR = new Pair(new Asset("BTC", 8), new Asset("USDT", 6));
  private static final BigDecimal TICK = new BigDecimal("0.01");
  private static final BigDecimal LOT = new BigDecimal("0.0001");
  private static final BigDecimal MIN_NOTIONAL = new BigDecimal("10");

  @Test
  void createsSpecWithPositiveValues() {
    PairSpec spec = new PairSpec(PAIR, TICK, LOT, MIN_NOTIONAL);

    assertThat(spec.pair()).isEqualTo(PAIR);
    assertThat(spec.tickSize()).isEqualByComparingTo("0.01");
    assertThat(spec.lotSize()).isEqualByComparingTo("0.0001");
    assertThat(spec.minNotional()).isEqualByComparingTo("10");
  }

  @ParameterizedTest
  @ValueSource(strings = {"0", "-0.01"})
  void rejectsNonPositiveTickSize(String value) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new PairSpec(PAIR, new BigDecimal(value), LOT, MIN_NOTIONAL))
        .withMessageContaining("tickSize");
  }

  @ParameterizedTest
  @ValueSource(strings = {"0", "-0.0001"})
  void rejectsNonPositiveLotSize(String value) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new PairSpec(PAIR, TICK, new BigDecimal(value), MIN_NOTIONAL))
        .withMessageContaining("lotSize");
  }

  @ParameterizedTest
  @ValueSource(strings = {"0", "-10"})
  void rejectsNonPositiveMinNotional(String value) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new PairSpec(PAIR, TICK, LOT, new BigDecimal(value)))
        .withMessageContaining("minNotional");
  }

  @Test
  void rejectsNullPair() {
    assertThatNullPointerException().isThrownBy(() -> new PairSpec(null, TICK, LOT, MIN_NOTIONAL));
  }
}

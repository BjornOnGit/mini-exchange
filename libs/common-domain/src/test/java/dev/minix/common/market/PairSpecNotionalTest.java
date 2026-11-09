package dev.minix.common.market;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import dev.minix.common.money.Asset;
import dev.minix.common.money.Money;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PairSpecNotionalTest {

  private static final Asset BTC = new Asset("BTC", 8);
  private static final Asset USDT = new Asset("USDT", 6);
  private static final Pair PAIR = new Pair(BTC, USDT);
  private static final PairSpec SPEC =
      new PairSpec(PAIR, new BigDecimal("0.01"), new BigDecimal("0.0001"), BigDecimal.TEN);

  @Test
  void computesNotionalInTheQuoteAsset() {
    Money notional = SPEC.notional(10000L, 15000L);

    assertThat(notional).isEqualTo(new Money(USDT, new BigDecimal("150")));
    assertThat(notional.toPlainString()).isEqualTo("150.000000");
  }

  @Test
  void computesSmallestExactDecimalNotional() {
    Money notional = SPEC.notional(1L, 1L);

    assertThat(notional.toPlainString()).isEqualTo("0.000001");
  }

  @Test
  void throwsOnOverflow() {
    assertThatExceptionOfType(ArithmeticException.class)
        .isThrownBy(() -> SPEC.notional(Long.MAX_VALUE, 2L));
  }

  @Test
  void rejectsNotionalFinerThanTheQuoteScale() {
    PairSpec fine =
        new PairSpec(PAIR, new BigDecimal("0.001"), new BigDecimal("0.0001"), BigDecimal.TEN);

    assertThatIllegalArgumentException()
        .isThrownBy(() -> fine.notional(1L, 1L))
        .withMessageContaining("fractional digits");
  }

  @ParameterizedTest
  @ValueSource(longs = {0L, -1L})
  void rejectsNonPositivePriceTicks(long priceTicks) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> SPEC.notional(priceTicks, 1L))
        .withMessageContaining("priceTicks");
  }

  @ParameterizedTest
  @ValueSource(longs = {0L, -1L})
  void rejectsNonPositiveLots(long lots) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> SPEC.notional(1L, lots))
        .withMessageContaining("lots");
  }
}

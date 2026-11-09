package dev.minix.common.market;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import dev.minix.common.money.Asset;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PairSpecTicksTest {

  private static final Pair PAIR = new Pair(new Asset("BTC", 8), new Asset("USDT", 6));
  private static final PairSpec SPEC =
      new PairSpec(PAIR, new BigDecimal("0.01"), new BigDecimal("0.0001"), BigDecimal.TEN);

  @Test
  void convertsExactMultipleToTicks() {
    assertThat(SPEC.priceToTicks(new BigDecimal("100.01"))).isEqualTo(10001L);
  }

  @Test
  void ignoresTrailingZerosInThePrice() {
    assertThat(SPEC.priceToTicks(new BigDecimal("100.0100"))).isEqualTo(10001L);
    assertThat(SPEC.priceToTicks(new BigDecimal("100"))).isEqualTo(10000L);
  }

  @Test
  void rejectsPriceThatIsNotAMultipleOfTickSize() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> SPEC.priceToTicks(new BigDecimal("100.005")))
        .withMessageContaining("multiple");
  }

  @ParameterizedTest
  @ValueSource(strings = {"0", "0.00", "-0.01", "-100"})
  void rejectsZeroAndNegativePrices(String price) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> SPEC.priceToTicks(new BigDecimal(price)))
        .withMessageContaining("positive");
  }

  @Test
  void rejectsPriceThatOverflowsLong() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> SPEC.priceToTicks(new BigDecimal("1E+20")))
        .withMessageContaining("too large");
  }

  @Test
  void rejectsNullPrice() {
    assertThatNullPointerException().isThrownBy(() -> SPEC.priceToTicks(null));
  }
}

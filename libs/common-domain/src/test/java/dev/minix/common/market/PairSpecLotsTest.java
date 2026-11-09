package dev.minix.common.market;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import dev.minix.common.money.Asset;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PairSpecLotsTest {

  private static final Pair PAIR = new Pair(new Asset("BTC", 8), new Asset("USDT", 6));
  private static final PairSpec SPEC =
      new PairSpec(PAIR, new BigDecimal("0.01"), new BigDecimal("0.0001"), BigDecimal.TEN);

  @Test
  void convertsExactMultipleToLots() {
    assertThat(SPEC.qtyToLots(new BigDecimal("1.5"))).isEqualTo(15000L);
    assertThat(SPEC.qtyToLots(new BigDecimal("0.0001"))).isEqualTo(1L);
  }

  @Test
  void rejectsQuantityThatIsNotAMultipleOfLotSize() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> SPEC.qtyToLots(new BigDecimal("0.00005")))
        .withMessageContaining("multiple");
  }

  @ParameterizedTest
  @ValueSource(strings = {"0", "-0.0001", "-1"})
  void rejectsZeroAndNegativeQuantities(String quantity) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> SPEC.qtyToLots(new BigDecimal(quantity)))
        .withMessageContaining("positive");
  }

  @Test
  void rejectsQuantityThatOverflowsLong() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> SPEC.qtyToLots(new BigDecimal("1E+20")))
        .withMessageContaining("too large");
  }

  @Test
  void rejectsNullQuantity() {
    assertThatNullPointerException().isThrownBy(() -> SPEC.qtyToLots(null));
  }

  @Test
  void convertsLotsToExactQuantity() {
    assertThat(SPEC.lotsToQty(15000L)).isEqualByComparingTo("1.5");
    assertThat(SPEC.lotsToQty(1L)).isEqualByComparingTo("0.0001");
  }

  @ParameterizedTest
  @ValueSource(longs = {0L, -1L, Long.MIN_VALUE})
  void rejectsZeroAndNegativeLots(long lots) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> SPEC.lotsToQty(lots))
        .withMessageContaining("positive");
  }
}

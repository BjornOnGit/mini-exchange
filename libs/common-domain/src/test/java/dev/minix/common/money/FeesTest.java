package dev.minix.common.money;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class FeesTest {

  private static final Asset USDT = new Asset("USDT", 6);

  private static Money usdt(String amount) {
    return new Money(USDT, new BigDecimal(amount));
  }

  @Test
  void chargesTwentyBpsOnAHundred() {
    assertThat(Fees.fee(usdt("100.000000"), 20).toPlainString()).isEqualTo("0.200000");
  }

  @Test
  void roundsUpTheSmallestAmount() {
    assertThat(Fees.fee(usdt("0.000001"), 20).toPlainString()).isEqualTo("0.000001");
  }

  @Test
  void roundsUpAnyRemainder() {
    assertThat(Fees.fee(usdt("0.333333"), 30).toPlainString()).isEqualTo("0.001000");
  }

  @Test
  void doesNotRoundWhenExact() {
    assertThat(Fees.fee(usdt("1000"), 10).toPlainString()).isEqualTo("1.000000");
  }

  @Test
  void zeroBpsOrZeroNotionalGivesZeroFee() {
    assertThat(Fees.fee(usdt("100"), 0).isZero()).isTrue();
    assertThat(Fees.fee(usdt("0"), 20).isZero()).isTrue();
  }

  @Test
  void keepsTheNotionalsAssetAndScale() {
    Money btc = new Money(new Asset("BTC", 8), new BigDecimal("1"));

    assertThat(Fees.fee(btc, 10).toPlainString()).isEqualTo("0.00100000");
    assertThat(Fees.fee(btc, 10).asset()).isEqualTo(btc.asset());
  }

  @Test
  void rejectsNegativeBps() {
    assertThatIllegalArgumentException().isThrownBy(() -> Fees.fee(usdt("100"), -1));
  }

  @Test
  void rejectsNegativeNotional() {
    assertThatIllegalArgumentException().isThrownBy(() -> Fees.fee(usdt("-1"), 20));
  }

  @Test
  void rejectsNullNotional() {
    assertThatNullPointerException().isThrownBy(() -> Fees.fee(null, 20));
  }
}

package dev.minix.common.money;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneySignTest {

  private static final Asset USDT = new Asset("USDT", 6);

  private static Money usdt(String amount) {
    return new Money(USDT, new BigDecimal(amount));
  }

  @Test
  void negateFlipsTheSign() {
    assertThat(usdt("1.5").negate()).isEqualTo(usdt("-1.5"));
    assertThat(usdt("-1.5").negate()).isEqualTo(usdt("1.5"));
  }

  @Test
  void negateOfZeroIsZero() {
    Money negated = usdt("0").negate();

    assertThat(negated).isEqualTo(usdt("0"));
    assertThat(negated.isZero()).isTrue();
    assertThat(negated.isNegative()).isFalse();
  }

  @Test
  void negativeZeroInputIsPlainZero() {
    Money zero = usdt("-0.000000");

    assertThat(zero).isEqualTo(usdt("0"));
    assertThat(zero.isZero()).isTrue();
    assertThat(zero.isNegative()).isFalse();
    assertThat(zero.isPositive()).isFalse();
  }

  @Test
  void isZeroIsTrueOnlyForZero() {
    assertThat(usdt("0").isZero()).isTrue();
    assertThat(usdt("0.000001").isZero()).isFalse();
    assertThat(usdt("-0.000001").isZero()).isFalse();
  }

  @Test
  void isNegativeIsTrueOnlyBelowZero() {
    assertThat(usdt("-1").isNegative()).isTrue();
    assertThat(usdt("0").isNegative()).isFalse();
    assertThat(usdt("1").isNegative()).isFalse();
  }

  @Test
  void isPositiveIsTrueOnlyAboveZero() {
    assertThat(usdt("1").isPositive()).isTrue();
    assertThat(usdt("0").isPositive()).isFalse();
    assertThat(usdt("-1").isPositive()).isFalse();
  }

  @Test
  void compareToOrdersAmountsOfTheSameAsset() {
    assertThat(usdt("1").compareTo(usdt("2"))).isNegative();
    assertThat(usdt("2").compareTo(usdt("1"))).isPositive();
    assertThat(usdt("1").compareTo(usdt("1.000000"))).isZero();
  }

  @Test
  void compareToRejectsDifferentAsset() {
    Money btc = new Money(new Asset("BTC", 8), BigDecimal.ONE);

    assertThatIllegalArgumentException().isThrownBy(() -> usdt("1").compareTo(btc));
  }
}

package dev.minix.common.money;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MoneyStringTest {

  private static final Asset USDT = new Asset("USDT", 6);

  @Test
  void toPlainStringHasNoExponent() {
    Money money = new Money(new Asset("BTC", 8), new BigDecimal("1E+3"));

    assertThat(money.toPlainString()).isEqualTo("1000.00000000");
  }

  @Test
  void parsesPlainDecimalAtAssetScale() {
    assertThat(Money.parse(USDT, "1.23").toPlainString()).isEqualTo("1.230000");
    assertThat(Money.parse(USDT, "1").toPlainString()).isEqualTo("1.000000");
    assertThat(Money.parse(USDT, "-1.5").toPlainString()).isEqualTo("-1.500000");
  }

  @Test
  void rejectsExponentForm() {
    assertThatIllegalArgumentException().isThrownBy(() -> Money.parse(USDT, "1E+3"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"1e3", "1E3", "", " ", " 1", "1 ", "+1", ".5", "1.", "abc", "1,5"})
  void rejectsNonPlainDecimalText(String text) {
    assertThatIllegalArgumentException().isThrownBy(() -> Money.parse(USDT, text));
  }

  @ParameterizedTest
  @ValueSource(strings = {"1.1234567", "1.2300000"})
  void rejectsMoreFractionalDigitsThanAssetScale(String text) {
    assertThatIllegalArgumentException().isThrownBy(() -> Money.parse(USDT, text));
  }
}

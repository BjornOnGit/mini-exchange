package dev.minix.common.money;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneyTest {

  private static final Asset USDT = new Asset("USDT", 6);

  @Test
  void acceptsAmountWithinAssetScale() {
    Money money = new Money(USDT, new BigDecimal("1.23"));

    assertThat(money.amount().toPlainString()).isEqualTo("1.230000");
  }

  @Test
  void rejectsMoreFractionalDigitsThanAssetScale() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Money(USDT, new BigDecimal("1.1234567")));
  }

  @Test
  void normalizesWholeNumberToAssetScale() {
    Money money = new Money(USDT, new BigDecimal("1"));

    assertThat(money.amount().toPlainString()).isEqualTo("1.000000");
    assertThat(money.amount().scale()).isEqualTo(6);
  }

  @Test
  void acceptsExtraTrailingZerosBeyondAssetScale() {
    Money money = new Money(USDT, new BigDecimal("1.2300000000"));

    assertThat(money.amount().toPlainString()).isEqualTo("1.230000");
  }

  @Test
  void rejectsFractionForZeroScaleAsset() {
    Asset jpy = new Asset("JPY", 0);

    assertThatIllegalArgumentException().isThrownBy(() -> new Money(jpy, new BigDecimal("1.5")));
  }

  @Test
  void equalAmountsAreEqualRegardlessOfInputScale() {
    assertThat(new Money(USDT, new BigDecimal("1")))
        .isEqualTo(new Money(USDT, new BigDecimal("1.000000")));
  }

  @Test
  void rejectsNullAsset() {
    assertThatNullPointerException().isThrownBy(() -> new Money(null, BigDecimal.ONE));
  }

  @Test
  void rejectsNullAmount() {
    assertThatNullPointerException().isThrownBy(() -> new Money(USDT, null));
  }
}

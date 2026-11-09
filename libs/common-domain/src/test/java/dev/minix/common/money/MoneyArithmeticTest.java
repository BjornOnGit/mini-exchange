package dev.minix.common.money;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneyArithmeticTest {

  private static final Asset USDT = new Asset("USDT", 6);
  private static final Asset BTC = new Asset("BTC", 8);

  private static Money usdt(String amount) {
    return new Money(USDT, new BigDecimal(amount));
  }

  @Test
  void addsAmountsOfTheSameAsset() {
    Money sum = usdt("1.5").plus(usdt("2.25"));

    assertThat(sum).isEqualTo(usdt("3.75"));
    assertThat(sum.amount().toPlainString()).isEqualTo("3.750000");
  }

  @Test
  void subtractsAmountsOfTheSameAsset() {
    Money difference = usdt("5").minus(usdt("1.25"));

    assertThat(difference).isEqualTo(usdt("3.75"));
  }

  @Test
  void subtractionCanProduceNegativeAmount() {
    Money difference = usdt("1").minus(usdt("2"));

    assertThat(difference.amount().toPlainString()).isEqualTo("-1.000000");
  }

  @Test
  void plusRejectsDifferentAsset() {
    Money btc = new Money(BTC, BigDecimal.ONE);

    assertThatIllegalArgumentException().isThrownBy(() -> usdt("1").plus(btc));
  }

  @Test
  void minusRejectsDifferentAsset() {
    Money btc = new Money(BTC, BigDecimal.ONE);

    assertThatIllegalArgumentException().isThrownBy(() -> usdt("1").minus(btc));
  }

  @Test
  void sameSymbolWithDifferentScaleIsAMismatch() {
    Money otherUsdt = new Money(new Asset("USDT", 8), BigDecimal.ONE);

    assertThatIllegalArgumentException().isThrownBy(() -> usdt("1").plus(otherUsdt));
  }
}

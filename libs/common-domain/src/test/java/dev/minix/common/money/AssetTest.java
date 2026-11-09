package dev.minix.common.money;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AssetTest {

  @Test
  void createsAssetWithSymbolAndScale() {
    Asset usdt = new Asset("USDT", 6);

    assertThat(usdt.symbol()).isEqualTo("USDT");
    assertThat(usdt.scale()).isEqualTo(6);
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 18})
  void acceptsBoundaryScales(int scale) {
    assertThat(new Asset("BTC", scale).scale()).isEqualTo(scale);
  }

  @Test
  void rejectsNullSymbol() {
    assertThatIllegalArgumentException().isThrownBy(() -> new Asset(null, 6));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "   "})
  void rejectsBlankSymbol(String symbol) {
    assertThatIllegalArgumentException().isThrownBy(() -> new Asset(symbol, 6));
  }

  @ParameterizedTest
  @ValueSource(strings = {"usdt", "Usdt"})
  void rejectsNonUppercaseSymbol(String symbol) {
    assertThatIllegalArgumentException().isThrownBy(() -> new Asset(symbol, 6));
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 19})
  void rejectsScaleOutsideRange(int scale) {
    assertThatIllegalArgumentException().isThrownBy(() -> new Asset("USDT", scale));
  }
}

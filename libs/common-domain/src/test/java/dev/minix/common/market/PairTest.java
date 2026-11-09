package dev.minix.common.market;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import dev.minix.common.money.Asset;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PairTest {

  private static final Asset BTC = new Asset("BTC", 8);
  private static final Asset USDT = new Asset("USDT", 6);
  private static final Map<String, Asset> ASSETS = Map.of("BTC", BTC, "USDT", USDT);

  @Test
  void symbolJoinsBaseAndQuoteWithDash() {
    assertThat(new Pair(BTC, USDT).symbol()).isEqualTo("BTC-USDT");
  }

  @Test
  void parsesKnownAssets() {
    Pair pair = Pair.parse("BTC-USDT", ASSETS);

    assertThat(pair).isEqualTo(new Pair(BTC, USDT));
    assertThat(pair.symbol()).isEqualTo("BTC-USDT");
  }

  @ParameterizedTest
  @ValueSource(strings = {"DOGE-USDT", "BTC-XYZ"})
  void rejectsUnknownAsset(String text) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> Pair.parse(text, ASSETS))
        .withMessageContaining("unknown asset");
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "BTCUSDT", "BTC-", "-USDT", "-", "BTC-USDT-ETH"})
  void rejectsMalformedText(String text) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> Pair.parse(text, ASSETS))
        .withMessageContaining("malformed pair");
  }
}

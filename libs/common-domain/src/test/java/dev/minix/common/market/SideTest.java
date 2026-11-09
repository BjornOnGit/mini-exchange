package dev.minix.common.market;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SideTest {

  @Test
  void oppositeOfBuyIsSell() {
    assertThat(Side.BUY.opposite()).isEqualTo(Side.SELL);
  }

  @Test
  void oppositeOfSellIsBuy() {
    assertThat(Side.SELL.opposite()).isEqualTo(Side.BUY);
  }

  @Test
  void oppositeOfOppositeIsTheSameSide() {
    for (Side side : Side.values()) {
      assertThat(side.opposite().opposite()).isEqualTo(side);
    }
  }
}

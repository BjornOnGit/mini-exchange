package dev.minix.common.ids;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TypedIdsTest {

  private static final String UUID_TEXT = "123e4567-e89b-12d3-a456-426614174000";
  private static final List<String> INVALID =
      Arrays.asList(
          null, "", " ", "   ", "has space", "a/b", "tab\there", "x".repeat(65), "caf\u00e9");

  static Stream<Arguments> idTypes() {
    return Stream.of(
        Arguments.of("OrderId", (Function<String, Object>) OrderId::parse),
        Arguments.of("TradeId", (Function<String, Object>) TradeId::parse),
        Arguments.of("AccountId", (Function<String, Object>) AccountId::parse),
        Arguments.of("ClientOrderId", (Function<String, Object>) ClientOrderId::parse));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("idTypes")
  void parsesAndPrintsTheValue(String name, Function<String, Object> parse) {
    assertThat(parse.apply(UUID_TEXT)).hasToString(UUID_TEXT);
    assertThat(parse.apply("a_B-9")).hasToString("a_B-9");
    assertThat(parse.apply("x".repeat(64))).hasToString("x".repeat(64));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("idTypes")
  void equalValuesAreEqual(String name, Function<String, Object> parse) {
    assertThat(parse.apply("abc")).isEqualTo(parse.apply("abc"));
    assertThat(parse.apply("abc")).hasSameHashCodeAs(parse.apply("abc"));
    assertThat(parse.apply("abc")).isNotEqualTo(parse.apply("abd"));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("idTypes")
  void rejectsBlankAndInvalidValues(String name, Function<String, Object> parse) {
    for (String bad : INVALID) {
      assertThatIllegalArgumentException()
          .as("%s should reject [%s]", name, bad)
          .isThrownBy(() -> parse.apply(bad));
    }
  }

  @Test
  void differentTypesWithTheSameValueAreNotEqual() {
    assertThat(OrderId.parse("abc")).isNotEqualTo(TradeId.parse("abc"));
  }
}

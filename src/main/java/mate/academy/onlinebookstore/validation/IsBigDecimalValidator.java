package mate.academy.onlinebookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.regex.Pattern;

/*
 There should be an implementation of my custom validation.
  But it's better to make the price field as string type, and then
  make check if it matches the BigDecimal pattern or not.
  Here is just an example how a custom annotation works.
 */
public class IsBigDecimalValidator implements ConstraintValidator<IsBigDecimal, BigDecimal> {
    private static final String PATTERN_OF_BIGDECIMAL = "[+]?\\d*[,.]?\\d*";

    @Override
    public boolean isValid(BigDecimal bigDecimal,
                           ConstraintValidatorContext constraintValidatorContext) {
        return bigDecimal != null && Pattern.compile(PATTERN_OF_BIGDECIMAL)
                .matcher(bigDecimal.toString()).matches();
    }
}

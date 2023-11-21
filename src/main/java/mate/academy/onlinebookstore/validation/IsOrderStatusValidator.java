package mate.academy.onlinebookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import mate.academy.onlinebookstore.model.Order;

public class IsOrderStatusValidator implements ConstraintValidator<IsOrderStatus, String> {

    @Override
    public boolean isValid(String status,
                           ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(Order.Status.values())
                .map(Enum::toString)
                .anyMatch(s -> s.equals(status));
    }
}

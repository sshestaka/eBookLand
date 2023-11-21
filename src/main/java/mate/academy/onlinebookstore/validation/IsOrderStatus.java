package mate.academy.onlinebookstore.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IsOrderStatusValidator.class)
@Target({ElementType.FIELD, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsOrderStatus {
    String message() default "invalid format Status";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

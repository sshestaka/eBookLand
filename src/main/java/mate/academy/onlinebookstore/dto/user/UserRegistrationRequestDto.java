package mate.academy.onlinebookstore.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import mate.academy.onlinebookstore.validation.FieldsValueMatch;

@Data
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "repeatPassword",
                message = "Passwords do not match!"
        )
})
public class UserRegistrationRequestDto {
    @NotEmpty
    @Size(min = 4, max = 50)
    private String email;
    @NotEmpty
    @Size(min = 4, max = 50)
    private String password;
    @NotEmpty
    @Size(min = 4, max = 50)
    private String repeatPassword;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String shippingAddress;
}

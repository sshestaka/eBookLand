package mate.academy.onlinebookstore.dto.user;

import lombok.Data;

@Data
public class UpdateUserRequestDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String shippingAddress;
}

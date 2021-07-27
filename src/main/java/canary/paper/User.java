package canary.paper;

import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long id;

    @Column(name = "email")
    @Email(message = "*Invalid")
    @NotEmpty(message = "*Required")
    private String email;

    @Column(name = "first_name")
    @NotEmpty(message = "*Required")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "*Required")
    private String lastName;

    @Column(name = "password")
    @NotEmpty(message = "*Required")
    @Size(min = 5, message = "*Must be at least 5 characters")
    private String password;

}
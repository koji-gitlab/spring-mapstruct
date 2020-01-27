package com.mitrais.app.mapper.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter @Setter
public class RegisterUser {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    // http://regexlib.com/REDetails.aspx?regexp_id=2967
    @NotEmpty
    @Pattern(regexp = "^((?:\\+62|62)|0)[2-9]{1}[0-9]+$", message = "{mobileNo.pattern}")
    private String mobileNo;

    @NotEmpty
    @Email
    private String email;

    @Past
    private LocalDate dob;

    private Boolean gender;

}

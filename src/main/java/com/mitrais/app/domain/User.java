package com.mitrais.app.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_mobile_no", columnNames = {"mobileNo"}),
                @UniqueConstraint(name = "uk_email", columnNames = {"email"})
        }
)
public class User implements Serializable {

    @Basic(optional = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NotEmpty
    @Basic(optional = false)
    private String firstName;

    @NotEmpty
    @Basic(optional = false)
    private String lastName;

    // http://regexlib.com/REDetails.aspx?regexp_id=2967
    @NotEmpty
    @Pattern(regexp = "^((?:\\+62|62)|0)[2-9]{1}[0-9]+$", message = "{mobileNo.pattern}")
    @Basic(optional = false)
    private String mobileNo;

    @NotEmpty
    @Email
    @Basic(optional = false)
    private String email;

    @Past
    @Basic
    private LocalDate dob;

    @Basic
    private Boolean gender;

}
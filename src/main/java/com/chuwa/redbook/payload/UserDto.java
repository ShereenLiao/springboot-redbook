package com.chuwa.redbook.payload;

import com.chuwa.redbook.util.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UserDto {
    private Long userId;
    @NotEmpty
    private String name;
    private Gender gender;
    private String bio;
}

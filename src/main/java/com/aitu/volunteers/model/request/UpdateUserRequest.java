package com.aitu.volunteers.model.request;

import com.aitu.volunteers.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String firstname;
    private String lastname;
    private Gender gender;
    private String IIN;
    private String phone;
    private Byte kazakh;
    private Byte russian;
    private Byte english;
}

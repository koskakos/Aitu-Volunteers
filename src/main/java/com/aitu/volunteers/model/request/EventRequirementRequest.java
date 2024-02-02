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
public class EventRequirementRequest {
    private Gender gender;
    private Integer age;
    private Byte kazakh;
    private Byte russian;
    private Byte english;
    private Boolean isCertificateRequired;
}

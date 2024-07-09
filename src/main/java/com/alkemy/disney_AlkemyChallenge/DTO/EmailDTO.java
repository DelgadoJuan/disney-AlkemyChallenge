package com.alkemy.disney_AlkemyChallenge.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    private String to;
    private String subject;
    private String body;
}

package com.ssafy.urturn.solving.dto;

import com.ssafy.urturn.problem.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCodeDto {

   private int round;
   private String code;
   private Language language;

}

package com.ssafy.urturn.solving.dto;

import com.ssafy.urturn.problem.Language;
import lombok.Getter;

@Getter
public class UserCodeDto {

   private int round;
   private String code;
   private Language language;

   public UserCodeDto(SwitchCodeRequest req){
      this.round = req.getRound();
      this.code = req.getCode();
      this.language = req.getLanguage();
   }

}

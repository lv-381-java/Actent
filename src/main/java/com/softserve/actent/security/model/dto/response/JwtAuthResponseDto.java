package com.softserve.actent.security.model.dto.response;

import com.softserve.actent.constant.StringConstants;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtAuthResponseDto {

    private String accessToken;
    private String tokenType = StringConstants.BEARER;

    public JwtAuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String toJson(){
        return ("{\n"+"\"accessToken\": "+"\""+getAccessToken()+"\",\n"+
                "\"tokenType\": "+"\""+getTokenType()+"\"\n" +"}");
    }
}

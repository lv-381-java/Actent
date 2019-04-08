package com.softserve.actent.verification.controller;

import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.verification.dto.ConfirmDto;
import com.softserve.actent.verification.dto.UserConfirmDto;
import com.softserve.actent.verification.service.Verification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(UrlConstants.API_V1)
public class VerificationController {

    @Autowired
    private Verification verification;

    @PostMapping(value = "/confirm")
    @ResponseStatus(HttpStatus.OK)
    public ConfirmDto confirmEmail(@RequestBody UserConfirmDto dto){

        System.out.println(dto.getLogin());
        System.out.println(dto.getUuid());

        ConfirmDto confirmDto = new ConfirmDto();

        if(verification.confirmUser(dto.getLogin().trim(), dto.getUuid().trim())){
            confirmDto.setVerification("VERIFIED");
        }else {
            confirmDto.setVerification("NON_VERIFIED");
        }
        return confirmDto;
    }

}

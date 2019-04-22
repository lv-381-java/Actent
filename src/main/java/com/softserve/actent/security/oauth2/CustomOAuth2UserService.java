package com.softserve.actent.security.oauth2;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.exceptions.security.OAuth2AuthenticationProcessingException;
import com.softserve.actent.exceptions.validation.ValidationException;
import com.softserve.actent.model.entity.Role;
import com.softserve.actent.model.entity.Status;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.repository.UserRepository;
import com.softserve.actent.security.model.UserPrincipal;
import com.softserve.actent.security.oauth2.user.GoogleOAuth2UserInfo;
import com.softserve.actent.security.oauth2.user.OAuth2UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;

@Service
public class  CustomOAuth2UserService extends DefaultOAuth2UserService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

            User user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);


        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        if(!userRepository.existsByEmailAndProviderIdNull(oAuth2UserInfo.getEmail())) {
                return userRepository.findByProviderId(oAuth2UserInfo.getId()).orElseGet(() -> {
                    User newUser = new User();

                    newUser.setProviderId(oAuth2UserInfo.getId());
                    newUser.setFirstName(oAuth2UserInfo.getName());

                    newUser.setEmail(oAuth2UserInfo.getEmail());
                    newUser.setStatus(Status.ACTIVE);
                    Role userRole = Role.ROLE_USER;
                    newUser.setRoleset(Collections.singleton(userRole));
                    return userRepository.save(newUser);
                });
            }else throw new ValidationException(ExceptionMessages.USER_BY_THIS_EMAIL_IS_EXIST, ExceptionCode.DUPLICATE_VALUE);

    }

}

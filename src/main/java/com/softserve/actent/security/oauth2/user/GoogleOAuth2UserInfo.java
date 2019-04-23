package com.softserve.actent.security.oauth2.user;

import java.util.Map;

public class GoogleOAuth2UserInfo  extends OAuth2UserInfo{

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getFirstName() {
        return (String) attributes.get("given_name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getLogin() {
        return (String) attributes.get("email");
    }

    @Override
    public String getLastName() {
        return (String) attributes.get("family_name");
    }

//     newUser.setFirstName((String) map.get("given_name"));
//                    newUser.setLogin((String) map.get("given_name"));
//                    newUser.setLastName((String) map.get("family_name"));
}

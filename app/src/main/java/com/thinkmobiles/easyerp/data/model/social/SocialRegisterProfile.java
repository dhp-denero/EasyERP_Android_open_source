package com.thinkmobiles.easyerp.data.model.social;

import android.text.TextUtils;

/**
 * @author Michael Soyma (Created on 4/24/2017).
 *         Company: Thinkmobiles
 *         Email:  michael.soyma@thinkmobiles.com
 */
public final class SocialRegisterProfile {

    public String accessToken;
    public String userId;
    public String email;
    public String login;
    public String first;
    public String last;
    public String country;
    public String profileUrl;
    public String flag;
    public String dbId;

    public static SocialRegisterProfile withFacebook(final FacebookResponse facebookResponse) {
        final SocialRegisterProfile socialRegisterProfile = new SocialRegisterProfile();
        socialRegisterProfile.flag = SocialType.FACEBOOK.getLabel();
        socialRegisterProfile.userId = facebookResponse.id;
        socialRegisterProfile.email = facebookResponse.email;
        socialRegisterProfile.login = TextUtils.isEmpty(facebookResponse.email) ? facebookResponse.id : facebookResponse.email;
        socialRegisterProfile.first = facebookResponse.first_name;
        socialRegisterProfile.last = facebookResponse.last_name;
        socialRegisterProfile.country = facebookResponse.location != null ? facebookResponse.location.name : null;
        socialRegisterProfile.profileUrl = facebookResponse.link;
        return socialRegisterProfile;
    }
}

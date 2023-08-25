package com.grandp.data.security.captcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConfigurationProperties(prefix = "google.recaptcha.key")
public class CaptchaSettings {

    @Value("${google.recaptcha.key.site}")
    private String site = "6LdMbHomAAAAAC0WqycxKDCoQMEDxazPuqltcD2t";

    @Value("${google.recaptcha.key.secret}")
    private String secret = "6LdMbHomAAAAAFQRJmw_h2AQNG4jxzmanxiyLrhH";

    //reCAPTCHA V3
    private String siteV3;
    private String secretV3;
    private float threshold;

    public CaptchaSettings() {
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSiteV3() {
        return siteV3;
    }

    public void setSiteV3(String siteV3) {
        this.siteV3 = siteV3;
    }

    public String getSecretV3() {
        return secretV3;
    }

    public void setSecretV3(String secretV3) {
        this.secretV3 = secretV3;
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }
}

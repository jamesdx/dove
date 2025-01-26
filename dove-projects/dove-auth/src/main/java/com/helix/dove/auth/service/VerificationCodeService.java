package com.helix.dove.auth.service;

/**
 * Verification Code Service Interface
 */
public interface VerificationCodeService {

    /**
     * Send email verification code
     *
     * @param email email address
     * @return verification code key
     */
    String sendEmailCode(String email);

    /**
     * Send SMS verification code
     *
     * @param mobile mobile number
     * @return verification code key
     */
    String sendSmsCode(String mobile);

    /**
     * Generate captcha code
     *
     * @return captcha code and key
     */
    CaptchaResult generateCaptcha();

    /**
     * Captcha Result
     */
    class CaptchaResult {
        private String key;
        private String image;

        public CaptchaResult(String key, String image) {
            this.key = key;
            this.image = image;
        }

        public String getKey() {
            return key;
        }

        public String getImage() {
            return image;
        }
    }
} 
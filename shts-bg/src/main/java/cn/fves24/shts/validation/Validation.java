package cn.fves24.shts.validation;

import cn.fves24.shts.common.ComMsg;
import cn.fves24.shts.common.Constants;
import cn.fves24.shts.model.User;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 参数验证
 *
 * @author fves
 */
public class Validation {


    /**
     * 校验注册入参
     *
     * @param user 用户,只验证username，email
     * @param code 验证码
     * @return 校验结果
     */
    public static ValidationResult validateRegisterParams(User user, String code) {
        ValidationResult validationUserNameResult = validateUsername(user.getUsername());
        ValidationResult validationEmailResult = validateEmail(user.getEmail());
        ValidationResult vvalidationCodeResult = validateCode(code);
        return getValidationResult(validationUserNameResult, validationEmailResult, vvalidationCodeResult);
    }

    /**
     * 校验登录入参
     * 登录方式：用户名密码
     *
     * @param user 用户
     * @return 校验结果
     */
    public static ValidationResult validateLoginParams(User user) {
        ValidationResult validateUsernameResult = validateUsername(user.getUsername());
        return getValidationResult(validateUsernameResult);
    }

    /**
     * 校验登录入参
     * 登录方式：邮箱验证码
     *
     * @param user 用户
     * @param code  验证码
     * @return 校验结果
     */
    public static ValidationResult validateLoginParams(User user, String code) {
        ValidationResult validateEmail = validateEmail(user.getEmail());
        ValidationResult validateCode = validateCode(code);
        return getValidationResult(validateEmail, validateCode);
    }


    /**
     * 校验邮箱
     *
     * @param email 邮箱
     * @return 校验结果
     */
    public static ValidationResult validateEmail(String email) {
        ValidationResult result = new ValidationResult();
        if (StringUtils.isEmpty(email)) {
            result.addMsg(ComMsg.EMAIL_EMPTY);
            return result;
        } else if (!pattern(Constants.EMAIL_REGEX, email)) {
            result.addMsg(ComMsg.EMAIL_INVALID);
        }
        return result;
    }

    /**
     * 校验用户名
     *
     * @param username 用户名
     * @return 校验结果
     */
    public static ValidationResult validateUsername(String username) {
        ValidationResult result = new ValidationResult();
        if (StringUtils.isEmpty(username)) {
            result.addMsg(ComMsg.USERNAME_EMPTY);
            return result;
        } else if (username.length() < Constants.USERNAME_MIN_LENGTH) {
            result.addMsg(ComMsg.USERNAME_TOO_SHORT);
        } else if (username.length() > Constants.USERNAME_MAX_LENGTH) {
            result.addMsg(ComMsg.USERNAME_TOO_LONG);
        }
        return result;
    }

    /**
     * 校验验证码
     *
     * @param code 验证码
     * @return 校验结果
     */
    private static ValidationResult validateCode(String code) {
        ValidationResult result = new ValidationResult();
        if (StringUtils.isEmpty(code)) {
            result.addMsg(ComMsg.CODE_EMPTY);
            return result;
        } else if (code.length() != Constants.CODE_LENGTH) {
            result.addMsg(ComMsg.CODE_ERROR);
        }
        return result;
    }

    /**
     * 校验手机号码
     *
     * @param phone 手机号码
     * @return 校验结果
     */
    private static ValidationResult validatePhone(String phone) {
        ValidationResult result = new ValidationResult();
        if (StringUtils.isEmpty(phone)) {
            result.addMsg(ComMsg.PHONE_EMPTY);
            return result;
        }
        if (!pattern(Constants.PHONE_REGEX, phone)) {
            result.addMsg(ComMsg.PHONE_INVALID);
            return result;
        }
        return result;
    }

    private static ValidationResult getValidationResult(ValidationResult... validationResults) {
        ValidationResult result = new ValidationResult();
        for (ValidationResult validationResult : validationResults) {
            if (validationResult.isHasErrors()) {
                result.addMsg(validationResult.getErrMsg());
            }
        }
        return result;
    }

    private static boolean pattern(String regex, String s) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(s).find();
    }
}

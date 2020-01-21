package com.ctw.audit.exception;


import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

import static org.springframework.util.ReflectionUtils.accessibleConstructor;

public enum ErrorEnum {
    NOTIFICATION_NAME_DUPLICATED("You already have another notification with same name."),
    NOTIFICATION_ID_NOT_NULL_ON_CREATE("Notification ID must be null on create."),
    NOTIFICATION_NOT_FOUND("Notification not found. id:"),
    USER_NOT_FOUND("User not found. id:"),
    YOU_CANNOT_EDIT_NOTIFICATION("You cannot edit this notification"),
    FORBIDDEN_NOTIFICATION_ACCESS("You has no permission to access this notification", ForbiddenApplicationException.class),
    NOTIFICATION_TYPE_NOT_SUPPORTED_YET("By now we are supporting only EMAIL notifications!"),
    EMAIL_MUST_HAVE_RECEIVER("You must inform a receiver to send an email!"),
    EMAIL_RECEIVER_NOT_VALID("You must inform a valid email as receiver!"),
    EMAIL_TEMPLATE_NOT_DEFINED("you must define a template do send an email"),
    EMAIL_CONFIG_NOT_DEFINED("We don't have email configuration for company "),
    NOTIFICATION_NOT_ACCESSIBLE("Notification doesn't exists or you has no permission to access it."),
    BAD_CREDENTIALS("Wrong username or password"),
    OBJ_REQUIRED("You must inform an object body"),
    ENTITY_NOT_FOUND("Entity not found. "),
    FORBIDDEN_BOT_ACCESS("You has no permission to access this bot", ForbiddenApplicationException.class),
    BOT_STARTED_CANNOT_BE_DELETED("You cannot delete a bot started. Please stop it first."),
    BOT_RBW_USERNAME_BLANK("rbwUsername cannot be blank"),
    BOT_RBW_PASSWORD_BLANK("rbwPassword cannot be blank"),
    BOT_RBW_APP_SECRET_BLANK("rbwAppSecret cannot be blank"),
    BOT_WATSON_USERNAME_BLANK("watsonUsername cannot be blank"),
    BOT_WATSON_PASSWORD_BLANK("watsonPassword cannot be blank"),
    BOT_START_ERROR("Your bot didn't start, please check your bot configurations."),
    BOT_STOP_ERROR("One error occurred on stop bot."),
    BOT_ALREADY_STARTED("This bot is already started."),
    BOT_NOT_STARTED("This bot is not started."),
    BOT_STARTED_IN_OTHER_RBW_ACC("There is another bot started in same rainbow account."),
    RBW_INVALID_HOST("Invalid Rainbow Host."),
    DEPARTMENT_NULL_ON_CREATE("We cannot create department NULL"),
    DEPARTMENT_NOT_FOUND("we didn't found this department"),
    DEPARTMENT_PARENT_NOT_FOUND("we didn't found this department parent"),
    DEPARTMENT_NAMELESS_ON_PERSIST("You cannot persist a nameless department."),
    DEPARTMENT_COMPANYLESS_ON_PERSIST("You cannot persist a companyless department."),
    DEPARTMENT_WITH_PARENT_FROM_DIFFERENT_COMPANY("Department and parent should be in the same company"),
    COMPANY_NOT_FOUND("We didn't found this company"),
    COMPANY_NAMELESS("You cannot persist a nameless company"),
    COMPANY_NOT_ACCESSIBLE("It seems you don't have permission to manage this company",ForbiddenApplicationException.class),
    USERS_NOT_FOUND_IN_COMPANY("Some users on this list didn't match requirements. Make sure they all exists in this company."),
    USER_ATTACHMENT_IN_ANOTHER_DEPARTMENT("Some users on this list didn't match requirements.Make sure they all don't have department"),
    FORBIDDEN_IP("You cannot request from your IP address", ForbiddenApplicationException.class),
    FORBIDDEN_APPLICATION("This token are not made from IDS-BOARD", ForbiddenApplicationException.class),
    DEPARTMENT_HAS_USERS("You cannot remove department with users"),
    DEPARTMENT_HAS_CHILDREN("This department has children, so to also delete its children you must pass withChildren=true as query param"),
    CHILDREN_HAVE_USERS("You should remove users from children department"),
    USER_ID_MUST_NOT_BE_NULL("User id must not be null to perform this operation."),
    LOGIN_HISTORY_TIME_NOT_ALLOWED("You can see histories only from 1, 3 or 6 months ago.");

    private final String message;
    private final Class<? extends RuntimeException> exception;
    private final static Logger logger = LoggerFactory.getLogger(ErrorEnum.class);


    ErrorEnum(String message) {
        this.message = message;
        this.exception = IllegalArgumentException.class;
    }

    ErrorEnum(String message, Class<? extends RuntimeException> e) {
        this.message = message;
        this.exception = e;
    }

    public void throwError(){
        try {
            Constructor<? extends RuntimeException> constructor = accessibleConstructor(this.exception, String.class);
            throw constructor.newInstance(this.toString());
        } catch (ReflectiveOperationException e) {
            logger.error(e.getLocalizedMessage(),e);
        }
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name()) + "|" + message;
    }
}

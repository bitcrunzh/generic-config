package com.bitcrunzh.generic.config.reflection.annotation.property;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StringProperty {
    String IP_ADDRESS_V4_PATTERN = Pattern.compile("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$").pattern();
    String IP_ADDRESS_V4_CIDR_PATTERN = Pattern.compile("^(?<ip>(?:[0-9]{1,3}\\.){3}[0-9]{1,3})/(?<subnet>([1-2]?[0-9])|(3[0-2]))$").pattern();
    String IP_ADDRESS_V4_AND_PORT_PATTERN = Pattern.compile("^(?<ip>(?:[0-9]{1,3}\\.){3}[0-9]{1,3}):(?<port>(6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5]?[0-9]{1,4}))$").pattern();
    String IP_ADDRESS_V4_MULTICAST_PATTERN = Pattern.compile("^2(?:2[4-9]|3\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)){3}$").pattern();

    /**
     * @return the minimum (inclusive) length of the String.
     */
    int minLength() default 0;
    /**
     * @return the maximum (inclusive) length of the String.
     */
    int maxLength() default Integer.MAX_VALUE;

    /**
     * White list of valid values.
     * An empty array means all values.
     */
    String[] validValues() default {};

    /**
     * Default recommended value for this property.
     */
    String defaultValue() default "";

    /**
     * Regular Expression Pattern, used for validation values.
     * Empty means no RegEx validation.
     */
    String regExValidationPattern() default "";

    /**
     * Name of the property. If empty, the field name will be used.
     */
    String name() default "";

    /**
     * Description of this property
     */
    String description() default "";

    /**
     * Model version of the class with this property, where this property was introduced.
     */
    String introducedInModelVersion() default "1.0";

    /**
     * Whether specifying this property as part of the object is mandatory.
     * If optional, the default value will be used when not specified.
     */
    boolean isOptional() default true;
}

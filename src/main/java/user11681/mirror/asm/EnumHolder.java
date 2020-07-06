package user11681.mirror.asm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumHolder {
    Class<? extends Enum<?>>[] value() default {};

    String[] targets() default {};
}

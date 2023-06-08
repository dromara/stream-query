package issue.org.dromara.streamquery.gitee.issue17BSNV;

import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@TableName
public @interface Table {
    @AliasFor(
        annotation = TableName.class,
        attribute = "value"
    )
    String value() default "";
}
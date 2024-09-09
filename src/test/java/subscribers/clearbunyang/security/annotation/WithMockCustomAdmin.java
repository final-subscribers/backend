package subscribers.clearbunyang.security.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;
import subscribers.clearbunyang.security.WithMockCustomAdminSecurityContextFactory;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomAdminSecurityContextFactory.class)
public @interface WithMockCustomAdmin {}

package subscribers.clearbunyang.security.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;
import subscribers.clearbunyang.security.WithMockCustomMemberSecurityContextFactory;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomMemberSecurityContextFactory.class)
public @interface WithMockCustomMember {}

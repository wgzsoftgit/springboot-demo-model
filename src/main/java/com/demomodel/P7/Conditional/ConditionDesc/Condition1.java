 package com.demomodel.P7.Conditional.ConditionDesc;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class Condition1 implements Condition  {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		System.err.println(this.getClass().getName());
        return true;
	}

}

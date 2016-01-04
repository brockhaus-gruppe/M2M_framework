package de.brockhaus.m2m.aspect;

import java.util.Date;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;

/**
 * An aspect logging the access time of a individual step within the handling time.
 * Configuration is switched to the *-STACKCONFIG.xml files as @Around is nice but
 * not flexible enough.
 * 
 * Some pretty useful examples of how to define pointcuts are given here:
 * http://blog.espenberntsen.net/2010/03/20/aspectj-cheat-sheet/
 * http://stackoverflow.com/questions/8426308/how-to-specify-single-pointcut-for-multiple-packages
 * 
 * Project: m2m-common
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 22, 2015
 *
 */
@Aspect
public class PerformanceLogAspect {
	
	private static Logger LOG = Logger.getLogger(PerformanceLogAspect.class);

//  This option is skipped for the favour of xml based configuration
//	@Around(   "execution (* de.brockhaus.m2m.config.aspect.Foo.doFoo(..)) ||" 
//			+ " execution (public * de.brockhaus.m2m.receiver.pojo.M2MMessagePOJOReceiverAdapter.bla(..))"
//			)
	public Object logAroundExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

		Object result = null;
		LOG.debug("before invocation: " + joinPoint.getSignature().getName());
		
		Date begin = new Date(System.currentTimeMillis());
		result = joinPoint.proceed();
		Date end = new Date(System.currentTimeMillis());
		long elapsed = end.getTime() - begin.getTime();
		
		LOG.debug("after invocation: " + joinPoint.getSignature().getName() + " => time elapsed(ms) : " + elapsed);
		
		return result;
	}
	
	public Object logBeforeExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		Date begin = new Date(System.currentTimeMillis());
		LOG.debug("before invocation: " + joinPoint.getSignature().getName() + " => start at: " + begin.getTime());
		
		return joinPoint.proceed();
	}
}

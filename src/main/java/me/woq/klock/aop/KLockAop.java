package me.woq.klock.aop;

import io.netty.util.internal.StringUtil;
import me.woq.klock.annotations.KLock;
import me.woq.klock.exceptions.KLockException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * description: TestAop
 * date: 2022/3/30 14:53
 * author: YangTao
 */
@Aspect
public class KLockAop {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String baseKey = "KLock::class::{0}::method::{1}";

    @Pointcut("@annotation(me.woq.klock.annotations.KLock)")
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        KLock kLock = method.getAnnotation(KLock.class);

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        String key = kLock.key();

        //为空默认使用类名+方法名
        if(StringUtil.isNullOrEmpty(key)){
            key = MessageFormat.format(baseKey, className, methodName);
        }else if(key.contains("#")){
            //使用 SPEL
            Parameter[] parameters = method.getParameters();
            Object[] args = joinPoint.getArgs();

            if(parameters == null || args == null || parameters.length <= 0 || args.length <= 0){
                throw new KLockException("parameters or args cant be null!");
            }

            EvaluationContext context = new StandardEvaluationContext();
            for(int i=0; i<args.length; i++){
                context.setVariable(parameters[i].getName(), args[i]);
            }

            ExpressionParser exp = new SpelExpressionParser();
            Expression expression = exp.parseExpression(key);

            key = MessageFormat.format(baseKey, className, methodName) + "::uniqueKey::" + expression.getValue(context).toString();
        }else{
            //使用用户传入固定值
            key = MessageFormat.format(baseKey, className, methodName) + "::uniqueKey::" + key;
        }

        try{
            if(redisTemplate.opsForValue().setIfAbsent(key, "", kLock.timeOut(), TimeUnit.MINUTES)){
                return joinPoint.proceed();
            }
        }finally {
            //释放锁
            redisTemplate.delete(key);
        }

        return null;
    }

}

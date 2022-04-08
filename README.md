# klock-spring-boot-starter

### 防止重复提交

使用方法:

* 引入依赖
  
```
  <dependency>
      <groupId>me.woq</groupId>
      <artifactId>klock-spring-boot-starter</artifactId>
      <version>${version}</version>
  </dependency>
```
* 在需要防止重复调用的controller或者方法上面添加 @KLock 注解

参数如下:

| key | 名称 | 描述 |
|---|---|---|
|key|配置唯一key|支持SPEL语法|
|timeOut|超时释放时间|默认3分钟|

举个例子：
```
1.SPEL表达式
@KLock(key = "#methodDto.filed", timeOut = 4)
public void method(MethodDto methodDto) {
    // code 
}

2.SPEL表达式 拼接
@KLock(key = "'name:' + #methodDto.userName + 'age:' + #methodDto.age")
public void method(MethodDto methodDto) {
    // code 
}

3.无参
@KLock
public void method(MethodDto methodDto) {
    // code 
}
```

  

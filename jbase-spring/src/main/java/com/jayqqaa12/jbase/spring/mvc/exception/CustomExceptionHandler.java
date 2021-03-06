package com.jayqqaa12.jbase.spring.mvc.exception;

import com.alibaba.fastjson.JSONException;
import com.jayqqaa12.jbase.exception.LockException;
import com.jayqqaa12.jbase.spring.exception.BusinessException;
import com.jayqqaa12.jbase.spring.exception.RetryException;
import com.jayqqaa12.jbase.spring.mvc.Resp;
import com.jayqqaa12.jbase.spring.mvc.RespCode;
import com.jayqqaa12.jbase.spring.mvc.i18n.LocaleKit;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Set;


@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class CustomExceptionHandler {

    /**
     * 操作数据库出现异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLException.class)
    public Resp handleException(SQLException e) {
        log.error("操作数据库出现异常:", e);
        return Resp.response(RespCode.DB_SQL_ERROR);
    }

    @ExceptionHandler(value = {BusinessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)

    public final Resp handleBusinessException(BusinessException ex) {
        log.warn("业务异常 {}", (ex));
        return Resp.response(ex.getCode(), ex.getMessage());
    }


    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Resp handleArgumenteException(IllegalArgumentException ex) {
        log.warn("请求参数异常 {}", (ex));
        return Resp.response(RespCode.PARAM_ERROR, LocaleKit.resolverOrGet(ex.getMessage()));
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Resp handlerValidatorException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

        ConstraintViolation violation = violations.stream().findFirst().get();
        String msg = violation.getMessage();

        if (!msg.contains(LocaleKit.MSG_PREFIX)) {
            String param = ((PathImpl) violation.getPropertyPath()).getLeafNode().asString();
            log.info("参数 {} 验证异常 {}", param, msg);
        } else log.info("参数验证异常 {}", LocaleKit.get(msg));

        return Resp.response(RespCode.PARAM_ERROR, msg);
    }


    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Resp handlerValidatorException(MethodArgumentNotValidException e) {

        ObjectError objectError = e.getBindingResult().getAllErrors().stream().findFirst().get();
        String msg = objectError.getDefaultMessage();

        if (!msg.contains(LocaleKit.MSG_PREFIX)) {
            DefaultMessageSourceResolvable resolvable = (DefaultMessageSourceResolvable) objectError.getArguments()[0];
            String param = resolvable.getDefaultMessage();
            log.info("参数 {} 验证异常 {}", param, msg);
        } else log.info("参数验证异常 {}", LocaleKit.get(msg));


        return Resp.response(RespCode.PARAM_ERROR, msg);
    }


    @ExceptionHandler(value = {BindException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Resp handlerBindException(BindException e) {

        ObjectError objectError = e.getAllErrors().stream().findFirst().get();
        String msg = objectError.getDefaultMessage();

        if (!msg.contains(LocaleKit.MSG_PREFIX)) {
            DefaultMessageSourceResolvable resolvable = (DefaultMessageSourceResolvable) objectError.getArguments()[0];
            String param = resolvable.getDefaultMessage();
            log.info("参数 {} 验证异常 {}", param, msg);
        } else log.info("参数验证异常 {}", LocaleKit.get(msg));

        return Resp.response(RespCode.PARAM_ERROR, msg);
    }


    @ExceptionHandler(value = {RetryException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Resp handleRetryException(RetryException ex) {
        log.warn("并发更新异常 {}", (ex));
        return Resp.response(RespCode.RETRY_ERROR);
    }

    @ExceptionHandler(value = {LockException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Resp handleLockException(LockException ex) {
        log.warn("幂等性异常 {}", (ex));
        return Resp.response(RespCode.RETRY_LOCK_ERROR);
    }


    @ExceptionHandler(value = {JSONException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Resp handleJsonException(JSONException ex) {
        log.warn("json异常 {}", (ex));
        return Resp.response(RespCode.REQ_JSON_FORMAT_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Resp handleGeneralException(Exception ex) {
        log.error("其他异常", ex);

        return Resp.response(RespCode.SERVER_ERROR);
    }





}

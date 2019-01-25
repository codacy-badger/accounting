package ru.ldwx.accounting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.ldwx.accounting.AuthorizedUser;
import ru.ldwx.accounting.util.ValidationUtil;
import ru.ldwx.accounting.util.exception.ErrorType;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @Autowired
    private MessageUtil messageUtil;

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        log.error("Exception at request " + req.getRequestURL(), rootCause);
        ModelAndView mav = new ModelAndView("exception/exception");
        mav.addObject("typeMessage", messageUtil.getMessage(ErrorType.APP_ERROR.getErrorCode()));
        mav.addObject("exception", rootCause);
        mav.addObject("message", ValidationUtil.getMessage(rootCause));

        // Interceptor is not invoked, put userTo
        AuthorizedUser authorizedUser = SecurityUtil.safeGet();
        if (authorizedUser != null) {
            mav.addObject("userTo", authorizedUser.getUserTo());
        }
        return mav;
    }
}



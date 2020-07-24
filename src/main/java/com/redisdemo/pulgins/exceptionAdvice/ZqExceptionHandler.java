package com.redisdemo.pulgins.exceptionAdvice;

import com.alibaba.fastjson.JSON;
import com.redisdemo.pulgins.exceptionAdvice.exception.FlowCtrlExceptoin;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ZqExceptionHandler {

    @ExceptionHandler(value = FlowCtrlExceptoin.class)
    public void exceptionHandler(HttpServletRequest req, HttpServletResponse resp, FlowCtrlExceptoin e) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=utf-8");
        Map<String, Object> reslut = new HashMap<String, Object>();
        reslut.put("errCode", 306);
        reslut.put("errMsg", "接口流量超过限制");
        out.append(JSON.toJSON(reslut).toString());
        out.flush();
        out.close();
    }

}

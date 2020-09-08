package com.sxmd.content;

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Description: 用户登录授权接口
 *
 * @author cy
 * @date 2020年09月02日 11:23
 * Version 1.0
 */
@Controller
@SessionAttributes("authorizationRequest")
public class LoginController {


    /**
     * 登录接口
     *
     * @return
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 登录接口错误页面
     *
     * @return
     */
    @GetMapping("/login-error")
    public ModelAndView loginError() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("isError", true);
        return modelAndView;
    }


    /**
     * 授权接口
     *
     * @param model
     * @return
     */
    @RequestMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model) {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model
                .get("authorizationRequest");
        ModelAndView view = new ModelAndView("base-grant");
        view.addObject("clientId", authorizationRequest.getClientId());
        view.addObject("scope", authorizationRequest.getScope().toArray()[0]);
        return view;
    }

}

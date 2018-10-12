package com.fiberhome.mapps.sign;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fiberhome.mapps.intergration.security.login.MplusLogin;
import com.fiberhome.mapps.intergration.security.sso.Validator;
import com.fiberhome.mapps.intergration.session.SessionContext;

@RestController
@RequestMapping
public class LoginController
{
    @Autowired
    MplusLogin mlogin;

    @Autowired
    Validator  validator;

    @RequestMapping("/login")
    public ModelAndView clientLogin(@RequestParam Map request)
    {
        // HashMap<String, String> request = new HashMap<String, String>();
        // request.put("loginname", username);
        // request.put("password", password);
        ModelAndView mv = new ModelAndView();
        String token = mlogin.clientLogin(request);
        if (token == null)
        {
            mv.setViewName("/needLogin.html");
        }
        else
        {
            mv.setViewName("redirect:/index.html?sessionId=" + URLEncoder.encode(token));
        }

        return mv;
    }

    @Value("${mplus.login.loginPage}")
    String loginPage;

    // https://192.168.160.154:8440/mos/arkContentLogin.action?sessionId=192.168.160.98%2302EFDB2CA42F6EA15D032B2CB8A684A1&orgUuidDefault=8cedb324-fc84-4606-8763-3857618a885b&orgCodeDefault=fiberhome
    @RequestMapping("/websso")
    public ModelAndView webSso(@RequestParam String sessionId, @RequestParam("orgUuidDefault") String orgId,
            @RequestParam("orgCodeDefault") String ecid)
    {

        ModelAndView mv = new ModelAndView();

        if (SessionContext.getUserId() == null)
        {
            mv.setViewName("redirect:" + loginPage);
        }
        else
        {
            mv.setViewName("/");
        }

        return mv;
    }

    @RequestMapping("/version")
    public String version() throws IOException
    {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("build.json");

        try
        {
            String json = IOUtils.toString(is);
            return json;
        }
        finally
        {
            is.close();
        }
    }
}

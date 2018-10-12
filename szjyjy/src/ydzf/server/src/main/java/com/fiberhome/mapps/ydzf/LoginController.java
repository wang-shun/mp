package com.fiberhome.mapps.ydzf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fiberhome.mapps.intergration.security.login.MplusLogin;
import com.fiberhome.mapps.intergration.security.sso.Validator;
import com.fiberhome.mapps.intergration.session.SessionContext;


@RestController
@RequestMapping
public class LoginController {
	@Autowired
	MplusLogin mlogin;
	
	@Autowired
	Validator validator;
	
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
	public ModelAndView webSso(@RequestParam String sessionId, 
			@RequestParam(name="orgUuidDefault", required=false) String orgId,
			@RequestParam(name="orgCodeDefault", required=false) String ecid,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		ModelAndView mv = new ModelAndView();
		
		if (SessionContext.getUserId() == null) {			
			mv.setViewName("redirect:" + loginPage);
		} else {		
			// index.html not cached
		    response.setHeader("Cache-Control","no-store");  
		    response.setHeader("Pragrma","no-cache");  
		    response.setDateHeader("Expires",0);
		    // Fix IE Blocking iFrame Cookies
		    response.addHeader("P3P","CP=\"IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT\"");		    
			mv.setViewName("/");
		}
		
		return mv;
	}
	
	@RequestMapping("/relogin")
	public ModelAndView relogin() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:" + loginPage);
		return mv;
	}
	
	@RequestMapping("/version")
	public String version() throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("build.json");
		
		try {
			String json = IOUtils.toString(is);
			return json;
		} finally {
			is.close();
		}
	}
	
	@RequestMapping("/opensso")
	public String openSSO(@RequestParam String sessionId, 
			@RequestParam("orgUuidDefault") String orgId,
			@RequestParam("orgCodeDefault") String ecid) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html>");
		sb.append("  <body>");
		sb.append("    <form>");
		sb.append("      URL:<input id='url' type='text' value='http://localhost:8080/websso' style='width:400px'>");
		sb.append("      <input type='button' value='open it' onclick='javascript:openUrl();'>");
		sb.append("    </form>");
		sb.append("    <script type='text/javascript'>");
		sb.append("      function openUrl() {");
		sb.append("        var url = document.getElementById('url').value;");
		sb.append("        url += '").append("?sessionId=").append(URLEncoder.encode(sessionId));
		sb.append("&orgUuidDefault=").append(orgId).append("&orgCodeDefault=").append(ecid).append("';");
		sb.append("        window.open(url);");
		sb.append("        ");
		sb.append("        ");
		sb.append("      }");
		sb.append("    </script>");
		sb.append("  </body>");
		sb.append("</html>");
		
		return sb.toString();
	}
	
	
}

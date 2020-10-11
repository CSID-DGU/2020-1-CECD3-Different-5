package com.different.webpage;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Handles requests for the application home page.
 */

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	/*@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}*/
	
	/*로그인창
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
	    // requestUrl 가 null 이 아니거나 referer 가 null이 아닐경우
	    if (request.getRequestURI() != null && request.getHeader("referer") != null) {
	      // 이전페이지가 loginGet이거나 직접 로그인URL에 접속하지 않았을때만 referrer 저장
	      if (!(request.getRequestURI().equals("/login/loginGet") && request.getHeader("referer").equals("http://localhost/login/loginGet"))) {
	        // dest 세션에 이전 페이지 정보 저장
	        session.setAttribute("dest", request.getHeader("referer"));
	      }
	    }
	    return "/login";
	}*/
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search() {
		return "search";
	}
	
	@RequestMapping(value = "/student", method = RequestMethod.GET)
	public String student() {
		return "student";
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail() {
		return "detail";
	}
}

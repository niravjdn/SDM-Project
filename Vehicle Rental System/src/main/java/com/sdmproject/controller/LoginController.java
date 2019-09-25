package com.sdmproject.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sdmproject.model.User;
import com.sdmproject.repository.UserRepository;
import com.sdmproject.service.UserService;

@Controller
public class LoginController {

	Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository repositoryHandler;

	// pass to all view served by this controller
	@ModelAttribute("username")
	public String getCurrentUser() {
		Object userDetails =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (userDetails instanceof UserDetails) {
			return userService.findUserByEmail(((UserDetails) userDetails).getUsername()).getFirstName();
		} else {
			return userDetails.toString();
		}
	}

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}


	@RequestMapping(value = "/clerk/home", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.setViewName("clerk/home");
		return modelAndView;
	}


}

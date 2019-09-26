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
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping(value = "/profile/changepassword", method = RequestMethod.GET)
	public ModelAndView changePassword() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());

		modelAndView.addObject("welcomeString",
				"Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.setViewName("changepassword");
		return modelAndView;
	}

	@RequestMapping(value = "/profile/changepassword", method = RequestMethod.POST)
	public ModelAndView changePasswordForUser(@RequestParam("password")String password, @RequestParam("confirmPassword")String confirmPassword) {
		ModelAndView modelAndView = new ModelAndView();
		logger.info("Got in Password Reset Action");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());

		logger.info("Got in Password Reset Action -" + user.getPassword());
		logger.info("Got in Password Reset Action -" + confirmPassword);
		if (password.equals(confirmPassword)) {
			// change password
			user.setPassword(password);
			userService.saveUser(user);
			logger.info("Success - Password Change");
			modelAndView.addObject("successMessage", "Password Changed Successfully.");
		} else {
			logger.info("Error - Password Change");
			modelAndView.addObject("errorMessage", "Password Mismatch.");
		}

		modelAndView.addObject("welcomeString",
				"Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.setViewName("changepassword");
		return modelAndView;
	}
	

}

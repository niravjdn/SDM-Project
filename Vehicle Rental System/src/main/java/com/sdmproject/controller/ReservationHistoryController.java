package com.sdmproject.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sdmproject.beans.FilterBean;
import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Reservation;
import com.sdmproject.model.ReservationHistory;
import com.sdmproject.model.Vehicle;
import com.sdmproject.service.ClientRecordService;
import com.sdmproject.service.ReservationHistoryService;
import com.sdmproject.service.ReservationService;
import com.sdmproject.service.UserService;
import com.sdmproject.service.VehicleService;

@Controller
public class ReservationHistoryController {
	Logger logger = LoggerFactory.getLogger(ReservationController.class);
	
	@Autowired
	private ClientRecordService clientRecordService;
	
	@Autowired
	private VehicleService vehicleRecordService;
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private ReservationHistoryService reservationHistoryService;

	@Autowired
	private UserService userService;

	private FilterBean filterBean = FilterBean.getInstance();
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
	}

	@ModelAttribute("username")
	public String getCurrentUser() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return userService.findUserByEmail(userDetails.getUsername()).getFirstName();
	}
	
	@RequestMapping(value = { "/admin/reservation/historyView"}, method = RequestMethod.GET)
	public ModelAndView reservationHistoryView(Optional<String> sort, Optional<String> order, Optional<String> firstName) {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("records", reservationHistoryService.findAllWithSort(sort,order));
		
		if(sort.isPresent()) {
			//if already landed on page, process map and add value to modelAndView
			for (Map.Entry<String, String> entry : filterBean.getMap().entrySet()) {
				modelAndView.addObject(entry.getKey(),  entry.getValue());
			}
		}else {
			//if landing on page for first time, clear the map
			filterBean.getMap().clear();
		}
		
		if(firstName.isPresent()) {
			modelAndView.addObject("firstName", firstName.get());
			filterBean.getMap().put("firstName", firstName.get());
		}
		System.out.println(" in " + filterBean.getMap());
		modelAndView.addObject("records", reservationHistoryService.filterMultipleAttribute(filterBean.getMap(), sort, order));
		modelAndView.setViewName("admin/reservationHistory");
		
		modelAndView.addObject("sortProperty", sort.isPresent() ? sort.get() : "id");
		modelAndView.addObject("order",  order.isPresent() ? order.get() : "asc" );

		
		return modelAndView;
	}
}

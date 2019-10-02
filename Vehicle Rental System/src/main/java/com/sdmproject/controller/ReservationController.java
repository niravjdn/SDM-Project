package com.sdmproject.controller;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
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

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Reservation;
import com.sdmproject.model.ReservationHistory;
import com.sdmproject.service.ClientRecordService;
import com.sdmproject.service.ReservationHistoryService;
import com.sdmproject.service.ReservationService;
import com.sdmproject.service.UserService;

@Controller
public class ReservationController {

	Logger logger = LoggerFactory.getLogger(ReservationController.class);

	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private ReservationHistoryService reservationHistoryService;

	@Autowired
	private UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
	}

	@ModelAttribute("username")
	public String getCurrentUser() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return userService.findUserByEmail(userDetails.getUsername()).getFirstName();
	}

	@RequestMapping(value = { "/clerk/reservation/Delete" }, method = RequestMethod.GET)
	public ModelAndView viewRentalRecord(Optional<String> sort, Optional<String> order) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("records",reservationService.findAllWithSort(sort,order));
		modelAndView.setViewName("clerk/deleteReservation");
		modelAndView.addObject("sortProperty", sort.isPresent() ? sort.get() : "id");
		modelAndView.addObject("order",  order.isPresent() ? order.get() : "asc" );
		return modelAndView;
	}

	@RequestMapping(value = { "/clerk/reservation/add" }, method = RequestMethod.GET)
	public ModelAndView clientRecordAdd() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("clerk/addReservation");
		return modelAndView;
	}

	@RequestMapping(value = { "/clerk/reservation/add" }, method = RequestMethod.POST)
	public ModelAndView clientRecordAddPost(Reservation record) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("clerk/rentalRecordAdd");

		try {
			reservationService.save(record);
			modelAndView.addObject("successMessage", "Reservation Record has been added successfully.");
		} catch (DuplicateEntryException e) {
			modelAndView.addObject("errorMessage", e.getMessage());
			modelAndView.addObject("record", record);
		}
		return modelAndView;
	}

	@RequestMapping(value = { "/clerk/reservation/delete/{id}" }, method = RequestMethod.GET)
	public ModelAndView clientRecordDelete(@PathVariable(value = "id") final int id) {
		ModelMap map = new ModelMap();
		reservationService.deleteReservationByID(id);
		map.addAttribute("successMessage", "Deleted Reservation Successfully");
		map.addAttribute("records", reservationService.findAll());
		return new ModelAndView("redirect:/clerk/reservation/Delete", map);
	}
	
	
	@RequestMapping(value = { "/clerk/reservation/returnView" }, method = RequestMethod.GET)
	public ModelAndView clientRecordReturnView(Optional<String> sort, Optional<String> order) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("records", reservationService.findAllOutReservationSort(sort, order));
		modelAndView.setViewName("clerk/returnVehicle");
		return modelAndView;
	}
	
	@RequestMapping(value = { "/clerk/reservation/return/{id}" }, method = RequestMethod.GET)
	public ModelAndView returnVehicle(@PathVariable(value = "id") final int id) {
		ModelMap map = new ModelMap();
		Reservation r = reservationService.findByID(id);
		ReservationHistory history = new ReservationHistory(r.getId(),r.getClient().getFirstName(), r.getClient().getLastName(), r.getClient().getDriverLicienceNo(), r.getClient().getExpiryDate(), r.getClient().getPhoneNo(), r.getVehicle().getColor(), r.getVehicle().getPlateNo(), r.getVehicle().getMake(), r.getVehicle().getModel(), r.getVehicle().getYear(), "user", r.getFromDateTime(), r.getToDateTime(), new Date());
		reservationHistoryService.save(history);
		reservationService.returnReservationByID(id);
		map.addAttribute("successMessage", "Deleted Reservation Successfully");
		return new ModelAndView("redirect:/clerk/reservation/returnView", map);
		
	}
	

}

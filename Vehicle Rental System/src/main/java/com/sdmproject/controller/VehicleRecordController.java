package com.sdmproject.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sdmproject.beans.FilterBean;
import com.sdmproject.model.Reservation;
import com.sdmproject.model.Vehicle;
import com.sdmproject.service.ReservationService;
import com.sdmproject.service.UserService;
import com.sdmproject.service.VehicleService;

@Controller 
public class VehicleRecordController {
	Logger log = LoggerFactory.getLogger(VehicleRecordController.class);
	
	@Autowired
	private VehicleService vehicleRecordService;
	
	@Autowired
	private ReservationService reservationService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private FilterBean filterBean;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
	}
	
	@ModelAttribute("username")
	public String getCurrentUser() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return userService.findUserByEmail(userDetails.getUsername()).getFirstName();
	}
	
	@RequestMapping(value = { "/vehicleRecord" }, method = RequestMethod.GET)
	public ModelAndView viewVehicleRecord(Optional<String> sort, Optional<String> order, Optional<String> type,
			Optional<String> make, Optional<String> model, Optional<String> color, Optional<String> year) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("records", vehicleRecordService.findAllWithSort(sort,order));
		
		if(sort.isPresent()) {
			//if already landed on page, process map and add value to modelAndView
			for (Map.Entry<String, String> entry : filterBean.getMap().entrySet()) {
				modelAndView.addObject(entry.getKey(),  entry.getValue());
			}
		}else {
			//if landing on page for first time, clear the map
			filterBean.getMap().clear();
		}
		
		if(type.isPresent()) {
			modelAndView.addObject("type", type.get());
			filterBean.getMap().put("type", type.get());
		}
		
		if(make.isPresent()) {
			modelAndView.addObject("make", make.get());
			filterBean.getMap().put("make", make.get());
		}
		
		if(model.isPresent()) {
			modelAndView.addObject("model", model.get());
			filterBean.getMap().put("model", model.get());
		}
		
		if(color.isPresent()) {
			filterBean.getMap().put("color", color.get());
			modelAndView.addObject("color", color.get());
		}
		
		if(year.isPresent()) {
			filterBean.getMap().put("year", year.get());
			modelAndView.addObject("year", year.get());
		}
		
		modelAndView.addObject("records", vehicleRecordService.filterMultipleAttribute(filterBean.getMap(), sort, order));
		
		
		modelAndView.setViewName("vehicleRecord");
		modelAndView.addObject("sortProperty", sort.isPresent() ? sort.get() : "id");
		modelAndView.addObject("order",  order.isPresent() ? order.get() : "asc" );
		return modelAndView;
	}

	@RequestMapping(value = { "/vehicleRecord/view/{id}" }, method = RequestMethod.GET)
	public ModelAndView vehicleRecordView(@PathVariable(value = "id") final int id, String sort, String order) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("vehicleRecordDetailView");
		Vehicle record = vehicleRecordService.findByID(id);
		
		// get all ids of vehicles - find position of current
		List<Integer> vehicleRecordIds = vehicleRecordService.filterMultipleAttribute(filterBean.getMap(), Optional.ofNullable(sort), Optional.ofNullable(order)).stream().map(Vehicle::getId)
				.collect(Collectors.toList());
		int indexOfTarget = vehicleRecordIds.indexOf(id);
		log.info("" + indexOfTarget);
		log.info("" + vehicleRecordIds);
		int indexOfPrevious = indexOfTarget != 0 ? vehicleRecordIds.get(indexOfTarget - 1) : -1;
		int indexofNext = indexOfTarget != vehicleRecordIds.size() - 1 ? vehicleRecordIds.get(indexOfTarget + 1) : -1;

		modelAndView.addObject("sortProperty", sort);
		modelAndView.addObject("order",  order);
		
		
		
		List<Reservation> reservations = reservationService.findAllOutReservationSort(Optional.empty(), Optional.empty());
		boolean isAvailable = reservations.stream().filter(reservation -> (reservation.getVehicle().getId() == id)).findAny().isPresent();
		modelAndView.addObject("vehicleAvailability", !isAvailable);

		modelAndView.addObject("previousItem", indexOfPrevious);

		modelAndView.addObject("nextItem", indexofNext);


		modelAndView.addObject("record", record);
		
		
		return modelAndView;
	}
}

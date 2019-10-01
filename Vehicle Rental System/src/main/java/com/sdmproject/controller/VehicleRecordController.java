package com.sdmproject.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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


import com.sdmproject.model.Vehicle;
import com.sdmproject.service.UserService;
import com.sdmproject.service.VehicleService;

@Controller 
public class VehicleRecordController {
	Logger log = LoggerFactory.getLogger(VehicleRecordController.class);
	
	@Autowired
	private VehicleService vehicleRecordService;

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
	
	@RequestMapping(value = { "/vehicleRecord" }, method = RequestMethod.GET)
	public ModelAndView viewClientRecord(Optional<String> sort, Optional<String> order, Optional<String> model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("records", vehicleRecordService.findAllWithSort(sort,order));
		
		if(model.isPresent()) {
			modelAndView.addObject("model", model.get());
			modelAndView.addObject("records",vehicleRecordService.filter(model.get()));
		}
		
		
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
				List<Integer> vehicleRecordIds = vehicleRecordService.findIDWithSort(sort, order);
				int indexOfTarget = vehicleRecordIds.indexOf(id);
				log.info("" + indexOfTarget);
				log.info("" + vehicleRecordIds);
				int indexOfPrevious = indexOfTarget != 0 ? vehicleRecordIds.get(indexOfTarget - 1) : -1;
				int indexofNext = indexOfTarget != vehicleRecordIds.size() - 1 ? vehicleRecordIds.get(indexOfTarget + 1) : -1;

				modelAndView.addObject("sortProperty", sort);
				modelAndView.addObject("order",  order);
				
				
				modelAndView.addObject("previousItem", indexOfPrevious);

				modelAndView.addObject("nextItem", indexofNext);

		
		modelAndView.addObject("record", record);
		
		
		return modelAndView;
	}
}

package com.sdmproject.controller;

import java.sql.SQLException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sdmproject.configuration.LastModifiedVehicle;
import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.service.ClientRecordService;
import com.sdmproject.service.UserService;

@Controller
public class ClientRecordController {

	Logger logger = LoggerFactory.getLogger(ClientRecordController.class);

	@Autowired
	private ClientRecordService clientRecordService;

	@Autowired
	private UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
	}

	@ModelAttribute("username")
	public String getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		} else {
			return principal.toString();
		}
	}

	@RequestMapping(value = { "/clerk/clientRecord" }, method = RequestMethod.GET)
	public ModelAndView viewClientRecord(Optional<String> sort, Optional<String> order) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("records", clientRecordService.findAllWithSort(sort,order));
		modelAndView.setViewName("clerk/clientRecord");
		modelAndView.addObject("sortProperty", sort.isPresent() ? sort.get() : "id");
		modelAndView.addObject("order",  order.isPresent() ? order.get() : "asc" );
		return modelAndView;
	}

	@RequestMapping(value = { "/clerk/clientRecord/add" }, method = RequestMethod.GET)
	public ModelAndView clientRecordAdd() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("clerk/clientRecordAdd");
		return modelAndView;
	}

	@RequestMapping(value = { "/clerk/clientRecord/add" }, method = RequestMethod.POST)
	public ModelAndView clientRecordAddPost(ClientRecord record) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("clerk/clientRecordAdd");

		try {
			clientRecordService.save(record);
			modelAndView.addObject("successMessage", "Client Record has been added successfully.");
		} catch (DuplicateEntryException e) {
			modelAndView.addObject("errorMessage", e.getMessage());
			modelAndView.addObject("record", record);
		}
		return modelAndView;
	}

	@RequestMapping(value = { "/clerk/clientRecord/update/{id}" }, method = RequestMethod.GET)
	public ModelAndView clientRecordUpdate(@PathVariable(value = "id") final int id,
			@RequestParam Optional<String> errorMessage, @RequestParam Optional<String> successMessage) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("clerk/clientRecordEdit");
		if (errorMessage.isPresent()) {
			modelAndView.addObject("errorMessage", errorMessage.get());
		}

		if (successMessage.isPresent()) {
			modelAndView.addObject("successMessage", successMessage.get());
		}

		ClientRecord record = clientRecordService.findByID(id);
		modelAndView.addObject("record", record);
		return modelAndView;
	}

	@RequestMapping(value = { "/clerk/clientRecord/update" }, method = RequestMethod.POST)
	public ModelAndView clientRecordUpdate(ClientRecord record) {
		ModelMap map = new ModelMap();
		try {
			clientRecordService.update(record);
			map.addAttribute("successMessage", "Client Record has been updated successfully.");
			LastModifiedVehicle.lastModifiedClient = record;
			return new ModelAndView("redirect:/clerk/clientRecord/update/" + record.getId(), map);
		} catch (DuplicateEntryException e) {
			map.addAttribute("errorMessage", e.getMessage());
			map.addAttribute("record", record);
			return new ModelAndView("redirect:/clerk/clientRecord/update/" + record.getId(), map);
		} catch (SQLException e) {
			map.addAttribute("errorMessage", e.getMessage());
			map.addAttribute("record", record);
			return new ModelAndView("redirect:/clerk/clientRecord/update/" + record.getId(), map);
		}
	}

	@RequestMapping(value = { "/clerk/clientRecord/view/{id}" }, method = RequestMethod.GET)
	public ModelAndView clientRecordView(@PathVariable(value = "id") final int id, String sort, String order) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("clerk/clientRecordDetailView");
		ClientRecord record = clientRecordService.findByID(id);

		// get all ids of clerk - find position of current
		List<Integer> clientRecordIds = clientRecordService.findIDWithSort(sort, order);
		int indexOfTarget = clientRecordIds.indexOf(id);
		logger.info("" + indexOfTarget);
		logger.info("" + clientRecordIds);
		int indexOfPrevious = indexOfTarget != 0 ? clientRecordIds.get(indexOfTarget - 1) : -1;
		int indexofNext = indexOfTarget != clientRecordIds.size() - 1 ? clientRecordIds.get(indexOfTarget + 1) : -1;

		modelAndView.addObject("sortProperty", sort);
		modelAndView.addObject("order",  order);
		
		
		modelAndView.addObject("previousItem", indexOfPrevious);

		modelAndView.addObject("nextItem", indexofNext);

		modelAndView.addObject("record", record);
		return modelAndView;
	}

	@RequestMapping(value = { "/clerk/clientRecord/delete/{id}" }, method = RequestMethod.GET)
	public ModelAndView clientRecordDelete(@PathVariable(value = "id") final int id, RedirectAttributes atts) {
		clientRecordService.deleteClientByID(id);
		atts.addFlashAttribute("successMessage", "Deleted Client Successfully");
		return new ModelAndView("redirect:/clerk/clientRecord/");
	}

}

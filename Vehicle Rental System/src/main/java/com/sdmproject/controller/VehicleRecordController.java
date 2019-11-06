package com.sdmproject.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
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
import com.sdmproject.helper.AddToListForFilter;
import com.sdmproject.model.Reservation;
import com.sdmproject.model.Vehicle;
import com.sdmproject.service.ReservationService;
import com.sdmproject.service.UserService;
import com.sdmproject.service.VehicleService;

import lombok.val;

@Controller
public class VehicleRecordController {
	Logger log = LoggerFactory.getLogger(VehicleRecordController.class);

	@Autowired
	private VehicleService vehicleRecordService;

	@Autowired
	private ReservationService reservationService;

	private FilterBean filterBean = FilterBean.getInstance();

	@Autowired
	private UserService userService;

	@ModelAttribute("username")
	public String getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		} else {
			return principal.toString();
		}
	}

	@RequestMapping(value = { "/admin/vehicleRecord/add" }, method = RequestMethod.GET)
	public ModelAndView vehicleRecordAdd() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/vehicleRecordAdd");
		return modelAndView;
	}

	@RequestMapping(value = { "/admin/vehicleRecord/add" }, method = RequestMethod.POST)
	public ModelAndView vehicleRecordAddPost(Vehicle record) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/vehicleRecordAdd");

		try {
			record.setColor(record.getColor().toUpperCase());
			vehicleRecordService.save(record);
			modelAndView.addObject("successMessage", "Vehicle Record has been added successfully.");
		} catch (DuplicateEntryException e) {
			modelAndView.addObject("errorMessage", e.getMessage());
			modelAndView.addObject("record", record);
		}
		return modelAndView;
	}

	@RequestMapping(value = { "/admin/vehicleRecord/update/{id}" }, method = RequestMethod.GET)
	public ModelAndView vehicleRecordUpdate(@PathVariable(value = "id") final int id,
			@RequestParam Optional<String> errorMessage, @RequestParam Optional<String> successMessage) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/vehicleRecordEdit");
		if (errorMessage.isPresent()) {
			modelAndView.addObject("errorMessage", errorMessage.get());
		}

		if (successMessage.isPresent()) {
			modelAndView.addObject("successMessage", successMessage.get());
		}

		Vehicle record = vehicleRecordService.findByID(id);
		modelAndView.addObject("record", record);
		return modelAndView;
	}

	@RequestMapping(value = { "/admin/vehicleRecord/update" }, method = RequestMethod.POST)
	public ModelAndView vehicleUpdate(Vehicle record) {
		ModelMap map = new ModelMap();
		vehicleRecordService.update(record);
		map.addAttribute("successMessage", "Vehicle Record has been updated successfully.");
		return new ModelAndView("redirect:/admin/vehicleRecord/update/" + record.getId(), map);
	}

	@RequestMapping(value = { "/common/vehicleRecord" }, method = RequestMethod.GET)
	public ModelAndView viewVehicleRecord(Optional<String> sort, Optional<String> order, Optional<String> type,
			Optional<String> make, Optional<String> model, Optional<String> color, Optional<String> year) {
		ModelAndView modelAndView = new ModelAndView();
		ArrayList<String> param = new ArrayList<String>();
		ArrayList<String> operator = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();

		if (sort.isPresent()) {
			// if already landed on page, process map and add value to modelAndView
			for (Map.Entry<String, String> entry : filterBean.getMap().entrySet()) {
				modelAndView.addObject(entry.getKey(), entry.getValue());
				AddToListForFilter.fillListUsingMap(param, operator, values, entry);
			}
		} else {
			// if landing on page for first time, clear the map
			filterBean.getMap().clear();
		}

		if (type.isPresent()) {
			modelAndView.addObject("type", type.get());
			filterBean.getMap().put("type", type.get());
			AddToListForFilter.add(param, operator, values,"type", "=", type.get());
		}

		if (make.isPresent()) {
			modelAndView.addObject("make", make.get());
			filterBean.getMap().put("make", make.get());
			AddToListForFilter.add(param, operator, values,"make", "=", make.get());
		}

		if (model.isPresent()) {
			modelAndView.addObject("model", model.get());
			filterBean.getMap().put("model", model.get());
			AddToListForFilter.add(param, operator, values,"model", "=", model.get());
			param.add("model");
			operator.add("=");
			values.add(model.get());
		}

		if (color.isPresent()) {
			filterBean.getMap().put("color", color.get());
			modelAndView.addObject("color", color.get());
			AddToListForFilter.add(param, operator, values,"color", "=", color.get());
		}

		if (year.isPresent()) {
			filterBean.getMap().put("year", year.get());
			modelAndView.addObject("year", year.get());
			Calendar prevYear = Calendar.getInstance();
			prevYear.add(Calendar.YEAR, - Integer.parseInt(year.get()));
			int greaterThan = prevYear.get(Calendar.YEAR);
			AddToListForFilter.add(param, operator, values,"year", ">=", greaterThan);
		}

		modelAndView.addObject("records",
				vehicleRecordService.filterMultipleAttribute(param, operator, values, sort, order));

		modelAndView.setViewName("common/vehicleRecord");
		modelAndView.addObject("sortProperty", sort.isPresent() ? sort.get() : "id");
		modelAndView.addObject("order", order.isPresent() ? order.get() : "asc");
		return modelAndView;
	}

	@RequestMapping(value = { "/common/vehicleRecord/view/{id}" }, method = RequestMethod.GET)
	public ModelAndView vehicleRecordView(@PathVariable(value = "id") final int id, String sort, String order) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/common/vehicleRecordDetailView");
		Vehicle record = vehicleRecordService.findByID(id);

		// get all ids of vehicles - find position of current
		ArrayList<String> param = new ArrayList<String>();
		ArrayList<String> operator = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();
		for (Map.Entry<String, String> entry : filterBean.getMap().entrySet()) {
			AddToListForFilter.fillListUsingMap(param, operator, values, entry);
		}

		List<Integer> vehicleRecordIds = vehicleRecordService.filterMultipleAttributeAndGetId(param, operator, values, Optional.of(sort), Optional.of(order));

		int indexOfTarget = vehicleRecordIds.indexOf(id);
		log.info("" + indexOfTarget);
		log.info("" + vehicleRecordIds);
		int indexOfPrevious = indexOfTarget != 0 ? vehicleRecordIds.get(indexOfTarget - 1) : -1;
		int indexofNext = indexOfTarget != vehicleRecordIds.size() - 1 ? vehicleRecordIds.get(indexOfTarget + 1) : -1;

		modelAndView.addObject("sortProperty", sort);
		modelAndView.addObject("order", order);

		List<Reservation> reservations = reservationService.findAllOutReservationSort(Optional.empty(),
				Optional.empty());
		boolean isAvailable = reservations.stream().filter(reservation -> (reservation.getVehicle().getId() == id))
				.findAny().isPresent();
		modelAndView.addObject("vehicleAvailability", !isAvailable);

		modelAndView.addObject("previousItem", indexOfPrevious);

		modelAndView.addObject("nextItem", indexofNext);

		modelAndView.addObject("record", record);

		return modelAndView;
	}

	

	@RequestMapping(value = { "/admin/vehicleRecord/delete/{id}" }, method = RequestMethod.GET)
	public ModelAndView vehicleRecordDelete(@PathVariable(value = "id") final int id, RedirectAttributes atts) {
		vehicleRecordService.deleteById(id);
		atts.addFlashAttribute("successMessage", "Deleted Vehicle Record Successfully");
		return new ModelAndView("redirect:/common/vehicleRecord/");
	}
}

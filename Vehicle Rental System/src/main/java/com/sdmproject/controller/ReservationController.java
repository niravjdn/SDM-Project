package com.sdmproject.controller;
import java.text.ParseException;
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
public class ReservationController {

	Logger logger = LoggerFactory.getLogger(ReservationController.class);

	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private ClientRecordService clientRecordService;
	
	@Autowired
	private VehicleService vehicleRecordService;
	
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

	@RequestMapping(value = { "/clerk/reservation/add" }, method = RequestMethod.GET)
	public ModelAndView createReservation() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("clerk/addReservation");
		List<Vehicle> vehicles = vehicleRecordService.findAll();
		modelAndView.addObject("vehicles", vehicles);
		
		List<ClientRecord> clients = clientRecordService.findAll();
		modelAndView.addObject("clients", clients);
		
		return modelAndView;
	}

	@RequestMapping(value = { "/clerk/reservation/add" }, method = RequestMethod.POST)
	public ModelAndView createReservationPost(int vehicle, int client,  String fromDate, String toDate, RedirectAttributes atts) throws ParseException, DuplicateEntryException {
		Vehicle v = vehicleRecordService.findByID(vehicle);
		ClientRecord c = clientRecordService.findByID(client);
		Reservation reservation = new Reservation();
		reservation.setClient(c);
		reservation.setVehicle(v);
		
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:MM");
		Date from = format.parse(fromDate);
		Date to = format.parse(toDate);
		System.out.println(fromDate);
		System.out.println(toDate);
		reservation.setFromDateTime(from);
		reservation.setToDateTime(to);
		reservation.setCreatedOn(new Date());
		reservationService.save(reservation);
		atts.addFlashAttribute("successMessage", "Reservation Added Successfully.");
		return new ModelAndView("redirect:/clerk/reservation/add");
	}
	
	@RequestMapping(value = { "/clerk/reservation/cancel" }, method = RequestMethod.GET)
	public ModelAndView viewReservationRecord(Optional<String> sort, Optional<String> order) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("records",reservationService.findAllWithSort(sort,order));
		modelAndView.setViewName("clerk/reservationRecord");
		modelAndView.addObject("sortProperty", sort.isPresent() ? sort.get() : "id");
		modelAndView.addObject("order",  order.isPresent() ? order.get() : "asc" );
		return modelAndView;
	}
	
	@RequestMapping(value = { "/clerk/reservation/cancel/{id}" }, method = RequestMethod.GET)
	public ModelAndView clientRecordDelete(@PathVariable(value = "id") final int id, RedirectAttributes atts) {
		reservationService.deleteReservationByID(id);
		atts.addFlashAttribute("successMessage", "Reservation Cancelled Successfully");
		return new ModelAndView("redirect:/clerk/reservation/cancel");
	}
	
	@RequestMapping(value = { "/clerk/reservation/returnView" }, method = RequestMethod.GET)
	public ModelAndView returnVehicleReturnView(Optional<String> sort, Optional<String> order) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("records", reservationService.findAllOutReservationSort(sort, order));
		modelAndView.setViewName("clerk/returnVehicle");
		return modelAndView;
	}
	
	@RequestMapping(value = { "/clerk/reservation/return/{id}" }, method = RequestMethod.GET)
	public ModelAndView returnVehicle(@PathVariable(value = "id") final int id, RedirectAttributes atts) {
		Reservation r = reservationService.findByID(id);
		ReservationHistory history = new ReservationHistory(r.getId(),r.getClient().getFirstName(), r.getClient().getLastName(), r.getClient().getDriverLicienceNo(), r.getClient().getExpiryDate(), r.getClient().getPhoneNo(), r.getVehicle().getColor(), r.getVehicle().getPlateNo(), r.getVehicle().getMake(), r.getVehicle().getModel(), r.getVehicle().getYear(), "user", r.getFromDateTime(), r.getToDateTime(), new Date());
		reservationHistoryService.save(history);
		reservationService.returnReservationByID(id);
		atts.addFlashAttribute("successMessage", "Vehicle Returned Successfully");
		return new ModelAndView("redirect:/clerk/reservation/returnView");
	}
	
	
	@RequestMapping(value = { "/admin/reservation/onging" }, method = RequestMethod.GET)
	public ModelAndView ongoingTransactions(Optional<String> sort, Optional<String> order) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("records", reservationService.findAllOutReservationSort(sort, order));
		modelAndView.setViewName("admin/currentOngoingRental");
		return modelAndView;
	}

	@RequestMapping(value = { "/admin/checkVehicleAvailibility" }, method = RequestMethod.GET)
	public ModelAndView checkVehicleAvailibility() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/checkVehicleAvailibility");
		return modelAndView;
	}
}

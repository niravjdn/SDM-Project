package com.sdmproject.controller;

import java.text.DateFormat;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.LastModified;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sdmproject.configuration.LastModifiedVehicle;
import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Reservation;
import com.sdmproject.model.ReservationHistory;
import com.sdmproject.model.Status;
import com.sdmproject.model.TypeOfReservation;
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
	
	

	@ModelAttribute("username")
	public String getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		} else {
			return principal.toString();
		}
	}

	@RequestMapping(value = { "/clerk/reservation/add" }, method = RequestMethod.GET)
	public ModelAndView createReservation() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("clerk/addReservation");
		List<Vehicle> vehicles = vehicleRecordService.findAll();
		modelAndView.addObject("vehicles", vehicles);
		
		LastModifiedVehicle.lastModifiedVehicle = null;
		LastModifiedVehicle.lastModifiedClient = null;
		
		List<ClientRecord> clients = clientRecordService.findAll();
		modelAndView.addObject("clients", clients);

		return modelAndView;
	}

	@RequestMapping(value = { "/clerk/reservation/add" }, method = RequestMethod.POST)
	public ModelAndView createReservationPost(int vehicle, boolean isRental, int client,
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date fromDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date toDate, RedirectAttributes atts)
			throws ParseException, DuplicateEntryException {
		Vehicle v = vehicleRecordService.findByID(vehicle);
		ClientRecord c = clientRecordService.findByID(client);
		
		if(LastModifiedVehicle.lastModifiedVehicle!= null && LastModifiedVehicle.lastModifiedVehicle.getId() == v.getId()) {
			atts.addFlashAttribute("errorMessage", "Reservation has been unsuccessful.");
			logger.debug("Reservation has been unsuccessful.");
			return new ModelAndView("redirect:/clerk/reservation/add");
		}
		
		if(LastModifiedVehicle.lastModifiedClient!= null && LastModifiedVehicle.lastModifiedClient.getId() == c.getId()) {
			atts.addFlashAttribute("errorMessage", "Reservation has been unsuccessful.");
			logger.debug("Reservation has been unsuccessful.");
			return new ModelAndView("redirect:/clerk/reservation/add");
		}
		
		List<Reservation> reservations = reservationService.findReservationWithDateRange(v.getId(), fromDate, toDate);
		if (reservations.isEmpty()) {
			if (fromDate.compareTo(toDate) == 1)
				atts.addFlashAttribute("errorMessage", "Please choose To_Date greater than From_Date");
			else {
				Reservation reservation = new Reservation();
				reservation.setClient(c);
				reservation.setVehicle(v);
				reservation.setTypeOfReservation(TypeOfReservation.valueOf(isRental ? 0 : 1).get());
				if(isRental) {
					atts.addFlashAttribute("successMessage", "Car has been rented successfully.");
				}else {
					atts.addFlashAttribute("successMessage", "Car has been reserved successfully.");
				}
				System.out.println(fromDate);
				System.out.println(toDate);
				reservation.setCreatedOn(new Date());
				reservation.setFromDateTime(fromDate);
				reservation.setToDateTime(toDate);
				
				reservationService.save(reservation);
			}
		} else {
			atts.addFlashAttribute("errorMessage", "Car has been reserved for another user.");
			logger.debug("Car has been reserved for another user.");
		}

		return new ModelAndView("redirect:/clerk/reservation/add");
	}

	@RequestMapping(value = { "/clerk/reservation/cancel" }, method = RequestMethod.GET)
	public ModelAndView getReservationRecords(Optional<String> sort, Optional<String> order) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("records", reservationService.findAllReservations(sort, order));
		modelAndView.setViewName("clerk/reservationRecord");
		modelAndView.addObject("sortProperty", sort.isPresent() ? sort.get() : "id");
		modelAndView.addObject("order", order.isPresent() ? order.get() : "asc");
		return modelAndView;
	}

	@RequestMapping(value = { "/clerk/reservation/cancel/{id}" }, method = RequestMethod.GET)
	public ModelAndView cancelReservation(@PathVariable(value = "id") final int id, RedirectAttributes atts) {
		// add to reservation history
		Reservation r = reservationService.findByID(id);
		ReservationHistory rh = new ReservationHistory(r.getId(), Status.CANCEL, r.getClient().getFirstName(),
				r.getClient().getLastName(), r.getClient().getDriverLicienceNo(), r.getClient().getExpiryDate(),
				r.getClient().getPhoneNo(), r.getVehicle().getColor(), r.getVehicle().getPlateNo(),
				r.getVehicle().getMake(), r.getVehicle().getModel(), r.getVehicle().getYear(), r.getFromDateTime(),
				r.getToDateTime(), new Date());
		reservationHistoryService.save(rh);

		reservationService.deleteReservationByID(id);
		atts.addFlashAttribute("successMessage", "Reservation Cancelled Successfully");
		return new ModelAndView("redirect:/clerk/reservation/cancel");
	}

	@RequestMapping(value = { "/clerk/reservation/makeitrental//{id}" }, method = RequestMethod.GET)
	public ModelAndView makeItRental(@PathVariable(value = "id") final int id, RedirectAttributes atts) {
		// add to reservation history
		Reservation r = reservationService.findByID(id);
		r.setFromDateTime(new Date());
		r.setTypeOfReservation(TypeOfReservation.RENTAL);
		reservationService.save(r);

		reservationService.deleteReservationByID(id);
		atts.addFlashAttribute("successMessage", "Reservation Marked as Rental Successfully");
		return new ModelAndView("redirect:/clerk/reservation/cancel");
	}

	@RequestMapping(value = { "/clerk/reservation/returnView" }, method = RequestMethod.GET)
	public ModelAndView returnVehicleReturnView(Optional<String> sort, Optional<String> order) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("records", reservationService.findAllRentals(sort, order));
		modelAndView.setViewName("clerk/returnVehicle");
		return modelAndView;
	}

	@RequestMapping(value = { "/clerk/reservation/return/{id}" }, method = RequestMethod.GET)
	public ModelAndView returnVehicle(@PathVariable(value = "id") final int id, RedirectAttributes atts) {
		Reservation r = reservationService.findByID(id);
		r.setToDateTime(new Date());
		ReservationHistory reservationHistory = new ReservationHistory(r.getId(), Status.RETURN,
				r.getClient().getFirstName(), r.getClient().getLastName(), r.getClient().getDriverLicienceNo(),
				r.getClient().getExpiryDate(), r.getClient().getPhoneNo(), r.getVehicle().getColor(),
				r.getVehicle().getPlateNo(), r.getVehicle().getMake(), r.getVehicle().getModel(),
				r.getVehicle().getYear(), r.getFromDateTime(), r.getToDateTime(), new Date());
		reservationHistoryService.save(reservationHistory);
		reservationService.returnReservationByID(id);
		atts.addFlashAttribute("successMessage", "Vehicle Returned Successfully");
		return new ModelAndView("redirect:/clerk/reservation/returnView");
	}

	@RequestMapping(value = { "/admin/rentals" }, method = RequestMethod.GET)
	public ModelAndView getCurrentRentals(Optional<String> sort, Optional<String> order) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("records", reservationService.findAllRentals(sort, order));
		modelAndView.setViewName("admin/rentals");
		return modelAndView;
	}

	@RequestMapping(value = { "/admin/rentals" }, method = RequestMethod.POST)
	public ModelAndView findRentalsByDueDate(
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date dueDate) {

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("/admin/rentals");

		modelAndView.addObject("records", reservationService.findAllRentalsOnDueDate(dueDate));
		modelAndView.addObject("dueDate", dueDate);

		return modelAndView;
	}

	@RequestMapping(value = { "/admin/reservations" }, method = RequestMethod.GET)
	public ModelAndView reservationsTransactions(Optional<String> sort, Optional<String> order) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("records", reservationService.findAllReservations(sort, order));
		modelAndView.setViewName("admin/reservations");
		return modelAndView;
	}

	@RequestMapping(value = { "/admin/reservations" }, method = RequestMethod.POST)
	public ModelAndView findReservationsByDueDate(
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date dueDate) {

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("/admin/reservations");

		modelAndView.addObject("records", reservationService.findAllResvationOnDueDate(dueDate));
		modelAndView.addObject("dueDate", dueDate);

		return modelAndView;
	}

	@RequestMapping(value = { "/admin/checkVehicleAvailibility" }, method = RequestMethod.GET)
	public ModelAndView checkVehicleAvailibility() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/checkVehicleAvailibility");

		// edit here
		List<Vehicle> vehicles = vehicleRecordService.findAll();
		modelAndView.addObject("vehicles", vehicles);

		return modelAndView;
	}

	@RequestMapping(value = { "/admin/checkVehicleAvailibility" }, method = RequestMethod.POST)
	public ModelAndView checkVehicleAvailibilityFromDateRange(@RequestParam("vehicleId") int vehicleId,
			@RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date fromDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date toDate) {

		ModelAndView modelAndView = new ModelAndView();
		System.out.println("fromDate " + fromDate.toString());
		System.out.println("todate " + toDate.toString());

		modelAndView.setViewName("admin/checkVehicleAvailibility");
		List<Vehicle> vehicles = vehicleRecordService.findAll();
		modelAndView.addObject("vehicles", vehicles);

		List<Reservation> reservations = reservationService.findReservationWithDateRange(vehicleId, fromDate, toDate);

		modelAndView.addObject("reservations", reservations);

		modelAndView.addObject("isVehicleAvailable", reservations.size() <= 0);
		modelAndView.addObject("vehicleId", vehicleId);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		modelAndView.addObject("fromDate", dateFormat.format(fromDate));
		modelAndView.addObject("toDate", dateFormat.format(toDate));

		return modelAndView;
	}
	

	@RequestMapping(value = { "/admin/checkVehicleDueDate" }, method = RequestMethod.GET)
	public ModelAndView checkVehicleDueDate() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/checkVehicleDueDate");

		// edit here
		List<Vehicle> vehicles = vehicleRecordService.findAll();
		modelAndView.addObject("vehicles", vehicles);

		return modelAndView;
	}

	
	@RequestMapping(value = { "/admin/checkVehicleDueDate" }, method = RequestMethod.POST)
	public ModelAndView checkVehicleDueDatePOST(@RequestParam("vehicleId") int vehicleId) {

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("admin/checkVehicleDueDate");
		List<Vehicle> vehicles = vehicleRecordService.findAll();
		modelAndView.addObject("vehicles", vehicles);

		List<Reservation> reservations = reservationService.findAllWithVehiclID(vehicleId);

		modelAndView.addObject("vehicleId", vehicleId);
		modelAndView.addObject("records", reservations);

		return modelAndView;
	}
	
	
	
}

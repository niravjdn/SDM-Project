package com.sdmproject.mapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sdmproject.model.Reservation;
import com.sdmproject.orm.Table;

public class ReservationMapper {
	private Table<Reservation, Integer> DAO;
	private List<Reservation> reservationList;

	public ReservationMapper() {
		this.DAO = new Table<Reservation, Integer>(Reservation.class);
		this.reservationList = new ArrayList<Reservation>();
	}

	public Reservation findById(int id) {
		Reservation resRecord;
		if (!reservationList.isEmpty()) {
			for (Reservation cr : this.reservationList) {
				if (cr.getId() == id)
					return cr;
			}
		}
		resRecord = DAO.queryById(id);
		reservationList.add(resRecord);
		return resRecord;
	}
	
	public List<Reservation> findAllReservations(String sortProp, String order) {
		if(!reservationList.isEmpty())
			return reservationList;
		reservationList = DAO.queryForParamsForDifferentOperations(new String[] { "typeOfReservation" },
				new String[] { "="},
				new String[] { "RESERVATION" });
		return reservationList;
	}
	
	public List<Reservation> findAllRentalWithVehicle(String sortProperty, String sortOrder, int id) {
		if(!reservationList.isEmpty())
			return reservationList;
		reservationList = DAO.queryForParamsForDifferentOperationsWithSort(new String[] { "typeOfReservation", "vehicleID" },
				new String[] { "=", "="}, new Object[] { "RENTAL", id}, sortProperty, sortOrder.equals("desc"));
		return reservationList;
	}
	
	public List<Reservation> findAllReservationOnDueDate(Date dueDate) {
		if(!reservationList.isEmpty())
			return reservationList;
		reservationList = DAO.queryForParamsForDifferentOperations(new String[] { "DATE(toDateTime)", "typeOfReservation" },
				new String[] { "=", "=" },
				new String[] { new SimpleDateFormat("yyyy-MM-dd").format(dueDate), "RESERVATION" });
		return reservationList;
	}
}

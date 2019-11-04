package com.sdmproject.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Reservation;
import com.sdmproject.orm.Table;

@Repository
public class ReservationRepository {
	Logger logger = LoggerFactory.getLogger(ReservationRepository.class);

	Table<Reservation, Integer> DAO;

	public ReservationRepository() {
		this.DAO = new Table<Reservation, Integer>(Reservation.class);
	}

	private List<Reservation> records = new ArrayList<Reservation>();
	private static int id = 1;

	public void save(Reservation record) {
		DAO.create(record);
	}

	public List<Reservation> findAllWithSort(Optional<String> sortProperty, Optional<String> sortOrder) {
		List<Reservation> result;
		if (sortProperty.isPresent()) {
			result = DAO.queryForAllWithSort(sortProperty.get(), sortOrder.get());
		} else {
			result = DAO.queryForAll();
		}
		System.out.println(result);
		return result;
	}

	public List<Reservation> findAllOutReservationSort(String sortProperty, String sortOrder) {
		List<Reservation> result = records.stream().filter(item -> (item.getFromDateTime().before(new Date())))
				.collect(Collectors.toList());

		return DAO.queryForParamsForDifferentOperationsWithSort(new String[] {"DATE(fromDateTime)"}, 
				new String[] {"<="}, new String[] {"DATE(CURDATE())"},
				sortProperty, sortOrder.equals("desc"));
	}


	public List<Reservation> findAllFutureWithSort(String sortProperty, String sortOrder) {
		return DAO.queryForParamsForDifferentOperationsWithSort(new String[] {"DATE(fromDateTime)"}, 
				new String[] {">"}, new String[] {"DATE(CURDATE())"},
				sortProperty, sortOrder.equals("desc"));
	}

	
	public void deleteReservationByID(int id) {
		DAO.deleteById(id);
	}

	public List<Reservation> findAll() {
		return DAO.queryForAll();
	}

	public void returnReservationByID(int id) {
		DAO.deleteById(id);
	}

	public Reservation findByID(int id) {
		return DAO.queryById(id);
	}

	public List<Reservation> findReservationWithDateRange(String plateNo, Date fromDate, Date toDate) {

		List<Reservation> result = records.stream().filter(i -> (i.getVehicle().getPlateNo().equals(plateNo)))
				.filter(i -> (i.getFromDateTime().compareTo(toDate) <= 0 && i.getToDateTime().compareTo(fromDate) >= 0))
				.collect(Collectors.toList());

		return result;
	}

	public List<Reservation> findAllOutReservationOnDueDate(Date dueDate) {
		List<Reservation> result = records.stream().filter(item -> (item.getToDateTime().compareTo(dueDate) == 0))
				.collect(Collectors.toList());

		return result;
	}
}

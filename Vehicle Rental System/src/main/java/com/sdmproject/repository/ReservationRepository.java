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

@Repository
public class ReservationRepository {
	Logger logger = LoggerFactory.getLogger(ReservationRepository.class);

	private List<Reservation> records = new ArrayList<Reservation>();
	private static int id = 1;
	
	public boolean isClientExist(String driverLicienceNo) {
		Reservation result = records.stream().filter(record -> record.getClient().getDriverLicienceNo().equals(driverLicienceNo))
				.findAny().orElse(null);

		if (result != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isVehicleExist(String plateNo) {
		Reservation result = records.stream().filter(record -> record.getVehicle().getPlateNo().equals(plateNo))
				.findAny().orElse(null);

		if (result != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public Reservation save(Reservation record) throws DuplicateEntryException {
		if (isClientExist(record.getClient().getDriverLicienceNo())&& isVehicleExist(record.getVehicle().getPlateNo())) {
			throw new DuplicateEntryException("The Client with same licence number already have booking.", null);
		}
		if (isVehicleExist(record.getVehicle().getPlateNo())) {
			throw new DuplicateEntryException("The Vehicle is not available", null);
		}
		record.setId(id++);
		records.add(record);
		return record;
	}


	public List<Reservation> findAllWithSort(Optional<String> sortProperty, Optional<String> sortOrder) {
		List<Reservation> result = records.stream().collect(Collectors.toList());
		if (sortProperty.isPresent()) {

			Collections.sort(result, new Comparator<Object>() {
				public int compare(Object o1, Object o2) {
					try {
						Method m = o1.getClass().getMethod("get" + WordUtils.capitalize(sortProperty.get()));
						if (sortProperty.get().contains("Date")) {
							Date s1 = (Date) m.invoke(o1);
							Date s2 = (Date) m.invoke(o2);
							return s1.compareTo(s2);
						} else if (sortProperty.get().contains("id")) {
							Integer s1 = (Integer) m.invoke(o1);
							Integer s2 = (Integer) m.invoke(o2);
							return s1.compareTo(s2);
						} else {
							String s1 = (String) m.invoke(o1);
							String s2 = (String) m.invoke(o2);
							return s1.compareTo(s2);
						}
					} catch (SecurityException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return 0;
				}
			});

			if (sortOrder.get().equals("desc")) {
				Collections.reverse(result);
			}
		}

		return result;
	}
	
	
	public List<Reservation> findAllOutReservationSort(Optional<String> sortProperty, Optional<String> sortOrder) {
		List<Reservation> result = records.stream().filter(item -> (item.getFromDateTime().before(new Date()) )).collect(Collectors.toList());
		if (sortProperty.isPresent()) {

			Collections.sort(result, new Comparator<Object>() {
				public int compare(Object o1, Object o2) {
					try {
						Method m = o1.getClass().getMethod("get" + WordUtils.capitalize(sortProperty.get()));
						if (sortProperty.get().contains("Date")) {
							Date s1 = (Date) m.invoke(o1);
							Date s2 = (Date) m.invoke(o2);
							return s1.compareTo(s2);
						} else if (sortProperty.get().contains("id")) {
							Integer s1 = (Integer) m.invoke(o1);
							Integer s2 = (Integer) m.invoke(o2);
							return s1.compareTo(s2);
						} else {
							String s1 = (String) m.invoke(o1);
							String s2 = (String) m.invoke(o2);
							return s1.compareTo(s2);
						}
					} catch (SecurityException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return 0;
				}
			});

			if (sortOrder.get().equals("desc")) {
				Collections.reverse(result);
			}
		}

		return result;
	}


	public void deleteReservationByID(int id) {
		for (Iterator<Reservation> iterator = records.iterator(); iterator.hasNext();) {
			if (iterator.next().getId() == id)
				iterator.remove();
		}
	}

	public List<Reservation> findAll() {
		return records;
	}

	public List<Integer> findAllReservationSortByID() {
		List<Integer> result = records.stream().sorted(Comparator.comparing(Reservation::getId))
				.map(Reservation::getId).collect(Collectors.toList());
		return result;
	}

	public void returnReservationByID(int id) {
		for (Iterator<Reservation> iterator = records.iterator(); iterator.hasNext();) {
			if (iterator.next().getId() == id)
				iterator.remove();
		}
	}

	public Reservation findByID(int id) {
		System.out.println(records);
		List<Reservation> result = records.stream().filter(record -> (record.getId() == id))
				.collect(Collectors.toList());
		return result.get(0);
	}


}


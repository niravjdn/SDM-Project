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

	public Reservation save(Reservation record) {
		record.setId(id++);
		records.add(record);
		return record;
	}

	public List<Reservation> findAllWithSort(Optional<String> sortProperty, Optional<String> sortOrder) {
		List<Reservation> result = records.stream().filter(item -> (item.getFromDateTime().after(new Date())))
				.collect(Collectors.toList());
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
		List<Reservation> result = records.stream().filter(item -> (item.getFromDateTime().before(new Date())))
				.collect(Collectors.toList());
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
		List<Integer> result = records.stream().sorted(Comparator.comparing(Reservation::getId)).map(Reservation::getId)
				.collect(Collectors.toList());
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

	public List<Reservation> findReservationWithDateRange(String plateNo, Date fromDate, Date toDate) {
		
		List<Reservation> result = records.stream().filter(i -> (i.getVehicle().getPlateNo().equals(plateNo)))
				.filter(i -> (i.getFromDateTime().compareTo(toDate) > 0 )).filter(i -> (i.getToDateTime().compareTo(fromDate) < 0 ))
				.collect(Collectors.toList());
		
		return result;
	}

}

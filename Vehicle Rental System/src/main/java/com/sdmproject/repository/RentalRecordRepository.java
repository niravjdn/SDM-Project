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
import com.sdmproject.model.RentalRecord;

@Repository
public class RentalRecordRepository {
	Logger logger = LoggerFactory.getLogger(RentalRecordRepository.class);

	private List<RentalRecord> records = new ArrayList<RentalRecord>();
	private static int id = 1;


	public boolean isClientExist(String driverLicienceNo) {
		RentalRecord result = records.stream().filter(record -> record.getDriverLicienceNo().equals(driverLicienceNo))
				.findAny().orElse(null);

		if (result != null) {
			return true;
		} else {
			return false;
		}
	}
	

	public RentalRecord save(RentalRecord record) throws DuplicateEntryException {
		if (isClientExist(record.getDriverLicienceNo())) {
			throw new DuplicateEntryException("The Client with same licience number already have reservation.", null);
		}
		record.setId(id++);
		records.add(record);
		return record;
	}
	
	public List<RentalRecord> findAllWithSort(Optional<String> sortProperty, Optional<String> sortOrder) {
		List<RentalRecord> result = records.stream().collect(Collectors.toList());
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


	public void deleteRentalRecordByID(int id) {
		for (Iterator<RentalRecord> iterator = records.iterator(); iterator.hasNext();) {
			if (iterator.next().getId() == id)
				iterator.remove();
		}
	}

	public List<RentalRecord> findAll() {
		return records;
	}

	public List<Integer> findAllRentalSortByID() {
		List<Integer> result = records.stream().sorted(Comparator.comparing(RentalRecord::getId))
				.map(RentalRecord::getId).collect(Collectors.toList());
		return result;
	}


}


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

@Repository
public class ClientRecordRepository {
	Logger logger = LoggerFactory.getLogger(ClientRecordRepository.class);

	private List<ClientRecord> records = new ArrayList<ClientRecord>();
	private static int id = 1;

	public int getCountOfVehicles() {
		return records.size();
	}

	public boolean isClientExist(String driverLicienceNo) {
		ClientRecord result = records.stream().filter(record -> record.getDriverLicienceNo().equals(driverLicienceNo))
				.findAny().orElse(null);

		if (result != null) {
			return true;
		} else {
			return false;
		}
	}

	public ClientRecord findByLicienceNo(String driverLicienceNo) {
		List<ClientRecord> result = records.stream()
				.filter(record -> (record.getDriverLicienceNo().equals(driverLicienceNo))).collect(Collectors.toList());
		return result.get(0);
	}

	public ClientRecord save(ClientRecord record) throws DuplicateEntryException {
		if (isClientExist(record.getDriverLicienceNo())) {
			throw new DuplicateEntryException("The Client with same licience number already exist.", null);
		}
		record.setId(id++);
		records.add(record);
		return record;
	}

	public ClientRecord update(ClientRecord record) throws DuplicateEntryException {
		int location = records.indexOf(record);
		records.set(location, record);
		logger.trace("Updated rows for saving user -----");
		return record;
	}

	public void deleteClientRecordByID(int id) {
		for (Iterator<ClientRecord> iterator = records.iterator(); iterator.hasNext();) {
			if (iterator.next().getId() == id)
				iterator.remove();
		}
	}

	public ClientRecord findById(int id) {

		List<ClientRecord> result = records.stream().filter(record -> (record.getId() == id))
				.collect(Collectors.toList());
		return result.get(0);
	}

	public List<ClientRecord> findAll() {
		return records;
	}

	public List<Integer> findAllIDsAndSortByID() {
		List<Integer> result = records.stream().sorted(Comparator.comparing(ClientRecord::getId))
				.map(ClientRecord::getId).collect(Collectors.toList());
		return result;
	}

	public List<ClientRecord> findAllWithSort(Optional<String> sortProperty, Optional<String> sortOrder) {
		List<ClientRecord> result = records.stream().collect(Collectors.toList());
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

	public List<Integer> findIDWithSort(String sortProperty, String sortOrder) {

		List<ClientRecord> result = records.stream().collect(Collectors.toList());

		Collections.sort(result, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				try {
					Method m = o1.getClass().getMethod("get" + WordUtils.capitalize(sortProperty));
					if (sortProperty.contains("Date")) {
						Date s1 = (Date) m.invoke(o1);
						Date s2 = (Date) m.invoke(o2);
						return s1.compareTo(s2);
					} else if (sortProperty.contains("id")) {
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
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}
		});

		if (sortOrder.equals("desc")) {
			Collections.reverse(result);
		}

		List<Integer> listOfIDs = result.stream().map(ClientRecord::getId).collect(Collectors.toList());

		return listOfIDs;
	}

}

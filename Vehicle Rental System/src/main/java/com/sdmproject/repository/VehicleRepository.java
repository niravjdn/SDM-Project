package com.sdmproject.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Vehicle;

@Repository
public class VehicleRepository {

	private List<Vehicle> records = new ArrayList<Vehicle>();

	private static int id = 1;

	Logger logger = LoggerFactory.getLogger(VehicleRepository.class);

	public int getCountOfVehicles() {
		return records.size();
	}

	public boolean isVehicleExist(String plateNo) {
		Vehicle result = records.stream().filter(record -> record.getPlateNo().equals(plateNo)).findAny().orElse(null);
		if (result != null) {
			return true;
		} else {
			return false;
		}
	}

	public Vehicle findByPlateNo(String plateNO) {
		List<Vehicle> result = records.stream().filter(record -> (record.getPlateNo().equals(plateNO)))
				.collect(Collectors.toList());
		return result.get(0);
	}

	public Vehicle save(Vehicle vehicle) throws DuplicateEntryException {
		if (isVehicleExist(vehicle.getPlateNo())) {
			throw new DuplicateEntryException("The Vehicle with same licience number already exist.", null);
		}
		vehicle.setId(id++);
		records.add(vehicle);
		return vehicle;
	}

	public void deleteVehicleByID(int id) {
		for (Iterator<Vehicle> iterator = records.iterator(); iterator.hasNext();) {
			if (iterator.next().getId() == id)
				iterator.remove();
		}
	}

	public List<Vehicle> findAllWithSort(Optional<String> sortProperty, Optional<String> sortOrder) {
		List<Vehicle> result = records.stream().collect(Collectors.toList());
		if (sortProperty.isPresent()) {

			Collections.sort(result, new Comparator<Object>() {
				public int compare(Object o1, Object o2) {
					try {
						Method m = o1.getClass().getMethod("get" + WordUtils.capitalize(sortProperty.get()));
						if (sortProperty.get().contains("year") || sortProperty.get().contains("id")) {
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

	public Vehicle findById(int id) {

		List<Vehicle> result = records.stream().filter(record -> (record.getId() == id)).collect(Collectors.toList());
		return result.get(0);
	}

	public List<Integer> findAllIDsAndSortByID() {
		List<Integer> result = records.stream().sorted(Comparator.comparing(Vehicle::getId)).map(Vehicle::getId)
				.collect(Collectors.toList());
		return result;
	}

	public List<Integer> findIDWithSort(String sortProperty, String sortOrder) {

		List<Vehicle> result = records.stream().collect(Collectors.toList());

		sort(sortProperty, result);

		if (sortOrder.equals("desc")) {
			Collections.reverse(result);
		}

		List<Integer> listOfIDs = result.stream().map(Vehicle::getId).collect(Collectors.toList());

		return listOfIDs;
	}

	private List<Vehicle> sort(String sortProperty, List<Vehicle> result) {
		Collections.sort(result, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				try {
					Method m = o1.getClass().getMethod("get" + WordUtils.capitalize(sortProperty));
					if (sortProperty.contains("year") || sortProperty.contains("id")) {
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
		return result;
	}

	public List<Vehicle> filterMultipleAttribute(Map<String, String> map, Optional<String> sortProperty, Optional<String> order) {
		List<Vehicle> filteredResult = records.stream().collect(Collectors.toList());
		if (!map.isEmpty()) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			filteredResult = filteredResult.stream().filter(item -> {
				try {
					return (predicateEvaluation(item, entry.getKey(), entry.getValue()));
				} catch (IllegalAccessException | SecurityException | NoSuchMethodException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}).collect(Collectors.toList());
		}
		}
		
		if(sortProperty.isPresent())
			filteredResult = sort(sortProperty.get(), filteredResult);
		
		if(order.isPresent()) {
			if(order.get().equals("desc"))
				Collections.reverse(filteredResult);
		}
		
		return filteredResult;
	}

	public boolean predicateEvaluation(Vehicle item, String key, String val) throws NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method m = item.getClass().getMethod("get" + WordUtils.capitalize(key));
		if (key.equals("year")) {
			int value = Integer.parseInt(val);
			int currentyear = Calendar.getInstance().get(Calendar.YEAR);
			if (item.getYear() > (currentyear - value))
				return true;
			else
				return false;
		} else {
			return ((String) m.invoke(item)).equalsIgnoreCase(val);
		}
	}

	public List<Vehicle> findAll() {
		return records;
	}

	public Vehicle update(Vehicle record) {
		int location = records.indexOf(record);
		records.set(location, record);
		logger.trace("Updated rows for saving user -----");
		return record;
	}

}

package com.sdmproject.mapper;

import java.util.ArrayList;
import java.util.List;

import com.sdmproject.model.ClientRecord;
import com.sdmproject.orm.Table;

public class ClientMapper {

	private Table<ClientRecord, Integer> DAO;
	private List<ClientRecord> cRec;

	public ClientMapper() {
		this.DAO = new Table<ClientRecord, Integer>(ClientRecord.class);
		this.cRec = new ArrayList<ClientRecord>();
	}

	public ClientRecord findById(int id) {
		ClientRecord clientRecord;
		if (!cRec.isEmpty()) {
			for (ClientRecord cr : this.cRec) {
				if (cr.getId() == id)
					return cr;
			}
		}
		clientRecord = DAO.queryById(id);
		cRec.add(clientRecord);
		return clientRecord;
	}

	public ClientRecord findByLicienceNo(String driverLicienceNo) {
		ClientRecord clientRecord;
		if (!cRec.isEmpty()) {
			for (ClientRecord cr : this.cRec) {
				if (cr.getDriverLicienceNo() == driverLicienceNo)
					return cr;
			}
		}

		clientRecord = DAO.queryForParamsEquality(new String[] { "driverLicienceNo" }, new Object[] { driverLicienceNo }).get(0);
		return clientRecord;
	}

	public List<ClientRecord> findAll() {
		if (!cRec.isEmpty()) {
			return cRec;
		}
		cRec = DAO.queryForAll();
		return cRec;
	}
	
	

}

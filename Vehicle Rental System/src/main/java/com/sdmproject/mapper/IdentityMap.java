package com.sdmproject.mapper;

import java.util.ArrayList;
import java.util.List;

import com.sdmproject.model.ClientRecord;
import com.sdmproject.orm.Table;

public class IdentityMap {

	private Table<ClientRecord, Integer> DAO;
	private List<ClientRecord> cRec;

	public IdentityMap() {
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
}

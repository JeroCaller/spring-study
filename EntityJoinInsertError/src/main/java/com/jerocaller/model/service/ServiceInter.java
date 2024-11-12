package com.jerocaller.model.service;

import java.util.List;

public interface ServiceInter<DTO> {
	List<DTO> selectAll();
}

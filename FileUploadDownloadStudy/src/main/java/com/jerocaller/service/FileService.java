package com.jerocaller.service;

import org.springframework.stereotype.Service;

import com.jerocaller.repository.FileEntityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

	private final FileEntityRepository fileEntityRepository;
	
	
}

package com.jerocaller.dto;

import com.jerocaller.entity.FileEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {
	
	private Integer id;
	private String filePath;
	private MembersResponse member;
	
	public static FileResponse toDto(FileEntity entity) {
		return FileResponse.builder()
			.id(entity.getId())
			.filePath(entity.getFilePath())
			.member(MembersResponse.toDto(entity.getMembers()))
			.build();
	}
	
}

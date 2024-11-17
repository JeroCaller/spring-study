package com.jerocaller.controller.command;

import org.springframework.stereotype.Component;

@Component
public class PageAndSortFactory {

	public CommandInter getCommand(Commands command) {
		if (command == null) return null;
		
		switch(command) {
			case ALL:
				return new SelectAllCommand();
			case SORT:
				return new SelectAllOnlySort();
			case PAGING:
				return new PageAndSortCommand();
		}
		
		return null;
	}
}

package com.jerocaller.controller.command;

public class PageAndSortFactory {
	private static final PageAndSortFactory factory = new PageAndSortFactory();
	private PageAndSortFactory() {}
	public static PageAndSortFactory getFactory() {
		return factory;
	}
	
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

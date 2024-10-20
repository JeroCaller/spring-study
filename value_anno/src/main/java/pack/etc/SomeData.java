package pack.etc;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class SomeData {
	private String name = "김큐엘";
	private int myNum = 132;
	private List<String> myFavFoods = Arrays.asList("햄버거", "치킨");
	
	public String getName() {
		return name;
	}
	
	public int getMyNum() {
		return myNum;
	}

	public List<String> getMyFavFoods() {
		return myFavFoods;
	}
	
}

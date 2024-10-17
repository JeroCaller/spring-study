package pack.model;

import java.util.Arrays;
import java.util.List;

public class SomeDao {
	
	public List<String> selectAllData() {
		List<String> dataList = Arrays.asList("강아지", "고양이", "거북이", "기린");
		return dataList;
	}
	
}

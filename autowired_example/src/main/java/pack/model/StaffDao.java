package pack.model;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class StaffDao implements DaoInterface {

	@Override
	public List<String> selectAll() {
		List<String> data = Arrays.asList(
			"직원-김큐엘",
			"직원-정디비",
			"직원-자바스"
		);
		
		return data;
	}
	
}

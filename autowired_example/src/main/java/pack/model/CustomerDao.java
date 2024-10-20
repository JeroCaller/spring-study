package pack.model;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class CustomerDao implements DaoInterface {

	@Override
	public List<String> selectAll() {
		List<String> data = Arrays.asList(
				"고객-나이썬",
				"고객-서부리",
				"고객-봄부트"
			);
			
		return data;
	}
	
}

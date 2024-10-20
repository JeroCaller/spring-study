package pack.etc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserData {
	
	// 필드 의존성 주입
	@Value("#{someData.name}")
	private String userName;
	
	private int userNum;
	private List<String> userFoods;
	
	public UserData() {}
	
	// 생성자 의존성 주입
	// 생성자 또는 setter 메서드 의존성 주입 방법 사용 시 
	// @Autowired 어노테이션과 같이 사용되어야 함.
	@Autowired
	public UserData(@Value("#{someData.myFavFoods}") List<String> foods) {
		this.userFoods = foods;
	}
	
	// setter 메서드 의존성 주입
	@Autowired
	public void setUserNum(@Value("#{someData.myNum}") int userNum) {
		this.userNum = userNum;
	}
	
	public void printUserData() {
		System.out.println("=== 유저 정보 ===");
		System.out.println("유저 이름: " + this.userName);
		System.out.println("유저 번호: " + this.userNum);
		System.out.println("유저가 좋아하는 음식 목록");
		
		int[] count = {0};
		this.userFoods.forEach(food -> {
			count[0]++;
			
			System.out.println(count[0] + ". " + food);
		});
	}
}

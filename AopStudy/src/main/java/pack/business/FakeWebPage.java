package pack.business;

import org.springframework.stereotype.Service;

@Service
public class FakeWebPage implements WebPage {
	
	@Override
	public void printWebPage() {
		System.out.println("제 웹 페이지에 오신 것을 환영합니다!");
	}
	
}

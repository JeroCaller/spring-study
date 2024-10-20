package pack.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:myconfig.properties")
public class UseInfo {
	
	@Value("${admin.name}")
	private String adminName;
	
	// # : spEL 사용
	// spEL 내에서의 EL (# 내부에 $) 사용 시 
	// EL($)은 작은 따옴표로 감싸줘야한다. 
	@Value("#{'${db.name}'}")
	private String dbName;
	
	@Value("${db.id}")
	private String dbId;
	
	@Value("${db.password}")
	private int dbPassword;
	
	public void printDbInfo() {
		System.out.println("DB 정보");
		System.out.println("Admin name: " + adminName);
		System.out.println("DB name: " + dbName);
		System.out.println("DB ID: " + dbId);
		System.out.println("DB Password: " + dbPassword);
	}
}

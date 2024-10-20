package pack.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
	private int id;
	private String name;
	private int price;
	private String category;
}

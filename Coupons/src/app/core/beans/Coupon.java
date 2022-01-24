package app.core.beans;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class Coupon {
	/*
	 * coupon bean
	 */
	private int id;
	private int companyId;
	private int categoryId;
	private String title;
	private String description;
	private Date startDate;
	private Date endDate;
	private int amount;
	private double price;
	private String image;

	public Coupon(int categoryId, String title, String description, Date startDate, Date endDate, int amount,
			double price, String image) {
		super();
		this.id = 0;
		this.categoryId = categoryId;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.price = price;
		this.image = image;
	}

	public Coupon(int id) { //to use the equals method in testing
		super();
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "Coupon [id=" + id + ", companyId=" + companyId + ", category=" + Category.getCategory(categoryId) + ", title=" + title
				+ ", description=" + description + ", startDate=" + startDate + ", endDate=" + endDate + ", amount="
				+ amount + ", price=" + price + ", image=" + image + "]";

	}
	
	// used for testing
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

}

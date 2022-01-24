package app.core.beans;

public enum Category {
	/*
	 * used to turn category id (int) in SQL to enum for easier use in Java  
	 */
	Food(1), Electricity(2), Restaurant(3), Vacation(4);
	
	private int id;

	private Category(int i) {
		this.id = i;
	}

	public int getId() {
		return this.id;
	}
	
	public static String getCategory(int id) {
		switch (id) {
		case 1: {
			return "Food";
		}
		case 2: {
			return "Electricity";
		}
		case 3: {
			return "Restaurant";
		}
		case 4: {
			return "Vacation";
		}
		default:
			throw new IllegalArgumentException("categoryId unexpected value: " + id);
		}
	}
	
}

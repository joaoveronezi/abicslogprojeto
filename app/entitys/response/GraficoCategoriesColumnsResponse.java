package entitys.response;

import java.util.ArrayList;
import java.util.List;

public class GraficoCategoriesColumnsResponse {

	private List<String> categories;
	
	private List<List<String>> columns;
	
	private int countPaises;
	
	private int countTipoCafes;
	
	public List<List<String>> getColumns() {
		return columns;
	}

	public void addColumns(List<String> column) {
		
		if(this.columns == null) {
			this.columns = new ArrayList<List<String>>();
		}
		
		this.columns.add(column);
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public int getCountPaises() {
		return countPaises;
	}

	public void setCountPaises(int countPaises) {
		this.countPaises = countPaises;
	}

	public int getCountTipoCafes() {
		return countTipoCafes;
	}

	public void setCountTipoCafes(int countTipoCafes) {
		this.countTipoCafes = countTipoCafes;
	}
}
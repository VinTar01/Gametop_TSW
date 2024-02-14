package model;

import java.io.Serializable;
import java.util.List;

public class GenreBean implements Serializable {

	private static final long serialVersionUID = -7545419204744508143L;
	
	private int genreId, deleteProductId;
	private String genreName;
	List<Integer> products;
	
	public GenreBean() {
		super();
	}

	public int getGenreId() {
		return genreId;
	}

	public void setGenreId(int genreId) {
		this.genreId = genreId;
	}

	public int getDeleteProductId() {
		return deleteProductId;
	}

	public void setDeleteProductId(int deleteProductId) {
		this.deleteProductId = deleteProductId;
	}

	public String getGenreName() {
		return genreName;
	}

	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}

	public List<Integer> getProducts() {
		return products;
	}

	public void setProducts(List<Integer> products) {
		this.products = products;
	}
	
}

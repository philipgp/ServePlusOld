package com.splus.pojo;

public class Rating {
	
	private String ratingId;
	private Registration regn;
	private RatingQuestion ratingQn;
	private byte rating;
	
	Rating(){}
	public Rating(String ratingId,Registration regn,RatingQuestion qn,byte rating){
		
		this.ratingId = ratingId;
		this.rating = rating;
		this.regn = regn;
		this.ratingQn = qn;
		
	}

	public String getRatingId() {
		return ratingId;
	}

	public void setRatingId(String ratingId) {
		this.ratingId = ratingId;
	}

	public Registration getRegn() {
		return regn;
	}

	public void setRegn(Registration regn) {
		this.regn = regn;
	}

	public RatingQuestion getRatingQn() {
		return ratingQn;
	}

	public void setRatingQn(RatingQuestion ratingQn) {
		this.ratingQn = ratingQn;
	}

	public byte getRating() {
		return rating;
	}

	public void setRating(byte rating) {
		this.rating = rating;
	}

}

package com.example.books.database.objects;

public class Author {
	private String name;
	
	public Author(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Author))  {
			return false;
		} else {
			return ((Author) o).getName().equals(this.name);
		}
	}
}

package cn.e3mail.freemarker.test;

public class Student {
	private int sno;
	private String name;
	private int age;
	private char genda;
	public Student() {
		super();
	}
	public Student(int sno, String name, int age, char genda) {
		super();
		this.sno = sno;
		this.name = name;
		this.age = age;
		this.genda = genda;
	}
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public char getGenda() {
		return genda;
	}
	public void setGenda(char genda) {
		this.genda = genda;
	}
	
	
}

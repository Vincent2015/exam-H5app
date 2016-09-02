package com.yonyou.esn.yycollege.model;

public class TestPaper {
    private Integer id;

    private String title;

    private String a;

    private String b;

    private String c;

    private String d;

    private String answer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

	@Override
	public String toString() {
		return "TestPaper [id=" + id + ", title=" + title + ", a=" + a + ", b=" + b + ", c=" + c + ", d=" + d
				+ ", answer=" + answer + "]";
	}
}
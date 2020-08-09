package org.boostcourse.bean;

public class UserBean {
    /* Bean은 3가지 특징을 가진다.
       1. 기본 생성자를 가진다.
       2. 필드는 private하게 선언한다.
       3. getter, setter 메소드를 가진다. (name이라는 필드의 getName(), setName() 메소드를 name 프로퍼티(property)라고 한다.)

    */
    private String name;
    private int age;
    private boolean male;

    public UserBean(){}

    public UserBean(String name, int age, boolean male){
        this.name = name;
        this.age = age;
        this.male = male;
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

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }
}

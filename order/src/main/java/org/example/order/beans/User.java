package org.example.order.beans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description 用户类
 * @Author danxiaodong
 * @Date 2023/7/18 21:17
 **/
@ApiModel("用户对象User")
public class User {
    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String username;
    /**
     * 年龄
     */
    @ApiModelProperty("年龄")
    private String age;
    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String gender;
    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String phoneNumber;
    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

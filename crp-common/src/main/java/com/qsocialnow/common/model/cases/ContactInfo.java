package com.qsocialnow.common.model.cases;

import java.io.Serializable;

import org.hibernate.validator.constraints.Email;

public class ContactInfo implements Serializable {

	private static final long serialVersionUID = 1700721065915782255L;

	private String phone;

    private String mobile;

    @Email(message = "app.invalid.email.validation")
    private String email;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

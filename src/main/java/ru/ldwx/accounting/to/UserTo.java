package ru.ldwx.accounting.to;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import ru.ldwx.accounting.util.UserUtil;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class UserTo extends BaseTo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 2, max = 100)
    @SafeHtml
    private String name;

    @Email
    @NotBlank
    @Size(max = 100)
    @SafeHtml
    private String email;

    @Size(min = 5, max = 32)
    private String password;

    @Range(min = 10, max = 10000)
    @NotNull
    private Integer sumPerDay = UserUtil.DEFAULT_SUM_PER_DAY;

    public UserTo() {
    }

    public UserTo(Integer id, String name, String email, String password, int sumPerDay) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.sumPerDay = sumPerDay;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSumPerDay() {
        return sumPerDay;
    }

    public void setSumPerDay(Integer sumPerDay) {
        this.sumPerDay = sumPerDay;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", sumPerDay='" + sumPerDay + '\'' +
                '}';
    }
}

package my.domain;

import lombok.Data;

@Data
public class User {
    private String User_Id;

    private String User_Name;



    @Override
    public String toString() {
        return "User{" +
                "Id='" + User_Id + '\'' +
                ", Name='" + User_Name + '\'' +
                '}';
    }
}

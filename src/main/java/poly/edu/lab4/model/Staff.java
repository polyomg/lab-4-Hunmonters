package poly.edu.lab4.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Staff {
    private String id;        // email
    private String fullname;
    private Boolean gender;   // true: Nam, false: Nữ
    private String birthday;  // "MM/dd/yyyy" (giữ String để đơn giản)
    private Double salary;
    private String photo;     // tên file ảnh (hiển thị)
    private Integer level;    // 0/1/2
}

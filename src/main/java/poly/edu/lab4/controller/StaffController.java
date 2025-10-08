package poly.edu.lab4.controller;

import poly.edu.lab4.model.Staff;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @ModelAttribute("levels")
    public Map<Integer, String> levels() {
        Map<Integer,String> m = new LinkedHashMap<>();
        m.put(0, "Úy"); m.put(1, "Tá"); m.put(2, "Tướng");
        return m;
    }

    @GetMapping("/create/form")
    public String form(Model model) {
        Staff s = Staff.builder()
                .photo("photo.jpg")
                .level(0)
                .gender(true)
                .salary(1000.0)
                .build();
        model.addAttribute("staff", s);
        model.addAttribute("message", "Vui lòng nhập thông tin nhân viên!");
        return "demo/staff-form";
    }

    @PostMapping("/create/save")
    public String save(
            @ModelAttribute("staff") Staff staff,
            @RequestPart(value = "photo_file", required = false) MultipartFile photoFile,
            Model model
    ) {
        // Gán tên file ảnh nếu upload
        if (photoFile != null && !photoFile.isEmpty()) {
            staff.setPhoto(photoFile.getOriginalFilename());
        }

        // Manual validate
        Map<String, String> errs = new HashMap<>();
        if (isBlank(staff.getId())) {
            errs.put("id", "Chưa nhập email");
        } else if (!isEmail(staff.getId())) {
            errs.put("id", "Email không đúng định dạng");
        }

        if (isBlank(staff.getFullname())) {
            errs.put("fullname", "Chưa nhập họ và tên");
        }

        if (staff.getGender() == null) {
            errs.put("gender", "Chưa chọn giới tính");
        }

        if (isBlank(staff.getBirthday()) || !isDateMMDDYYYY(staff.getBirthday())) {
            errs.put("birthday", "Ngày sinh phải định dạng MM/dd/yyyy");
        }

        if (staff.getSalary() == null) {
            errs.put("salary", "Chưa nhập lương");
        } else if (staff.getSalary() < 1000) {
            errs.put("salary", "Lương tối thiểu là 1000");
        }

        if (staff.getLevel() == null || staff.getLevel() < 0 || staff.getLevel() > 2) {
            errs.put("level", "Cấp bậc không hợp lệ");
        }

        if (!errs.isEmpty()) {
            model.addAttribute("message", "Vui lòng sửa các lỗi sau!");
            model.addAttribute("errs", errs);
            return "demo/staff-form";
        }

        model.addAttribute("message", "Dữ liệu đã nhập đúng!");
        return "demo/staff-result";
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    private boolean isEmail(String s) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(regex).matcher(s).matches();
    }

    private boolean isDateMMDDYYYY(String s) {
        // đơn giản: MM/dd/yyyy (không kiểm tra ngày hợp lệ theo lịch)
        return s.matches("^(0[1-9]|1[0-2])/([0-2][0-9]|3[01])/\\d{4}$");
    }
}

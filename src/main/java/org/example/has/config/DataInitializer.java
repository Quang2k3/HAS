package org.example.has.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.has.domain.entity.Department;
import org.example.has.domain.entity.User;
import org.example.has.domain.enums.Role;
import org.example.has.repository.DepartmentRepository;
import org.example.has.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initializes default data when the application starts.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initAdminAccount();
        initDepartments();
    }

    private void initAdminAccount() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Quản trị viên hệ thống")
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            log.info("✅ Đã tạo tài khoản Admin mặc định (admin / admin123)");
        }
    }

    /**
     * Initializes default departments.
     */
    private void initDepartments() {
        if (departmentRepository.count() == 0) {
            departmentRepository.save(Department.builder()
                    .code("DEP001")
                    .name("Nội tổng quát")
                    .description("Khoa khám và điều trị các bệnh nội khoa tổng quát")
                    .build());

            departmentRepository.save(Department.builder()
                    .code("DEP002")
                    .name("Tim mạch")
                    .description("Khoa chuyên khám và điều trị các bệnh về tim mạch")
                    .build());

            departmentRepository.save(Department.builder()
                    .code("DEP003")
                    .name("Da liễu")
                    .description("Khoa chuyên khám và điều trị các bệnh về da")
                    .build());

            departmentRepository.save(Department.builder()
                    .code("DEP004")
                    .name("Tai mũi họng")
                    .description("Khoa chuyên khám và điều trị các bệnh tai, mũi, họng")
                    .build());

            departmentRepository.save(Department.builder()
                    .code("DEP005")
                    .name("Mắt")
                    .description("Khoa chuyên khám và điều trị các bệnh về mắt")
                    .build());

            log.info("✅ Đã tạo 5 khoa khám bệnh mẫu");
        }
    }
}

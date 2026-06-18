package org.example.has.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.has.domain.entity.*;
import org.example.has.domain.enums.AppointmentStatus;
import org.example.has.domain.enums.Gender;
import org.example.has.domain.enums.Role;
import org.example.has.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Initializes default data when the application starts.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initAdminAccount();
        List<Department> departments = initDepartments();
        List<Doctor> doctors = initDoctors(departments);
        List<Patient> patients = initPatients();
        List<Appointment> appointments = initAppointments(patients, doctors);
        initMedicalRecords(appointments);
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
        }

        if (!userRepository.existsByUsername("admin2")) {
            User admin2 = User.builder()
                    .username("admin2")
                    .password(passwordEncoder.encode("123456"))
                    .fullName("Quản trị viên hệ thống")
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin2);
        }
    }

    /**
     * Initializes default departments.
     */
    private List<Department> initDepartments() {
        List<Department> list = new ArrayList<>();
        if (departmentRepository.count() == 0) {
            list.add(departmentRepository.save(Department.builder()
                    .code("DEP001")
                    .name("Nội tổng quát")
                    .description("Khoa khám và điều trị các bệnh nội khoa tổng quát")
                    .build()));

            list.add(departmentRepository.save(Department.builder()
                    .code("DEP002")
                    .name("Tim mạch")
                    .description("Khoa chuyên khám và điều trị các bệnh về tim mạch")
                    .build()));

            list.add(departmentRepository.save(Department.builder()
                    .code("DEP003")
                    .name("Da liễu")
                    .description("Khoa chuyên khám và điều trị các bệnh về da")
                    .build()));

            list.add(departmentRepository.save(Department.builder()
                    .code("DEP004")
                    .name("Tai mũi họng")
                    .description("Khoa chuyên khám và điều trị các bệnh tai, mũi, họng")
                    .build()));

            list.add(departmentRepository.save(Department.builder()
                    .code("DEP005")
                    .name("Mắt")
                    .description("Khoa chuyên khám và điều trị các bệnh về mắt")
                    .build()));

            log.info("✅ Đã tạo 5 khoa khám bệnh mẫu");
        } else {
            list = departmentRepository.findAll();
        }
        return list;
    }

    /**
     * Initializes default doctors.
     */
    private List<Doctor> initDoctors(List<Department> departments) {
        List<Doctor> list = new ArrayList<>();
        if (doctorRepository.count() == 0 && !departments.isEmpty()) {
            // Doctor 1 -> DEP001 (Nội tổng quát)
            list.add(createDoctor("DOC001", "Nguyễn Văn Minh", Gender.MALE, "0911223344", 
                    "minh.nv@hospital.com", "Nội khoa tổng quát", "doctor_minh", "doctor123", departments.get(0)));

            // Doctor 2 -> DEP002 (Tim mạch)
            list.add(createDoctor("DOC002", "Lê Thị Lan", Gender.FEMALE, "0922334455", 
                    "lan.lt@hospital.com", "Tim mạch học", "doctor_lan", "doctor123", departments.get(1)));

            // Doctor 3 -> DEP003 (Da liễu)
            list.add(createDoctor("DOC003", "Trần Anh Dũng", Gender.MALE, "0933445566", 
                    "dung.ta@hospital.com", "Da liễu thẩm mỹ", "doctor_dung", "doctor123", departments.get(2)));

            // Doctor 4 -> DEP004 (Tai mũi họng)
            list.add(createDoctor("DOC004", "Phạm Thanh Hương", Gender.FEMALE, "0944556677", 
                    "huong.pt@hospital.com", "Tai mũi họng trẻ em", "doctor_huong", "doctor123", departments.get(3)));

            // Doctor 5 -> DEP005 (Mắt)
            list.add(createDoctor("DOC005", "Hoàng Hoài Nam", Gender.MALE, "0955667788", 
                    "nam.hh@hospital.com", "Nhãn khoa tổng quát", "doctor_nam", "doctor123", departments.get(4)));

            log.info("✅ Đã tạo 5 bác sĩ mẫu tương ứng với 5 khoa");
        } else {
            list = doctorRepository.findAll();
        }
        return list;
    }

    private Doctor createDoctor(String code, String fullName, Gender gender, String phone, 
                                 String email, String specialty, String username, String password, Department department) {
        User user;
        if (!userRepository.existsByUsername(username)) {
            user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .fullName(fullName)
                    .role(Role.DOCTOR)
                    .enabled(true)
                    .build();
            user = userRepository.save(user);
        } else {
            user = userRepository.findByUsername(username).orElse(null);
        }

        return doctorRepository.save(Doctor.builder()
                .code(code)
                .fullName(fullName)
                .gender(gender)
                .phone(phone)
                .email(email)
                .specialty(specialty)
                .user(user)
                .department(department)
                .build());
    }

    /**
     * Initializes default patients.
     */
    private List<Patient> initPatients() {
        List<Patient> list = new ArrayList<>();
        if (patientRepository.count() == 0) {
            list.add(createPatient("PAT001", "Trần Thị Vy", LocalDate.of(1995, 8, 12), Gender.FEMALE, 
                    "0912345678", "123 Nguyễn Trãi, Thanh Xuân, Hà Nội", "patient_vy", "patient123"));

            list.add(createPatient("PAT002", "Nguyễn Hùng Cường", LocalDate.of(1988, 3, 22), Gender.MALE, 
                    "0987654321", "456 Lê Lợi, Quận 1, TP. Hồ Chí Minh", "patient_cuong", "patient123"));

            list.add(createPatient("PAT003", "Lê Thanh Hoa", LocalDate.of(2000, 11, 5), Gender.FEMALE, 
                    "0905123456", "789 Trần Hưng Đạo, Hải Châu, Đà Nẵng", "patient_hoa", "patient123"));

            log.info("✅ Đã tạo 3 bệnh nhân mẫu");
        } else {
            list = patientRepository.findAll();
        }
        return list;
    }

    private Patient createPatient(String code, String fullName, LocalDate dob, Gender gender, 
                                   String phone, String address, String username, String password) {
        User user;
        if (!userRepository.existsByUsername(username)) {
            user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .fullName(fullName)
                    .role(Role.PATIENT)
                    .enabled(true)
                    .build();
            user = userRepository.save(user);
        } else {
            user = userRepository.findByUsername(username).orElse(null);
        }

        return patientRepository.save(Patient.builder()
                .code(code)
                .fullName(fullName)
                .dateOfBirth(dob)
                .gender(gender)
                .phone(phone)
                .address(address)
                .user(user)
                .build());
    }

    /**
     * Initializes default appointments.
     */
    private List<Appointment> initAppointments(List<Patient> patients, List<Doctor> doctors) {
        List<Appointment> list = new ArrayList<>();
        if (appointmentRepository.count() == 0 && !patients.isEmpty() && !doctors.isEmpty()) {
            LocalDate today = LocalDate.now();

            // APP001: SCHEDULED
            list.add(appointmentRepository.save(Appointment.builder()
                    .code("APP001")
                    .appointmentDate(today.plusDays(1))
                    .appointmentTime(LocalTime.of(9, 0))
                    .status(AppointmentStatus.SCHEDULED)
                    .reason("Khám định kỳ tai mũi họng do đau họng kéo dài")
                    .patient(patients.get(0)) // Trần Thị Vy
                    .doctor(doctors.get(3))  // Phạm Thanh Hương (Tai mũi họng)
                    .build()));

            // APP002: COMPLETED
            list.add(appointmentRepository.save(Appointment.builder()
                    .code("APP002")
                    .appointmentDate(today.minusDays(1))
                    .appointmentTime(LocalTime.of(14, 0))
                    .status(AppointmentStatus.COMPLETED)
                    .reason("Kiểm tra huyết áp và nhịp tim không ổn định")
                    .patient(patients.get(1)) // Nguyễn Hùng Cường
                    .doctor(doctors.get(1))  // Lê Thị Lan (Tim mạch)
                    .build()));

            // APP003: CANCELLED
            list.add(appointmentRepository.save(Appointment.builder()
                    .code("APP003")
                    .appointmentDate(today.minusDays(2))
                    .appointmentTime(LocalTime.of(10, 30))
                    .status(AppointmentStatus.CANCELLED)
                    .reason("Khám da liễu do dị ứng mụn đỏ")
                    .patient(patients.get(2)) // Lê Thanh Hoa
                    .doctor(doctors.get(2))  // Trần Anh Dũng (Da liễu)
                    .build()));

            log.info("✅ Đã tạo 3 lịch hẹn khám mẫu");
        } else {
            list = appointmentRepository.findAll();
        }
        return list;
    }

    /**
     * Initializes default medical records.
     */
    private void initMedicalRecords(List<Appointment> appointments) {
        if (medicalRecordRepository.count() == 0) {
            for (Appointment apt : appointments) {
                if (apt.getStatus() == AppointmentStatus.COMPLETED && apt.getCode().equals("APP002")) {
                    medicalRecordRepository.save(MedicalRecord.builder()
                            .code("MR001")
                            .diagnosis("Tăng huyết áp độ 1, rối loạn lipid máu nhẹ")
                            .result("Huyết áp đo tại phòng khám 145/90 mmHg. Nhịp tim 82 lần/phút. Tiếng tim đều rõ. Xét nghiệm mỡ máu Cholesterol toàn phần tăng nhẹ.")
                            .prescription("1. Amlodipin 5mg - 30 viên (uống 1 viên vào buổi sáng sau ăn)\n2. Atorvastatin 10mg - 30 viên (uống 1 viên vào buổi tối trước khi đi ngủ)")
                            .notes("Hạn chế ăn mặn (dưới 5g muối/ngày). Giảm tối đa mỡ động vật và phủ tạng. Tăng cường rau xanh, trái cây. Tập thể dục 30 phút mỗi ngày. Tái khám sau 30 ngày hoặc khi có triệu chứng đau đầu, chóng mặt, tức ngực.")
                            .createdAt(LocalDateTime.now().minusDays(1))
                            .appointment(apt)
                            .build());
                    log.info("✅ Đã tạo hồ sơ bệnh án mẫu cho lịch hẹn APP002");
                    break;
                }
            }
        }
    }
}

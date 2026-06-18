# HƯỚNG DẪN KIỂM THỬ HỆ THỐNG QUẢN LÝ ĐẶT LỊCH KHÁM BỆNH (HAS) QUA SWAGGER UI

Tài liệu này cung cấp hướng dẫn chi tiết từng bước cho đội ngũ kiểm thử (QA/Tester) để thực hiện kiểm thử các API của dự án **Hospital Appointment System (HAS)** thông qua giao diện Swagger UI.

---

## I. THÔNG TIN CHUNG & KHỞI TẠO MÔI TRƯỜNG

*   **Địa chỉ Swagger UI:** `http://localhost:8080/swagger-ui.html` (chuyển hướng sang `http://localhost:8080/swagger-ui/index.html`)
*   **Tệp đặc tả OpenAPI (JSON):** `http://localhost:8080/api-docs` hoặc `http://localhost:8080/v3/api-docs`
*   **Cơ chế xác thực:** Hệ thống sử dụng mã định danh **JWT (JSON Web Token)**. Token được cấp khi đăng nhập hoặc đăng ký thành công, có thời gian hiệu lực là **24 giờ** (`86400000ms`).
*   **Cách sử dụng Token trên Swagger:**
    1. Gọi API Đăng nhập (`POST /api/auth/login`) hoặc Đăng ký (`POST /api/auth/register`).
    2. Sao chép chuỗi mã thông báo `token` trong phần phản hồi `data`.
    3. Nhấp vào nút **Authorize 🔓** (ở góc trên cùng bên phải giao diện Swagger).
    4. Dán chuỗi token vào ô **Value** (Không cần gõ thêm chữ "Bearer " phía trước, Swagger sẽ tự động thêm tiếp đầu ngữ này).
    5. Nhấp **Authorize** rồi **Close**. Lúc này, các API yêu cầu bảo mật sẽ được kích hoạt quyền truy cập.

---

## II. DANH SÁCH TÀI KHOẢN KIỂM THỬ MẶC ĐỊNH (DATA INITIALIZER)

Khi ứng dụng khởi động, hệ thống đã tự động nạp một số dữ liệu mẫu trong cơ sở dữ liệu để phục vụ việc kiểm thử ngay lập tức. Dưới đây là danh sách tài khoản sẵn có:

### 1. Tài khoản Quản trị viên (ADMIN)
| Username | Password | Tên hiển thị | Vai trò (Role) | Chức năng chính |
| :--- | :--- | :--- | :--- | :--- |
| `admin` | `admin123` | Quản trị viên hệ thống | `ROLE_ADMIN` | Quản lý bác sĩ, bệnh nhân, lịch hẹn, xem báo cáo |

### 2. Danh sách Bác sĩ (DOCTOR)
*Mật khẩu mặc định của tất cả bác sĩ mẫu là:* `doctor123` (Vai trò: `ROLE_DOCTOR`)

| ID | Username | Tên bác sĩ | Giới tính | Số điện thoại | Khoa trực thuộc | Chuyên khoa |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **1** | `doctor_minh` | Nguyễn Văn Minh | Nam | `0911223344` | Nội tổng quát (DEP001) | Nội khoa tổng quát |
| **2** | `doctor_lan` | Lê Thị Lan | Nữ | `0922334455` | Tim mạch (DEP002) | Tim mạch học |
| **3** | `doctor_dung` | Trần Anh Dũng | Nam | `0933445566` | Da liễu (DEP003) | Da liễu thẩm mỹ |
| **4** | `doctor_huong` | Phạm Thanh Hương | Nữ | `0944556677` | Tai mũi họng (DEP004) | Tai mũi họng trẻ em |
| **5** | `doctor_nam` | Hoàng Hoài Nam | Nam | `0955667788` | Mắt (DEP005) | Nhãn khoa tổng quát |

### 3. Danh sách Bệnh nhân (PATIENT)
*Mật khẩu mặc định của tất cả bệnh nhân mẫu là:* `patient123` (Vai trò: `ROLE_PATIENT`)

| ID | Username | Tên bệnh nhân | Ngày sinh | Giới tính | Số điện thoại | Địa chỉ thường trú |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **1** | `patient_vy` | Trần Thị Vy | 12/08/1995 | Nữ | `0912345678` | 123 Nguyễn Trãi, Thanh Xuân, Hà Nội |
| **2** | `patient_cuong` | Nguyễn Hùng Cường | 22/03/1988 | Nam | `0987654321` | 456 Lê Lợi, Quận 1, TP. Hồ Chí Minh |
| **3** | `patient_hoa` | Lê Thanh Hoa | 05/11/2000 | Nữ | `0905123456` | 789 Trần Hưng Đạo, Hải Châu, Đà Nẵng |

### 4. Dữ liệu Lịch hẹn khám ban đầu (Appointments)
*   **Lịch hẹn 1 (Mã: `APP001`)**: Trạng thái `SCHEDULED` (Chờ khám). Ngày khám: Ngày mai, giờ khám: `09:00`. Bệnh nhân: Trần Thị Vy (ID 1) - Bác sĩ: Phạm Thanh Hương (ID 4). Lý do: "Khám định kỳ tai mũi họng do đau họng kéo dài".
*   **Lịch hẹn 2 (Mã: `APP002`)**: Trạng thái `COMPLETED` (Đã khám xong). Ngày khám: Ngày hôm qua, giờ khám: `14:00`. Bệnh nhân: Nguyễn Hùng Cường (ID 2) - Bác sĩ: Lê Thị Lan (ID 2). Có đính kèm Hồ sơ bệnh án **MR001** (Chẩn đoán: Tăng huyết áp độ 1, rối loạn lipid máu nhẹ).
*   **Lịch hẹn 3 (Mã: `APP003`)**: Trạng thái `CANCELLED` (Đã hủy). Ngày khám: Ngày hôm kia, giờ khám: `10:30`. Bệnh nhân: Lê Thanh Hoa (ID 3) - Bác sĩ: Trần Anh Dũng (ID 3).

---

## III. QUY TRÌNH KIỂM THỬ LUỒNG NGHIỆP VỤ CHÍNH (STEP-BY-STEP FLOWS)

Để kiểm tra xem hệ thống có chạy đúng logic nghiệp vụ liên kết hay không, hãy thực hiện kiểm thử theo các kịch bản sau:

### Luồng 1: Đăng ký, Đăng nhập và Đặt lịch hẹn (Vai trò Bệnh nhân)
1.  **Bước 1 - Đăng ký:** Mở thẻ **1. Authentication**, chọn API `POST /api/auth/register`. Nhập thông tin bệnh nhân mới (ví dụ: username `patient_test`, mật khẩu `test123456`, tên `Nguyễn Văn Kiểm Thử`). Gửi yêu cầu. Hệ thống sẽ trả về mã JWT token.
2.  **Bước 2 - Lưu Token:** Lấy JWT token nhận được ở Bước 1 dán vào mục **Authorize 🔓** của Swagger.
3.  **Bước 3 - Tra cứu Bác sĩ:** Người dùng muốn chọn bác sĩ chuyên khoa tim mạch. Gọi API `GET /api/doctors/department/2` để xem danh sách các bác sĩ thuộc khoa Tim mạch (ID khoa Tim mạch mặc định là `2`). Tìm thấy bác sĩ Lê Thị Lan (ID bác sĩ là `2`).
4.  **Bước 4 - Đặt lịch hẹn:** Gọi API `POST /api/appointments` để đặt lịch. Cung cấp các thông tin:
    *   `patientId`: ID bệnh nhân vừa tạo (Có thể tra cứu từ thông tin trả về lúc đăng ký hoặc dùng tài khoản `patient_vy` có ID là `1`).
    *   `doctorId`: `2` (Bác sĩ Lê Thị Lan)
    *   `appointmentDate`: Chọn một ngày trong tương lai (Ví dụ: `2026-07-20`)
    *   `appointmentTime`: `09:30`
    *   `reason`: "Kiểm tra tim đập nhanh"
5.  **Bước 5 - Kiểm tra xung đột lịch:** Tiếp tục thực hiện lại yêu cầu trên một lần nữa với cùng thông tin bác sĩ, ngày, giờ khám. Hệ thống bắt buộc phải báo lỗi **400 Bad Request** với thông báo xung đột lịch khám của bác sĩ.
6.  **Bước 6 - Bệnh nhân xem lịch cá nhân:** Gọi API `GET /api/appointments/patient/{patientId}` (với ID bệnh nhân của mình) để kiểm tra xem lịch hẹn vừa đặt có xuất hiện trong danh sách và ở trạng thái `SCHEDULED` hay không.

### Luồng 2: Thực hiện khám bệnh và Hoàn thành ca khám (Vai trò Bác sĩ / Admin)
1.  **Bước 1 - Bác sĩ đăng nhập:** Gọi API `POST /api/auth/login` với tài khoản bác sĩ (ví dụ: `doctor_lan` / `doctor123`). Lấy token mới và dán vào mục **Authorize 🔓**.
2.  **Bước 2 - Bác sĩ xem danh sách lịch hẹn cần khám:** Gọi API `GET /api/appointments/doctor/{doctorId}` (với ID của bác sĩ Lê Thị Lan là `2`). Xác định ID của lịch hẹn đang chờ khám (`SCHEDULED`) - ví dụ: Lịch hẹn vừa đặt ở Luồng 1.
3.  **Bước 3 - Thực hiện khám và Hoàn thành ca khám:** Khi đã khám xong, gọi API `PUT /api/appointments/{id}/complete` (với `id` là ID lịch hẹn). Hệ thống sẽ cập nhật trạng thái của lịch hẹn thành `COMPLETED`.
4.  **Bước 4 - Kiểm tra trạng thái:** Thử gọi API hủy lịch hẹn `PUT /api/appointments/{id}/cancel` đối với ca khám đã hoàn thành này. Hệ thống bắt buộc phải trả về lỗi **400 Bad Request** vì chỉ cho phép hủy lịch hẹn ở trạng thái `SCHEDULED`.

### Luồng 3: Quản trị và Thống kê hệ thống (Vai trò Admin)
1.  **Bước 1 - Admin đăng nhập:** Gọi API `POST /api/auth/login` với tài khoản `admin` / `admin123`. Lấy token và tiến hành **Authorize**.
2.  **Bước 2 - Quản lý Bác sĩ:** Gọi API `POST /api/doctors` để tạo thêm một bác sĩ mới, ví dụ: bác sĩ Răng Hàm Mặt, cung cấp tài khoản đăng nhập đi kèm.
3.  **Bước 3 - Xem Báo cáo Thống kê:** Gọi API `GET /api/statistics/overview`. Hệ thống trả về số liệu tổng hợp (Tổng số bệnh nhân, bác sĩ, lịch hẹn theo từng trạng thái cụ thể và danh sách phân bổ bác sĩ theo các khoa khám bệnh). Kiểm tra xem số liệu tổng quan có tăng lên sau khi thêm bác sĩ mới ở bước 2 hay không.

---

## IV. CHI TIẾT CÁC API, THAM SỐ VÀ CÁC TRƯỜNG DỮ LIỆU

Dưới đây là mô tả chi tiết của từng API trong 5 nhóm Controller được cấu hình hiển thị trên Swagger UI:

### 1. Nhóm API Xác thực (Tag: `1. Authentication`)

#### 1.1. Đăng ký tài khoản bệnh nhân mới (`POST /api/auth/register`)
*   **Phân quyền:** Công khai (Không cần Token)
*   **Yêu cầu Request Body (JSON):**
    ```json
    {
      "username": "patient_test",
      "password": "password123",
      "fullName": "Nguyễn Văn A",
      "phone": "0988776655",
      "address": "Số 1 Đại Cồ Việt, Hai Bà Trưng, Hà Nội"
    }
    ```
    *   `username` *(Chuỗi, Bắt buộc)*: Độ dài từ 3 đến 50 ký tự, không được trùng lặp trong hệ thống.
    *   `password` *(Chuỗi, Bắt buộc)*: Độ dài tối thiểu 6 ký tự.
    *   `fullName` *(Chuỗi, Bắt buộc)*: Họ tên đầy đủ của bệnh nhân.
    *   `phone` *(Chuỗi, Tùy chọn)*: Số điện thoại liên lạc.
    *   `address` *(Chuỗi, Tùy chọn)*: Địa chỉ thường trú.
*   **Phản hồi (Response) khi thành công (201 Created):**
    ```json
    {
      "success": true,
      "message": "Đăng ký thành công",
      "data": {
        "token": "eyJhbGciOiJIUzUxMiJ9...",
        "username": "patient_test",
        "fullName": "Nguyễn Văn A",
        "role": "PATIENT"
      }
    }
    ```

#### 1.2. Đăng nhập hệ thống (`POST /api/auth/login`)
*   **Phân quyền:** Công khai (Không cần Token)
*   **Yêu cầu Request Body (JSON):**
    ```json
    {
      "username": "admin",
      "password": "admin123"
    }
    ```
    *   `username` *(Chuỗi, Bắt buộc)*: Tên đăng nhập.
    *   `password` *(Chuỗi, Bắt buộc)*: Mật khẩu truy cập.
*   **Phản hồi khi thành công (200 OK):**
    ```json
    {
      "success": true,
      "message": "Đăng nhập thành công",
      "data": {
        "token": "eyJhbGciOiJIUzUxMiJ9...",
        "username": "admin",
        "fullName": "Quản trị viên hệ thống",
        "role": "ADMIN"
      }
    }
    ```

---

### 2. Nhóm API Quản lý Bác sĩ (Tag: `3. Doctor Management`)

#### 2.1. Lấy danh sách tất cả bác sĩ (`GET /api/doctors`)
*   **Phân quyền:** Yêu cầu đã đăng nhập (Bất kỳ vai trò nào có JWT hợp lệ)
*   **Tham số:** Không có.
*   **Phản hồi thành công (200 OK):** Trả về danh sách chứa các thông tin như ID, mã số bác sĩ, tên chuyên khoa, thông tin liên lạc và khoa trực thuộc.

#### 2.2. Xem chi tiết thông tin bác sĩ theo ID (`GET /api/doctors/{id}`)
*   **Phân quyền:** Yêu cầu đã đăng nhập.
*   **Tham số đường dẫn (Path Parameter):**
    *   `id` *(Số nguyên, Bắt buộc)*: ID duy nhất của bác sĩ cần xem (ví dụ: `1`).
*   **Phản hồi lỗi (404 Not Found):** Nếu truyền ID không tồn tại trong hệ thống.

#### 2.3. Lấy danh sách bác sĩ theo khoa (`GET /api/doctors/department/{departmentId}`)
*   **Phân quyền:** Yêu cầu đã đăng nhập.
*   **Tham số đường dẫn (Path Parameter):**
    *   `departmentId` *(Số nguyên, Bắt buộc)*: ID của khoa khám bệnh cần lọc bác sĩ (ví dụ: `1` cho khoa Nội tổng quát, `2` cho khoa Tim mạch).

#### 2.4. Tìm kiếm bác sĩ theo tên (`GET /api/doctors/search`)
*   **Phân quyền:** Yêu cầu đã đăng nhập.
*   **Tham số truy vấn (Query Parameter):**
    *   `keyword` *(Chuỗi, Bắt buộc)*: Từ khóa tìm kiếm theo tên bác sĩ (Ví dụ: `Minh`, `Lan`). Không phân biệt chữ hoa/thường.

#### 2.5. Thêm mới bác sĩ (`POST /api/doctors`)
*   **Phân quyền:** Chỉ tài khoản có vai trò `ADMIN` mới được phép gọi.
*   **Yêu cầu Request Body (JSON):**
    ```json
    {
      "code": "DOC006",
      "fullName": "Phạm Minh Hoàng",
      "gender": "MALE",
      "phone": "0966554433",
      "email": "hoang.pm@hospital.com",
      "specialty": "Răng Hàm Mặt học",
      "username": "doctor_hoang",
      "password": "doctor123"
    }
    ```
    *   `code` *(Chuỗi, Bắt buộc)*: Mã định danh bác sĩ (Duy nhất).
    *   `fullName` *(Chuỗi, Bắt buộc)*: Họ tên bác sĩ.
    *   `gender` *(MALE / FEMALE / OTHER, Tùy chọn)*: Giới tính.
    *   `phone` *(Chuỗi, Tùy chọn)*: Số điện thoại.
    *   `email` *(Chuỗi định dạng Email, Tùy chọn)*: Email bác sĩ.
    *   `specialty` *(Chuỗi, Tùy chọn)*: Chuyên khoa của bác sĩ.
    *   `username` *(Chuỗi, Tùy chọn)*: Tên tài khoản đăng nhập mong muốn để tạo tài khoản hệ thống đi kèm (Nếu nhập trường này thì bắt buộc phải nhập `password`).
    *   `password` *(Chuỗi, Tùy chọn)*: Mật khẩu cho tài khoản bác sĩ mới.
*   **Phản hồi thành công (201 Created):** Trả về đối tượng Bác sĩ đã tạo thành công và liên kết với tài khoản User mới có vai trò `DOCTOR`.

#### 2.6. Cập nhật thông tin bác sĩ (`PUT /api/doctors/{id}`)
*   **Phân quyền:** Chỉ tài khoản có vai trò `ADMIN` mới được phép gọi.
*   **Tham số đường dẫn:** `id` của bác sĩ cần cập nhật.
*   **Yêu cầu Request Body (JSON):** Giống như khi tạo mới (Tuy nhiên, không thể cập nhật mã bác sĩ `code`, `username` và `password` qua API này - các trường này chỉ dùng khi tạo mới).

#### 2.7. Xóa thông tin bác sĩ (`DELETE /api/doctors/{id}`)
*   **Phân quyền:** Chỉ dành cho `ADMIN`.
*   **Tham số đường dẫn:** `id` bác sĩ cần xóa.

---

### 3. Nhóm API Quản lý Bệnh nhân (Tag: `4. Patient Management`)

#### 3.1. Lấy danh sách tất cả bệnh nhân (`GET /api/patients`)
*   **Phân quyền:** Chỉ dành cho vai trò `ADMIN` hoặc `DOCTOR`.
*   **Phản hồi thành công (200 OK):** Danh sách toàn bộ bệnh nhân trong hệ thống.

#### 3.2. Xem thông tin chi tiết bệnh nhân theo ID (`GET /api/patients/{id}`)
*   **Phân quyền:** Quyền truy cập dành cho `ADMIN`, `DOCTOR` hoặc chính bệnh nhân sở hữu tài khoản (`PATIENT`).
*   **Tham số đường dẫn:** `id` bệnh nhân cần xem.

#### 3.3. Tìm kiếm bệnh nhân theo tên (`GET /api/patients/search`)
*   **Phân quyền:** Chỉ dành cho `ADMIN` hoặc `DOCTOR`.
*   **Tham số truy vấn (Query Parameter):**
    *   `keyword` *(Chuỗi, Bắt buộc)*: Từ khóa tìm kiếm theo họ tên bệnh nhân (Ví dụ: `Vy`, `Cường`).

#### 3.4. Tạo mới hồ sơ bệnh nhân (`POST /api/patients`)
*   **Phân quyền:** Chỉ dành cho `ADMIN`.
*   **Yêu cầu Request Body (JSON):**
    ```json
    {
      "code": "PAT004",
      "fullName": "Hoàng Thị Cúc",
      "dateOfBirth": "1992-05-15",
      "gender": "FEMALE",
      "phone": "0981122334",
      "address": "456 Kim Mã, Ba Đình, Hà Nội"
    }
    ```
    *   `code` *(Chuỗi, Bắt buộc)*: Mã bệnh nhân (Duy nhất).
    *   `fullName` *(Chuỗi, Bắt buộc)*: Họ tên đầy đủ.
    *   `dateOfBirth` *(Chuỗi định dạng yyyy-MM-dd, Tùy chọn)*: Ngày sinh bệnh nhân.
    *   `gender` *(MALE / FEMALE / OTHER, Tùy chọn)*: Giới tính.
    *   `phone`, `address` *(Tùy chọn)*: Thông tin liên hệ.

#### 3.5. Cập nhật hồ sơ bệnh nhân (`PUT /api/patients/{id}`)
*   **Phân quyền:** Dành cho vai trò `ADMIN` hoặc chính người dùng `PATIENT`.
*   **Tham số đường dẫn:** `id` của bệnh nhân cần sửa đổi.
*   **Yêu cầu Request Body (JSON):** Chứa các thông tin cập nhật (Họ tên, ngày sinh, giới tính, số điện thoại, địa chỉ).

#### 3.6. Xóa thông tin bệnh nhân (`DELETE /api/patients/{id}`)
*   **Phân quyền:** Chỉ dành cho `ADMIN`.
*   **Tham số đường dẫn:** `id` bệnh nhân cần xóa.

---

### 4. Nhóm API Quản lý Lịch hẹn khám (Tag: `5. Appointment Management`)

#### 4.1. Danh sách toàn bộ lịch hẹn khám (`GET /api/appointments`)
*   **Phân quyền:** Chỉ dành cho `ADMIN`.
*   **Phản hồi thành công (200 OK):** Trả về toàn bộ danh sách lịch hẹn đã được đặt trên hệ thống.

#### 4.2. Xem chi tiết lịch hẹn theo ID (`GET /api/appointments/{id}`)
*   **Phân quyền:** Yêu cầu người dùng đăng nhập có vai trò `ADMIN`, `DOCTOR` hoặc chính bệnh nhân sở hữu lịch hẹn đó.

#### 4.3. Lấy danh sách lịch hẹn theo bệnh nhân (`GET /api/appointments/patient/{patientId}`)
*   **Phân quyền:** `ADMIN`, `DOCTOR` hoặc chính bệnh nhân (`PATIENT`) sở hữu lịch hẹn.
*   **Tham số đường dẫn:** `patientId` (ID của bệnh nhân cần tra cứu).

#### 4.4. Lấy danh sách lịch hẹn theo bác sĩ (`GET /api/appointments/doctor/{doctorId}`)
*   **Phân quyền:** Chỉ dành cho `ADMIN` hoặc `DOCTOR`.
*   **Tham số đường dẫn:** `doctorId` (ID bác sĩ cần xem danh sách lịch khám phụ trách).

#### 4.5. Lấy danh sách lịch hẹn trong một ngày (`GET /api/appointments/date`)
*   **Phân quyền:** Chỉ dành cho `ADMIN` hoặc `DOCTOR`.
*   **Tham số truy vấn (Query Parameter):**
    *   `date` *(Định dạng yyyy-MM-dd, Bắt buộc)*: Ngày cần lọc danh sách lịch hẹn (Ví dụ: `2026-06-18`).

#### 4.6. Đặt lịch hẹn khám bệnh mới (`POST /api/appointments`)
*   **Phân quyền:** Cho phép `ADMIN` đặt hộ hoặc người dùng có vai trò `PATIENT` tự đặt lịch khám.
*   **Yêu cầu Request Body (JSON):**
    ```json
    {
      "patientId": 1,
      "doctorId": 2,
      "appointmentDate": "2026-07-15",
      "appointmentTime": "14:30",
      "reason": "Khám tim mạch định kỳ, dạo gần đây hay bị khó thở nhẹ"
    }
    ```
    *   `patientId` *(Số nguyên, Bắt buộc)*: ID của bệnh nhân khám.
    *   `doctorId` *(Số nguyên, Bắt buộc)*: ID của bác sĩ đảm nhận khám.
    *   `appointmentDate` *(Chuỗi yyyy-MM-dd, Bắt buộc)*: Ngày khám mong muốn.
    *   `appointmentTime` *(Chuỗi HH:mm, Bắt buộc)*: Giờ khám mong muốn.
    *   `reason` *(Chuỗi, Tùy chọn)*: Triệu chứng hoặc lý do đặt khám.
*   **Logic ràng buộc kiểm thử:**
    *   Hệ thống kiểm tra xem bác sĩ được đặt lịch đã có lịch khám ở trạng thái `SCHEDULED` trùng ngày và trùng giờ khám hay chưa. Nếu bị trùng, hệ thống trả về lỗi **400 Bad Request** kèm thông báo lỗi cụ thể.

#### 4.7. Hủy lịch hẹn khám bệnh (`PUT /api/appointments/{id}/cancel`)
*   **Phân quyền:** Cho phép `ADMIN` hoặc bệnh nhân (`PATIENT`) sở hữu lịch hẹn này.
*   **Tham số đường dẫn:** `id` của lịch hẹn cần hủy.
*   **Ràng buộc kiểm thử:** Chỉ cho phép hủy lịch khám nếu trạng thái hiện tại của nó là `SCHEDULED`. Nếu lịch đã hoàn thành (`COMPLETED`) hoặc đã hủy trước đó, hệ thống sẽ trả về lỗi **400 Bad Request**. Trạng thái sau khi hủy thành công sẽ chuyển thành `CANCELLED`.

#### 4.8. Hoàn thành ca khám bệnh (`PUT /api/appointments/{id}/complete`)
*   **Phân quyền:** Chỉ dành cho `ADMIN` hoặc bác sĩ đảm nhận ca khám (`DOCTOR`).
*   **Tham số đường dẫn:** `id` của lịch hẹn cần kết thúc.
*   **Ràng buộc kiểm thử:** Chỉ cho phép hoàn thành nếu lịch hẹn đang ở trạng thái `SCHEDULED`. Trạng thái sau khi hoàn thành thành công sẽ chuyển thành `COMPLETED`.

---

### 5. Nhóm API Báo cáo & Thống kê (Tag: `6. Reports & Statistics`)

#### 5.1. Xem thống kê tổng quan hệ thống (`GET /api/statistics/overview`)
*   **Phân quyền:** Chỉ dành cho vai trò `ADMIN`.
*   **Phản hồi thành công (200 OK):**
    ```json
    {
      "success": true,
      "message": "Thành công",
      "data": {
        "totalPatients": 3,
        "totalDoctors": 5,
        "totalDepartments": 5,
        "totalAppointments": 3,
        "scheduledAppointments": 1,
        "completedAppointments": 1,
        "cancelledAppointments": 1,
        "doctorsByDepartment": {
          "Nội tổng quát": 1,
          "Tim mạch": 1,
          "Da liễu": 1,
          "Tai mũi họng": 1,
          "Mắt": 1
        }
      }
    }
    ```

#### 5.2. Đếm số lượng lịch hẹn khám theo ngày cụ thể (`GET /api/statistics/date`)
*   **Phân quyền:** Chỉ dành cho vai trò `ADMIN`.
*   **Tham số truy vấn (Query Parameter):**
    *   `date` *(Định dạng yyyy-MM-dd, Bắt buộc)*: Ngày cần thống kê lịch hẹn (Ví dụ: `2026-06-18`).

#### 5.3. Lấy danh sách lịch hẹn khám của một bác sĩ cụ thể (`GET /api/statistics/doctor/{doctorId}`)
*   **Phân quyền:** Dành cho vai trò `ADMIN` hoặc `DOCTOR`.
*   **Tham số đường dẫn:** `doctorId` (ID bác sĩ cần xem thống kê danh sách lịch khám phụ trách).

---

## V. CÁC LƯU Ý QUAN TRỌNG KHI KIỂM THỬ (CHÚ Ý DÀNH CHO QA/TESTER)

Trong quá trình phân tích mã nguồn hệ thống, có một số điểm lưu ý đặc biệt mà đội ngũ kiểm thử cần nắm vững:

1.  **Dịch vụ Bệnh án (Medical Record) và Khoa khám bệnh (Department) chưa được cấu hình API trực tiếp:**
    *   Trong mã nguồn, các tệp lớp xử lý nghiệp vụ `MedicalRecordService` và `DepartmentService` đã được xây dựng đầy đủ.
    *   Tuy nhiên, dự án hiện **không khai báo** các lớp REST Controller tương ứng cho 2 nghiệp vụ này (`MedicalRecordController` và `DepartmentController` không tồn tại).
    *   Do đó, trên giao diện Swagger UI **sẽ không hiển thị** các API để tương tác trực tiếp với Khoa khám bệnh (Thêm/Sửa/Xóa khoa) hay Hồ sơ bệnh án (Tạo bệnh án sau khi khám xong).
    *   Hiện tại, dữ liệu về Khoa khám bệnh và Hồ sơ bệnh án chỉ được nạp sẵn thông qua tiến trình khởi tạo dữ liệu mẫu lúc chạy ứng dụng (`DataInitializer.java`). Tester chỉ có thể kiểm thử gián tiếp thông qua các trường thông tin đi kèm trong kết quả phản hồi của API Bác sĩ, Lịch hẹn và Thống kê.

2.  **Thiếu ràng buộc đối chiếu ID người dùng (Security Vulnerability/Logic Flaw):**
    *   Đối với API cập nhật hoặc xem chi tiết thông tin bệnh nhân (`GET/PUT /api/patients/{id}`), cấu hình an ninh Spring Security cho phép vai trò `PATIENT` thực hiện.
    *   Tuy nhiên, ở tầng xử lý nghiệp vụ (`PatientService`), hệ thống **chưa kiểm tra đối chứng** xem token của bệnh nhân đang đăng nhập có trùng với `id` bệnh nhân gửi lên trong yêu cầu hay không.
    *   Điều này có nghĩa là bất kỳ tài khoản bệnh nhân nào sau khi đăng nhập và có Token hợp lệ đều có thể tùy ý thay đổi `id` trên đường dẫn để đọc hoặc sửa đổi thông tin của bất kỳ bệnh nhân nào khác trong cơ sở dữ liệu. Cần ghi nhận đây là một lỗi nghiệp vụ/bảo mật khi báo cáo kiểm thử.

3.  **Không thể gán hay cập nhật khoa khám bệnh cho bác sĩ qua API:**
    *   Trong `DoctorRequest.java` không chứa trường dữ liệu khoa (`departmentId` hoặc `departmentCode`).
    *   Vì vậy, khi tạo mới một bác sĩ qua API `POST /api/doctors` hoặc cập nhật qua API `PUT /api/doctors/{id}`, bác sĩ đó sẽ không thuộc về bất kỳ khoa nào (giá trị `department` trong cơ sở dữ liệu sẽ bằng `null`). Việc thiết lập khoa khám bệnh cho bác sĩ hiện tại chỉ có thể thực hiện được thông qua dữ liệu khởi tạo mặc định bằng mã cứng trong tệp `DataInitializer.java`.

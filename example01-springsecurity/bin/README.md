# Ví dụ 1 — Login với Spring Boot 4.1.1 + Spring Security 7 + MapStruct + Thymeleaf
### (dùng Lombok, MySQL, không dùng thymeleaf-layout-dialect, không có mail/Cloudinary)

Dự án dựa trên **Ví dụ 1** trong tài liệu đính kèm: bảng `User`, `Role`, chức năng **Login**
(đăng nhập bằng **email**), thông tin user hiển thị ở `header.html`.

## 1. Khác biệt so với tài liệu gốc

| Mục | Tài liệu gốc | Project này |
|---|---|---|
| ORM mapping | Lombok (`@Data`, `@AllArgsConstructor`...) | **Giữ nguyên Lombok** theo yêu cầu |
| Database | H2 + SQL Server | **MySQL** (`mysql-connector-j`) |
| Email / Cloudinary | Có khai báo dependency (không dùng tới) | **Bỏ hẳn** — project này không cần gửi mail hay upload ảnh |
| Layout | Đúng như tài liệu: **không dùng `thymeleaf-layout-dialect`**, chỉ `th:replace` fragment `head(title)` | Giữ nguyên |
| Quan hệ Role ↔ User | `@Data` thường, dễ StackOverflow do đệ quy `toString/equals` 2 chiều | Thêm `@ToString.Exclude` / `@EqualsAndHashCode.Exclude` trên 2 trường quan hệ để tránh lỗi — không đổi kiến trúc |

## 2. Yêu cầu

- JDK 21+, Maven 3.9+, MySQL 8.x

## 3. Cấu hình

```bash
cp .env.example .env
```

Sửa lại `.env`:

```properties
DB_URL=jdbc:mysql://localhost:3306/login_demo?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Ho_Chi_Minh&createDatabaseIfNotExist=true
DB_USERNAME=root
DB_PASSWORD=your_password
```

## 4. Chạy

```bash
mvn spring-boot:run
```

Mặc định chạy ở `http://localhost:8080`.

## 5. Tài khoản mẫu (seed tự động — `DataInitializer`)

| Email | Password | Role |
|---|---|---|
| `admin@shop.vn` (đổi được qua `ADMIN_EMAIL` trong `.env`) | `123456` (đổi qua `ADMIN_PASSWORD`) | ADMIN |

> Lưu ý: `Role.name` lưu **không có tiền tố `ROLE_`** (vd: `"USER"`, `"ADMIN"`) vì
> `CustomUserDetailsService` dùng `.roles(u.getRole().getName())` — Spring Security tự
> động thêm tiền tố `ROLE_` khi so khớp `hasRole(...)`.

## 6. Luồng hoạt động

- `GET /login` → hiển thị form đăng nhập (email + password).
- `POST /login` → do **Spring Security tự động xử lý** (không cần viết Controller) —
  xác thực qua `CustomUserDetailsService.loadUserByUsername(email)`.
- Đăng nhập thành công → chuyển tới `/` (yêu cầu đã đăng nhập), `header.html` hiển thị
  email người dùng (`sec:authentication="name"`) và nút Đăng xuất.
- `POST /logout` → xoá session, quay lại `/login?logout=true`.

## 7. Cấu trúc thư mục

```
src/main/java/vn/iotstar/
 ├─ Springboot18Application.java
 ├─ config/          EncodingConfig, SecurityConfig, DataInitializer
 ├─ controller/       AuthController, HomeController
 ├─ dto/              UserDTO, LoginDTO
 ├─ entity/           Role, User        (Lombok @Data)
 ├─ mapper/           UserMapper (MapStruct)
 ├─ repository/       RoleRepository, UserRepository
 ├─ security/         CustomUserDetailsService
 └─ service/          UserService (+ impl/UserServiceImpl)

src/main/resources/
 ├─ application.properties
 ├─ static/css/app.css
 └─ templates/
     ├─ layouts/layout.html     # fragment head(title) — KHÔNG dùng layout-dialect
     ├─ fragments/header.html   # dùng thymeleaf-extras-springsecurity6 (sec:)
     ├─ auth/login.html
     └─ home.html

.env.example
```

## 8. Ghi chú

- `UserService`/`UserMapper`/`UserRepository.findByEmailContainingIgnoreCaseOrFullNameContainingIgnoreCase(...)`
  được giữ lại đúng như code mẫu trong tài liệu dù chưa có Controller nào gọi tới
  (tài liệu chỉ yêu cầu chức năng Login) — sẵn sàng để mở rộng thêm CRUD User sau này.
- Nếu muốn bổ sung Register/OTP/Forgot-password/CRUD User-Product theo *Ví dụ 3* của
  tài liệu, nhắn mình tiếp tục — đó là bản đầy đủ hơn nhiều (đã có sẵn ở lượt trước,
  file `shop-springboot-4-1-1-mysql-no-lombok.zip`).

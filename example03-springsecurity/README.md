# Shop — Spring Boot 4.1.1 + Security 7 + MapStruct + Thymeleaf + Cloudinary + MySQL
###

Dự án đầy đủ dựa trên tài liệu **HƯỚNG DẪN SPRING BOOT + SECURITY** (55 trang) đính kèm:
Register + xác nhận OTP qua email, Login (session), Forgot password (OTP qua email),
CRUD User + Product (tìm kiếm, phân trang), upload ảnh Cloudinary, đếm số User/Product.

Hai thay đổi so với tài liệu gốc theo yêu cầu:

1. **Không dùng Lombok** — toàn bộ entity/DTO/service/controller được viết tay
   constructor, getter/setter, builder (không còn `@Data/@Getter/@Setter/@Builder/
   @RequiredArgsConstructor`). `pom.xml` không có dependency `lombok`.
2. **Dùng MySQL thay vì SQL Server** — thay `mssql-jdbc` bằng `mysql-connector-j`,
   `spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver`.

## 1. Yêu cầu

- JDK 21+
- Maven 3.9+
- MySQL 8.x
- (Tuỳ chọn) Tài khoản Gmail có App Password để gửi OTP qua SMTP
- (Tuỳ chọn) Tài khoản Cloudinary để upload ảnh sản phẩm

## 2. Cấu hình

Copy `.env.example` thành `.env` ở thư mục gốc project rồi điền thông tin thật:

```bash
cp .env.example .env
```

```properties
DB_URL=jdbc:mysql://localhost:3306/shop?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Ho_Chi_Minh&createDatabaseIfNotExist=true
DB_USERNAME=root
DB_PASSWORD=your_password
```

> `allowPublicKeyRetrieval=true` tránh lỗi **"Public Key Retrieval is not allowed"**
> khi MySQL 8 dùng plugin xác thực `caching_sha2_password`.
> `createDatabaseIfNotExist=true` giúp không cần tạo database `shop` thủ công.
> `DDL_AUTO=update` sẽ tự sinh bảng `roles`, `users`, `products`, `otp_tokens`.

Nếu chưa có Cloudinary/SMTP thật, vẫn có thể chạy app để xem CRUD User/Product và
đăng nhập bằng tài khoản mẫu — chỉ chức năng **gửi OTP qua email** và **upload ảnh**
sẽ báo lỗi cho tới khi điền thông tin thật.

## 3. Chạy ứng dụng

```bash
mvn spring-boot:run
```

Ứng dụng chạy ở `http://localhost:8080` (hoặc theo `SERVER_PORT` trong `.env`).

## 4. Tài khoản mẫu (seed tự động lúc khởi động)

| Username | Password | Role       | Trạng thái |
|----------|----------|------------|------------|
| admin    | 123456   | ROLE_ADMIN | enabled    |
| user01   | 123456   | ROLE_USER  | enabled    |

Đăng nhập bằng `admin/123456` để vào `/users` quản lý User; `/products` dùng chung
cho cả hai role đã đăng nhập.

> Tài liệu gốc chỉ tạo Role qua SQL và không seed sẵn tài khoản nào (đăng ký qua
> `/register` chỉ tạo `ROLE_USER`) — ta cần seed 1 tài khoản `admin` để có
> đường vào trang quản trị `/users` ngay từ lần chạy đầu, tránh không ai có quyền ADMIN để tạo ADMIN đầu tiên.

## 5. Luồng chức năng chính

- `/register` → nhập thông tin → OTP gửi qua email → `/verify-otp` → kích hoạt tài khoản.
- `/login` → Spring Security session-based login (field mặc định `username`/`password`).
- `/forgot-password` → gửi OTP → `/reset-password` (nhập email + OTP + mật khẩu mới).
- `/products` → CRUD sản phẩm của chính người dùng đang đăng nhập, ảnh upload lên Cloudinary.
- `/users` (chỉ ROLE_ADMIN) → CRUD User, tìm kiếm, phân trang, đếm số Product mỗi User.
- `/` → Dashboard: tổng số User, tổng số Product.

## 6. Cấu trúc thư mục

```
src/main/java/vn/iotstar/
 ├─ ShopSpringboot411Application.java   # main + seed Role/User mẫu
 ├─ entity/       User, Product, Role, OtpToken
 ├─ dto/          UserDTO, ProductDTO, RegisterDTO, LoginDTO,
 │                ResetPasswordDTO, VerifyOtpDTO, ForgotPasswordDTO
 ├─ mapper/       UserMapper, ProductMapper (MapStruct)
 ├─ repository/   UserRepository, ProductRepository, RoleRepository, OtpTokenRepository
 ├─ service/      UserService, ProductService, OtpService, EmailService,
 │                CloudinaryService, AuthService (+ package impl/)
 ├─ config/       CloudinaryConfig, SecurityConfig, EncodingConfig
 ├─ security/     CustomUserDetails, CustomUserDetailsService
 └─ controller/   UserController, ProductController, HomeController,
                  ErrorController, AuthController

src/main/resources/
 ├─ application.properties      # đọc biến từ .env
 ├─ static/css/app.css
 └─ templates/
     ├─ layouts/layout.html     # fragment head(title) + page(content)
     ├─ fragments/{header,footer}.html
     ├─ home.html, error.html
     ├─ auth/{register,login,reset-password,forgot-password,verify-otp}.html
     ├─ users/{list,form}.html
     └─ products/{list,form}.html

.env.example   # copy thành .env và điền thông tin thật
```

## 7. Những chỗ khác biệt kỹ thuật đáng chú ý so với tài liệu gốc

- Entity/DTO dùng **builder pattern viết tay** (inner class `Builder`) thay cho
  `@Builder` của Lombok — cú pháp gọi (`User.builder()...build()`) giữ nguyên như
  code mẫu, chỉ phần triển khai bên trong là thủ công.
- `@Column(columnDefinition = "nvarchar(...)")` (đặc thù SQL Server) trong tài liệu gốc
  đã được bỏ, thay bằng `length = ...` chuẩn — MySQL dùng `VARCHAR`/`NVARCHAR` không
  giống cú pháp SQL Server.
- `ErrorController.error()` trong tài liệu gốc thiếu `@GetMapping` (không thể truy cập
  được) — đã bổ sung `@GetMapping` để endpoint `/error` hoạt động.

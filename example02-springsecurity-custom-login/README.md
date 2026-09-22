# Custom Login — Spring Boot 4 + Spring Security 7 + MapStruct + Thymeleaf + MySQL

## 1. Yêu cầu

- JDK 21+
- Maven 3.9+
- MySQL 8.x đang chạy

## 2. Cấu hình MySQL

Tạo user/pass phù hợp rồi sửa lại `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/springboot1_9?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
```

Nhờ `createDatabaseIfNotExist=true`, không cần tạo database `springboot1_9` trước — MySQL sẽ tự tạo khi kết nối
(chỉ cần user có quyền CREATE DATABASE). `spring.jpa.hibernate.ddl-auto=update` sẽ tự sinh bảng `roles` và `users`.

## 3. Chạy ứng dụng

```bash
mvn spring-boot:run
```

Ứng dụng chạy ở `http://localhost:8081`.

## 4. Tài khoản mẫu (được seed tự động lúc khởi động)

| Username  | Password | Role       |
|-----------|----------|------------|
| user01    | 123456   | ROLE_USER  |
| admin01   | 123456   | ROLE_ADMIN |

Có thể đăng nhập bằng **username hoặc email** (`user01` hoặc `user01@gmail.com`).

## 5. Những điểm khác so với tài liệu gốc

- **Không dùng Lombok**: `Role`, `User`, `UserDTO`, `CustomUserDetails`, `CustomUserDetailsService`, `SecurityConfig`
  được viết lại bằng constructor/getter/setter/builder thủ công, không còn `@Getter/@Setter/@Builder/
  @RequiredArgsConstructor`. `pom.xml` cũng bỏ dependency `lombok` và `lombok-mapstruct-binding`.
- **Dùng MySQL thay vì H2/SQL Server**: thay `h2` + `mssql-jdbc` bằng `mysql-connector-j`,
  bỏ `spring-boot-h2console`, cấu hình `application.properties` trỏ tới MySQL.
- Thêm sẵn 1 tài khoản `ROLE_ADMIN` (`admin01`) bên cạnh `ROLE_USER` (`user01`) để tiện test phân quyền
  `/admin/**`.

## 6. Cấu trúc thư mục

```
src/main/java/vn/iotstar/
 ├─ Springboot19Application.java   # main + seed dữ liệu mẫu
 ├─ entity/        Role, User
 ├─ repository/    RoleRepository, UserRepository
 ├─ dto/           UserDTO, LoginDTO
 ├─ mapper/        UserMapper (MapStruct)
 ├─ security/      CustomUserDetails, CustomUserDetailsService
 ├─ config/        SecurityConfig
 └─ controller/    AuthController, HomeController

src/main/resources/
 ├─ application.properties
 ├─ static/images/avatar-default.png
 └─ templates/
     ├─ auth/login.html
     ├─ fragments/header.html   # hiển thị fullname + avatar
     ├─ layouts/layout.html
     └─ home.html
```

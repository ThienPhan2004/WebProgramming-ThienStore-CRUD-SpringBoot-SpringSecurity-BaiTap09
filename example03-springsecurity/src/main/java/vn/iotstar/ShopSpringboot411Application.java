package vn.iotstar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@SpringBootApplication
public class ShopSpringboot411Application {

    public static void main(String[] args) {
        SpringApplication.run(ShopSpringboot411Application.class, args);
    }

    @Bean
    CommandLineRunner init(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));

            // Tài khoản admin mặc định để đăng nhập lần đầu và quản lý Users/Products.
            // Đăng ký qua /register chỉ tạo ROLE_USER, nên cần 1 admin có sẵn.
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@shop.vn")
                        .password(passwordEncoder.encode("123456"))
                        .fullName("Quản Trị Viên")
                        .enabled(true)
                        .role(adminRole)
                        .build();
                userRepository.save(admin);
            }

            if (userRepository.findByUsername("user01").isEmpty()) {
                User user = User.builder()
                        .username("user01")
                        .email("user01@shop.vn")
                        .password(passwordEncoder.encode("123456"))
                        .fullName("Nguyễn Hữu Trung")
                        .enabled(true)
                        .role(userRole)
                        .build();
                userRepository.save(user);
            }
        };
    }
}

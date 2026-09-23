package vn.iotstar.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lưu tên KHÔNG có tiền tố "ROLE_" (vd: "USER", "ADMIN")
    // vì CustomUserDetailsService dùng .roles(...) - Spring Security tự thêm "ROLE_".
    @Column(nullable = false, unique = true, length = 30)
    private String name;

    // Loại khỏi toString/equals để tránh đệ quy vô hạn Role <-> User.
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();

    public Role(String name) {
        this.name = name;
    }
}

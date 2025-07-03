package com.university.parking;

import com.university.parking.model.Role;
import com.university.parking.model.User;
import com.university.parking.repository.RoleRepository;
import com.university.parking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

@SpringBootApplication
public class ParkingManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingManagementApplication.class, args);
	}

	@Bean
	public CommandLineRunner setupAdmin(UserRepository userRepo,
										RoleRepository roleRepo,
										BCryptPasswordEncoder encoder) {
		return args -> {
			String adminEmail = "kartik@gmail.com";

			// 🚨 Check if admin exists
			if (!userRepo.existsByEmail(adminEmail)) {

				// 🔐 Ensure role is loaded as managed entity
				Role adminRole = roleRepo.findByName("ROLE_ADMIN");
				if (adminRole == null) {
					adminRole = roleRepo.save(new Role(null, "ROLE_ADMIN"));
					System.out.println("🆕 ROLE_ADMIN created in DB.");
				}

				// 👤 Create admin user
				User admin = new User();
				admin.setFirstName("Kartik");
				admin.setLastName("Garg");
				admin.setEmail(adminEmail);
				admin.setUniversityId("21ADM123");
				admin.setPassword(encoder.encode("kartik123"));
				admin.setEnabled(true); // ✅ Admin should be enabled by default
				admin.setRoles(Collections.singletonList(adminRole));

				userRepo.save(admin);
				System.out.println("✅ Admin user created: " + adminEmail);
			}
		};
	}
}

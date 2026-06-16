package org.example.has.service;

import lombok.RequiredArgsConstructor;
import org.example.has.config.JwtTokenProvider;
import org.example.has.domain.entity.Patient;
import org.example.has.domain.entity.User;
import org.example.has.domain.enums.Role;
import org.example.has.dto.request.LoginRequest;
import org.example.has.dto.request.RegisterRequest;
import org.example.has.dto.response.AuthResponse;
import org.example.has.exception.DuplicateResourceException;
import org.example.has.repository.PatientRepository;
import org.example.has.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service xử lý logic xác thực và đăng ký.
 *
 * <p>
 * Cung cấp các chức năng:
 * </p>
 * <ul>
 * <li>Đăng nhập — xác thực username/password, trả JWT token</li>
 * <li>Đăng ký — tạo tài khoản Patient mới</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class AuthService {

        private final AuthenticationManager authenticationManager;
        private final JwtTokenProvider jwtTokenProvider;
        private final UserRepository userRepository;
        private final PatientRepository patientRepository;
        private final PasswordEncoder passwordEncoder;

        /**
         * Đăng nhập hệ thống.
         *
         * <p>
         * Quy trình:
         * </p>
         * <ol>
         * <li>Xác thực username/password qua AuthenticationManager</li>
         * <li>Tạo JWT token</li>
         * <li>Trả về token cùng thông tin user</li>
         * </ol>
         *
         * @param request chứa username và password
         * @return AuthResponse chứa JWT token và thông tin user
         * @throws org.springframework.security.authentication.BadCredentialsException nếu
         *                                                                             sai
         *                                                                             thông
         *                                                                             tin
         */
        public AuthResponse login(LoginRequest request) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsername(), request.getPassword()));

                String token = jwtTokenProvider.generateToken(authentication);

                User user = userRepository.findByUsername(request.getUsername())
                                .orElseThrow();

                return AuthResponse.builder()
                                .token(token)
                                .tokenType("Bearer")
                                .username(user.getUsername())
                                .fullName(user.getFullName())
                                .role(user.getRole().name())
                                .build();
        }

        /**
         * Đăng ký tài khoản bệnh nhân mới.
         *
         * <p>
         * Quy trình:
         * </p>
         * <ol>
         * <li>Kiểm tra username chưa tồn tại</li>
         * <li>Tạo User với role PATIENT</li>
         * <li>Tạo Patient entity liên kết</li>
         * <li>Tự động tạo mã bệnh nhân (PAT + ID)</li>
         * <li>Trả JWT token để đăng nhập ngay</li>
         * </ol>
         *
         * @param request chứa thông tin đăng ký
         * @return AuthResponse chứa JWT token
         * @throws DuplicateResourceException nếu username đã tồn tại
         */
        @Transactional
        public AuthResponse register(RegisterRequest request) {
                if (userRepository.existsByUsername(request.getUsername())) {
                        throw new DuplicateResourceException("User", "username", request.getUsername());
                }

                User user = User.builder()
                                .username(request.getUsername())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .fullName(request.getFullName())
                                .role(Role.PATIENT)
                                .enabled(true)
                                .build();
                user = userRepository.save(user);

                Patient patient = Patient.builder()
                                .code("PAT" + String.format("%03d", user.getId()))
                                .fullName(request.getFullName())
                                .phone(request.getPhone())
                                .address(request.getAddress())
                                .user(user)
                                .build();
                patientRepository.save(patient);

                String token = jwtTokenProvider.generateTokenFromUsername(user.getUsername());

                return AuthResponse.builder()
                                .token(token)
                                .tokenType("Bearer")
                                .username(user.getUsername())
                                .fullName(user.getFullName())
                                .role(user.getRole().name())
                                .build();
        }
}

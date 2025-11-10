package com.ev.auth.service;

import com.ev.auth.dto.AuthRequest;
import com.ev.auth.dto.AuthResponse;
import com.ev.auth.dto.RegisterRequest;
import com.ev.auth.entity.Account;
import com.ev.auth.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Đăng ký tài khoản mới.
     * Trả về Account ID và Token.
     */
    public AuthResponse register(RegisterRequest request) {
        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setRole(request.getRole());
        account.setStatus("ACTIVE"); // Mặc định là Active

        Account savedAccount = accountRepository.save(account);

        // Tạo token
        String jwtToken = jwtService.generateToken(savedAccount);

        return new AuthResponse(savedAccount.getAccountId(), savedAccount.getUsername(), jwtToken);
    }

    /**
     * Đăng nhập.
     * Trả về Account ID và Token.
     */
    public AuthResponse login(AuthRequest request) {
        // 1. Xác thực (Spring Security lo)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 2. Nếu xác thực thành công, tìm lại user
        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow();

        // 3. Tạo token
        String jwtToken = jwtService.generateToken(account);

        return new AuthResponse(account.getAccountId(), account.getUsername(), jwtToken);
    }
}
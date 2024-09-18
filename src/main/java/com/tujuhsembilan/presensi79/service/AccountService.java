package com.tujuhsembilan.presensi79.service;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.ChangePasswordRequest;
import com.tujuhsembilan.presensi79.model.Account;
import com.tujuhsembilan.presensi79.model.Employee;
import com.tujuhsembilan.presensi79.repository.AccountRepository;
import com.tujuhsembilan.presensi79.repository.EmployeeRepository;
import com.tujuhsembilan.presensi79.util.MessageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountService {

    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MessageUtil messageUtil;

    @Transactional
    public MessageResponse changeEmployeePassword(Integer idEmployee, ChangePasswordRequest request) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(idEmployee);

            if (!optionalEmployee.isPresent()) {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Employee"),
                    HttpStatus.BAD_REQUEST.value(),
                    messageUtil.get("application.status.error")
                );
            }

            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return new MessageResponse(
                    messageUtil.get("application.error.newpassword.notmatch"),
                    HttpStatus.BAD_REQUEST.value(),
                    messageUtil.get("application.status.error")
                );
            }

            Employee employee = optionalEmployee.get();
            Account account = employee.getAccount();
            String username = employee.getAccount().getUsername();

            if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
                return new MessageResponse(
                    messageUtil.get("application.error.changepassword.incorrect"),
                    HttpStatus.BAD_REQUEST.value(),
                    messageUtil.get("application.status.error")
                );
            }

            account.setPassword(passwordEncoder.encode(request.getNewPassword()));
            account.setModifiedBy(username);
            account.setModifiedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))));
            accountRepository.save(account);

            return new MessageResponse(
                messageUtil.get("application.success.changepassword"),
                HttpStatus.OK.value(),
                messageUtil.get("application.status.ok")
            );
        } catch (Exception e) {
            return new MessageResponse(
                messageUtil.get("application.error.internal"),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messageUtil.get("application.status.error"),
                e.getMessage()
            );
        }
    }
}

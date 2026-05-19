package com.group.thr.hedi.Service.Interface;

public interface IEmailService {
    void sendPasswordResetEmail(String toEmail, String resetLink);
}
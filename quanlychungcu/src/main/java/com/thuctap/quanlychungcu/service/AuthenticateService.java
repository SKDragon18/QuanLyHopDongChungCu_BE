package com.thuctap.quanlychungcu.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.thuctap.quanlychungcu.dto.AuthencicationResponse;
import com.thuctap.quanlychungcu.dto.DangNhapDTO;
import com.thuctap.quanlychungcu.model.TaiKhoan;
import com.thuctap.quanlychungcu.repository.TaiKhoanRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.var;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticateService {
    @Autowired
    TaiKhoanService taiKhoanService;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String signerKey;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long validDuration;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long refreshableDuration;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    public String authenticate (DangNhapDTO dangNhapDTO, TaiKhoan taiKhoan){
        boolean authenticated = passwordEncoder.matches(dangNhapDTO.getMatKhau(), taiKhoan.getMatKhau());
        if(!authenticated)return null;
        String token = generateToken(taiKhoan);
        return token;
    }

    public String generateToken(TaiKhoan taiKhoan){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
        .subject(taiKhoan.getTenDangNhap())
        .issuer("quanlydichvuchungcu.com")
        .issueTime(new Date())
        .expirationTime(new Date(
            Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()
        ))
        .jwtID(UUID.randomUUID().toString())
        .claim("scope", taiKhoan.getQuyen().getTenQuyen())
        .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try{
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        }
        catch(JOSEException e){
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    public Boolean introspect(String token) throws JOSEException, ParseException{
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime =  signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        return verified && expiryTime.after(new Date());
    }
    // private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException{
    //     JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
    //     SignedJWT signedJWT = SignedJWT.parse(token);
    //     Date expiryTime=(isRefresh)?
    //     new Date(signedJWT.getJWTClaimsSet().getIssueTime()
    //     .toInstant().plus(refreshableDuration, ChronoUnit.SECONDS).toEpochMilli())
    //     : signedJWT.getJWTClaimsSet().getExpirationTime();

    //     var verified = signedJWT.verify(verifier);

    //     if(!(verified && expiryTime.after(new Date()))) return null;
    //     return verified;
    // }
}

package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.domain.Account;
import kr.co.lionkorea.domain.AccountRole;
import kr.co.lionkorea.domain.Member;
import kr.co.lionkorea.domain.Roles;
import kr.co.lionkorea.dto.MemberDetails;
import kr.co.lionkorea.dto.request.FindMembersRequest;
import kr.co.lionkorea.dto.request.GrantNewAccountRequest;
import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.response.*;
import kr.co.lionkorea.enums.Role;
import kr.co.lionkorea.exception.AccountException;
import kr.co.lionkorea.exception.MemberException;
import kr.co.lionkorea.repository.AccountRepository;
import kr.co.lionkorea.repository.MemberRepository;
import kr.co.lionkorea.repository.RolesRepository;
import kr.co.lionkorea.service.EmailService;
import kr.co.lionkorea.service.MemberService;
import kr.co.lionkorea.service.RedisService;
import kr.co.lionkorea.utils.Base62;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final RolesRepository rolesRepository;
    private final BCryptPasswordEncoder encoder;
    private final RedisService redisService;
    private final EmailService emailService;

    @Value("${server.host}")
    private String host;

    @Override
    @Transactional
    public void saveMemberAll(List<SaveMemberRequest> requests) {
        List<Member> collect = requests.stream().map(Member::dtoToEntity).collect(Collectors.toList());
        memberRepository.saveAll(collect);
    }

    @Override
    @Transactional
    public SaveMemberResponse saveMember(SaveMemberRequest request) {
        Member savedMember = memberRepository.save(Member.dtoToEntity(request));
        return new SaveMemberResponse(savedMember.getId(), "저장되었습니다.");
    }

    @Override
    @Transactional
    public SaveMemberResponse updateMember(Long memberId, SaveMemberRequest request) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(HttpStatus.NOT_FOUND, "존재하지 않은 회원입니다."));
        findMember.changeInfo(request);
        memberRepository.save(findMember);
        return new SaveMemberResponse(findMember.getId(), "수정되었습니다.");
    }

    @Override
    public PagedModel<FindMembersResponse> findMembers(FindMembersRequest request, Pageable pageable) {
        return memberRepository.findMembersPaging(request, pageable);
    }

    @Override
    public FindMemberDetailResponse findMemberById(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(HttpStatus.NOT_FOUND, "존재하지 않은 회원입니다."));
        return FindMemberDetailResponse.entityToDto(findMember);
    }

    @Override
    @Transactional
    public GrantNewAccountResponse grantNewAccount(GrantNewAccountRequest request) {

        boolean isExistsLoginId = isExistsLoginId(request.getLoginId());
        if (isExistsLoginId) {
            throw new AccountException("중복되는 아이디입니다.");
        }

        String randomPassword = getRandomPassword();
        if(request.getRole().equals("super_admin")){
            randomPassword = "superAdmin1234";
        }

        log.info("{}의 password : {}", request.getLoginId(), randomPassword);

        Long memberId = request.getMemberId();
        request.setPassword(encoder.encode(randomPassword));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(HttpStatus.NOT_FOUND, "존재하지 않은 회원입니다."));
        Role role = Role.valueOf(("role_" + request.getRole()).toUpperCase());
        Roles roles = rolesRepository.findByRoleName(role);

        Account savedAccount = accountRepository.save(Account.dtoToEntity(request, member, roles));

        // 최상위 관리자 계정이 아니면 비밀번호 변경 링크 보내기
        if(!request.getRole().equals("super_admin")){
            if (StringUtils.hasText(request.getTo())) {
                // shortUrl 생성 후 Redis에 저장
                String value = String.valueOf(memberId);
                String key = createShortUrl();
                redisService.save(key, value, 30L);

                String subject = "비밀변호 변경 링크입니다.";
                String shortUrl = host + "/password/" + key;

                emailService.sendEmail(request.getTo(), subject, shortUrl);
            }
        }
        
        return new GrantNewAccountResponse(savedAccount.getLoginId(), randomPassword);
    }

    @Override
    public boolean isExistsLoginId(String loginId) {
        return accountRepository.existsByLoginId(loginId);
    }

    @Override
    public MemberDetails findUserDetails(String loginId){
        Account account = accountRepository.findByLoginIdAndUseYnIsTrue(loginId).orElseThrow(() -> new MemberException(HttpStatus.NOT_FOUND, "존재하지 않은 회원입니다."));
        Member member = account.getMember();
        Set<Role> roles = account.getAccountRoles().stream().map(AccountRole::getRoles).map(Roles::getRoleName).collect(Collectors.toSet());

        return MemberDetails.builder()
                .loginId(account.getLoginId())
                .memberName(member.getMemberName())
                .memberId(member.getId())
                .roles(roles)
                .password(account.getPassword())
                .build();

    }

    @Override
    public List<FindMemberByAccountResponse> findMemberAccount(Long memberId) {
        List<FindMemberByAccountResponse> list = accountRepository.findByMemberIdWithAccount(memberId);
        list.forEach(dto -> {
            dto.setRoleKor(dto.getRole().getKoreanName());
            dto.setRoleEng(dto.getRole().getEnglishName());
        });
        return list;
    }

    @Override
    public DecodeShortUrlResponse decodeShortUrl(String shortUrl) {
        if(!redisService.hasKey(shortUrl)){
            throw new MemberException("존재하지 않거나, 만료된 url입니다.\n 관리자에게 문의해 주세요.");
        }
        String value = redisService.getValue(shortUrl);
        return new DecodeShortUrlResponse(Long.parseLong(value));
    }

    private String getRandomPassword(){
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%^&*()-_+=<>?";
        final int length = 8;

        String allCharacters = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;
        SecureRandom secureRandom = new SecureRandom();

        StringBuilder password = new StringBuilder(length);

        // At least one upper case letter
        password.append(upperCaseLetters.charAt(secureRandom.nextInt(upperCaseLetters.length())));

        // At least one lower case letter
        password.append(lowerCaseLetters.charAt(secureRandom.nextInt(lowerCaseLetters.length())));

        // At least one special character
        password.append(specialCharacters.charAt(secureRandom.nextInt(specialCharacters.length())));

        // Fill the rest of the password length with random characters
        for (int i = 3; i < length; i++) {
            password.append(allCharacters.charAt(secureRandom.nextInt(allCharacters.length())));
        }

        // Shuffle the characters to ensure randomness
        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int randomIndex = secureRandom.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }
        return new String(passwordArray);
    }

    private String createShortUrl(){
        UUID uuid = UUID.randomUUID();
        long leastSignificantBits = uuid.getLeastSignificantBits();
        String encode = Base62.encode(Math.abs(leastSignificantBits));
        if (encode.length() > 7) {
            return encode.substring(0, 7);
        } else {
            // 7글자보다 짧은 경우 앞에 'Z'을 추가하여 7글자로 맞춤
            return String.format("%7s", encode).replace(' ', 'Z');
        }
    }
}

package com.sysmatic2.finalbe.member.repository;

import com.sysmatic2.finalbe.member.dto.SimpleProfileDTO;
import com.sysmatic2.finalbe.member.entity.MemberEntity;
import com.sysmatic2.finalbe.util.RandomKeyGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    // memberEntity 생성하는 메소드
    private MemberEntity createMemberEntity(String nickname, String memberGradeCode) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberId(RandomKeyGenerator.createUUID());
        memberEntity.setMemberGradeCode(memberGradeCode);
        memberEntity.setMemberStatusCode("MEMBER_STATUS_ACTIVE");
        memberEntity.setEmail("test@test.com");
        memberEntity.setPassword("qwer1234!");
        memberEntity.setNickname(nickname);
        memberEntity.setPhoneNumber("01012345678");
        return memberEntity;
    };

    // memberEntity 생성하는 메소드
    private MemberEntity createMemberEntity(String nickname, String memberGradeCode, String introduction, String fileId) {
        MemberEntity memberEntity = createMemberEntity(nickname, memberGradeCode);
        memberEntity.setIntroduction(introduction);
        memberEntity.setFileId(fileId);
        return memberEntity;
    };

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    // memberId로 member 찾아 simpleProfileDTO 반환하는 기능 테스트

    // 1. memberId로 저장된 member가 없으면 반환된 DTO가 없다. (Optional.of(null)과 같다.)
    @Test
    @DisplayName("없는 memberId로 조회하면 null")
    public void findSimpleProfileTest_1() {
        // 존재하지 않는 memberId로 조회 시, 반환값이 없다.
        String notExistMemberId = "notExistMemberId";
        Optional<SimpleProfileDTO> simpleProfileByMemberId = memberRepository.findSimpleProfileByMemberId(notExistMemberId);
        assertTrue(simpleProfileByMemberId.isEmpty());
    }

    // 2. memberId로 저장된 member가 있으면 하나의 DTO가 반환된다.
    // 3. memberId로 저장된 member가 있으면 반환된 DTO 내 필드값이 저장한 값과 동일해야 한다.
    @Test
    @DisplayName("존재하는 memberId로 조회하면 하나의 DTO의 반환")
    public void findSimpleProfileTest_2() {
        String nickname = "nickname";
        String memberGradeCode = "MEMBER_GRADE_TRADER";
        MemberEntity member = createMemberEntity(nickname, memberGradeCode);

        memberRepository.save(member);

        // 조회하면 값 존재
        Optional<SimpleProfileDTO> simpleProfileByMemberId = memberRepository.findSimpleProfileByMemberId(member.getMemberId());
        assertTrue(simpleProfileByMemberId.isPresent());

        // 저장한 필드값과 반환된 필드값 동일
        SimpleProfileDTO simpleProfileDTO = simpleProfileByMemberId.get();
        assertEquals(simpleProfileDTO.getNickname(), member.getNickname());
        assertEquals(simpleProfileDTO.getMemberType(), member.getMemberGradeCode().replace("MEMBER_ROLE_", ""));
        assertNull(simpleProfileDTO.getIntroduction());
        assertNull(simpleProfileDTO.getFileId());
    }

    @Test
    @DisplayName("존재하는 memberId로 조회하면 하나의 DTO의 반환")
    public void findSimpleProfileTest_3() {
        String nickname = "nickname";
        String memberGradeCode = "MEMBER_GRADE_TRADER";
        String introduction = "introduction";
        String fileId = "fileId";
        MemberEntity member = createMemberEntity(nickname, memberGradeCode, introduction, fileId);

        memberRepository.save(member);

        // 조회하면 값 존재
        Optional<SimpleProfileDTO> simpleProfileByMemberId = memberRepository.findSimpleProfileByMemberId(member.getMemberId());
        assertTrue(simpleProfileByMemberId.isPresent());

        // 저장한 필드값과 반환된 필드값 동일
        SimpleProfileDTO simpleProfileDTO = simpleProfileByMemberId.get();
        assertEquals(simpleProfileDTO.getNickname(), member.getNickname());
        assertEquals(simpleProfileDTO.getMemberType(), member.getMemberGradeCode().replace("MEMBER_ROLE_", ""));
        assertEquals(simpleProfileDTO.getIntroduction(), member.getIntroduction());
        assertEquals(simpleProfileDTO.getFileId(), member.getFileId());
    }
}
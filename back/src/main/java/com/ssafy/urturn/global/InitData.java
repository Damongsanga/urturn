package com.ssafy.urturn.global;

import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_MEMBER;
import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_PROBLEM;
import static com.ssafy.urturn.history.HistoryResult.*;
import static com.ssafy.urturn.member.Level.*;
import static com.ssafy.urturn.problem.Language.CPP;
import static com.ssafy.urturn.problem.Language.JAVA;
import static com.ssafy.urturn.problem.Language.PYTHON;

import com.ssafy.urturn.global.auth.Role;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import com.ssafy.urturn.history.HistoryResult;
import com.ssafy.urturn.history.entity.History;
import com.ssafy.urturn.history.repository.HistoryRepository;
import com.ssafy.urturn.member.Level;
import com.ssafy.urturn.member.entity.Member;
import com.ssafy.urturn.member.repository.MemberRepository;
import com.ssafy.urturn.problem.Language;
import com.ssafy.urturn.problem.entity.MemberProblem;
import com.ssafy.urturn.problem.entity.Problem;
import com.ssafy.urturn.problem.entity.Testcase;
import com.ssafy.urturn.problem.repository.MemberProblemRepository;
import com.ssafy.urturn.problem.repository.ProblemRepository;
import com.ssafy.urturn.problem.repository.TestcaseRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InitData {

    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final MemberProblemRepository memberProblemRepository;
    private final ProblemRepository problemRepository;
    private final TestcaseRepository testcaseRepository;

    private final PasswordEncoder passwordEncoder;
    @Value("spring.security.oauth2.client.registration.password-salt")
    private String salt;

    // test를 위해 LEVEL1 문제 2개는 주석처리하여 LEVEL1을 선택하면 테스트문제만 반환되도록 하였습니다.
    @PostConstruct
    public void initData(){

        Long testMemberId1 = createTestMember("test1", "test1");
        Long testMemberId2 = createTestMember("test2", "test2");

        createProblem("두배로 늘려요", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/0000%EB%B2%88+%ED%85%8C%EC%8A%A4%ED%8A%B8+%EB%AC%B8%EC%A0%9C.md", LEVEL1);

        createTestcase("두배로 늘려요", "2", "4", true);
        createTestcase("두배로 늘려요", "0", "0", false);
        createTestcase("두배로 늘려요", "-2", "-4", false);

        createProblem("안녕하세요 감사해요 잘있어요 다시 만나요", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/0000%EB%B2%88+%ED%85%8C%EC%8A%A4%ED%8A%B8+%EB%AC%B8%EC%A0%9C2.md", LEVEL1);

        createTestcase("안녕하세요 감사해요 잘있어요 다시 만나요", "SSAFY", "SSAFYHELLO", true);
        createTestcase("안녕하세요 감사해요 잘있어요 다시 만나요", "010", "010HELLO", false);
        createTestcase("안녕하세요 감사해요 잘있어요 다시 만나요", "재길", "재길HELLO", false);

//        createProblem("유기농 양상추", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1012%EB%B2%88+%EC%9C%A0%EA%B8%B0%EB%86%8D+%EC%96%91%EC%83%81%EC%B6%94.md", LEVEL1);
//
//        createTestcase("유기농 양상추", "2\n"
//            + "10 8 17\n"
//            + "0 0\n"
//            + "1 0\n"
//            + "1 1\n"
//            + "4 2\n"
//            + "4 3\n"
//            + "4 5\n"
//            + "2 4\n"
//            + "3 4\n"
//            + "7 4\n"
//            + "8 4\n"
//            + "9 4\n"
//            + "7 5\n"
//            + "8 5\n"
//            + "9 5\n"
//            + "7 6\n"
//            + "8 6\n"
//            + "9 6\n"
//            + "10 10 1\n"
//            + "5 5 ", "5\n"
//            + "1", true);
//
//        createTestcase("유기농 양상추", "1\n"
//            + "5 3 6\n"
//            + "0 2\n"
//            + "1 2\n"
//            + "2 2\n"
//            + "3 2\n"
//            + "4 2\n"
//            + "4 0", "2", true);
//
//
//        createProblem("술래잡기", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1697%EB%B2%88+%EC%88%A0%EB%9E%98%EC%9E%A1%EA%B8%B0.md", LEVEL1);
//
//        createTestcase("술래잡기", "5 17", "4", true);

        createProblem("빙하", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/2573%EB%B2%88+%EB%B9%99%ED%95%98.md", LEVEL2);

        createTestcase("빙하", "5 7\n"
                + "0 0 0 0 0 0 0\n"
                + "0 2 4 5 3 0 0\n"
                + "0 3 0 2 5 2 0\n"
                + "0 7 6 2 4 0 0\n"
                + "0 0 0 0 0 0 0", "2", true);

        createTestcase("빙하", "5 5 \n"
                + "0 0 0 0 0 \n"
                + "0 1 1 1 0 \n"
                + "0 1 0 1 0 \n"
                + "0 1 1 1 0 \n"
                + "0 0 0 0 0", "0", true);

        createTestcase("빙하", "4 4 \n"
                + "0 0 0 0 \n"
                + "0 3 1 0 \n"
                + "0 1 3 0 \n"
                + "0 0 0 0", "1", false);

        createProblem("암호화", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1759%EB%B2%88+%EC%95%94%ED%98%B8%ED%99%94.md", LEVEL2);

        createTestcase("암호화", "4 6\n"
            + "a t c i s w", "acis\n"
            + "acit\n"
            + "aciw\n"
            + "acst\n"
            + "acsw\n"
            + "actw\n"
            + "aist\n"
            + "aisw\n"
            + "aitw\n"
            + "astw\n"
            + "cist\n"
            + "cisw\n"
            + "citw\n"
            + "istw", true);

        createProblem("정육면체 던지기", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/14499%EB%B2%88+%EC%A0%95%EC%9C%A1%EB%A9%B4%EC%B2%B4+%EB%8D%98%EC%A7%80%EA%B8%B0.md", LEVEL2);

        createTestcase("정육면체 던지기", "4 2 0 0 8\n"
            + "0 2\n"
            + "3 4\n"
            + "5 6\n"
            + "7 8\n"
            + "4 4 4 1 3 3 3 2", "0\n"
            + "0\n"
            + "3\n"
            + "0\n"
            + "0\n"
            + "8\n"
            + "6\n"
            + "3", true);
        createTestcase("정육면체 던지기", "3 3 1 1 9\n"
            + "1 2 3\n"
            + "4 0 5\n"
            + "6 7 8\n"
            + "1 3 2 2 4 4 1 1 3", "0\n"
            + "0\n"
            + "0\n"
            + "3\n"
            + "0\n"
            + "1\n"
            + "0\n"
            + "6\n"
            + "0", true);
        createTestcase("정육면체 던지기", "2 2 0 0 16\n"
            + "0 2\n"
            + "3 4\n"
            + "4 4 4 4 1 1 1 1 3 3 3 3 2 2 2 2", "0\n"
            + "0\n"
            + "0\n"
            + "0", true);
        createTestcase("정육면체 던지기", "3 3 0 0 16\n"
            + "0 1 2\n"
            + "3 4 5\n"
            + "6 7 8\n"
            + "4 4 1 1 3 3 2 2 4 4 1 1 3 3 2 2", "0\n"
            + "0\n"
            + "0\n"
            + "6\n"
            + "0\n"
            + "8\n"
            + "0\n"
            + "2\n"
            + "0\n"
            + "8\n"
            + "0\n"
            + "2\n"
            + "0\n"
            + "8\n"
            + "0\n"
            + "2", true);

        createProblem("축제", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1238%EB%B2%88+%EC%B6%95%EC%A0%9C.md", LEVEL2);

        createTestcase("축제", "4 8 2\n"
            + "1 2 4\n"
            + "1 3 2\n"
            + "1 4 7\n"
            + "2 1 1\n"
            + "2 3 5\n"
            + "3 1 2\n"
            + "3 4 4\n"
            + "4 2 3", "10", true);
        createTestcase("축제", "10 24 2\n"
            + "9 5 9 \n"
            + "7 9 2 \n"
            + "8 1 7 \n"
            + "7 10 2 \n"
            + "6 1 6 \n"
            + "10 6 5 \n"
            + "5 1 6 \n"
            + "5 7 9 \n"
            + "3 8 9 \n"
            + "4 10 7 \n"
            + "8 10 6 \n"
            + "7 2 6 \n"
            + "4 3 1 \n"
            + "10 9 9 \n"
            + "8 5 9 \n"
            + "2 1 4 \n"
            + "3 9 6 \n"
            + "1 2 4 \n"
            + "8 3 3 \n"
            + "1 8 4 \n"
            + "8 7 8 \n"
            + "6 8 5 \n"
            + "1 4 2 \n"
            + "2 3 4", "29", false);
        createTestcase("축제", "7 21 4\n"
            + "1 2 7 \n"
            + "7 3 7 \n"
            + "6 5 1 \n"
            + "6 7 9 \n"
            + "3 6 9 \n"
            + "2 3 4 \n"
            + "3 5 8 \n"
            + "2 7 5 \n"
            + "4 1 3 \n"
            + "1 3 3 \n"
            + "6 2 8 \n"
            + "2 5 1 \n"
            + "4 3 1 \n"
            + "5 4 2 \n"
            + "5 1 1 \n"
            + "3 2 6 \n"
            + "6 3 3 \n"
            + "5 7 8 \n"
            + "2 6 6 \n"
            + "1 6 3 \n"
            + "3 1 9", "28", false);

        createProblem("욕심쟁이 토끼", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1937%EB%B2%88+%EC%9A%95%EC%8B%AC%EC%9F%81%EC%9D%B4+%ED%86%A0%EB%81%BC.md", LEVEL3);

        createTestcase("욕심쟁이 토끼", "4\n"
            + "14 9 12 10\n"
            + "1 11 5 4\n"
            + "7 15 2 13\n"
            + "6 3 16 8", "4", true);

        createProblem("미확인 도착지", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/9370%EB%B2%88+%EB%AF%B8%ED%99%95%EC%9D%B8+%EB%8F%84%EC%B0%A9%EC%A7%80.md", LEVEL3);

        createTestcase("미확인 도착지", "5 4 2\n"
            + "1 2 3\n"
            + "1 2 6\n"
            + "2 3 2\n"
            + "3 4 4\n"
            + "3 5 3\n"
            + "5\n"
            + "4\n"
            + "6 9 2\n"
            + "2 3 1\n"
            + "1 2 1\n"
            + "1 3 3\n"
            + "2 4 4\n"
            + "2 5 5\n"
            + "3 4 3\n"
            + "3 6 2\n"
            + "4 5 4\n"
            + "4 6 3\n"
            + "5 6 7\n"
            + "5\n"
            + "6", "4 5\n"
            + "6", true);
        createTestcase("미확인 도착지", "1\n"
            + "6 7 3\n"
            + "1 4 5\n"
            + "1 2 1\n"
            + "2 4 2\n"
            + "2 3 2\n"
            + "3 5 3\n"
            + "4 5 3\n"
            + "5 6 4\n"
            + "2 6 9\n"
            + "5\n"
            + "3\n"
            + "6", "5 6", false);
        createTestcase("미확인 도착지", "1\n"
            + "5 5 2\n"
            + "1 2 3\n"
            + "1 4 3\n"
            + "4 5 3\n"
            + "1 2 2\n"
            + "2 3 2\n"
            + "3 5 2\n"
            + "3\n"
            + "5", "3 5", false);

        createTestcase("미확인 도착지", "5 4 2\n"
            + "1 2 3\n"
            + "1 2 1\n"
            + "2 3 1\n"
            + "3 4 1\n"
            + "2 5 1\n"
            + "4 5", "4", false);
        createTestcase("미확인 도착지", "3\n"
            + "5 5 1\n"
            + "1 3 5\n"
            + "1 2 1\n"
            + "2 4 2\n"
            + "2 3 2\n"
            + "3 5 3\n"
            + "4 5 3\n"
            + "5\n"
            + "5 5 1\n"
            + "1 4 5\n"
            + "1 2 1\n"
            + "2 4 2\n"
            + "2 3 2\n"
            + "3 5 3\n"
            + "4 5 3\n"
            + "5\n"
            + "6 7 3\n"
            + "1 4 5\n"
            + "1 2 1\n"
            + "2 4 2\n"
            + "2 3 2\n"
            + "3 5 3\n"
            + "4 5 3\n"
            + "5 6 4\n"
            + "2 6 9\n"
            + "5\n"
            + "3\n"
            + "6", "5\n"
            + "5\n"
            + "5 6", false);
        createTestcase("미확인 도착지", "1\n"
            + "4 4 1\n"
            + "1 1 4\n"
            + "1 2 1\n"
            + "1 4 3\n"
            + "4 3 1\n"
            + "2 3 1\n"
            + "4", "4", false);

        createProblem("로봇 조종하기", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/2169%EB%B2%88+%EB%A1%9C%EB%B4%87+%EC%A1%B0%EC%A2%85%ED%95%98%EA%B8%B0.md", LEVEL4);

        createTestcase("로봇 조종하기", "5 5\n"
            + "10 25 7 8 13\n"
            + "68 24 -78 63 32\n"
            + "12 -69 100 -29 -25\n"
            + "-16 -22 -57 -33 99\n"
            + "7 -76 -11 77 15", "319", true);
        createTestcase("로봇 조종하기", "3 5\n"
            + "0 0 0 0 5\n"
            + "-10000 0 5 5 5\n"
            + "1000 0 0 0 1000", "1020", false);
        createTestcase("로봇 조종하기", "5 5\n"
            + "-1 -1 -1 -1 -1\n"
            + "-1 -1 -1 -1 -1\n"
            + "-1 -1 -1 -1 -1\n"
            + "-1 -1 -1 -1 -1\n"
            + "-1 -1 -1 -1 -1", "-9", false);
        createTestcase("로봇 조종하기", "9 12\n"
            + "0 0 0 0 0 0 0 0 0 0 0 1\n"
            + "0 1 1 1 1 0 0 1 1 1 1 0\n"
            + "0 0 0 0 0 0 0 1 1 1 1 0\n"
            + "0 1 1 1 1 0 0 1 1 1 1 0\n"
            + "0 0 0 0 0 0 0 0 0 0 0 0\n"
            + "0 1 1 1 1 0 0 1 1 1 1 0\n"
            + "0 1 1 1 1 0 0 0 0 0 0 0\n"
            + "0 1 1 1 1 0 0 1 1 1 1 0\n"
            + "1 0 0 0 0 0 0 0 0 0 0 0", "10", false);
        createTestcase("로봇 조종하기", "4 2\n"
            + "0 0\n"
            + "0 0\n"
            + "1 0\n"
            + "0 0", "7", false);
        createTestcase("로봇 조종하기", " 5 6\n"
            + "1 1 1 1 1 1\n"
            + "1 0 0 0 0 1\n"
            + "1 0 1 1 0 1\n"
            + "1 0 0 0 0 1\n"
            + "1 1 1 1 1 1", "3", false);

        createProblem("AMAAAAZZZZEEEE", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/16985%EB%B2%88+AMAAAAZZZZEEEE.md", LEVEL4);

        createTestcase("AMAAAAZZZZEEEE", "1 1 1 1 1\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "1 1 1 1 1\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "1 1 1 1 1\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "1 1 1 1 1\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "1 1 1 1 1\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0", "12", true);
        createTestcase("AMAAAAZZZZEEEE", "1 1 1 1 1\n"
            + "1 0 0 0 1\n"
            + "1 0 0 0 1\n"
            + "1 0 0 0 1\n"
            + "1 1 1 1 1\n"
            + "0 0 0 0 0\n"
            + "0 1 1 1 0\n"
            + "0 1 0 1 0\n"
            + "0 1 1 1 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 1 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 1 1 1 0\n"
            + "0 1 0 1 0\n"
            + "0 1 1 1 0\n"
            + "0 0 0 0 0\n"
            + "1 1 1 1 1\n"
            + "1 0 0 0 1\n"
            + "1 0 0 0 1\n"
            + "1 0 0 0 1\n"
            + "1 1 1 1 1", "-1", true);
        createTestcase("AMAAAAZZZZEEEE", "1 1 1 1 1\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "1 1 1 1 1\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "1 1 1 1 1\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "1 1 1 1 1\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "1 1 1 1 1", "12", true);
        createTestcase("AMAAAAZZZZEEEE", "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1\n"
            + "1 1 1 1 1", "12", true);
        createTestcase("AMAAAAZZZZEEEE", "0 0 0 1 0\n"
            + "0 0 0 0 0\n"
            + "1 0 1 1 1\n"
            + "0 0 0 1 0\n"
            + "0 0 1 0 0\n"
            + "0 1 0 0 0\n"
            + "1 1 0 0 0\n"
            + "1 0 0 1 0\n"
            + "0 1 1 1 0\n"
            + "0 1 0 1 0\n"
            + "0 0 1 0 0\n"
            + "1 0 0 0 0\n"
            + "0 1 0 0 0\n"
            + "0 0 1 0 0\n"
            + "1 1 1 0 0\n"
            + "1 0 0 0 1\n"
            + "1 0 0 0 0\n"
            + "0 0 1 0 1\n"
            + "0 1 1 0 0\n"
            + "0 1 0 0 0\n"
            + "0 0 0 1 0\n"
            + "1 0 0 0 0\n"
            + "0 0 1 0 0\n"
            + "0 1 0 0 1\n"
            + "0 1 0 0 0", "22", true);
        createTestcase("AMAAAAZZZZEEEE", "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "1 0 0 0 1\n"
            + "0 0 1 0 0\n"
            + "0 0 1 1 1\n"
            + "0 1 0 0 1\n"
            + "0 0 0 0 1\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 1 0 0 0\n"
            + "0 1 0 0 1\n"
            + "1 0 0 1 0\n"
            + "0 0 0 1 0\n"
            + "0 1 1 0 0\n"
            + "0 1 0 0 0\n"
            + "1 0 1 0 0\n"
            + "0 0 0 0 0\n"
            + "1 0 0 0 0\n"
            + "0 0 0 1 0\n"
            + "1 0 0 0 0\n"
            + "0 0 0 1 0\n"
            + "0 0 0 0 1\n"
            + "1 1 0 0 0\n"
            + "1 0 0 1 1\n"
            + "1 0 0 0 0", "-1", true);
        createTestcase("AMAAAAZZZZEEEE", "1 1 0 0 0\n"
            + "0 0 0 0 1\n"
            + "0 0 1 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 1 1 1\n"
            + "1 0 0 0 0\n"
            + "0 0 1 0 0\n"
            + "0 0 1 1 1\n"
            + "0 0 1 0 0\n"
            + "0 0 0 0 0\n"
            + "0 0 1 0 1\n"
            + "0 0 0 0 0\n"
            + "0 0 0 1 0\n"
            + "0 0 1 0 1\n"
            + "0 0 1 0 0\n"
            + "1 0 0 0 0\n"
            + "0 0 1 1 0\n"
            + "1 0 1 0 0\n"
            + "0 0 1 0 1\n"
            + "0 0 1 1 0\n"
            + "1 1 0 1 1\n"
            + "0 0 0 0 1\n"
            + "0 1 0 1 0\n"
            + "0 1 0 0 0", "16", true);
        createTestcase("AMAAAAZZZZEEEE", "0 0 1 0 0\n"
            + "0 0 0 0 0\n"
            + "1 1 0 0 0\n"
            + "0 0 1 0 0\n"
            + "1 1 1 0 0\n"
            + "0 0 0 0 1\n"
            + "1 0 0 0 0\n"
            + "0 1 0 0 1\n"
            + "0 0 0 0 0\n"
            + "0 1 0 1 0\n"
            + "1 0 0 0 1\n"
            + "1 1 1 1 1\n"
            + "1 1 0 0 0\n"
            + "0 0 0 1 0\n"
            + "0 0 0 1 0\n"
            + "0 0 0 1 1\n"
            + "0 0 1 0 0\n"
            + "0 1 1 1 0\n"
            + "1 0 0 0 0\n"
            + "0 1 1 0 1\n"
            + "0 1 0 0 0\n"
            + "0 0 0 1 0\n"
            + "1 0 0 0 0\n"
            + "0 0 0 1 0\n"
            + "0 0 0 1 0", "18", true);

        createProblem("베늑전쟁", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/2325%EB%B2%88+%EB%B2%A0%EB%8A%91%EC%A0%84%EC%9F%81.md", LEVEL5);

        createTestcase("베늑전쟁", "5 6\n"
            + "1 2 4\n"
            + "1 3 3\n"
            + "2 3 1\n"
            + "2 4 4\n"
            + "2 5 7\n"
            + "4 5 1", "11", true);
        createTestcase("베늑전쟁", "6 7\n"
            + "1 2 1\n"
            + "2 3 4\n"
            + "3 4 4\n"
            + "4 6 4\n"
            + "1 5 5\n"
            + "2 5 2\n"
            + "5 6 5", "13", true);
        createTestcase("베늑전쟁", "5 7\n"
            + "1 2 8\n"
            + "1 4 10\n"
            + "2 3 9\n"
            + "2 4 10\n"
            + "2 5 1\n"
            + "3 4 7\n"
            + "3 5 10", "27", true);

        createProblem("네트워크 연결", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/3780%EB%B2%88+%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC+%EC%97%B0%EA%B2%B0.md", LEVEL5);

        createTestcase("네트워크 연결", "1\n"
            + "4\n"
            + "E 3\n"
            + "I 3 1\n"
            + "E 3\n"
            + "I 1 2\n"
            + "E 3\n"
            + "I 2 4\n"
            + "E 3\n"
            + "O", "0\n"
            + "2\n"
            + "3\n"
            + "5", true);
        createTestcase("네트워크 연결", "1\n"
            + "4\n"
            + "I 4 2\n"
            + "I 2 3\n"
            + "I 1 4\n"
            + "E 1\n"
            + "O", "6", false);
        createTestcase("네트워크 연결", "3\n"
            + "20\n"
            + "I 16 1\n"
            + "I 15 9\n"
            + "I 8 6\n"
            + "I 5 19\n"
            + "I 1 17\n"
            + "I 14 13\n"
            + "I 9 20\n"
            + "I 13 19\n"
            + "I 10 13\n"
            + "I 20 17\n"
            + "E 20\n"
            + "I 19 6\n"
            + "I 18 12\n"
            + "I 17 2\n"
            + "I 4 2\n"
            + "I 6 17\n"
            + "E 16\n"
            + "E 2\n"
            + "E 4\n"
            + "E 17\n"
            + "I 2 18\n"
            + "E 13\n"
            + "E 2\n"
            + "E 6\n"
            + "E 20\n"
            + "E 4\n"
            + "I 3 14\n"
            + "I 7 10\n"
            + "E 5\n"
            + "E 12\n"
            + "E 12\n"
            + "I 11 10\n"
            + "E 3\n"
            + "E 19\n"
            + "E 2\n"
            + "E 11\n"
            + "E 8\n"
            + "E 1\n"
            + "E 4\n"
            + "E 13\n"
            + "E 15\n"
            + "E 15\n"
            + "E 8\n"
            + "E 6\n"
            + "E 19\n"
            + "E 14\n"
            + "E 18\n"
            + "E 11\n"
            + "E 15\n"
            + "E 12\n"
            + "E 8\n"
            + "E 1\n"
            + "E 5\n"
            + "E 5\n"
            + "E 14\n"
            + "E 12\n"
            + "E 11\n"
            + "E 6\n"
            + "E 14\n"
            + "E 15\n"
            + "E 4\n"
            + "E 17\n"
            + "E 16\n"
            + "E 5\n"
            + "E 11\n"
            + "E 13\n"
            + "E 6\n"
            + "E 15\n"
            + "E 15\n"
            + "E 13\n"
            + "E 20\n"
            + "E 20\n"
            + "E 3\n"
            + "E 18\n"
            + "E 8\n"
            + "E 7\n"
            + "E 10\n"
            + "E 19\n"
            + "E 11\n"
            + "E 10\n"
            + "E 7\n"
            + "E 15\n"
            + "E 20\n"
            + "E 14\n"
            + "E 9\n"
            + "E 2\n"
            + "E 15\n"
            + "E 12\n"
            + "E 11\n"
            + "E 14\n"
            + "E 7\n"
            + "E 16\n"
            + "E 8\n"
            + "E 2\n"
            + "E 13\n"
            + "E 16\n"
            + "E 11\n"
            + "E 5\n"
            + "E 7\n"
            + "E 13\n"
            + "O", "3\n"
            + "46\n"
            + "0\n"
            + "2\n"
            + "15\n"
            + "67\n"
            + "22\n"
            + "48\n"
            + "40\n"
            + "24\n"
            + "75\n"
            + "0\n"
            + "0\n"
            + "79\n"
            + "61\n"
            + "22\n"
            + "71\n"
            + "50\n"
            + "53\n"
            + "24\n"
            + "67\n"
            + "57\n"
            + "57\n"
            + "50\n"
            + "48\n"
            + "61\n"
            + "68\n"
            + "6\n"
            + "71\n"
            + "57\n"
            + "0\n"
            + "50\n"
            + "53\n"
            + "75\n"
            + "75\n"
            + "68\n"
            + "0\n"
            + "71\n"
            + "48\n"
            + "68\n"
            + "57\n"
            + "24\n"
            + "37\n"
            + "68\n"
            + "75\n"
            + "71\n"
            + "67\n"
            + "48\n"
            + "57\n"
            + "57\n"
            + "67\n"
            + "40\n"
            + "40\n"
            + "79\n"
            + "6\n"
            + "50\n"
            + "73\n"
            + "70\n"
            + "61\n"
            + "71\n"
            + "70\n"
            + "73\n"
            + "57\n"
            + "40\n"
            + "68\n"
            + "51\n"
            + "22\n"
            + "57\n"
            + "0\n"
            + "71\n"
            + "68\n"
            + "73\n"
            + "68\n"
            + "50\n"
            + "22\n"
            + "67\n"
            + "68\n"
            + "71\n"
            + "75\n"
            + "73\n"
            + "67", false);

        // 10개 기록
        createHistory(testMemberId1, testMemberId2, 1L, 2L, JAVA, JAVA, 5, SUCCESS);
        createHistory(testMemberId1, testMemberId2, 3L, 4L, JAVA, JAVA, 6, SUCCESS);
        createHistory(testMemberId1, testMemberId2, 5L, 6L, JAVA, JAVA, 7, SUCCESS);
        createHistory(testMemberId2, testMemberId1, 7L, 8L, null, JAVA, 20, FAILURE);
        createHistory(testMemberId1, testMemberId2, 9L, 10L, CPP, PYTHON, 20, FAILURE);
        createHistory(testMemberId1, testMemberId2, 1L, 2L, null, null, 5, SURRENDER);
        createHistory(testMemberId2, testMemberId1, 7L, 8L, JAVA, JAVA, 5, SUCCESS);
        createHistory(testMemberId1, testMemberId2, 9L, 10L, JAVA, JAVA, 10, SUCCESS);
        createHistory(testMemberId1, testMemberId2, 1L, 2L, PYTHON, PYTHON, 2, SUCCESS);
        createHistory(testMemberId1, testMemberId2, 3L, 4L, PYTHON, JAVA, 6, SUCCESS);


    }

    private Long createTestMember(String nickname, String password){
        if (!memberRepository.existsByNickname(nickname)){
            Member member = Member.builder()
                .nickname(nickname)
                .password(passwordEncoder.encode(password + salt))
                .profileImage("https://a305-project-bucket.s3.ap-northeast-2.amazonaws.com/UserProfileImage/baseImage.jpg")
                .githubAccessToken("githubAccessToken")
                .email("damongsanga@email.com")
                .level(Level.LEVEL1)
                .roles(List.of(Role.USER)).build();
            return memberRepository.save(member).getId();
        }
        return null;
    }

    // language가 null이면 못푼 것으로 간주
    private void createHistory(Long managerId, Long pairId, Long problemId1, Long problemId2, Language language1, Language language2, int totalRound, HistoryResult result){

        Member manager = memberRepository.findById(managerId).orElseThrow(() -> new RestApiException(NO_MEMBER));
        Member pair = memberRepository.findById(pairId).orElseThrow(() -> new RestApiException(NO_MEMBER));
        Problem problem1 = problemRepository.findById(problemId1).orElseThrow(() -> new RestApiException(NO_PROBLEM));
        Problem problem2 = problemRepository.findById(problemId2).orElseThrow(() -> new RestApiException(NO_PROBLEM));

        History history = History.builder()
            .manager(manager).pair(pair)
            .problem1(problem1).problem2(problem2)
            .code1(language1 == null? null : "첫 번째 정답 코드입니다")
            .code2(language2 == null? null :"두 번째 정답 코드입니다")
            .language1(language1).language2(language2)
            .endTime(LocalDateTime.now().plusMinutes(1))
            .totalRound(totalRound)
            .retro1("첫 번째 문제 회고입니다.").retro2("두 번째 문제 회고입니다.")
            .result(result).build();
        historyRepository.save(history);

        // 문제 풀이 기록
        if (language1 != null){
        memberProblemRepository.save(MemberProblem.builder().member(manager).problem(problem1).build());
        memberProblemRepository.save(MemberProblem.builder().member(manager).problem(problem2).build());
        }
        if (language2 != null ){
        memberProblemRepository.save(MemberProblem.builder().member(pair).problem(problem2).build());
        memberProblemRepository.save(MemberProblem.builder().member(pair).problem(problem1).build());
        }
    }


    private void createProblem(String title, String content, Level level){

        if (problemRepository.existsByTitle(title)) return;

        Problem problem = Problem.builder()
            .title(title)
            .content(content)
            .level(level).build();
        problemRepository.save(problem);

    }

    private void createTestcase(String title, String stdin, String expectedOutput, boolean isPublic){
        Problem problem = problemRepository.findByTitle(title);

        if (testcaseRepository.existsByStdin(stdin)) return;
        testcaseRepository.save(Testcase.builder()
            .problem(problem)
            .stdin(stdin)
            .expectedOutput(expectedOutput)
            .isPublic(isPublic)
            .build());

    }

}

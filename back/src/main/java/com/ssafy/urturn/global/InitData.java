package com.ssafy.urturn.global;

import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_MEMBER;
import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_PROBLEM;
import static com.ssafy.urturn.history.HistoryResult.FAILURE;
import static com.ssafy.urturn.history.HistoryResult.SUCCESS;
import static com.ssafy.urturn.history.HistoryResult.SURRENDER;
import static com.ssafy.urturn.member.Level.LEVEL1;
import static com.ssafy.urturn.member.Level.LEVEL2;
import static com.ssafy.urturn.member.Level.LEVEL3;
import static com.ssafy.urturn.member.Level.LEVEL4;
import static com.ssafy.urturn.member.Level.LEVEL5;
import static com.ssafy.urturn.problem.Language.CPP;
import static com.ssafy.urturn.problem.Language.JAVA;
import static com.ssafy.urturn.problem.Language.PYTHON;

import com.ssafy.urturn.global.auth.Role;
import com.ssafy.urturn.global.exception.RestApiException;
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
import com.ssafy.urturn.global.cache.CacheDatas;
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
    private final CacheDatas cacheDatas;

    private final PasswordEncoder passwordEncoder;
    @Value("spring.security.oauth2.client.registration.password-salt")
    private String salt;

    // test를 위해 LEVEL1 문제 2개는 주석처리하여 LEVEL1을 선택하면 테스트문제만 반환되도록 하였습니다.
    @PostConstruct
    public void initData(){

        cacheDatas.clearAllCache();

        Long testMemberId1 = createTestMember("test1", "test1");
        Long testMemberId2 = createTestMember("test2", "test2");

        createProblem("유기농 양상추", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1012%EB%B2%88+%EC%9C%A0%EA%B8%B0%EB%86%8D+%EC%96%91%EC%83%81%EC%B6%94.md", LEVEL1);

        createTestcase("유기농 양상추", """
            2
            10 8 17
            0 0
            1 0
            1 1
            4 2
            4 3
            4 5
            2 4
            3 4
            7 4
            8 4
            9 4
            7 5
            8 5
            9 5
            7 6
            8 6
            9 6
            10 10 1
            5 5""", "5\n"
            + "1", true);

        createTestcase("유기농 양상추", """
            1
            5 3 6
            0 2
            1 2
            2 2
            3 2
            4 2
            4 0""", "2", true);

        createTestcase("유기농 양상추", """
            1
            5 5 5
            0 1
            1 0
            1 2
            2 1
            1 1""", "1", false);
        createTestcase("유기농 양상추", """
            1
            3 3 6
            0 0
            1 1
            2 2
            0 2
            2 0
            1 2""", "3", false);
        createTestcase("유기농 양상추", """
            3
            1 1 1
            0 0
            5 5 6
            0 0
            0 3
            1 4
            2 3
            3 3
            4 4
            8 8 15
            0 0
            0 1
            0 2
            1 2
            1 3
            1 6
            1 7
            2 3
            3 5
            4 5
            5 7
            6 2
            6 4
            7 5
            7 7""", "1\n"
            + "5\n"
            + "8", false);

        createProblem("술래잡기", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1697%EB%B2%88+%EC%88%A0%EB%9E%98%EC%9E%A1%EA%B8%B0.md", LEVEL1);

        createTestcase("술래잡기", "5 17", "4", true);
        createTestcase("술래잡기", "0 1", "1", false);
        createTestcase("술래잡기", "1 15", "5", false);
        createTestcase("술래잡기", "1 100000", "21", false);
        createTestcase("술래잡기", "3482 45592", "637", false);
        createTestcase("술래잡기", "5 35", "5", false);
        createTestcase("술래잡기", "10007 98767", "2343", false);

        createProblem("빙하", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/2573%EB%B2%88+%EB%B9%99%ED%95%98.md", LEVEL2);

        createTestcase("빙하", """
            5 7
            0 0 0 0 0 0 0
            0 2 4 5 3 0 0
            0 3 0 2 5 2 0
            0 7 6 2 4 0 0
            0 0 0 0 0 0 0""", "2", true);

        createTestcase("빙하", """
            5 5
            0 0 0 0 0
            0 1 1 1 0
            0 1 0 1 0
            0 1 1 1 0
            0 0 0 0 0""", "0", true);

        createTestcase("빙하", """
            4 4
            0 0 0 0
            0 3 1 0
            0 1 3 0
            0 0 0 0""", "1", false);

        createProblem("암호화", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1759%EB%B2%88+%EC%95%94%ED%98%B8%ED%99%94.md", LEVEL2);

        createTestcase("암호화", "4 6\n"
            + "a t c i s w", """
            acis
            acit
            aciw
            acst
            acsw
            actw
            aist
            aisw
            aitw
            astw
            cist
            cisw
            citw
            istw""", true);

        createProblem("정육면체 던지기", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/14499%EB%B2%88+%EC%A0%95%EC%9C%A1%EB%A9%B4%EC%B2%B4+%EB%8D%98%EC%A7%80%EA%B8%B0.md", LEVEL2);

        createTestcase("정육면체 던지기", """
            4 2 0 0 8
            0 2
            3 4
            5 6
            7 8
            4 4 4 1 3 3 3 2""", """
            0
            0
            3
            0
            0
            8
            6
            3""", true);
        createTestcase("정육면체 던지기", """
            3 3 1 1 9
            1 2 3
            4 0 5
            6 7 8
            1 3 2 2 4 4 1 1 3""", """
            0
            0
            0
            3
            0
            1
            0
            6
            0""", true);
        createTestcase("정육면체 던지기", """
            2 2 0 0 16
            0 2
            3 4
            4 4 4 4 1 1 1 1 3 3 3 3 2 2 2 2""", """
            0
            0
            0
            0""", true);
        createTestcase("정육면체 던지기", """
            3 3 0 0 16
            0 1 2
            3 4 5
            6 7 8
            4 4 1 1 3 3 2 2 4 4 1 1 3 3 2 2""", """
            0
            0
            0
            6
            0
            8
            0
            2
            0
            8
            0
            2
            0
            8
            0
            2""", true);

        createProblem("축제", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1238%EB%B2%88+%EC%B6%95%EC%A0%9C.md", LEVEL2);

        createTestcase("축제", """
            4 8 2
            1 2 4
            1 3 2
            1 4 7
            2 1 1
            2 3 5
            3 1 2
            3 4 4
            4 2 3""", "10", true);
        createTestcase("축제", """
            10 24 2
            9 5 9
            7 9 2
            8 1 7
            7 10 2
            6 1 6
            10 6 5
            5 1 6
            5 7 9
            3 8 9
            4 10 7
            8 10 6
            7 2 6
            4 3 1
            10 9 9
            8 5 9
            2 1 4
            3 9 6
            1 2 4
            8 3 3
            1 8 4
            8 7 8
            6 8 5
            1 4 2
            2 3 4""", "29", false);
        createTestcase("축제", """
            7 21 4
            1 2 7
            7 3 7
            6 5 1
            6 7 9
            3 6 9
            2 3 4
            3 5 8
            2 7 5
            4 1 3
            1 3 3
            6 2 8
            2 5 1
            4 3 1
            5 4 2
            5 1 1
            3 2 6
            6 3 3
            5 7 8
            2 6 6
            1 6 3
            3 1 9""", "28", false);

        createProblem("욕심쟁이 토끼", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/1937%EB%B2%88+%EC%9A%95%EC%8B%AC%EC%9F%81%EC%9D%B4+%ED%86%A0%EB%81%BC.md", LEVEL3);

        createTestcase("욕심쟁이 토끼", """
            4
            14 9 12 10
            1 11 5 4
            7 15 2 13
            6 3 16 8""", "4", true);

        createProblem("미확인 도착지", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/9370%EB%B2%88+%EB%AF%B8%ED%99%95%EC%9D%B8+%EB%8F%84%EC%B0%A9%EC%A7%80.md", LEVEL3);

        createTestcase("미확인 도착지", """
            5 4 2
            1 2 3
            1 2 6
            2 3 2
            3 4 4
            3 5 3
            5
            4
            6 9 2
            2 3 1
            1 2 1
            1 3 3
            2 4 4
            2 5 5
            3 4 3
            3 6 2
            4 5 4
            4 6 3
            5 6 7
            5
            6""", "4 5\n"
            + "6", true);
        createTestcase("미확인 도착지", """
            1
            6 7 3
            1 4 5
            1 2 1
            2 4 2
            2 3 2
            3 5 3
            4 5 3
            5 6 4
            2 6 9
            5
            3
            6""", "5 6", false);
        createTestcase("미확인 도착지", """
            1
            5 5 2
            1 2 3
            1 4 3
            4 5 3
            1 2 2
            2 3 2
            3 5 2
            3
            5""", "3 5", false);

        createTestcase("미확인 도착지", """
            5 4 2
            1 2 3
            1 2 1
            2 3 1
            3 4 1
            2 5 1
            4 5""", "4", false);
        createTestcase("미확인 도착지", """
            3
            5 5 1
            1 3 5
            1 2 1
            2 4 2
            2 3 2
            3 5 3
            4 5 3
            5
            5 5 1
            1 4 5
            1 2 1
            2 4 2
            2 3 2
            3 5 3
            4 5 3
            5
            6 7 3
            1 4 5
            1 2 1
            2 4 2
            2 3 2
            3 5 3
            4 5 3
            5 6 4
            2 6 9
            5
            3
            6""", """
            5
            5
            5 6""", false);
        createTestcase("미확인 도착지", """
            1
            4 4 1
            1 1 4
            1 2 1
            1 4 3
            4 3 1
            2 3 1
            4""", "4", false);

        createProblem("로봇 조종하기", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/2169%EB%B2%88+%EB%A1%9C%EB%B4%87+%EC%A1%B0%EC%A2%85%ED%95%98%EA%B8%B0.md", LEVEL4);

        createTestcase("로봇 조종하기", """
            5 5
            10 25 7 8 13
            68 24 -78 63 32
            12 -69 100 -29 -25
            -16 -22 -57 -33 99
            7 -76 -11 77 15""", "319", true);
        createTestcase("로봇 조종하기", """
            3 5
            0 0 0 0 5
            -10000 0 5 5 5
            1000 0 0 0 1000""", "1020", false);
        createTestcase("로봇 조종하기", """
            5 5
            -1 -1 -1 -1 -1
            -1 -1 -1 -1 -1
            -1 -1 -1 -1 -1
            -1 -1 -1 -1 -1
            -1 -1 -1 -1 -1""", "-9", false);
        createTestcase("로봇 조종하기", """
            9 12
            0 0 0 0 0 0 0 0 0 0 0 1
            0 1 1 1 1 0 0 1 1 1 1 0
            0 0 0 0 0 0 0 1 1 1 1 0
            0 1 1 1 1 0 0 1 1 1 1 0
            0 0 0 0 0 0 0 0 0 0 0 0
            0 1 1 1 1 0 0 1 1 1 1 0
            0 1 1 1 1 0 0 0 0 0 0 0
            0 1 1 1 1 0 0 1 1 1 1 0
            1 0 0 0 0 0 0 0 0 0 0 0""", "10", false);
        createTestcase("로봇 조종하기", """
            4 2
            0 0
            0 0
            1 0
            0 0""", "7", false);
        createTestcase("로봇 조종하기", """
             5 6
            1 1 1 1 1 1
            1 0 0 0 0 1
            1 0 1 1 0 1
            1 0 0 0 0 1
            1 1 1 1 1 1""", "3", false);

        createProblem("AMAAAAZZZZEEEE", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/16985%EB%B2%88+AMAAAAZZZZEEEE.md", LEVEL4);

        createTestcase("AMAAAAZZZZEEEE", """
            1 1 1 1 1
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            1 1 1 1 1
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            1 1 1 1 1
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            1 1 1 1 1
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            1 1 1 1 1
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0""", "12", true);
        createTestcase("AMAAAAZZZZEEEE", """
            1 1 1 1 1
            1 0 0 0 1
            1 0 0 0 1
            1 0 0 0 1
            1 1 1 1 1
            0 0 0 0 0
            0 1 1 1 0
            0 1 0 1 0
            0 1 1 1 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 1 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 1 1 1 0
            0 1 0 1 0
            0 1 1 1 0
            0 0 0 0 0
            1 1 1 1 1
            1 0 0 0 1
            1 0 0 0 1
            1 0 0 0 1
            1 1 1 1 1""", "-1", true);
        createTestcase("AMAAAAZZZZEEEE", """
            1 1 1 1 1
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            1 1 1 1 1
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            1 1 1 1 1
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            1 1 1 1 1
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 0 0 0
            1 1 1 1 1""", "12", true);
        createTestcase("AMAAAAZZZZEEEE", """
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1
            1 1 1 1 1""", "12", true);
        createTestcase("AMAAAAZZZZEEEE", """
            0 0 0 1 0
            0 0 0 0 0
            1 0 1 1 1
            0 0 0 1 0
            0 0 1 0 0
            0 1 0 0 0
            1 1 0 0 0
            1 0 0 1 0
            0 1 1 1 0
            0 1 0 1 0
            0 0 1 0 0
            1 0 0 0 0
            0 1 0 0 0
            0 0 1 0 0
            1 1 1 0 0
            1 0 0 0 1
            1 0 0 0 0
            0 0 1 0 1
            0 1 1 0 0
            0 1 0 0 0
            0 0 0 1 0
            1 0 0 0 0
            0 0 1 0 0
            0 1 0 0 1
            0 1 0 0 0""", "22", true);
        createTestcase("AMAAAAZZZZEEEE", """
            0 0 0 0 0
            0 0 0 0 0
            1 0 0 0 1
            0 0 1 0 0
            0 0 1 1 1
            0 1 0 0 1
            0 0 0 0 1
            0 0 0 0 0
            0 0 0 0 0
            0 1 0 0 0
            0 1 0 0 1
            1 0 0 1 0
            0 0 0 1 0
            0 1 1 0 0
            0 1 0 0 0
            1 0 1 0 0
            0 0 0 0 0
            1 0 0 0 0
            0 0 0 1 0
            1 0 0 0 0
            0 0 0 1 0
            0 0 0 0 1
            1 1 0 0 0
            1 0 0 1 1
            1 0 0 0 0""", "-1", true);
        createTestcase("AMAAAAZZZZEEEE", """
            1 1 0 0 0
            0 0 0 0 1
            0 0 1 0 0
            0 0 0 0 0
            0 0 0 0 0
            0 0 1 1 1
            1 0 0 0 0
            0 0 1 0 0
            0 0 1 1 1
            0 0 1 0 0
            0 0 0 0 0
            0 0 1 0 1
            0 0 0 0 0
            0 0 0 1 0
            0 0 1 0 1
            0 0 1 0 0
            1 0 0 0 0
            0 0 1 1 0
            1 0 1 0 0
            0 0 1 0 1
            0 0 1 1 0
            1 1 0 1 1
            0 0 0 0 1
            0 1 0 1 0
            0 1 0 0 0""", "16", true);
        createTestcase("AMAAAAZZZZEEEE", """
            0 0 1 0 0
            0 0 0 0 0
            1 1 0 0 0
            0 0 1 0 0
            1 1 1 0 0
            0 0 0 0 1
            1 0 0 0 0
            0 1 0 0 1
            0 0 0 0 0
            0 1 0 1 0
            1 0 0 0 1
            1 1 1 1 1
            1 1 0 0 0
            0 0 0 1 0
            0 0 0 1 0
            0 0 0 1 1
            0 0 1 0 0
            0 1 1 1 0
            1 0 0 0 0
            0 1 1 0 1
            0 1 0 0 0
            0 0 0 1 0
            1 0 0 0 0
            0 0 0 1 0
            0 0 0 1 0""", "18", true);

        createProblem("베늑전쟁", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/2325%EB%B2%88+%EB%B2%A0%EB%8A%91%EC%A0%84%EC%9F%81.md", LEVEL5);

        createTestcase("베늑전쟁", """
            5 6
            1 2 4
            1 3 3
            2 3 1
            2 4 4
            2 5 7
            4 5 1""", "11", true);
        createTestcase("베늑전쟁", """
            6 7
            1 2 1
            2 3 4
            3 4 4
            4 6 4
            1 5 5
            2 5 2
            5 6 5""", "13", true);
        createTestcase("베늑전쟁", """
            5 7
            1 2 8
            1 4 10
            2 3 9
            2 4 10
            2 5 1
            3 4 7
            3 5 10""", "27", true);

        createProblem("네트워크 연결", "https://urturn-problem.s3.ap-northeast-2.amazonaws.com/problem/3780%EB%B2%88+%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC+%EC%97%B0%EA%B2%B0.md", LEVEL5);

        createTestcase("네트워크 연결", """
            1
            4
            E 3
            I 3 1
            E 3
            I 1 2
            E 3
            I 2 4
            E 3
            O""", """
            0
            2
            3
            5""", true);
        createTestcase("네트워크 연결", """
            1
            4
            I 4 2
            I 2 3
            I 1 4
            E 1
            O""", "6", false);
        createTestcase("네트워크 연결", """
            3
            20
            I 16 1
            I 15 9
            I 8 6
            I 5 19
            I 1 17
            I 14 13
            I 9 20
            I 13 19
            I 10 13
            I 20 17
            E 20
            I 19 6
            I 18 12
            I 17 2
            I 4 2
            I 6 17
            E 16
            E 2
            E 4
            E 17
            I 2 18
            E 13
            E 2
            E 6
            E 20
            E 4
            I 3 14
            I 7 10
            E 5
            E 12
            E 12
            I 11 10
            E 3
            E 19
            E 2
            E 11
            E 8
            E 1
            E 4
            E 13
            E 15
            E 15
            E 8
            E 6
            E 19
            E 14
            E 18
            E 11
            E 15
            E 12
            E 8
            E 1
            E 5
            E 5
            E 14
            E 12
            E 11
            E 6
            E 14
            E 15
            E 4
            E 17
            E 16
            E 5
            E 11
            E 13
            E 6
            E 15
            E 15
            E 13
            E 20
            E 20
            E 3
            E 18
            E 8
            E 7
            E 10
            E 19
            E 11
            E 10
            E 7
            E 15
            E 20
            E 14
            E 9
            E 2
            E 15
            E 12
            E 11
            E 14
            E 7
            E 16
            E 8
            E 2
            E 13
            E 16
            E 11
            E 5
            E 7
            E 13
            O""", """
            3
            46
            0
            2
            15
            67
            22
            48
            40
            24
            75
            0
            0
            79
            61
            22
            71
            50
            53
            24
            67
            57
            57
            50
            48
            61
            68
            6
            71
            57
            0
            50
            53
            75
            75
            68
            0
            71
            48
            68
            57
            24
            37
            68
            75
            71
            67
            48
            57
            57
            67
            40
            40
            79
            6
            50
            73
            70
            61
            71
            70
            73
            57
            40
            68
            51
            22
            57
            0
            71
            68
            73
            68
            50
            22
            67
            68
            71
            75
            73
            67""", false);

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

        try {
            Long giljaeId = memberRepository.findByNickname("747Socker").orElseThrow().getId();
            Long taehiId = memberRepository.findByNickname("nyanpasu-life").orElseThrow().getId();

            createHistory(giljaeId, taehiId, 1L, 2L, JAVA, JAVA, 5, SUCCESS);
            createHistory(giljaeId, taehiId, 3L, 4L, JAVA, JAVA, 6, SUCCESS);
            createHistory(taehiId, giljaeId, 9L, 10L, CPP, null, 20, FAILURE);
            createHistory(taehiId, giljaeId, 5L, 6L, JAVA, JAVA, 7, SUCCESS);
            createHistory(taehiId, giljaeId, 7L, 8L, null, JAVA, 20, FAILURE);
            createHistory(taehiId, giljaeId, 11L, 12L, null, null, 20, FAILURE);
        } catch (Exception e){
            log.info("길재 or 태희 github id가 없습니다.");
        }

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
        return memberRepository.findByNickname(nickname).orElseThrow(() -> new RestApiException(NO_MEMBER)).getId();
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

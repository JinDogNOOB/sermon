package com.jindognoob.sermon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jindognoob.sermon.domain.Account;
import com.jindognoob.sermon.domain.etypes.AccountSignupType;
import com.jindognoob.sermon.domain.etypes.QuestionStatusType;
import com.jindognoob.sermon.dto.Paging;
import com.jindognoob.sermon.service.AccountService;
import com.jindognoob.sermon.service.BoardService;
import com.jindognoob.sermon.service.exceptions.ContentAuthorizationViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Transactional
public class BoardServiceTest {
    @Autowired
    AccountService accountService;
    @Autowired
    BoardService boardService;

    private Account createTest0Account() {
        Long id = accountService.signup("tester0@test.com", "qwer1234", AccountSignupType.THIS);
        return accountService.getAccountInfo(id);
    }

    private Account createTest1Account() {
        Long id = accountService.signup("tester1@test.com", "qwer1234", AccountSignupType.THIS);
        return accountService.getAccountInfo(id);
    }

    @Test
    public void 질문_등록() {
        Account account = createTest0Account();
        Long id = boardService.addQuestion(account.getEmail(), "question", "question");

        assertEquals("question", boardService.getQuestion(id).getTitle());
    }

    @Test
    public void 없는계정으로_질문등록_실패_확인() {
        Assertions.assertThrows(Exception.class, () -> {
            boardService.addQuestion("uhehe", "question", "question");
        });
    }

    @Test
    public void 질문_삭제() {
        Account account = createTest0Account();
        Long id = boardService.addQuestion(account.getEmail(), "question", "question");
        try {
            boardService.deleteQuestion(account.getEmail(), id);
        } catch (Exception e) {

        }
        assertEquals(null, boardService.getQuestion(id));
    }

    @Test
    public void 소유자아닐때_질문_삭제_실패_확인() {
        Account account = createTest0Account();
        Account account2 = createTest1Account();
        Long id = boardService.addQuestion(account.getEmail(), "question", "question");

        Assertions.assertThrows(ContentAuthorizationViolationException.class, () -> {
            boardService.deleteQuestion(account2.getEmail(), id);
        });
    }

    @Test
    public void ClOSED된_질문_삭제_실패_확인() {
        Account account = createTest0Account();
        Account account2 = createTest1Account();
        Long id = boardService.addQuestion(account.getEmail(), "question", "question");

        try {
            Long answerId = boardService.addAnswer(account2.getEmail(), "answer", "answer", id);
            boardService.adoptAnswer(account.getEmail(), id, answerId);
        } catch (Exception e) {
        }

        Assertions.assertThrows(Exception.class, () -> {
            boardService.deleteQuestion(account.getEmail(), id);
        });
    }

    @Test
    public void 질문에_답변_생성_확인() {
        Account account = createTest0Account();
        Account account2 = createTest1Account();
        Long id = boardService.addQuestion(account.getEmail(), "question", "question");
        try {
            boardService.addAnswer(account2.getEmail(), "answer", "answer", id);
        } catch (Exception e) {
        }
        assertEquals("answer", boardService.getAnswersOfQuestion(id).get(0).getTitle());
    }

    @Test
    public void CLOSED된_질문에_답변_생성_실패_확인() {
        Account account = createTest0Account();
        Account account2 = createTest1Account();
        Long questionId = boardService.addQuestion(account.getEmail(), "question", "question");
        try {
            Long answerId = boardService.addAnswer(account2.getEmail(), "answer", "answer", questionId);
            boardService.adoptAnswer(account.getEmail(), questionId, answerId);
        } catch (Exception e) {
        }
        Assertions.assertThrows(Exception.class, () -> {
            boardService.addAnswer(account2.getEmail(), "aaa", "bbb", questionId);
        });
    }

    @Test
    public void 자문자답_실패_확인() {
        Account account = createTest0Account();
        Long questionId = boardService.addQuestion(account.getEmail(), "question", "question");

        Assertions.assertThrows(IllegalStateException.class, () -> {
            boardService.addAnswer(account.getEmail(), "aaa", "bbb", questionId);
        });
    }

    @Test
    public void CLOSED된_질문에_등록된_답변_삭제_실패_확인() {
        Account account = createTest0Account();
        Account account2 = createTest1Account();
        Long questionId = boardService.addQuestion(account.getEmail(), "question", "question");

        try {
            Long answerId = boardService.addAnswer(account2.getEmail(), "answer", "answer", questionId);
            boardService.adoptAnswer(account.getEmail(), questionId, answerId);
            Assertions.assertThrows(Exception.class, () -> {
                boardService.deleteAnswer(account2.getEmail(), answerId);
            });
        } catch (Exception e) {
            log.info("eeee");
        }
    }

    @Test
    public void 소유가_아닌_답변_삭제_실패_확인() {
        Account account = createTest0Account();
        Account account2 = createTest1Account();
        Long questionId = boardService.addQuestion(account.getEmail(), "question", "question");

        try {
            Long answerId = boardService.addAnswer(account2.getEmail(), "answer", "answer", questionId);
            // boardService.adoptAnswer(account.getEmail(), questionId, answerId);
            Assertions.assertThrows(ContentAuthorizationViolationException.class, () -> {
                boardService.deleteAnswer(account.getEmail(), answerId);
            });
        } catch (Exception e) {
            log.info("eeee");
        }
    }

    @Test
    public void 질문_페이징_조회_확인0() {
        Account account = createTest0Account();
        for (int i = 0; i < 30; i++) {
            boardService.addQuestion(account.getEmail(), "question", "question");
        }
        log.info("PageTest : " + boardService.getQuestions(new Paging(2, 10)).size());
        int length = boardService.getQuestions(new Paging(2, 10)).size();
        assertEquals(10, length);
    }

    @Test
    public void 질문_페이징_조회_확인() {
        Account account = createTest0Account();
        for (int i = 0; i < 30; i++)
            boardService.addQuestion(account.getEmail(), "question", "question");
        log.info("PageTest : " + boardService.getQuestions(new Paging(1, 10)).size());
        log.info("PageTest : " + boardService.getQuestions(new Paging(2, 10)).size());
        assertEquals(20, boardService.getQuestions(new Paging(1, 10)).size()
                + boardService.getQuestions(new Paging(2, 10)).size());
    }

    @Test
    public void 질문_타입에따른_페이징_조회_확인() {
        try {
            Account account = createTest0Account();
            Account account1 = createTest1Account();
            for (int i = 0; i < 30; i++) {
                long qid = boardService.addQuestion(account.getEmail(), "question", "question");
                long aid = boardService.addAnswer(account1.getEmail(), "a", "a", qid);
                if (i < 15)
                    boardService.adoptAnswer(account.getEmail(), qid, aid);
            }
            log.info("PageTest : " + boardService.getQuestions(new Paging(1, 10), QuestionStatusType.ACTIVE).size());
            log.info("PageTest : " + boardService.getQuestions(new Paging(2, 10), QuestionStatusType.CLOSED).size());
            log.info("PageTest : " + boardService.getQuestions(new Paging(1, 30), QuestionStatusType.CLOSED).size());

            assertEquals(20, boardService.getQuestions(new Paging(1, 10), QuestionStatusType.ACTIVE).size()
                    + boardService.getQuestions(new Paging(2, 10), QuestionStatusType.CLOSED).size());
        } catch (Exception e) {
        }
    }

    // @Test
    // public void 가7(){

    // }

    // @Test
    // public void 가8(){

    // }

    // @Test
    // public void 가9(){

    // }

    // @Test
    // public void 가0(){

    // }

    // @Test
    // public void 가11(){

    // }

    // @Test
    // public void 가12(){

    // }

}

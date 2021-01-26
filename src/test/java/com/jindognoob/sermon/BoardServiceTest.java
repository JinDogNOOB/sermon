package com.jindognoob.sermon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import com.jindognoob.sermon.domain.Account;
import com.jindognoob.sermon.domain.etypes.AccountSignupType;
import com.jindognoob.sermon.domain.etypes.QuestionStatusType;
import com.jindognoob.sermon.dto.AccountDTO;
import com.jindognoob.sermon.dto.Paging;
import com.jindognoob.sermon.dto.QuestionDTO;
import com.jindognoob.sermon.service.AccountService;
import com.jindognoob.sermon.service.BoardService;
import com.jindognoob.sermon.service.exceptions.AnswerDoesNotBelongsToQuestionException;
import com.jindognoob.sermon.service.exceptions.ContentAuthorizationViolationException;
import com.jindognoob.sermon.service.exceptions.QuestionStatusRuleViolationException;

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

    private AccountDTO createTest0Account() {
        Long id = accountService.signup("tester0@test.com", "qwer1234", "nickname", AccountSignupType.THIS);
        return accountService.getAccountInfo(id);
    }

    private AccountDTO createTest1Account() {
        Long id = accountService.signup("tester1@test.com", "qwer1234", "nickname", AccountSignupType.THIS);
        return accountService.getAccountInfo(id);
    }

    @Test
    public void 질문_등록() {
        AccountDTO account = createTest0Account();
        Long id = boardService.addQuestion(account.getEmail(), "question", "question", "");

        assertEquals("question", boardService.getQuestion(id).getTitle());
    }

    @Test
    public void 없는계정으로_질문등록_실패_확인() {
        Assertions.assertThrows(Exception.class, () -> {
            boardService.addQuestion("uhehe", "question", "question", "");
        });
    }

    @Test
    public void 질문_삭제() {
        AccountDTO account = createTest0Account();
        Long id = boardService.addQuestion(account.getEmail(), "question", "question", "");
        try {
            boardService.deleteQuestion(account.getEmail(), id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(null, boardService.getQuestion(id));
    }

    @Test
    public void 소유자아닐때_질문_삭제_실패_확인() {
        AccountDTO account = createTest0Account();
        AccountDTO account2 = createTest1Account();
        Long id = boardService.addQuestion(account.getEmail(), "question", "question", "");

        Assertions.assertThrows(ContentAuthorizationViolationException.class, () -> {
            boardService.deleteQuestion(account2.getEmail(), id);
        });
    }

    @Test
    public void ClOSED된_질문_삭제_실패_확인() {
        AccountDTO account = createTest0Account();
        AccountDTO account2 = createTest1Account();
        Long id = boardService.addQuestion(account.getEmail(), "question", "question", "");

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
        AccountDTO account = createTest0Account();
        AccountDTO account2 = createTest1Account();
        Long id = boardService.addQuestion(account.getEmail(), "question", "question", "");
        try {
            boardService.addAnswer(account2.getEmail(), "answer", "answer", id);
        } catch (Exception e) {
        }
        assertEquals("answer", boardService.getAnswersOfQuestion(id).get(0).getTitle());
    }

    @Test
    public void CLOSED된_질문에_답변_생성_실패_확인() {
        AccountDTO account = createTest0Account();
        AccountDTO account2 = createTest1Account();
        Long questionId = boardService.addQuestion(account.getEmail(), "question", "question", "");
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
        AccountDTO account = createTest0Account();
        Long questionId = boardService.addQuestion(account.getEmail(), "question", "question", "");

        Assertions.assertThrows(IllegalStateException.class, () -> {
            boardService.addAnswer(account.getEmail(), "aaa", "bbb", questionId);
        });
    }

    @Test
    public void CLOSED된_질문에_등록된_답변_삭제_실패_확인() {
        AccountDTO account = createTest0Account();
        AccountDTO account2 = createTest1Account();
        Long questionId = boardService.addQuestion(account.getEmail(), "question", "question", "");

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
        AccountDTO account = createTest0Account();
        AccountDTO account2 = createTest1Account();
        Long questionId = boardService.addQuestion(account.getEmail(), "question", "question", "");

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
        AccountDTO account = createTest0Account();
        for (int i = 0; i < 30; i++) {
            boardService.addQuestion(account.getEmail(), "question", "question", "");
        }
        log.info("PageTest : " + boardService.getQuestions(new Paging(2, 10), "").size());
        int length = boardService.getQuestions(new Paging(2, 10), "").size();
        assertEquals(10, length);
    }

    @Test
    public void 질문_페이징_조회_확인() {
        AccountDTO account = createTest0Account();
        for (int i = 0; i < 30; i++)
            boardService.addQuestion(account.getEmail(), "question", "question", "");
        log.info("PageTest : " + boardService.getQuestions(new Paging(1, 10), "").size());
        log.info("PageTest : " + boardService.getQuestions(new Paging(2, 10), "").size());
        assertEquals(20, boardService.getQuestions(new Paging(1, 10), "").size()
                + boardService.getQuestions(new Paging(2, 10), "").size());
    }

    @Test
    public void 질문_타입에따른_페이징_조회_확인() {
        try {
            AccountDTO account = createTest0Account();
            AccountDTO account1 = createTest1Account();
            for (int i = 0; i < 30; i++) {
                long qid = boardService.addQuestion(account.getEmail(), "question", "question", "");
                long aid = boardService.addAnswer(account1.getEmail(), "a", "a", qid);
                if (i < 15)
                    boardService.adoptAnswer(account.getEmail(), qid, aid);
            }
            // log.info("PageTest : " + boardService.getQuestions(new Paging(1, 10), QuestionStatusType.ACTIVE).size());
            // log.info("PageTest : " + boardService.getQuestions(new Paging(2, 10), QuestionStatusType.CLOSED).size());
            // log.info("PageTest : " + boardService.getQuestions(new Paging(1, 30), QuestionStatusType.CLOSED).size());

            assertEquals(20, boardService.getQuestions(new Paging(1, 10), QuestionStatusType.ACTIVE).size()
                    + boardService.getQuestions(new Paging(1, 10), QuestionStatusType.CLOSED).size());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void 자신이_올린_질문_조회_확인(){
        try {
            AccountDTO account = createTest0Account();
            AccountDTO account1 = createTest1Account();
            
            for (int i = 0; i < 30; i++) {
                boardService.addQuestion(account.getEmail(), "question", "question", "");
                boardService.addQuestion(account1.getEmail(), "question", "question", "");
            }


            assertEquals(30, boardService.getMyQuestions(account.getEmail(), new Paging(1, 30)).size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void 자신이_올린_질문_조회_확인_타입에따라(){
        try {
            AccountDTO account = createTest0Account();
            AccountDTO account1 = createTest1Account();
            for (int i = 0; i < 30; i++) {
                long qid = boardService.addQuestion(account.getEmail(), "question", "question", "");
                long aid = boardService.addAnswer(account1.getEmail(), "a", "a", qid);
                if(i < 15) boardService.adoptAnswer(account.getEmail(), qid, aid);
            }
            List<QuestionDTO> results = boardService.getMyQuestions(account.getEmail(), new Paging(1, 30), QuestionStatusType.CLOSED);
            for(QuestionDTO result : results)
                log.info(result.getTitle());

            assertEquals(15, boardService.getMyQuestions(account.getEmail(), new Paging(1, 30), QuestionStatusType.CLOSED).size());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void 답변_채택_권한자아닐때_예외_확인(){
        try {
            AccountDTO account0 = createTest0Account();
            AccountDTO account1 = createTest1Account();

            long qid = boardService.addQuestion(account0.getEmail(), "question", "question", "");
            long aid = boardService.addAnswer(account1.getEmail(), "a", "a", qid);

            Assertions.assertThrows(ContentAuthorizationViolationException.class, () -> {
                boardService.adoptAnswer(account1.getEmail(), qid, aid);
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void CLOSED된_질문에서_채택시도할시_예외_확인(){
        try {
            AccountDTO account0 = createTest0Account();
            AccountDTO account1 = createTest1Account();

            long qid = boardService.addQuestion(account0.getEmail(), "question", "question", "");
            long aid = boardService.addAnswer(account1.getEmail(), "a", "a", qid);
            long zzz = boardService.addAnswer(account1.getEmail(), "a", "a", qid);
            boardService.adoptAnswer(account0.getEmail(), qid, aid);

            Assertions.assertThrows(QuestionStatusRuleViolationException.class, () -> {
                boardService.adoptAnswer(account0.getEmail(), qid, zzz);
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void 질문에_포함되지않는_답변_채택시도시_예외_확인(){
        try {
            AccountDTO account0 = createTest0Account();
            AccountDTO account1 = createTest1Account();

            long qid = boardService.addQuestion(account0.getEmail(), "question", "question", "");

            long aqid = boardService.addQuestion(account0.getEmail(), "question", "question", "");
            long aid = boardService.addAnswer(account1.getEmail(), "a", "a", aqid);

            Assertions.assertThrows(AnswerDoesNotBelongsToQuestionException.class, () -> {
                boardService.adoptAnswer(account0.getEmail(), qid, aid);
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void 포인트_테스트(){
        try {
            AccountDTO account0 = createTest0Account();
            AccountDTO account1 = createTest1Account();

            boardService.addQuestion(account0.getEmail(), "question", "question", "");
            long aqid = boardService.addQuestion(account0.getEmail(), "question", "question", "");

            long aid = boardService.addAnswer(account1.getEmail(), "a", "a", aqid);
            boardService.adoptAnswer(account0.getEmail(), aqid, aid);

            assertEquals(11, accountService.getAccountInfo(account0.getId()).getPointAmount() + 
                accountService.getAccountInfo(account1.getId()).getPointAmount());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}

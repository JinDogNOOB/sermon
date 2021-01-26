package com.jindognoob.sermon.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.jindognoob.sermon.domain.Question;
import com.jindognoob.sermon.domain.QuestionHashTag;
import com.jindognoob.sermon.domain.etypes.QuestionStatusType;
import com.jindognoob.sermon.utils.ModelMapperUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;
    private String title;
    private String content;
    private Date createdDate;
    private QuestionStatusType status;

    private List<String> hashTags;

    private Long accountId;
    private String nickname;

    private Long answersSize;
    private Long viewCount;


    public static QuestionDTO of(Question question){
        if(question == null) return null;
        QuestionDTO questionDTO = ModelMapperUtils.getInstance().map(question, QuestionDTO.class);
        
        questionDTO.setAccountId(question.getAccount().getId());
        questionDTO.setAnswersSize((long)question.getAnswers().size());
        questionDTO.setNickname(question.getAccount().getNickname());
        questionDTO.setHashTags(getHashTagsFromQuestion(question));
        return questionDTO;
    }

    // 아마 accountID는 포함안될거임 실제 보고 필요하면 추가 
    public static List<QuestionDTO> of(List<Question> questions){
        if(questions == null) return null;
            List<QuestionDTO> result = new ArrayList<QuestionDTO>();
        for(Question question : questions){
            result.add(QuestionDTO.of(question));
        }
        return result;
/*         return questions.stream()
        .map(questionEntity -> ModelMapperUtils.getInstance().map(questionEntity, QuestionDTO.class))
        .collect(Collectors.toList()); */

    }

    private static List<String> getHashTagsFromQuestion(Question question){
        List<String> result = new ArrayList<String>();
        List<QuestionHashTag> qhts = question.getQuestionHashTags();
        for(QuestionHashTag qht : qhts)
            result.add(qht.getHashTag().getTag());
        return result;
    }

}

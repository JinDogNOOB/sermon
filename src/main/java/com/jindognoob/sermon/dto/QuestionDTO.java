package com.jindognoob.sermon.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.jindognoob.sermon.domain.Question;
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
    private Long accountId;
    private QuestionStatusType status;
    private List<AnswerDTO> answers = new ArrayList<AnswerDTO>();


    public static QuestionDTO of(Question question){
        QuestionDTO questionDTO = ModelMapperUtils.getInstance().map(question, QuestionDTO.class);
        
        questionDTO.setAccountId(question.getId());
        questionDTO.setAnswers(AnswerDTO.of(question.getAnswers()));
        return questionDTO;
    }

    // 아마 accountID는 포함안될거임 실제 보고 필요하면 추가 
    public static List<QuestionDTO> of(List<Question> questions){
        return questions.stream()
        .map(questionEntity -> ModelMapperUtils.getInstance().map(questionEntity, QuestionDTO.class))
        .collect(Collectors.toList());
    }

}

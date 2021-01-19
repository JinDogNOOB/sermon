package com.jindognoob.sermon.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.jindognoob.sermon.domain.Account;
import com.jindognoob.sermon.domain.etypes.AccountRoleType;
import com.jindognoob.sermon.domain.etypes.AccountSignupType;
import com.jindognoob.sermon.utils.ModelMapperUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDTO {
    private Long id;

    private String email;
    private String password;
    private AccountSignupType signupType;
    private AccountRoleType roleType;
    private long point;

    /**
     * ModelMapper보다 BeanUtil.copyProperties가 더 빠름
     */
    public static AccountDTO of(Account account){
        // 필드명 동일할시 그냥 map (src, target.class) https://dbbymoon.tistory.com/4 참고
        AccountDTO accountDTO = ModelMapperUtils.getInstance().map(account, AccountDTO.class);
        // 이름이 다른 필드는 직접 Set을 통해 매핑
        
        accountDTO.setPassword(null);
        accountDTO.setPoint(account.getPoint().getAmount());

        return accountDTO;
    }

    public static List<AccountDTO> of(List<Account> accounts){
        return accounts.stream()
        .map(accountEntity -> ModelMapperUtils.getInstance().map(accountEntity, AccountDTO.class))
        .collect(Collectors.toList());
    }

}

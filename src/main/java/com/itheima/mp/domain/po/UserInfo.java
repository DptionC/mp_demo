package com.itheima.mp.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Autor：林建威
 * @DateTime：2024/5/9 10:35
 **/
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class UserInfo {
    private Integer age;
    private String intro;
    private String gender;
}

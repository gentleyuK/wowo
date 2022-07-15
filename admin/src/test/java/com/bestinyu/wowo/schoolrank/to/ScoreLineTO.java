package com.bestinyu.wowo.schoolrank.to;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreLineTO {
    private int year;
    private String scoreLine;
    private String rank;

    public static ScoreLineTO createNull() {
        return new ScoreLineTO(0, "", "");
    }
}

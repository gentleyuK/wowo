package com.bestinyu.wowo.schoolrank.to;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SchoolTO {
    private String name;
    private String rankingRank;
    private String rankingRank2022;
    private String xiaoYouHuiRank;
    private String city;
    private StringBuilder rankInfo2017;
    private StringBuilder rankingRankInfo;

    private List<ScoreLineTO> scoreLines;

    public void addScoreLine(ScoreLineTO to) {
        if (this.scoreLines == null) {
            this.scoreLines = new ArrayList<>();
        }
        this.scoreLines.add(to);
    }

    public ScoreLineTO getScoreLine(int year) {
        if (this.scoreLines == null) {
            return ScoreLineTO.createNull();
        }
        for (ScoreLineTO to : this.scoreLines) {
            if (to.getYear() == year) {
                return to;
            }
        }
        return ScoreLineTO.createNull();
    }

    public void addRankInfo2017(String info) {
        if (this.rankInfo2017 == null) {
            this.rankInfo2017 = new StringBuilder();
        }
        this.rankInfo2017.append(info).append("\r\n");
    }

    public void addRankingRankInfo(String info) {
        if (this.rankingRankInfo == null) {
            this.rankingRankInfo = new StringBuilder();
        }
        this.rankingRankInfo.append(info).append("\r\n");
    }

    public StringBuilder getRankInfo2017() {
        if (this.rankInfo2017 == null) {
            return new StringBuilder();
        }
        return rankInfo2017;
    }

    public StringBuilder getRankingRankInfo() {
        if (this.rankingRankInfo == null) {
            return new StringBuilder();
        }
        return rankingRankInfo;
    }
}

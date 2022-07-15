package com.bestinyu.wowo.schoolrank;

import com.bestinyu.wowo.schoolrank.to.RankTO;
import com.bestinyu.wowo.schoolrank.to.SchoolTO;
import com.bestinyu.wowo.schoolrank.to.ScoreLineTO;
import com.bestinyu.wowo.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SchoolRankTest {

    @org.junit.Test
    public void httpTest() throws IOException {
        // String res = HttpUtils.httpGet("https://www.dxsbb.com/news/70635.html", "GBK");
        // System.out.println(res);
        // subjectRankParse("https://www.dxsbb.com/news/70635.html");
        getRankingSubjectRank("0101");
    }

    private List<RankTO> subjectRankParse(String requestUrl) throws IOException {
        Document doc = Jsoup.connect(requestUrl).get();

        String title = doc.getElementsByTag("h1").first().text();

        Element tableDiv = doc.getElementsByClass("tablebox").first();
        Element tbody = tableDiv.getElementsByTag("tbody").first();

        Elements trs = tbody.children();

        List<RankTO> result = new ArrayList<>();
        for (Element tr : trs) {
            if (tr.hasClass("tablehead")) {
                continue;
            }
            Elements tds = tr.children();
            List<String> texts = tds.eachText();
            result.add(new RankTO(title.substring(2, title.length() - 8), texts.get(2), texts.get(0)));
        }
        return result;
    }

    private void rankingSubjectRankParse(String subCode, String subName, List<SchoolTO> schools) {
        List<String> ranks = getRankingSubjectRank(subCode);
        for (int i = 0; i < ranks.size(); i++) {
            boolean found = false;
            for (SchoolTO school : schools) {
                if (ranks.get(i).equals(school.getName())) {
                    found = true;
                    school.addRankingRankInfo(subName + ":" + (i + 1));
                }
            }

            if (!found) {
                log.error("sub:{}, school:{}, rank:{} not found.", subName, ranks.get(i), i + 1);
            }
        }
    }

    private List<SchoolTO> getRankingSchoolRank() {
        List<String> schools = getRankingRank("https://www.shanghairanking.cn/_nuxt/static/1655530991" +
                "/rankings/bcur/202210/payload.js");
        List<SchoolTO> result = new ArrayList<>();
        for (int i = 0; i < schools.size(); i++) {
            SchoolTO to = new SchoolTO();
            to.setName(schools.get(i));
            to.setRankingRank2022(String.valueOf(i + 1));
            result.add(to);
        }
        return result;
    }

    private List<String> getRankingSubjectRank(String subjectCode) {
        // https://www.shanghairanking.cn/_nuxt/static/1655530991/rankings/bcsr/2021/0101/payload.js
        return getRankingRank("https://www.shanghairanking.cn/_nuxt/static/1655530991" +
                "/rankings/bcsr/2021/" + subjectCode + "/payload.js");
    }

    private List<String> getRankingRank(String requestUrl) {

        String res = HttpUtils.httpGet(requestUrl, "UTF-8");
        res = res.substring(res.indexOf("return ") + "return ".length(), res.lastIndexOf("}"));

        List<String> result = new ArrayList<>();
        for (int i = 0; i < res.length(); ) {
            int nameIndex = res.indexOf("univNameCn", i);
            if (nameIndex <= i) {
                break;
            }
            int fromIndex = res.indexOf("\"", nameIndex);
            int endIndex = res.indexOf("\"", fromIndex + 1);
            i = endIndex;
            result.add(res.substring(fromIndex + 1, endIndex));

        }
        return result;
    }

    @org.junit.Test
    public void test1() throws IOException {
        List<SchoolTO> schools = getRankingSchoolRank();
        // parseSchools("C:/Users/Bestinyu/Desktop/志愿/软科排名.txt", schools);
        // parseSchools2("C:/Users/Bestinyu/Desktop/志愿/校友会排名.txt", schools);
        parseScoreLine(schools, "C:/Users/Bestinyu/Desktop/志愿/2021一本.txt", 2021);
        parseScoreLine(schools, "C:/Users/Bestinyu/Desktop/志愿/2020一本.txt", 2020);
        parseScoreLine(schools, "C:/Users/Bestinyu/Desktop/志愿/2019一本.txt", 2019);

        rankingSubjectRankParse("0101", "哲学", schools);

        rankingSubjectRankParse("0201", "理论经济学", schools);
        rankingSubjectRankParse("0202", "应用经济学", schools);

        rankingSubjectRankParse("0301", "法学", schools);
        rankingSubjectRankParse("0302", "政治学", schools);
        rankingSubjectRankParse("0303", "社会学", schools);
        rankingSubjectRankParse("0304", "民族学", schools);
        rankingSubjectRankParse("0305", "马克思主义理论", schools);

        rankingSubjectRankParse("0401", "教育学", schools);
        rankingSubjectRankParse("0402", "心理学", schools);
        rankingSubjectRankParse("0403", "体育学", schools);

        rankingSubjectRankParse("0501", "中国语言文学", schools);
        rankingSubjectRankParse("0502", "外国语言文学", schools);
        rankingSubjectRankParse("0503", "新闻传播学", schools);

        rankingSubjectRankParse("0601", "考古学", schools);
        rankingSubjectRankParse("0602", "中国史", schools);
        rankingSubjectRankParse("0603", "世界史", schools);

        rankingSubjectRankParse("1201", "管理科学与工程", schools);
        rankingSubjectRankParse("1202", "工商管理", schools);
        rankingSubjectRankParse("1203", "农林经济管理", schools);
        rankingSubjectRankParse("1204", "公共管理", schools);
        rankingSubjectRankParse("1205", "图书情报与档案管理", schools);

        // parseSubjectRank("https://www.dxsbb.com/news/70634.html", schools);
        // parseSubjectRank("https://www.dxsbb.com/news/70635.html", schools);
        // parseSubjectRank("https://www.dxsbb.com/news/70636.html", schools);
        // parseSubjectRank("https://www.dxsbb.com/news/70638.html", schools);
        // parseSubjectRank("https://www.dxsbb.com/news/70639.html", schools);
        // parseSubjectRank("https://www.dxsbb.com/news/70637.html", schools);
        // parseSubjectRank("https://www.dxsbb.com/news/70626.html", schools);
        // parseSubjectRank("https://www.dxsbb.com/news/70627.html", schools);
        // parseSubjectRank("https://www.dxsbb.com/news/70628.html", schools);
        // parseSubjectRank("https://www.dxsbb.com/news/70629.html", schools);
        // parseSubjectRank("https://www.dxsbb.com/news/70630.html", schools);
        // parseSubjectRank("https://www.dxsbb.com/news/70633.html", schools);
        // parseSubjectRank("https://www.dxsbb.com/news/70632.html", schools);
        // parseSubjectRank("https://www.dxsbb.com/news/70631.html", schools);
        // parseSubjectRank("", schools);
        // parseSubjectRank("", schools);
        // parseSubjectRank("", schools);
        // parseSubjectRank("", schools);
        // parseSubjectRank("", schools);

        writeExcel("C:/Users/Bestinyu/Desktop/志愿/软科排名_" + System.currentTimeMillis() + ".xlsx", schools);
    }

    private void parseSubjectRank(String url, List<SchoolTO> schools) throws IOException {
        List<RankTO> rankTOS = subjectRankParse(url);
        for (RankTO to : rankTOS) {
            boolean found = false;
            for (SchoolTO school : schools) {
                if (to.getSchool().equals(school.getName())) {
                    found = true;
                    school.addRankInfo2017(to.getRankName() + ":" + to.getRank());
                }
            }
            if (!found) {
                log.error("rankName:{}, rank:{}, school:{}", to.getRankName(), to.getRank(), to.getSchool());
            }
        }
    }

    private void writeExcel(String file, List<SchoolTO> schools) throws IOException {
        String[] head = new String[]{
                "学校",
                "2022软科大学排名",
                // "软科大学排名",
                // "校友会排名",
                // "省市",
                // "2017年学科排名",
                "2021年软科学科排名",
                "2021投档线", "2021投档排名",
                "2020投档线", "2020投档排名",
                "2019投档线", "2019投档排名"
        };
        writeExcel(file, head, schools);
    }

    private void writeExcel(String file, String[] headData, List<SchoolTO> schools) throws IOException {
        File excel = new File(file);
        if (!excel.exists() && !excel.createNewFile()) {
            log.error("file:{} create failed.", file);
            return;
        }

        try (Workbook wb = new XSSFWorkbook();
             OutputStream os = new FileOutputStream(file)) {
            Sheet sheet = wb.createSheet("Sheet1");

            int i = 0;
            Row head = sheet.createRow(i++);
            for (int j = 0; j < headData.length; j++) {
                Cell cell = head.createCell(j, CellType.STRING);
                cell.setCellValue(headData[j]);
            }

            XSSFCellStyle cellStyle = ((XSSFWorkbook) wb).createCellStyle();
            cellStyle.setWrapText(true);

            for (SchoolTO school : schools) {
                Row row = sheet.createRow(i++);
                int j = 0;
                row.createCell(j++, CellType.STRING).setCellValue(school.getName());
                row.createCell(j++, CellType.STRING).setCellValue(school.getRankingRank2022());
                // row.createCell(j++, CellType.STRING).setCellValue(school.getXiaoYouHuiRank());
                // row.createCell(j++, CellType.STRING).setCellValue(school.getCity());

                // Cell cell = row.createCell(j++, CellType.STRING);
                // cell.setCellValue(new XSSFRichTextString(school.getRankInfo2017().toString()));
                // cell.setCellStyle(cellStyle);

                Cell cell2 = row.createCell(j++, CellType.STRING);
                cell2.setCellValue(new XSSFRichTextString(school.getRankingRankInfo().toString()));
                cell2.setCellStyle(cellStyle);

                ScoreLineTO scoreLine2021 = school.getScoreLine(2021);
                row.createCell(j++, CellType.STRING).setCellValue(scoreLine2021.getScoreLine());
                row.createCell(j++, CellType.STRING).setCellValue(scoreLine2021.getRank());

                ScoreLineTO scoreLine2020 = school.getScoreLine(2020);
                row.createCell(j++, CellType.STRING).setCellValue(scoreLine2020.getScoreLine());
                row.createCell(j++, CellType.STRING).setCellValue(scoreLine2020.getRank());

                ScoreLineTO scoreLine2019 = school.getScoreLine(2019);
                row.createCell(j++, CellType.STRING).setCellValue(scoreLine2019.getScoreLine());
                row.createCell(j++, CellType.STRING).setCellValue(scoreLine2019.getRank());
            }
            sheet.setColumnWidth(2, 30 * 256);
            // sheet.setColumnWidth(5, 40 * 256);
            wb.write(os);
        }
    }

    private void writeTxt(String file, String[] headData, String[][] data) throws IOException {
        File excel = new File(file);
        if (!excel.exists() && !excel.createNewFile()) {
            log.error("file:{} create failed.", file);
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println(toLine(headData));
            for (String[] row : data) {
                pw.println(toLine(row));
            }
        }
    }

    private String toLine(String[] lineData) {
        StringBuilder sb = new StringBuilder();
        for (String s : lineData) {
            sb.append(s);
        }
        return sb.toString();
    }

    private List<SchoolTO> parseSchools(String file, List<SchoolTO> schools) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // 跳过第一行
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] info = line.split("[\\s]+");
                String name = info[1];

                boolean found = false;
                for (SchoolTO to : schools) {
                    if (to.getName().equals(name)) {
                        found = true;
                        to.setRankingRank(info[0]);
                    }
                }

                if (!found) {
                    SchoolTO to = new SchoolTO();
                    to.setName(info[1]);
                    to.setRankingRank(info[0]);
                    to.setCity(info[2]);
                    schools.add(to);
                }
            }
        }
        return schools;
    }

    private List<SchoolTO> parseSchools2(String file, List<SchoolTO> schools) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String rank = line;
                line = br.readLine();
                String name = br.readLine();
                line = br.readLine();
                String score = br.readLine();
                line = br.readLine();
                String star = br.readLine();
                line = br.readLine();
                line = br.readLine();
                line = br.readLine();

                boolean found = false;
                for (SchoolTO to : schools) {
                    if (to.getName().equals(name)) {
                        found = true;
                        to.setXiaoYouHuiRank(rank);
                    }
                }

                if (!found) {
                    SchoolTO to = new SchoolTO();
                    to.setName(name);
                    to.setXiaoYouHuiRank(rank);
                    to.setCity("");
                    schools.add(to);
                }
            }
        }
        return schools;
    }

    private void parseScoreLine(List<SchoolTO> schools, String file, int year) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // 跳过第一行
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] info = line.split("[\\s]+");
                setScoreLine(schools, info[3], year, info[4], info[5]);
            }
        }
    }

    private void setScoreLine(List<SchoolTO> schools, String school, int year, String scoreLine, String rank) {
        boolean found = false;
        for (SchoolTO to : schools) {
            if (to.getName().equals(school) ||
                    ((school.endsWith("▲") || school.endsWith("★"))
                            && to.getName().equals(school.substring(0, school.length() - 1)))) {
                found = true;
                to.addScoreLine(new ScoreLineTO(year, scoreLine, rank));
            }
        }

        if (!found) {
            log.error("year:{}, school:{}, scoreLine:{}, rank:{}, not found.", year, school, scoreLine, rank);
        }
    }
}

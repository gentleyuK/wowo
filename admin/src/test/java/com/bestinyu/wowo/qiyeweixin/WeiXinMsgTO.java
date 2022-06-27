package com.bestinyu.wowo.qiyeweixin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class WeiXinMsgTO {
    private String touser;
    private String toparty;
    private String totag;
    private String msgtype = "text";
    private int agentid;
    private Text text;
    private int safe = 0;
    private int enable_id_trans = 0;
    private int enable_duplicate_check = 0;
    private int duplicate_check_interval = 1800;

    @Data
    class Text {
        private String content;

        public Text() {
        }

        public Text(String content) {
            this.content = content;
        }
    }

    @JsonIgnore
    public void setTextContent(String content) {
        this.text = new Text(content);
    }

}

package cn.easy.xinjing.bean.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzhongyi on 2016/10/14.
 */
public class LetterToArrayBean {
    private String letter;
    private List<?> array;

    public static List<LetterToArrayBean> valueOf(Map<String, List> mapList) {
        List<LetterToArrayBean> result = new ArrayList<>();
        for (Map.Entry<String, List> entry : mapList.entrySet()) {
            result.add(new LetterToArrayBean(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public LetterToArrayBean(String letter, List<?> array) {
        this.letter = letter;
        this.array = array;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public List<?> getArray() {
        return array;
    }

    public void setArray(List<?> array) {
        this.array = array;
    }
}

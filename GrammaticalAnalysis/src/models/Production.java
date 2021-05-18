package models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/16
 */
public class Production {
    private static int ID=0;
    private int id;
    String leftPart = "";
    List<String> rightParts = new ArrayList<>();

    public Production() {
        this.id = ID++;
    }

    public Production(String s) {
        this.id = ID++;
        separate(s);
    }

    private void separate(String s) {
        s = s.replace(" ", "");
        boolean left = true;
        int i = 0;
        while (i < s.length()) {
            boolean special = true;
            if (s.charAt(i) == '-' && s.charAt(i + 1) == '>') {
                i += 2;
                left = false;
                continue;
            }
            StringBuilder indexString = new StringBuilder();
//            while (s.charAt(i) >= 'a' && s.charAt(i) <= 'z' || s.charAt(i) >= 'A' && s.charAt(i) <= 'Z') {
//                special = false;
//                indexString.append(s.charAt(i));
//                i++;
//                if (i == s.length()) {
//                    break;
//                }
//            }
//            while (s.charAt(i) >= 'a' && s.charAt(i) <= 'z') {
//                special = false;
//                indexString.append(s.charAt(i));
//                i++;
//                if (i == s.length()) {
//                    break;
//                }
//            }
//            if (special) {
//                if (s.charAt(i) == ' ') {
//                    i++;
//                    continue;
//                }else {
//                    indexString.append(s.charAt(i));
//                    i++;
//                }
//            }
            if (s.charAt(i) == ' ') {
                i++;
                continue;
            }else {
                indexString.append(s.charAt(i));
                i++;
            }
            if (left) {
                leftPart = indexString.toString();
            } else {
                rightParts.add(indexString.toString());
            }
        }
    }

    public static int getID() {
        return ID;
    }

    public int getId() {
        return id;
    }

    public String getLeftPart() {
        return leftPart;
    }

    public List<String> getRightParts() {
        return rightParts;
    }

    @Override
    public String toString() {
        return "{左部 = " + leftPart + ", 右部 = " + rightParts + "}\n";
    }
}

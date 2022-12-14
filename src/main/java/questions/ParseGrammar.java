package questions;

import java.util.LinkedList;

public class ParseGrammar {

    private static char[] leftBrackets = {'[', '{', '('};
    private static char[] rightBrackets = {']', '}', ')'};
    private static char[] operator = {'&', '|', '^', '+', '-', '*', '/'};
    private static int MAX_OPERATION_LEVEL = 3;

    public static int parseMath(String sentence) {
        char[] chars = sentence.toCharArray();

        int level = 1;
        int maxLevel = level;
        Expression expression = new Expression();
        for (int i=0; i<chars.length; i++) {
            char c = chars[i];
            if (isLeftBracket(c)) {
                maxLevel++;
                level++;
                continue;
            }
            if (isRightBracket(c)) {
                level--;
                continue;
            }
            if (isNum(c)) {
                LevelAndNum levelAndNum = getNum(chars, i);
                i = levelAndNum.level - 1;
                expression.numList.add(new LevelAndNum(level, levelAndNum.num));
                continue;
            }
            if (isOperation(c)) {
                expression.operationList.add(c);
            }
        }
        return calculateExpression(expression, maxLevel);
    }

    private static int calculateExpression(Expression expression, int maxLevel) {
        LinkedList<LevelAndNum> numList = expression.numList;
        for (int level = maxLevel; level >= 1 ; level--) {
            int left = -1;
            for (int i = 0; i < numList.size(); i++) {
                LevelAndNum levelAndNum = numList.get(i);
                if (left == -1 && levelAndNum.level == level) {
                    left = i;
                    continue;
                }
                if (left > -1 && (levelAndNum.level != level || i == numList.size() - 1)) {
                    if (i == numList.size() - 1 && numList.getLast().level == level) {
                        i++;
                    }
                    calculateSameLevel(left, i - 1, expression);
                    numList.set(left, new LevelAndNum(level-1, numList.get(left).num));
                    left = -1;
                }
            }
        }
        return numList.get(0).num;
    }

    private static void calculateSameLevel(int left, int right, Expression expression) {
        LinkedList<Character> operationList = expression.operationList;
        LinkedList<LevelAndNum> numList = expression.numList;
        for (int k = MAX_OPERATION_LEVEL; k > 0; k--) {
            int i = left;
            while (i < right) {
                char operation = operationList.get(i);
                if (getOperationLevel(operation) == k) {
                    operationList.remove(i);
                    LevelAndNum levelAndNum = new LevelAndNum(numList.get(i).level, operate(numList.get(i).num, numList.get(i + 1).num, operation));
                    numList.set(i, levelAndNum);
                    numList.remove(i + 1);
                    right--;
                    continue;
                }
                i++;
            }
        }
    }

    private static int operate(int left, int right, char operation) {
        switch (operation) {
            case '^': return powerOperation(left, right);
            case '*': return left * right;
            case '/': return left / right;
            case '&': return left & right;
            case '|': return left | right;
            case '+': return left + right;
            case '-': return left - right;
        }
        return 0;
    }

    private static int powerOperation(int bottom, int index) {
        int result = 1;
        for (int i=0; i<index; i++) {
            result = result * bottom;
        }
        return result;
    }

    private static class Expression {
        LinkedList<LevelAndNum> numList = new LinkedList<>();
        LinkedList<Character> operationList = new LinkedList<>();
    }

    private static class LevelAndNum {
        private int level;
        private int num;
        public LevelAndNum(int level, int num) {
            this.level = level;
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public int getLevel() {
            return level;
        }
    }

    private static boolean isLeftBracket(char c) {
        return isIn(leftBrackets, c);
    }

    private static boolean isRightBracket(char c) {
        return isIn(rightBrackets, c);
    }

    private static int getOperationLevel(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '&':
            case '|':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }

    private static boolean isIn(char[] chars, char c) {
        for (char c1 : chars) {
            if (c1 == c) {
                return true;
            }
        }
        return false;
    }

    private static boolean isOperation(char c) {
        return isIn(operator, c);
    }

    private static boolean isNum(char c) {
        return c >= '0' && c <= '9';
    }

    private static LevelAndNum getNum(char[] chars, int index) {
        int result = 0;
        while (index < chars.length && isNum(chars[index])) {
            result = appendNum(result, chars[index]);
            index++;
        }
        return new LevelAndNum(index, result);
    }

    private static int appendNum(int num, char c) {
         return num * 10 + parseNum(c);
    }

    private static int parseNum(char c) {
        return c - '0';
    }

}

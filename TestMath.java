import java.util.ArrayList;
import java.util.List;

class Parser{
    private String expression;

    public Parser(String expr){
        this.expression = expr;
        System.out.println(split(expr, "+-")[0]);
        System.out.println(split(expr, "+-")[1]);
    }
    public int eval(){
        return eval(expression);
    }

    public boolean first(String expr, String delim){
        int p=0;
        for(int i=0; i<expr.length(); i++){
            if(expr.charAt(i) == '(')
                p++;
            else if(expr.charAt(i) == ')')
                p--;
            boolean isDelim = false;
            for(int j=0; j<delim.length(); j++){
                if(expr.charAt(i) == delim.charAt(j))
                    isDelim = true;
            }
            if(isDelim && p==0)
                return true;
        }
        return false;
    }

    public int eval(String expr){
        expr = strip(expr);
        if(first(expr, "+-")){
            List<String>[] r = split(expr, "+-");
            int s = eval(r[0].get(0));
            for(int i=0; i<r[1].size(); i++){
                switch(r[1].get(i)){
                    case "+":
                        s += eval(r[0].get(i+1));
                        break;
                    case "-":
                        s -= eval(r[0].get(i+1));
                        break;
                }
            }
            return s;
        }else if(first(expr, "*/")){
            List<String>[] r = split(expr, "/*");
            int l0 = r[0].size();
            int l1 = r[1].size();

            int n = expr.length() - 1;
            while(!expr.substring(n, n+1).equals(r[1].get(l1-1)))
                n--;

            switch(r[1].get(l1-1)){
                case "*":
                    return eval(expr.substring(0, n)) * eval(r[0].get(l0-1));
                case "/":
                    return eval(expr.substring(0, n)) / eval(r[0].get(l0-1));
            }
        }else{
            return Integer.parseInt(expr);
        }
        return 0;
    }

    public int getClose(String expr, int i){
        i = i+1;
        int p=1;
        while(i < expr.length()){
            if(expr.charAt(i) == '(')
                p++;
            else if(expr.charAt(i) == ')')
                p--;
            if(p == 0)
                return i;
            i++;
        }
        return -1;
    }

    public String strip(String expr){
        int i=0;
        while(i < expr.length()){
            if(getClose(expr, i) == expr.length() - 1 - i)
                i++;
            else
                return expr.substring(i, expr.length()-i);
        }
        return null;
    }

    public List<String>[] split(String expr, String delim){
        List<String>[] r = new ArrayList[2];
        r[0] = new ArrayList<String>();
        r[1] = new ArrayList<String>();
        int n=0;
        int p=0;
        for(int i=0; i<expr.length(); i++){
            if(expr.charAt(i) == '(')
                p++;
            else if(expr.charAt(i) == ')')
                p--;

            boolean isdelim = false;
            for(int j=0; j<delim.length(); j++){
                if(expr.charAt(i) == delim.charAt(j))
                    isdelim = true;
            }

            if(isdelim && p==0){
                r[0].add(strip(expr.substring(n, i)));
                r[1].add(expr.substring(i, i+1));
                n=i+1;
            }
        }
        r[0].add(expr.substring(n));
        return r;
    }

    public int getClose(int i){
        if(expression.charAt(i) != '(')
            return -1;
        int n=1;
        i++;
        while(n!=0 && i < expression.length()){
            if(expression.charAt(i) == '(')
                n++;
            else if(expression.charAt(i) == ')')
                n--;
        }
        return -1;
    }
}
public class TestMath {
    public static void main(String[] args){
        String expr = "((5+3)/2-6*3)+1";
        Parser p =new Parser(expr);
        System.out.println(p.eval());
    }
}

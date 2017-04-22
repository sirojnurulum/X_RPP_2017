
import java.util.Stack;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ABC
 */
public class NewClass {

    public static void main(String[] args) {
        Stack<Stack<String>> a = new Stack<>();
        Stack<String> b = new Stack<>();
        Stack<String> c = new Stack<>();
        for (int i = 0; i < 5; i++) {
            b.push(String.valueOf(i));
            c.push(String.valueOf(i));
        }
        a.push(c);
        a.push(b);

        while (!a.isEmpty()) {
            if (a.peek().isEmpty()) {
                a.pop();
            } else {
                System.out.println(a.peek().size());
                a.peek().pop();
            }
        }
    }

}

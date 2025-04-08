package src.main.java;

import src.main.java.asm.AddSubMechanism;
import static src.main.java.asm.AddSubMechanism.addSub;

public class Main {
    public static void main(String[] args) {

        System.out.println("Addition");
        AddSubMechanism.Result function1 = addSub(false, false, false, false);
        AddSubMechanism.Result function2 = addSub(false, false, true, false);
        AddSubMechanism.Result function3 = addSub(false, true, false, false);
        AddSubMechanism.Result function4 = addSub(false, true, true, false);
        AddSubMechanism.Result function5 = addSub(true, false, false, false);
        AddSubMechanism.Result function6 = addSub(true, false, true, false);
        AddSubMechanism.Result function7 = addSub(true, true, false, false);
        AddSubMechanism.Result function8 = addSub(true, true, true, false);
        System.out.println(function1);
        System.out.println(function2);
        System.out.println(function3);
        System.out.println(function4);
        System.out.println(function5);
        System.out.println(function6);
        System.out.println(function7);
        System.out.println(function8);

        System.out.println("Subtraction");
        function1 = addSub(false, false, false, true);
        function2 = addSub(false, false, true, true);
        function3 = addSub(false, true, false, true);
        function4 = addSub(false, true, true, true);
        function5 = addSub(true, false, false, true);
        function6 = addSub(true, false, true, true);
        function7 = addSub(true, true, false, true);
        function8 = addSub(true, true, true, true);

        System.out.println(function1);
        System.out.println(function2);
        System.out.println(function3);
        System.out.println(function4);
        System.out.println(function5);
        System.out.println(function6);
        System.out.println(function7);
        System.out.println(function8);
    }
}
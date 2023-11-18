import kt2java.Utils;
import org.junit.jupiter.api.Assertions;
import ru.otus.otuskotlin.m1l7.kt2java.InteroperabilityJava;
import org.junit.jupiter.api.Test;
import ru.otus.otuskotlin.m1l7.kt2java.MyClass;

public class Ex5InteroperabilityJavaTest {

    /**
     * Демонстрация доступа к компаньонам
     */
    @Test
    void companionMethods() {
        // Companion
        InteroperabilityJava.Companion.functionOne();
        InteroperabilityJava.functionOne();
    }

    @Test
    void companionMethod2() {
        InteroperabilityJava.functionOne();
    }

    /**
     * Работа с методами с дефолтными аргументами.
     * Требуется @JvmOverloads
     */
    @Test
    void defaultArguments() {
        System.out.println(
                new InteroperabilityJava().defaults()
        );
        System.out.println(new InteroperabilityJava().defaults("p1"));
        System.out.println(
                new InteroperabilityJava().defaults("123", 123, true)
        );
    }

    /**
     * Явное указание наименования в Java через @JvmName
     */
    @Test
    void jvmName() {
        System.out.println(new InteroperabilityJava().customName());
    }

    /**
     * Доступ к глобальным функциям (которые вне классов)
     * Обратите внимание!!!
     * В котлине это два файла.
     * Но за счет JvmMultifileClass и @file:JvmName мы получаем к ним доступ через Utils
     */
    @Test
    void globalFunctions() {
        Assertions.assertEquals("date", Utils.getDate());
        Assertions.assertEquals("name", Utils.getName());
    }

    @Test
    void properties() {
        // Дефлотные значения
        MyClass mc = new MyClass();
        Assertions.assertEquals("a-prop", mc.a); // доступ только по backing field
        Assertions.assertEquals("b-prop", mc.getB()); // доступ только по геттеру
        Assertions.assertEquals("c-prop", mc.getC()); // доступ только по геттеру


        // Кастомные значения
        MyClass mcc = new MyClass("a-field", "b-field", "c-field");
        // доступ по сеттеру
        mcc.setC("c-changed");

        Assertions.assertEquals("a-field", mcc.a); // доступ только по backing field
        Assertions.assertEquals("b-field", mcc.getB()); // доступ только по геттеру
        Assertions.assertEquals("c-changed", mcc.getC()); // доступ только по геттеру
    }
}

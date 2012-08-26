package cz.nocach.masaryk.objectg;

import org.junit.Test;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class ApiExamplesTest {
    @Test
    public void classWithInt(){

    }

    public static class ClassWithInt{
        private int integer1;
        private Integer integer2;

        public int getInteger1() {
            return integer1;
        }

        public void setInteger1(int integer1) {
            this.integer1 = integer1;
        }

        public Integer getInteger2() {
            return integer2;
        }

        public void setInteger2(Integer integer2) {
            this.integer2 = integer2;
        }
    }
}

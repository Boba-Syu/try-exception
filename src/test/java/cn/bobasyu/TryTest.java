package cn.bobasyu;

import cn.bobasyu.te.Try;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;

public class TryTest {

    @Test
    public void test1() {
        Try<StringBuffer> _try = Try.failable(() -> {
            FileReader fileReader = new FileReader("D:\\data\\新建文本文档.txt");
            StringBuffer stringBuffer = new StringBuffer();
            int c = fileReader.read();
            while (c != -1) {
                stringBuffer.append((char) c);
                c = fileReader.read();
            }
            fileReader.close();
            return stringBuffer;
        });
        System.out.println(_try.get());
    }

    @Test
    public void testMap() {
        Try<Integer> _try = Try.failable(() -> 1);
        Try<Integer> try2 = _try.flatMap(x -> _try.map(y -> x + y));
        Assert.assertEquals(2, try2.get().intValue());
    }
}

package operations.test

import com.developmentontheedge.be5.operation.OperationStatus
import com.developmentontheedge.be5.test.SqlMockOperationTest
import com.developmentontheedge.beans.json.JsonFactory
import org.junit.Ignore
import org.junit.Test

import static org.junit.Assert.assertEquals

class TestOperation extends SqlMockOperationTest
{
    @Test
    @Ignore
    void test()
    {
        Object first = generateOperation("testtable", "Test 1D", "TestOperation", "").getFirst()

        assertEquals("{" +
                "'values':{'name':'','beginDate':''}," +
                "'meta':{'/name':{'displayName':'Имя'},'/beginDate':{'displayName':'Дата начала','type':'Date'}}," +
                "'order':['/name','/beginDate']" +
            "}", oneQuotes(JsonFactory.bean(first)))

        def second = executeOperation("testtable", "Test 1D", "TestOperation", "", [name:"test", beginDate: '2017-12-20']).getSecond()

        assertEquals OperationStatus.FINISHED, second.getStatus()
        assertEquals "test message", second.getMessage()
    }

}

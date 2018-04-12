package operations.publications

import com.developmentontheedge.be5.test.SqlMockOperationTest
import com.developmentontheedge.beans.json.JsonFactory
import org.junit.Test

import static org.junit.Assert.assertEquals


class InsertPublication extends SqlMockOperationTest
{
    @Test
    void generate()
    {
        Object first = generateOperation("publications", "Compact view", "InsertPublication", "").getFirst()

        assertEquals("{'values':{'inputType':'PubMed','PMID':'','categoryID':''},'meta':{'/inputType':{'displayName':'Ввод','reloadOnChange':true,'tagList':[['PubMed','PubMed'],['Вручную','manually']]},'/PMID':{'displayName':'PMID','type':'Long','validationRules':[{'attr':{'max':'9223372036854775807','min':'0'},'type':'range'},{'attr':'1','type':'step'}]},'/categoryID':{'displayName':'Category','tagList':[]}},'order':['/inputType','/PMID','/categoryID']}",
                oneQuotes(JsonFactory.bean(first)))
    }

}

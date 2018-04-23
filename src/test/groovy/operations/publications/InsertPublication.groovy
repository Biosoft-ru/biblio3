package operations.publications

import com.developmentontheedge.be5.test.SqlMockOperationTest
import com.developmentontheedge.beans.json.JsonFactory
import org.junit.Test
import ru.biosoft.biblio.BiblioUtils

import static org.junit.Assert.assertEquals


class InsertPublication extends SqlMockOperationTest
{
    @Test
    void generate()
    {
        setSession(BiblioUtils.BIOSTORE_PROJECTS, Collections.singletonList("Demo"))

        Object first = generateOperation("publications", "Compact view", "Insert", "").getFirst()

        assertEquals("{'values':{'inputType':'PubMed','PMID':'','categoryID':''},'meta':{'/inputType':{'displayName':'Ввод','reloadOnChange':true,'tagList':[['PubMed','PubMed'],['manually','Вручную']]},'/PMID':{'displayName':'PMID','type':'Long','reloadOnChange':true,'validationRules':[{'attr':{'max':'9223372036854775807','min':'0'},'type':'range'},{'attr':'1','type':'step'}]},'/categoryID':{'displayName':'Category','reloadOnChange':true,'groupId':'2','groupName':'Категория','tagList':[]}},'order':['/inputType','/PMID','/categoryID']}",
                oneQuotes(JsonFactory.bean(first)))
    }

}

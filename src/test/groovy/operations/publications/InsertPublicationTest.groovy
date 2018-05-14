package operations.publications

import com.developmentontheedge.beans.json.JsonFactory
import org.junit.Test
import ru.biosoft.biblio.Biblio3Test
import ru.biosoft.biblio.util.BiblioUtils

import static org.junit.Assert.assertEquals


class InsertPublicationTest extends Biblio3Test
{
    @Test
    void generate()
    {
        setSession(BiblioUtils.BIOSTORE_PROJECTS, Collections.singletonList("Demo"))

        Object first = generateOperation("publications", "Compact view", "Insert", "").getFirst()

        assertEquals("{'values':{'inputType':'PubMed','PMID':'','categoryID':''},'meta':{'/inputType':{'displayName':'Ввод','reloadOnChange':true,'cssClasses':'col-lg-4','tagList':[['PubMed','PubMed'],['manually','Вручную']]},'/PMID':{'displayName':'PMID','type':'Long','reloadOnChange':true,'validationRules':[{'attr':{'max':'9223372036854775807','min':'0'},'type':'range'},{'attr':'1','type':'step'}]},'/categoryID':{'displayName':'Category','type':'Long','reloadOnChange':true,'groupId':'2','groupName':'Категория','tagList':[]}},'order':['/inputType','/PMID','/categoryID']}",
                oneQuotes(JsonFactory.bean(first)))
    }

}

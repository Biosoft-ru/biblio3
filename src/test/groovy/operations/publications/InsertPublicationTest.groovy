package operations.publications

import com.developmentontheedge.beans.json.JsonFactory
import org.junit.Before
import org.junit.Test
import ru.biosoft.biblio.Biblio3Test
import ru.biosoft.biblio.util.BioStore

import static org.junit.Assert.assertEquals


class InsertPublicationTest extends Biblio3Test
{
    @Before
    void setUp()
    {
        initUserWithRoles("Annotator")
    }

    @Test
    void generate()
    {
        setSession(BioStore.BIOSTORE_PROJECTS, Collections.singletonList("Demo"))

        Object first = generateOperation("publications", "Compact view", "Insert", "").getFirst()

        assertEquals("{'values':{'inputType':'PubMed','PMID':'','categoryID':''},'meta':{'/inputType':{'displayName':'Input type','reloadOnChange':true,'cssClasses':'col-lg-4','tagList':[['PubMed','PubMed'],['manually','Вручную']]},'/PMID':{'displayName':'PMID','type':'Long','reloadOnChange':true,'validationRules':[{'attr':{'max':'9223372036854775807','min':'0'},'type':'range'},{'attr':'1','type':'step'}]},'/categoryID':{'displayName':'Category','type':'Long','reloadOnChange':true,'groupId':'2','groupName':'Category','tagList':[]}},'order':['/inputType','/PMID','/categoryID']}",
                oneQuotes(JsonFactory.bean(first)))
    }

}

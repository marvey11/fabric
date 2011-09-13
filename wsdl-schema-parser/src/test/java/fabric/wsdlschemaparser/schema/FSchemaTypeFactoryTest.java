package fabric.wsdlschemaparser.schema;

import org.apache.log4j.*;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.xb.xsdschema.Facet;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType;
import org.junit.Test;
import java.io.File;

import static org.junit.Assert.*;

public class FSchemaTypeFactoryTest {
    static{
        PatternLayout patternLayout = new PatternLayout(
                "%-13d{HH:mm:ss,SSS} | %-20.20C{3} | %-5p | %m%n");
        final Appender appender = new ConsoleAppender(patternLayout);
        Logger.getRootLogger().removeAllAppenders();
        Logger.getRootLogger().addAppender(appender);
        Logger.getRootLogger().setLevel(Level.INFO);
    }

    @Test
    public void testDefaultAndFixedValue() throws Exception {
        File file = new File("src/test/resources/schemas/defaultFixedValues.xsd");
        FSchema schema = new FSchema(file);
        FTopLevelObjectList objectList = schema.getTopLevelObjectList();
        FElement otherColor         = objectList.getTopLevelElement("OtherColor");
        FElement foregroundColor    = objectList.getTopLevelElement("ForegroundColor");
        FElement backgroundColor    = objectList.getTopLevelElement("BackgroundColor");

        /*
        Tests
         */
        assertNull("Default value of OtherColor must be null.",
                otherColor.getDefaultValue());
        assertNull("Fixed value of OtherColor must be null.",
                otherColor.getFixedValue());
        assertEquals("Default value of ForegroundColor must be \"red\".",
                "red", foregroundColor.getDefaultValue());
        assertNull("Fixed value of ForegroundColor must be null.",
                foregroundColor.getFixedValue());
        assertNull("Default value of BackgroundColor must be null.",
                backgroundColor.getDefaultValue());
        assertEquals("Fixed value of ForegroundColor must be \"white\".",
                "white", backgroundColor.getFixedValue());
    }

    @Test
    public void testList() throws Exception {
        File file = new File("src/test/resources/schemas/list.xsd");
        FSchema schema = new FSchema(file);
        FTopLevelObjectList objectList = schema.getTopLevelObjectList();
        FSchemaType intList    = objectList.getTopLevelElement("IntList").getSchemaType();
        FSchemaType intValue   = objectList.getTopLevelElement("IntValue").getSchemaType();
        FSchemaType intList2   = objectList.getTopLevelElement("AnotherIntList").getSchemaType();
        FSchemaType restrictedList
                = objectList.getTopLevelElement("IntListWithRestriction").getSchemaType();

        /*
        Tests
         */
        assertTrue("IntList has to be a xs:list of type xs:integer.",
                intList.isSimple()
                && ((FSimpleType) intList).isList()
                && ((FList)intList).getItemType() instanceof FInteger);
        assertTrue("IntValue has to be a single value of type xs:integer.",
                intValue.isSimple()
                && !((FSimpleType) intValue).isList()
                && intValue instanceof FInteger);
        assertTrue("AnotherIntList has to be a xs:list of type xs:integer.",
                intList2.isSimple()
                && ((FSimpleType) intList2).isList()
                && ((FList)intList2).getItemType() instanceof FInteger);
        assertTrue("IntListWithRestriction has to be a xs:list of type IntListType.",
                restrictedList.isSimple()
                && ((FSimpleType) restrictedList).isList()
                && ((FList)restrictedList).getItemType() instanceof FInteger);
        assertTrue("Length of IntListWithRestriction has to be restricted to 6.",
                ((FSimpleType) restrictedList).getRestrictions()
                        .getIntegerValue(SchemaType.FACET_LENGTH) == 6);
    }
}
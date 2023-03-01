package com.van.processor.common;

import org.junit.jupiter.api.Test;
import static com.van.processor.common.Utils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {

    @Test
    public void getLastWord_ok() {
        assertEquals("test", getLastWord("wee.ere.test"));
    }

    @Test
    public void getClassOfGeneric_ok() {
        assertEquals("com.van.testService.model.House", getClassOfGeneric("java.util.List<com.van.testService.model.House>"));
    }

    @Test
    public void getCollectioType_ok() {
        assertEquals("List", getCollectioType("java.util.List<com.van.testService.model.House>"));
    }

    @Test
    public void getCollectioType_object_ok() {
        assertEquals(TYPE_OBJECT, getCollectioType("can.van.Come"));
    }

}

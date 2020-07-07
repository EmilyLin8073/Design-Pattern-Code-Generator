import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestCases {
    //Instantiate for all the classes that needed in the test cases
    //Instantiate DePaCoGeToolWindow class
    private static DePaCoGeToolWindow dePaCoGeToolWindow;

    //Instantiate MyToolWindowFactory class
    private static MyToolWindowFactory myToolWindowFactory;

    //Instantiate all the design pattern's dialog class
    private static AFPatternDialog afPatternDialog;
    private static BuilderPatternDialog builderPatternDialog;
    private static FacadePatternDialog facadePatternDialog;
    private static FactoryPatternDialog factoryPatternDialog;
    private static CORPatternDialog corPatternDialog;
    private static MediatorPatternDialog mediatorPatternDialog;
    private static TemplatePatternDialog templatePatternDialog;
    private static VisitorPatternDialog visitorPatternDialog;

    //Instantiate all the design pattern's generator class
    private static AFPatternGenerator afPatternGenerator;
    private static BuilderPatternGenerator builderPatternGenerator;
    private static FacadePatternGenerator facadePatternGenerator;
    private static FactoryPatternGenerator factoryPatternGenerator;
    private static CORPatternGenerator corPatternGenerator;
    private static MediatorPatternGenerator mediatorPatternGenerator;
    private static TemplatePatternGenerator templatePatternGenerator;
    private static VisitorPatternGenerator visitorPatternGenerator;

    //Instantiate the ProjectFileStorage class
    private static ProjectFileStorage projectFileStorage;


    @BeforeAll
    public static void init(){
        //Initialize the file path
        String path = "/src";

        //Mock the DePaCoGeToolWindow class
        dePaCoGeToolWindow = mock(DePaCoGeToolWindow.class);

        //Mock the MyToolWindowFactory class
        myToolWindowFactory = mock(MyToolWindowFactory.class);

        //Mock all the design pattern's dialog class
        afPatternDialog = mock(AFPatternDialog.class);
        builderPatternDialog = mock(BuilderPatternDialog.class);
        facadePatternDialog = mock(FacadePatternDialog.class);
        factoryPatternDialog = mock(FactoryPatternDialog.class);
        corPatternDialog = mock(CORPatternDialog.class);
        mediatorPatternDialog = mock(MediatorPatternDialog.class);
        templatePatternDialog = mock(TemplatePatternDialog.class);
        visitorPatternDialog = mock(VisitorPatternDialog.class);

        //Mock all the design pattern's generator class
        afPatternGenerator = mock(AFPatternGenerator.class);
        builderPatternGenerator = mock(BuilderPatternGenerator.class);
        facadePatternGenerator = mock(FacadePatternGenerator.class);
        factoryPatternGenerator = mock(FactoryPatternGenerator.class);
        corPatternGenerator = mock(CORPatternGenerator.class);
        mediatorPatternGenerator = mock(MediatorPatternGenerator.class);
        templatePatternGenerator = mock(TemplatePatternGenerator.class);
        visitorPatternGenerator = mock(VisitorPatternGenerator.class);

        //Mock the projectFileStorage class
        projectFileStorage = mock(ProjectFileStorage.class);

        //Create fileNameList object
        projectFileStorage.fileNameList = new ArrayList<String>();

        //Add item in the fileNameList in the projectFileStorage class
        projectFileStorage.fileNameList.add("TestClass");
        projectFileStorage.fileNameList.add("TestClass2");
        projectFileStorage.fileNameList.add("TestClass3");
    }

    @Test
    public void testAFPDFName(){
        //Create the new text field object
        afPatternDialog.userAFInterface = new TextField();
        //Set the text
        afPatternDialog.userAFInterface.setText("testAFIName");

        //Test if two variables are the same
        assertEquals(afPatternDialog.AFInterface, afPatternGenerator.userAFInterface);
    }

    @Test
    public void testBPDFName(){
        //Create the new text field object
        builderPatternDialog.userCOCObject = new TextField();
        //Set the text
        builderPatternDialog.userCOCObject.setText("testBCOCOName");

        //Test if two variables are the same
        assertEquals(builderPatternDialog.COCObject, builderPatternGenerator.userCOCObject);
    }

    @Test
    public void testCORPDFName(){
        //Create the new text field object
        corPatternDialog.userHBMethod = new TextField();
        //Set the text
        corPatternDialog.userHBMethod.setText("testCORHBMName");

        //Test if two variables are the same
        assertEquals(corPatternDialog.HBMethod, corPatternGenerator.userHBMethod);
    }

    @Test
    public void testFacadeDPName(){
        //Create the new text field object
        facadePatternDialog.userFChildClass = new TextField();
        //Set the text
        facadePatternDialog.userFChildClass.setText("testFacadeDPFCCName");

        //Test if two variables are the same
        assertEquals(facadePatternDialog.FChildClass, facadePatternGenerator.userFChildClass);
    }

    @Test
    public void testFactoryDFName(){
        //Create the new text field object
        factoryPatternDialog.userPInterface = new TextField();
        //Set the text
        factoryPatternDialog.userPInterface.setText("testPIName");

        //Test if two variables are the same
        assertEquals(factoryPatternDialog.PInterface, factoryPatternGenerator.userPInterface);
    }

    @Test
    public void tesetMDPName(){
        //Create the new text field object
        mediatorPatternDialog.userCObject = new TextField();
        //Set the text
        mediatorPatternDialog.userCObject.setText("testCOName");

        //Test if two variables are the same
        assertEquals(mediatorPatternDialog.CObject, mediatorPatternGenerator.userCObject);
    }

    @Test
    public void testTDPName(){
        //Create the new text field object
        templatePatternDialog.userTClass = new TextField();
        //Set the text
        templatePatternDialog.userTClass.setText("testTCName");

        //Test if two variables are the same
        assertEquals(templatePatternDialog.TClass, templatePatternGenerator.userTClass);
    }

    @Test
    public void testVDPName(){
        //Create the new text field object
        visitorPatternDialog.userVClass = new TextField();
        //Set the text
        visitorPatternDialog.userVClass.setText("testVCName");

        //Test if two variables are the same
        assertEquals(visitorPatternDialog.VClass, visitorPatternGenerator.userVClass);
    }

    @Test
    public void testFilePath2(){
        //Test if the dePaCoGeToolWindow and myToolWindowFactory are using the same file path
        assertEquals(dePaCoGeToolWindow.path, myToolWindowFactory.path);
    }

    @Test
    public void testFilePath(){
        //Test if the dePaCoGeToolWindow and all the design pattern's generator class are using the
        //same file path
        assertEquals(dePaCoGeToolWindow.path, afPatternGenerator.path);
        assertEquals(dePaCoGeToolWindow.path, builderPatternGenerator.path);
        assertEquals(dePaCoGeToolWindow.path, factoryPatternGenerator.path);
        assertEquals(dePaCoGeToolWindow.path, facadePatternGenerator.path);
        assertEquals(dePaCoGeToolWindow.path, mediatorPatternGenerator.path);
        assertEquals(dePaCoGeToolWindow.path, corPatternGenerator.path);
        assertEquals(dePaCoGeToolWindow.path, templatePatternGenerator.path);
        assertEquals(dePaCoGeToolWindow.path, visitorPatternGenerator.path);
    }

    @Test
    public void testAFDPErrorDialog(){
        //Assign the AFInterface variable in the AFPatternDialog class to match to one element in the fileNameList
        afPatternDialog.PIChildClass = "TestClass";

        assertFalse(afPatternDialog.nameClashChecker());
    }

    @Test
    public void testBDPErrorDialog(){
        //Assign the AFInterface variable in the AFPatternDialog class to match to one element in the fileNameList
        builderPatternDialog.COClass = "TestClass2";

        assertFalse(builderPatternDialog.nameClashChecker());
    }

    @Test
    public void testFacadeDPErrorDialog(){
        //Assign the AFInterface variable in the AFPatternDialog class to match to one element in the fileNameList
        facadePatternDialog.Class = "TestClass3";

        assertFalse(facadePatternDialog.nameClashChecker());
    }

    @Test
    public void testFactoryDPErrorDialog(){
        //Assign the AFInterface variable in the AFPatternDialog class to match to one element in the fileNameList
        factoryPatternDialog.CChildClass = "TestClass3";

        assertFalse(factoryPatternDialog.nameClashChecker());
    }

    @Test
    public void testCORDPErrorDialogC(){
        //Assign the AFInterface variable in the AFPatternDialog class to match to one element in the fileNameList
        corPatternDialog.HChildClass1 = "TestClass2";

        assertFalse(corPatternDialog.nameClashChecker());
    }

    @Test
    public void testMDPErrorDialog(){
        //Assign the AFInterface variable in the AFPatternDialog class to match to one element in the fileNameList
        mediatorPatternDialog.MClass = "TestClass";

        assertFalse(mediatorPatternDialog.nameClashChecker());
    }

    @Test
    public void testTDPErrorDialog(){
        //Assign the AFInterface variable in the AFPatternDialog class to match to one element in the fileNameList
        templatePatternDialog.TChildClass = "TestClass2";

        assertFalse(templatePatternDialog.nameClashChecker());
    }

    @Test
    public void testVDPErrorDialog(){
        //Assign the AFInterface variable in the AFPatternDialog class to match to one element in the fileNameList
        visitorPatternDialog.VChildClass = "TestClass3";

        assertFalse(visitorPatternDialog.nameClashChecker());
    }
}

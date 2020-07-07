import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class MediatorPatternDialog extends DialogWrapper implements Logs{
    //Variable for panel, label and textField
    private JPanel panel;
    private JLabel getMClass, getMMethod, getMChildClass, getMCMethod, getCClass, getCChildClass1,
                   getCChildClass2, getCCCSMethod, getCCCGMethod, getMObject, getCObject, getCCCString,
                   getCCC1Object, getCCC2Object, warningMessage;
    public TextField userMClass, userMMethod, userMChildClass, userMCMethod, userCClass, userCChildClass1,
                    userCChildClass2, userCCCSMethod, userCCCGMethod, userMObject, userCObject,userCCCString,
                    userCCC1Object, userCCC2Object;

    //Instance for afPatternGenerator
    private MediatorPatternGenerator mediatorPatternGenerator;

    //String variable to pass to afPatternGenerator class
    public String path, MClass, MMethod, MChildClass, MCMethod, CClass, CChildClass1,
                         CChildClass2, CCCSMethod, CCCGMethod, MObject, CObject, CCCString,
                         CCC1Object, CCC2Object;

    //Instance for ProjectFileStorage
    public ProjectFileStorage projectFileStorage;

    //Variable for error Message in the error dialog
    private String errorMessage;

    //Variable for iterator through the list
    public Iterator i;

    //Variable to store the current checking class name
    public String currentClassName;

    //Constructor
    protected MediatorPatternDialog(String path, Project project) {
        super(true);

        //Set the title for the pop up window
        setTitle("Mediator Design Pattern Dialog");

        //Create new panel
        panel = new JPanel(new GridBagLayout());

        //Create new ProjectFileStorage object
        projectFileStorage = new ProjectFileStorage(project);

        //Setting the file path
        this.path = path;

        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        initLabel();
        initTextField();

        //For the pop up window layout
        GridBag gb = new GridBag();
        gb.setDefaultInsets(new Insets(0,0, AbstractLayout.DEFAULT_VGAP,AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        //Add all the labels and test fields in the panel
        panel.add(warningMessage, gb.nextLine().nextLine().next().weightx(0.2));
        panel.add(getMClass, gb.nextLine().next().weightx(0.8));
        panel.add(userMClass, gb.nextLine().next().weightx(0.2));
        panel.add(getMMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userMMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getMChildClass, gb.nextLine().next().weightx(0.8));
        panel.add(userMChildClass, gb.nextLine().next().weightx(0.2));
        panel.add(getMCMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userMCMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getCClass, gb.nextLine().next().weightx(0.8));
        panel.add(userCClass, gb.nextLine().next().weightx(0.2));
        panel.add(getCChildClass1, gb.nextLine().next().weightx(0.8));
        panel.add(userCChildClass1, gb.nextLine().next().weightx(0.2));
        panel.add(getCChildClass2, gb.nextLine().next().weightx(0.8));
        panel.add(userCChildClass2, gb.nextLine().next().weightx(0.2));
        panel.add(getCCCGMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userCCCGMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getCCCSMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userCCCSMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getCCCString, gb.nextLine().nextLine().next().weightx(0.2));
        panel.add(userCCCString, gb.nextLine().next().weightx(0.8));
        panel.add(getMObject, gb.nextLine().next().weightx(0.2));
        panel.add(userMObject, gb.nextLine().next().weightx(0.8));
        panel.add(getCObject, gb.nextLine().next().weightx(0.2));
        panel.add(userCObject, gb.nextLine().next().weightx(0.8));
        panel.add(getCCC1Object, gb.nextLine().next().weightx(0.2));
        panel.add(userCCC1Object, gb.nextLine().next().weightx(0.8));
        panel.add(getCCC2Object, gb.nextLine().next().weightx(0.2));
        panel.add(userCCC2Object, gb.nextLine().next().weightx(0.8));

        return panel;
    }

    @Override
    protected void doOKAction() {
        logger().info("Generating Mediator design pattern");

        //Convert the text field input into string
        MClass = userMClass.getText();
        MMethod = userMMethod.getText();
        MChildClass = userMChildClass.getText();
        MCMethod = userMCMethod.getText();
        CClass = userCClass.getText();
        CChildClass1 = userCChildClass1.getText();
        CChildClass2 = userCChildClass2.getText();
        CCCGMethod = userCCCGMethod.getText();
        CCCSMethod = userCCCSMethod.getText();
        CCCString = userCCCString.getText();
        MObject = userMObject.getText();
        CObject = userCObject.getText();
        CCC1Object = userCCC1Object.getText();
        CCC2Object = userCCC2Object.getText();

        if(nameClashChecker()) {
            //Creating the afPatternGenerator for generating the design pattern
            mediatorPatternGenerator = new MediatorPatternGenerator(path, MClass, MMethod, MChildClass,
                    MCMethod, CClass, CChildClass1, CChildClass2, CCCSMethod, CCCGMethod,
                    MObject, CObject, CCCString, CCC1Object, CCC2Object);
            mediatorPatternGenerator.generatePattern();

            //Close the pop up window after user press the ok button
            close(0);
        }
        else {
            //Generate the error dialog
            JOptionPane.showMessageDialog(new JPanel(), errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Method for initializing the label
    private void initLabel(){
        getMClass = new JLabel("Please enter the name of mediator abstract class");
        getMMethod = new JLabel("This abstract method is inside the mediator abstract class" +
                                " which will mediate the colleague object. Feel free to change it if needed");
        getMChildClass = new JLabel("Please enter the concrete class name which will extends" +
                                    " the mediator abstract class");
        getMCMethod = new JLabel("This method is inside the concrete class" +
                                 " which will set the colleague objects. Feel free to change it if needed");
        getCClass = new JLabel("Please enter the name of colleague abstract class");
        getCChildClass1 = new JLabel("Please enter the first concrete class name which will extends the" +
                                     " colleague abstract class");
        getCChildClass2 = new JLabel("Please enter the second concrete class name which will extends the" +
                                     " colleague abstract class");
        getCCCGMethod = new JLabel("This method is inside the colleague child classes" +
                                   " which will get the state of your colleague classes. Feel free to change it if needed");
        getCCCSMethod = new JLabel("This method is inside the colleague child classes" +
                                   " which will set the state of your colleague classes. Feel free to change it if needed");
        getCCCString = new JLabel("This String name is inside the colleague child classes" +
                                  " which will store the state string of your colleague classes." +
                                  " Feel free to change it if needed");
        getMObject = new JLabel("This is the name of the mediator abstract class object." +
                                " Feel free to change it if needed");
        getCObject = new JLabel("This is the name of the colleague abstract class object." +
                                " Feel free to change it if needed");
        getCCC1Object = new JLabel("This is the name of the first colleague abstract class object." +
                                   " Feel free to change it if needed");
        getCCC2Object = new JLabel("This is the name of second colleague abstract class object." +
                                   " Feel free to change it if needed");

        warningMessage = new JLabel("Please enter all the text field, otherwise, it won't be able to generate code");
    }

    //Method for initializing the text field
    private void initTextField(){
        userMClass = new TextField();
        userMMethod = new TextField("mediate");
        userMChildClass = new TextField();
        userMCMethod = new TextField("setColleagues");
        userCClass = new TextField();
        userCChildClass1 = new TextField();
        userCChildClass2 = new TextField();
        userCCCGMethod = new TextField("getState");
        userCCCSMethod = new TextField("setState");
        userCCCString = new TextField("state");
        userMObject = new TextField("mediator");
        userCObject = new TextField("colleague");
        userCCC1Object = new TextField("colleague1");
        userCCC2Object = new TextField("colleague2");
    }

    //Method for checking name clash
    public boolean nameClashChecker() {
        logger().info("In the nameClashChecker");

        i = projectFileStorage.fileNameList.iterator();

        //Loop through the list to check the name clash
        while (i.hasNext()) {
            //Storing the iterator to current class name
            currentClassName = (String) i.next();

            logger().debug("Checking the class name :" + currentClassName);

            //Loop to check if there's name clashed with user's project
            if (currentClassName.equals(MClass) || currentClassName.equals(MChildClass) ||
                    currentClassName.equals(CClass) || currentClassName.equals(CChildClass1) ||
                    currentClassName.equals(CChildClass2)) {

                logger().debug("The name of the clash class : " + currentClassName);

                //Generate the error message
                errorMessage = "The class name " + currentClassName + " is already exist";

                return false;
            }
        }

        //Loop to check if user enter same class name twice
        if(MClass.equals(MChildClass) || MClass.equals(CClass) ||
                MClass.equals(CChildClass1) || MClass.equals(CChildClass2)){
            //Generate the error message
            errorMessage = "You enter " + MClass + " as class name twice";
            return false;
        }
        else if(MChildClass.equals(CClass) || MChildClass.equals(CChildClass1) ||
                MChildClass.equals(CChildClass2)){
            //Generate the error message
            errorMessage = "You enter " + MChildClass + " as class name twice";
            return false;
        }
        else if(CClass.equals(CChildClass1) || CClass.equals(CChildClass2)){
            //Generate the error message
            errorMessage = "You enter " + CClass + " as class name twice";
            return false;
        }
        else if(CChildClass1.equals(CChildClass2)){
            //Generate the error message
            errorMessage = "You enter " + CChildClass1 + " as class name twice";
            return false;
        }

        //Check if the there's two method with the same name in the same scope
        if(CCCGMethod.equals(CCCSMethod)){
            //Generate the error message
            errorMessage = "You enter " + CCCGMethod + " as method name twice in the same scope";
            return false;
        }

        //Check if there's two object with the same name in the same scope
        if(CCC1Object.equals(CCC2Object)){
            //Generate the error message
            errorMessage = "You enter " + CCC1Object + " as object name twice in the same scope";
            return false;
        }

        return true;
    }
}

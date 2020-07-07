import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class CORPatternDialog extends DialogWrapper implements Logs {
    //Variable for panel, label and textField
    private JPanel panel;
    private JLabel getHClass, getHObject, getHMethod, getHChildClass1, getHChildClass2,
                   getHBMethod, warningMessage;
    public TextField userHClass, userHObject, userHMethod, userHChildClass1, userHChildClass2,
                     userHBMethod;

    //Instance for afPatternGenerator
    private CORPatternGenerator corPatternGenerator;

    //String variable to pass to afPatternGenerator class
    public String path, HClass, HObject, HMethod, HChildClass1, HChildClass2, HBMethod;

    //Instance for ProjectFileStorage
    public ProjectFileStorage projectFileStorage;

    //Variable for error Message in the error dialog
    private String errorMessage;

    //Variable for iterator through the list
    public Iterator i;

    //Variable to store the current checking class name
    public String currentClassName;

    //Constructor
    protected CORPatternDialog(String path, Project project) {
        super(true);

        //Set the title for the pop up window
        setTitle("Chain of Responsibility Design Pattern Dialog");

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
        panel.add(getHClass, gb.nextLine().next().weightx(0.8));
        panel.add(userHClass, gb.nextLine().next().weightx(0.2));
        panel.add(getHObject, gb.nextLine().next().weightx(0.8));
        panel.add(userHObject, gb.nextLine().next().weightx(0.2));
        panel.add(getHMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userHMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getHBMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userHBMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getHChildClass1, gb.nextLine().next().weightx(0.8));
        panel.add(userHChildClass1, gb.nextLine().next().weightx(0.2));
        panel.add(getHChildClass2, gb.nextLine().next().weightx(0.8));
        panel.add(userHChildClass2, gb.nextLine().next().weightx(0.2));

        return panel;
    }

    @Override
    protected void doOKAction() {
        logger().info("Generating COR design pattern");

        //Convert the text field input into string
        HClass = userHClass.getText();
        HObject = userHObject.getText();
        HMethod = userHMethod.getText();
        HBMethod = userHBMethod.getText();
        HChildClass1 = userHChildClass1.getText();
        HChildClass2 = userHChildClass2.getText();

        if(nameClashChecker()) {
            //Creating the afPatternGenerator for generating the design pattern
            corPatternGenerator = new CORPatternGenerator(path, HClass, HObject, HMethod, HChildClass1, HChildClass2, HBMethod);
            corPatternGenerator.generatePattern();

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
        getHClass = new JLabel("Please enter the name of handler abstract class");
        getHObject = new JLabel("This is the name of handler abstract class object inside" +
                                " the handler abstract class. Feel free to change it if needed");
        getHMethod = new JLabel("This method is inside the handler abstract class" +
                                " which will handle the request. Feel free to change it if needed");
        getHBMethod = new JLabel("This method is inside the handle abstract class" +
                                 " which will determine if can handle the request or not. Feel free to change it if needed");
        getHChildClass1 = new JLabel("Please enter the first concrete class name which will extends the" +
                                     " handler abstract class");
        getHChildClass2 = new JLabel("Please enter the second concrete class name which will extends the" +
                                     " handler abstract class");

        warningMessage = new JLabel("Please enter all the text field, otherwise, it won't be able to generate code");
    }

    //Method for initializing the text field
    private void initTextField(){
        userHClass = new TextField();
        userHObject = new TextField("successor");
        userHMethod = new TextField("handleRequest");
        userHBMethod = new TextField("canHandleRequest");
        userHChildClass1 = new TextField();
        userHChildClass2 = new TextField();
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
            if (currentClassName.equals(HClass) || currentClassName.equals(HChildClass1) ||
                    currentClassName.equals(HChildClass2)) {
                logger().debug("The name of the clash class : " + currentClassName);

                //Generate the error message
                errorMessage = "The class name " + currentClassName + " is already exist";

                return false;
            }
        }

        //Loop to check if user enter same class name twice
        if(HClass.equals(HChildClass1) || HClass.equals(HChildClass2)){
            //Generate the error message
            errorMessage = "You enter " + HClass + " as class name twice";
            return false;
        }
        else if(HChildClass1.equals(HChildClass2)){
            //Generate the error message
            errorMessage = "You enter " + HChildClass1 + " as class name twice";
            return false;
        }

        //Check if the there's two method with the same name in the same scope
        if(HMethod.equals(HBMethod)){
            //Generate the error message
            errorMessage = "You enter " + HMethod + " as method name twice in the same scope";
            return false;
        }

        return true;
    }
}

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class VisitorPatternDialog extends DialogWrapper implements Logs{
    //Variable for panel, label and textField
    private JPanel panel;
    private JLabel getEClass, getEChildClass, getECMethod, getEMethod, getVClass, getVMethod,
                   getVChildClass, getVCObject, getECObject, warningMessage;
    public TextField userEClass, userEChildClass, userECMethod, userEMethod, userVClass, userVMethod,
            userVChildClass, userVCObject, userECObject;

    //Instance for afPatternGenerator
    private VisitorPatternGenerator visitorPatternGenerator;

    //String variable to pass to VisitorPatternGenerator class
    public String path, EClass, EChildClass, ECMethod, EMethod, VClass, VMethod, VChildClass,
                         VCObject, ECObject;

    //Instance for ProjectFileStorage
    public ProjectFileStorage projectFileStorage;

    //Variable for error Message in the error dialog
    private String errorMessage;

    //Variable for iterator through the list
    public Iterator i;

    //Variable to store the current checking class name
    public String currentClassName;

    //Constructor
    protected VisitorPatternDialog(String path, Project project) {
        super(true);

        //Set the title for the pop up window
        setTitle("Visitor Design Pattern Dialog");

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
        panel.add(getEClass, gb.nextLine().next().weightx(0.8));
        panel.add(userEClass, gb.nextLine().next().weightx(0.2));
        panel.add(getEMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userEMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getEChildClass, gb.nextLine().next().weightx(0.8));
        panel.add(userEChildClass, gb.nextLine().next().weightx(0.2));
        panel.add(getECMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userECMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getVClass, gb.nextLine().next().weightx(0.8));
        panel.add(userVClass, gb.nextLine().next().weightx(0.2));
        panel.add(getVMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userVMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getVChildClass, gb.nextLine().next().weightx(0.8));
        panel.add(userVChildClass, gb.nextLine().next().weightx(0.2));
        panel.add(getVCObject, gb.nextLine().next().weightx(0.8));
        panel.add(userVCObject, gb.nextLine().next().weightx(0.2));
        panel.add(getECObject, gb.nextLine().next().weightx(0.8));
        panel.add(userECObject, gb.nextLine().next().weightx(0.2));
        return panel;
    }

    @Override
    protected void doOKAction() {
        logger().info("Generating Visitor design pattern");

        //Convert the text field input into string
        EClass = userEClass.getText();
        EChildClass = userEChildClass.getText();
        ECMethod = userECMethod.getText();
        EMethod = userEMethod.getText();
        VMethod = userVMethod.getText();
        VChildClass = userVChildClass.getText();
        VCObject = userVCObject.getText();
        VClass = userVClass.getText();
        ECObject = userECObject.getText();

        if(nameClashChecker()) {
            //Creating the afPatternGenerator for generating the design pattern
            visitorPatternGenerator = new VisitorPatternGenerator(path, EClass, EChildClass, ECMethod,
                    EMethod, VClass, VMethod, VChildClass, VCObject, ECObject);
            visitorPatternGenerator.generatePattern();

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
        getEClass = new JLabel("Please enter the name of the element abstract class");
        getEMethod = new JLabel("This abstract method is inside the element abstract class" +
                                " which will be handling visiting the element. Feel free to change it if needed");
        getEChildClass = new JLabel("Please enter the concrete class name which will extends the" +
                                    " element abstract class");
        getECMethod = new JLabel("This method is inside the concrete class" +
                                 " which will be doing the operation. Feel free to change it if needed");
        getVClass = new JLabel("Please enter the name of the visitor abstract class");
        getVMethod = new JLabel("This abstract method is inside the visitor abstract class" +
                                " which the concrete child class will implement. Feel free to change it if needed");
        getVChildClass = new JLabel("Please enter the concrete class name which will extends the" +
                                    " visitor abstract class");
        getVCObject = new JLabel("This is the name of the visitor abstract class object." +
                                 " Feel free to change it if needed");
        getECObject = new JLabel("This is the name of the element child class object." +
                                 " Feel free to change it if needed");

        warningMessage = new JLabel("Please enter all the text field, otherwise, it won't be able to generate code");
    }

    //Method for initializing the text field
    private void initTextField(){
        userEClass = new TextField();
        userEMethod = new TextField("accept");
        userEChildClass = new TextField();
        userECMethod = new TextField("operation");
        userVClass = new TextField();
        userVMethod = new TextField("visitElement");
        userVChildClass = new TextField();
        userVCObject = new TextField("visitor");
        userECObject = new TextField("e");
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
            if (currentClassName.equals(EClass) || currentClassName.equals(EChildClass) ||
                    currentClassName.equals(VClass) || currentClassName.equals(VChildClass)) {
                logger().debug("The name of the clash class : " + currentClassName);

                //Generate the error message
                errorMessage = "The class name " + currentClassName + " is already exist";

                return false;
            }
        }

        //Loop to check if user enter same class name twice
        if(EClass.equals(EChildClass) || EClass.equals(VClass) || EClass.equals(VChildClass)){
            //Generate the error message
            errorMessage = "You enter " + EClass + " as class name twice";
            return false;
        }
        else if(EChildClass.equals(VClass) || EChildClass.equals(VChildClass)){
            //Generate the error message
            errorMessage = "You enter " + EChildClass + " as class name twice";
            return false;
        }
        else if(VClass.equals(VChildClass)){
            //Generate the error message
            errorMessage = "You enter " + VClass + " as class name twice";
            return false;
        }

        return true;
    }
}

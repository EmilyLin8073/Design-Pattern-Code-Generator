import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class FactoryPatternDialog extends DialogWrapper implements Logs {
    //Variable for panel, label and textField
    private JPanel panel;
    private JLabel getCClass, getCChildClass, getPInterface, getCMethod, getPChildClass,
                   getPObject, warningMessage, getGNMethod;
    public TextField userCClass, userCChildClass, userPInterface, userCMethod, userPChildClass,
                     userPObject, userGNMethod;

    //Instance for afPatternGenerator
    private FactoryPatternGenerator factoryPatternGenerator;

    //String variable to pass to afPatternGenerator class
    public String path, CClass, CChildClass, PInterface, CMethod, PChildClass, PObject, GNMethod;

    //Instance for ProjectFileStorage
    public ProjectFileStorage projectFileStorage;

    //Variable for error Message in the error dialog
    private String errorMessage;

    //Variable for iterator through the list
    public Iterator i;

    //Variable to store the current checking class name
    public String currentClassName;

    //Constructor
    protected FactoryPatternDialog(String path, Project project) {
        super(true);

        //Set the title for the pop up window
        setTitle("Factory Design Pattern Dialog");

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
        panel.add(warningMessage, gb.nextLine().nextLine().next().weightx(0.5));
        panel.add(getCClass, gb.nextLine().next().weightx(0.8));
        panel.add(userCClass, gb.nextLine().next().weightx(0.2));
        panel.add(getCChildClass, gb.nextLine().next().weightx(0.8));
        panel.add(userCChildClass, gb.nextLine().next().weightx(0.2));
        panel.add(getPInterface, gb.nextLine().next().weightx(0.8));
        panel.add(userPInterface, gb.nextLine().next().weightx(0.2));
        panel.add(getCMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userCMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getPObject, gb.nextLine().next().weightx(0.8));
        panel.add(userPObject, gb.nextLine().next().weightx(0.2));
        panel.add(getPChildClass, gb.nextLine().next().weightx(0.8));
        panel.add(userPChildClass, gb.nextLine().next().weightx(0.2));
        panel.add(getGNMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userGNMethod, gb.nextLine().next().weightx(0.2));

        return panel;
    }

    @Override
    protected void doOKAction() {
        logger().info("Generating Factory design pattern");

        //Convert the text field input into string
        CClass = userCClass.getText();
        CChildClass = userCChildClass.getText();
        PInterface = userPInterface.getText();
        CMethod = userCMethod.getText();
        PObject = userPObject.getText();
        PChildClass = userPChildClass.getText();
        GNMethod = userGNMethod.getText();

        if(nameClashChecker()) {
            //Creating the afPatternGenerator for generating the design pattern
            factoryPatternGenerator = new FactoryPatternGenerator(path, CClass, CChildClass, PInterface,
                    CMethod, PChildClass, PObject, GNMethod);
            factoryPatternGenerator.generatePattern();

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
        getCClass = new JLabel("Please enter the name of the creator abstract class");
        getCChildClass = new JLabel("Please enter the concrete class name which will extends the" +
                                    " creator abstract class");
        getPInterface = new JLabel("Please enter the name of the product interface");
        getCMethod = new JLabel("This method is inside the creator abstract class" +
                                " which has the product interface type. Feel free to change it if needed");
        getPObject = new JLabel("This is the name of product interface object inside the" +
                                " creator abstract class. Feel free to change it if needed");

        getPChildClass = new JLabel("Please enter the concrete class name which will implements the" +
                                    " product interface");
        getGNMethod = new JLabel("This method is inside the concrete class. This method" +
                                 " will return the name of concrete class. Feel free to change it if needed");

        warningMessage = new JLabel("Please enter all the text field, otherwise, it won't be able to generate code");
    }

    //Method for initializing the text field
    private void initTextField(){
        userCClass = new TextField();
        userCChildClass = new TextField();
        userPInterface = new TextField();
        userCMethod = new TextField("factoryMethod");
        userPObject = new TextField("product");
        userPChildClass = new TextField();
        userGNMethod = new TextField("getName");
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
            if (currentClassName.equals(CClass) || currentClassName.equals(CChildClass) ||
                    currentClassName.equals(PInterface) || currentClassName.equals(PChildClass)) {
                logger().debug("The name of the clash class : " + currentClassName);

                //Generate the error message
                errorMessage = "The class name " + currentClassName + " is already exist";

                return false;
            }
        }

        //Loop to check if user enter same class name twice
        if(CClass.equals(CChildClass) || CClass.equals(PInterface) || CClass.equals(PChildClass)){
            //Generate the error message
            errorMessage = "You enter " + CClass + " as class name twice";
            return false;
        }
        else if(CChildClass.equals(PInterface) || CChildClass.equals(PChildClass)){
            //Generate the error message
            errorMessage = "You enter " + CChildClass + " as class name twice";
            return false;
        }
        else if(PInterface.equals(PChildClass)){
            //Generate the error message
            errorMessage = "You enter " + PInterface + " as class name twice";
            return false;
        }

        return true;
    }
}

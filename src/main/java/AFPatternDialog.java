import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class AFPatternDialog extends DialogWrapper implements Logs {
    //Variable for panel, label and textField
    private JPanel panel;
    private JLabel getAFInterface, getMethod, getMS, getAFIChildClass, getPIChildClass,
                   getGNMethod, warningMessage;
    public TextField userAFInterface, userM, userMS, userAFIChildClass, userPIChildClass, userGNMethod;

    //Instance for afPatternGenerator
    private AFPatternGenerator afPatternGenerator;

    //String variable to pass to afPatternGenerator class
    public String path, AFInterface, method, methodSignature, AFIChildClass, PIChildClass, GNMethod;

    //Instance for ProjectFileStorage
    public ProjectFileStorage projectFileStorage;

    //Variable for error Message in the error dialog
    private String errorMessage;

    //Variable for iterator through the list
    public Iterator i;

    //Variable to store the current checking class name
    public String currentClassName;


    //Constructor
    protected AFPatternDialog(String path, Project project) {
        super(true);

        //Set the title for the pop up window
        setTitle("Abstract Factory Design Pattern Dialog");

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
        panel.add(getAFInterface, gb.nextLine().next().weightx(0.8));
        panel.add(userAFInterface, gb.nextLine().next().weightx(0.2));
        panel.add(getMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userM, gb.nextLine().next().weightx(0.2));
        panel.add(getMS, gb.nextLine().next().weightx(0.8));
        panel.add(userMS, gb.nextLine().next().weightx(0.2));
        panel.add(getAFIChildClass, gb.nextLine().next().weightx(0.8));
        panel.add(userAFIChildClass, gb.nextLine().next().weightx(0.2));
        panel.add(getPIChildClass, gb.nextLine().next().weightx(0.8));
        panel.add(userPIChildClass, gb.nextLine().next().weightx(0.2));
        panel.add(getGNMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userGNMethod, gb.nextLine().next().weightx(0.2));

        return panel;
    }

    @Override
    protected void doOKAction() {
        logger().info("Generating AF design pattern");

        //Convert the text field input into string
        AFInterface = userAFInterface.getText();
        method = userM.getText();
        methodSignature = userMS.getText();
        AFIChildClass = userAFIChildClass.getText();
        PIChildClass = userPIChildClass.getText();
        GNMethod = userGNMethod.getText();

        if(nameClashChecker()) {
            //Creating the afPatternGenerator for generating the design pattern
            afPatternGenerator = new AFPatternGenerator(path, AFInterface, method, methodSignature,
                                                        AFIChildClass, PIChildClass, GNMethod);
            afPatternGenerator.generatePattern();

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
        getAFInterface = new JLabel("Please enter the name of abstract factory interface");
        getMethod = new JLabel("This method is in the abstract factory interface" +
                                " which will create the product. Feel free to change the name if needed");
        getMS = new JLabel("Please enter the method signature for the method that create the product." +
                           " This method signature is the name of the second interface");
        getAFIChildClass = new JLabel("Please enter the concrete class name which will implements the" +
                                      " abstract factory interface");
        getPIChildClass = new JLabel("Please enter the concrete class name which will implements the second interface");

        getGNMethod = new JLabel("This method is inside the concrete class. This method" +
                                " will return the name of concrete class. Feel free to change the name if needed");

        warningMessage = new JLabel("Please enter all the text field, otherwise, it won't be able to generate code");
    }

    //Method for initializing the text field
    private void initTextField(){
        userAFInterface = new TextField();
        userM = new TextField("createProduct");
        userMS = new TextField();
        userAFIChildClass = new TextField();
        userPIChildClass = new TextField();
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
            if (currentClassName.equals(AFInterface) || currentClassName.equals(AFIChildClass) ||
                    currentClassName.equals(PIChildClass)) {

                logger().debug("The name of the clash class : " + currentClassName);

                //Generate the error message
                errorMessage = "The class name " + currentClassName + " is already exist";

                return false;
            }
        }

        //Loop to check if user enter same class name twice
        if(AFInterface.equals(AFIChildClass) || AFInterface.equals(PIChildClass)){
            //Generate the error message
            errorMessage = "You enter " + AFInterface + " as class name twice";
            return false;
        }
        else if(AFIChildClass.equals(PIChildClass)){
            //Generate the error message
            errorMessage = "You enter " + AFIChildClass + " as class name twice";
            return false;
        }

        return true;
    }
}

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class FacadePatternDialog extends DialogWrapper implements Logs {
    //Variable for panel, label and textField
    private JPanel panel;
    private JLabel getFClass, getFMethod, getFChildClass, getClass, getCMethod, getCObject, warningMessage;
    public TextField userFClass, userFMethod, userFChildClass, userClass, userCMethod, userCObject;

    //Instance for afPatternGenerator
    private FacadePatternGenerator facadePatternGenerator;

    //String variable to pass to afPatternGenerator class
    public String path, FClass, FMethod, FChildClass, Class, CMethod, CObject;

    //Instance for ProjectFileStorage
    public ProjectFileStorage projectFileStorage;

    //Variable for error Message in the error dialog
    private String errorMessage;

    //Variable for iterator through the list
    public Iterator i;

    //Variable to store the current checking class name
    public String currentClassName;

    //Constructor
    protected FacadePatternDialog(String path, Project project) {
        super(true);

        //Set the title for the pop up window
        setTitle("Facade Design Pattern Dialog");

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
        panel.add(getFClass, gb.nextLine().next().weightx(0.8));
        panel.add(userFClass, gb.nextLine().next().weightx(0.2));
        panel.add(getFMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userFMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getFChildClass, gb.nextLine().next().weightx(0.8));
        panel.add(userFChildClass, gb.nextLine().next().weightx(0.2));
        panel.add(getClass, gb.nextLine().next().weightx(0.8));
        panel.add(userClass, gb.nextLine().next().weightx(0.2));
        panel.add(getCMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userCMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getCObject, gb.nextLine().next().weightx(0.8));
        panel.add(userCObject, gb.nextLine().next().weightx(0.2));

        return panel;
    }

    @Override
    protected void doOKAction() {
        logger().info("Generating Facade design pattern");

        //Convert the text field input into string
        FClass = userFClass.getText();
        FMethod = userFMethod.getText();
        FChildClass = userFChildClass.getText();
        Class = userClass.getText();
        CMethod = userCMethod.getText();
        CObject = userCObject.getText();

        if(nameClashChecker()) {
            //Creating the afPatternGenerator for generating the design pattern
            facadePatternGenerator = new FacadePatternGenerator(path, FClass, FMethod, FChildClass, Class, CMethod, CObject);
            facadePatternGenerator.generatePattern();

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
        getFClass = new JLabel("Please enter the name of the facade abstract class");
        getFMethod = new JLabel("This method is inside the facade abstract class" +
                                " which will get the concrete class name. Feel free to change it if needed");
        getFChildClass = new JLabel("Please enter the concrete class name which will extends the" +
                                    " facade abstract class");
        getClass = new JLabel("Please enter the class name");
        getCMethod = new JLabel("This method is inside the class which will return" +
                                " the class name. Feel free to change it if needed");

        getCObject = new JLabel("This is the name of class object inside the concrete class" +
                                " which extends the facade abstract class. Feel free to change it if needed");

        warningMessage = new JLabel("Please enter all the text field, otherwise, it won't be able to generate code");
    }

    //Method for initializing the text field
    private void initTextField(){
        userFClass = new TextField();
        userFMethod = new TextField("operation");
        userFChildClass = new TextField();
        userClass = new TextField();
        userCMethod = new TextField("operation1");
        userCObject = new TextField("object1");
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
            if (currentClassName.equals(FClass) || currentClassName.equals(FChildClass) ||
                    currentClassName.equals(Class)) {
                logger().debug("The name of the clash class : " + currentClassName);

                //Generate the error message
                errorMessage = "The class name " + currentClassName + " is already exist";

                return false;
            }
        }

        //Loop to check if user enter same class name twice
        if(FClass.equals(FChildClass) || FClass.equals(Class)){
            //Generate the error message
            errorMessage = "You enter " + FClass + " as class name twice";
            return false;
        }
        else if(FChildClass.equals(Class)){
            //Generate the error message
            errorMessage = "You enter " + FChildClass + " as class name twice";
            return false;
        }

        return true;
    }
}

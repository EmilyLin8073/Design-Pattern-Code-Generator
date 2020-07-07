import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class TemplatePatternDialog extends DialogWrapper implements Logs{
    //Variable for panel, label and textField
    private JPanel panel;
    private JLabel getTClass, getTChildClass, getTCMethod, getTMethod, warningMessage;
    public TextField userTClass, userTChildClass, userTCMethod, userTMethod;

    //Instance for afPatternGenerator
    private TemplatePatternGenerator templatePatternGenerator;

    //String variable to pass to afPatternGenerator class
    public String path, TClass, TChildClass, TCMethod, TMethod;

    //Instance for ProjectFileStorage
    public ProjectFileStorage projectFileStorage;

    //Variable for error Message in the error dialog
    private String errorMessage;

    //Variable for iterator through the list
    public Iterator i;

    //Variable to store the current checking class name
    public String currentClassName;

    //Constructor
    protected TemplatePatternDialog(String path, Project project) {
        super(true);

        //Set the title for the pop up window
        setTitle("Template Design Pattern Dialog");

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
        panel.add(getTClass, gb.nextLine().next().weightx(0.8));
        panel.add(userTClass, gb.nextLine().next().weightx(0.2));
        panel.add(getTMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userTMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getTChildClass, gb.nextLine().next().weightx(0.8));
        panel.add(userTChildClass, gb.nextLine().next().weightx(0.2));
        panel.add(getTCMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userTCMethod, gb.nextLine().next().weightx(0.2));

        return panel;
    }

    @Override
    protected void doOKAction() {
        logger().info("Generating Template design pattern");

        //Convert the text field input into string
        TClass = userTClass.getText();
        TMethod = userTMethod.getText();
        TChildClass = userTChildClass.getText();
        TCMethod = userTCMethod.getText();

        if(nameClashChecker()) {
            //Creating the afPatternGenerator for generating the design pattern
            templatePatternGenerator = new TemplatePatternGenerator(path, TClass, TChildClass, TCMethod, TMethod);
            templatePatternGenerator.generatePattern();

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
        getTClass = new JLabel("Please enter the name of the template abstract class");
        getTMethod = new JLabel("This method is inside the template abstract class" +
                                " which will contain the method that implemented by the concrete class." +
                                " Feel free to change it if needed");
        getTChildClass = new JLabel("Please enter the concrete class name which will extends the" +
                                    " template abstract class");
        getTCMethod = new JLabel("This is the method which the concrete class will implement." +
                                 " Feel free to change it if needed");

        warningMessage = new JLabel("Please enter all the text field, otherwise, it won't be able to generate code");
    }

    //Method for initializing the text field
    private void initTextField(){
        userTClass = new TextField();
        userTMethod = new TextField("templateMethod");
        userTChildClass = new TextField();
        userTCMethod = new TextField("primitive");
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
            if (currentClassName.equals(TClass) || currentClassName.equals(TChildClass)) {
                logger().debug("The name of the clash class : " + currentClassName);

                //Generate the error message
                errorMessage = "The class name " + currentClassName + " is already exist";

                return false;
            }
        }

        //Loop to check if user enter same class name twice
        if(TClass.equals(TChildClass)){
            //Generate the error message
            errorMessage = "You enter " + TClass + " as class name twice";
            return false;
        }

        return true;
    }
}

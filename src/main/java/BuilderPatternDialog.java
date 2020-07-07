import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class BuilderPatternDialog extends DialogWrapper implements Logs {
    //Variable for panel, label and textField
    private JPanel panel;
    private JLabel getBInterface, getBMethod, getBCOMethod, getBChildClass, getCOClass,
                   getPInterface, getPPInterface, getPPChildClass, getCOCObject,
                   getGNMethod, warningMessage;
    public TextField userBInterface, userBMethod, userBCOMethod, userBChildClass, userCOClass,
                     userPInterface, userPPInterface, userPPChildClass, userCOCObject, userGNMethod;

    //Instance for afPatternGenerator
    private BuilderPatternGenerator builderPatternGenerator;

    //String variable to pass to afPatternGenerator class
    public String path, BInterface, BMethod, BCOMethod, BChildClass, COClass,
                         PInterface, PPInterface, PPChildClass, COCObject, GNMethod;

    //Instance for ProjectFileStorage
    public ProjectFileStorage projectFileStorage;

    //Variable for error Message in the error dialog
    private String errorMessage;

    //Variable for iterator through the list
    public Iterator i;

    //Variable to store the current checking class name
    public String currentClassName;


    //Constructor
    protected BuilderPatternDialog(String path, Project project) {
        super(true);

        //Set the title for the pop up window
        setTitle("Builder Design Pattern Dialog");

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
        panel.add(getBInterface, gb.nextLine().next().weightx(0.8));
        panel.add(userBInterface, gb.nextLine().next().weightx(0.2));
        panel.add(getBMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userBMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getBChildClass, gb.nextLine().next().weightx(0.8));
        panel.add(userBChildClass, gb.nextLine().next().weightx(0.2));
        panel.add(getCOClass, gb.nextLine().next().weightx(0.8));
        panel.add(userCOClass, gb.nextLine().next().weightx(0.2));
        panel.add(getCOCObject, gb.nextLine().next().weightx(0.8));
        panel.add(userCOCObject, gb.nextLine().next().weightx(0.2));
        panel.add(getBCOMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userBCOMethod, gb.nextLine().next().weightx(0.2));
        panel.add(getPInterface, gb.nextLine().next().weightx(0.8));
        panel.add(userPInterface, gb.nextLine().next().weightx(0.2));
        panel.add(getPPInterface, gb.nextLine().next().weightx(0.8));
        panel.add(userPPInterface, gb.nextLine().next().weightx(0.2));
        panel.add(getPPChildClass, gb.nextLine().next().weightx(0.8));
        panel.add(userPPChildClass, gb.nextLine().next().weightx(0.2));
        panel.add(getGNMethod, gb.nextLine().next().weightx(0.8));
        panel.add(userGNMethod, gb.nextLine().next().weightx(0.2));

        return panel;
    }

    @Override
    protected void doOKAction() {
        logger().info("Generating Builder design pattern");

        //Convert the text field input into string
        BInterface = userBInterface.getText();
        BMethod = userBMethod.getText();
        BChildClass = userBChildClass.getText();
        COClass = userCOClass.getText();
        COCObject = userCOCObject.getText();
        BCOMethod = userBCOMethod.getText();
        PInterface = userPInterface.getText();
        PPInterface = userPPInterface.getText();
        PPChildClass = userPPChildClass.getText();
        GNMethod = userGNMethod.getText();

        if(nameClashChecker()) {
            //Creating the afPatternGenerator for generating the design pattern
            builderPatternGenerator = new BuilderPatternGenerator(path, BInterface, BMethod, BChildClass,
                    COClass, COCObject, BCOMethod, PInterface, PPInterface, PPChildClass, GNMethod);

            builderPatternGenerator.generatePattern();

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
        getBInterface = new JLabel("Please enter the name of the builder interface");
        getBMethod = new JLabel("This method is inside the builder interface" +
                                " which will build the product. Feel free to change it if needed");
        getBChildClass = new JLabel("Please enter the concrete class name which will implements" +
                                    " the builder interface");
        getCOClass = new JLabel("Please enter the name of the complex object class");
        getCOCObject = new JLabel("This is the name of complex object class object" +
                                  " which will be used in this design pattern. Feel free to change it if needed");
        getBCOMethod = new JLabel("This method is inside the builder interface" +
                                  " which has the type of complex object class. " +
                                  "This method will get the product name as result. Feel free to change it if needed");
        getPInterface = new JLabel("Please enter the name of the product interface");
        getPPInterface = new JLabel("Please enter the interface which will extends the product interface");
        getPPChildClass = new JLabel("Please enter the concrete class name which will implement the interface" +
                                     " which extends the product interface");
        getGNMethod = new JLabel("This method is inside the concrete class. This method" +
                                 " will return the name of concrete class. Feel free to change it if needed");

        warningMessage = new JLabel("Please enter all the text field, otherwise, it won't be able to generate code");
    }

    //Method for initializing the text field
    private void initTextField(){
        userBInterface = new TextField();
        userBMethod = new TextField("buildPart");
        userBChildClass = new TextField();
        userCOClass = new TextField();
        userCOCObject = new TextField("co");
        userBCOMethod = new TextField("getResult");
        userPInterface = new TextField();
        userPPInterface = new TextField();
        userPPChildClass = new TextField();
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
            if (currentClassName.equals(BInterface) || currentClassName.equals(BChildClass) ||
                    currentClassName.equals(COClass) || currentClassName.equals(PInterface) ||
                    currentClassName.equals(PPInterface) || currentClassName.equals(PPChildClass)) {

                logger().debug("The name of the clash class : " + currentClassName);

                //Generate the error message
                errorMessage = "The class name " + currentClassName + " is already exist";

                return false;
            }
        }

        //Loop to check if user enter same class name twice
        if(BInterface.equals(BChildClass) || BInterface.equals(COClass) ||
                BInterface.equals(PInterface) || BInterface.equals(PPInterface) ||
                BInterface.equals(PPChildClass)){
            //Generate the error message
            errorMessage = "You enter " + BInterface + " as class name twice";
            return false;
        }
        else if(BChildClass.equals(COClass) || BChildClass.equals(PInterface) ||
                BChildClass.equals(PPInterface) || BChildClass.equals(PPChildClass)){
            //Generate the error message
            errorMessage = "You enter " + BChildClass + " as class name twice";
            return false;
        }
        else if(COClass.equals(PInterface) || COClass.equals(PPInterface) || COClass.equals(PPChildClass)){
            //Generate the error message
            errorMessage = "You enter " + COClass + " as class name twice";
            return false;
        }
        else if(PInterface.equals(PPInterface) || PInterface.equals(PPChildClass)){
            //Generate the error message
            errorMessage = "You enter " + PInterface + " as class name twice";
            return false;
        }
        else if(PPInterface.equals(PPChildClass)){
            //Generate the error message
            errorMessage = "You enter " + PPInterface + " as class name twice";
            return false;
        }

        return true;
    }
}

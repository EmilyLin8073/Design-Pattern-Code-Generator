import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.components.JBList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DePaCoGeToolWindow extends SimpleToolWindowPanel implements Logs{
    //Variable for panel, frame, label, list
    private JPanel panel;
    private JLabel label;
    public JBList DPList;
    public String userSelectedValue;
    public String path;

    //Instance for dialog for each design patterns
    private AFPatternDialog afPatternDialog;
    private BuilderPatternDialog builderPatternDialog;
    private CORPatternDialog corPatternDialog;
    private FacadePatternDialog facadePatternDialog;
    private FactoryPatternDialog factoryPatternDialog;
    private TemplatePatternDialog templatePatternDialog;
    private MediatorPatternDialog mediatorPatternDialog;
    private VisitorPatternDialog visitorPatternDialog;

    //User project object
    public Project project;

    //Constructor
    public DePaCoGeToolWindow(String path, Project project){
        super(true, true);
        this.path = path;
        this.project = project;
        init();
    }

    public void init() {
        //Set up object for panel, frame, label and list
        panel = new JPanel();
        label = new JLabel("Please select the design pattern you want :");
        DPList = new JBList<String>("Abstract Factory", "Builder", "Factory", "Facade",
                                    "Chain of Responsibility", "Mediator", "Visitor", "Template");

        //List listener
        DPList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //Store selected item into string
                userSelectedValue = (String) DPList.getSelectedValue();
                generateDP();
            }
        });

        panel.add(label);   //Add label to the panel
        panel.add(DPList);  //Add the list to the panel

        setContent(panel);  //Add the panel to the content
    }

    private void generateDP(){
        //Loop for calling the design patterns' dialog
        if(userSelectedValue.equals("Abstract Factory")){
            logger().debug("Clicked on AF design pattern");

            afPatternDialog = new AFPatternDialog(path, project);
            afPatternDialog.showAndGet();
        }
        else if(userSelectedValue.equals("Builder")){
            logger().debug("Clicked on Builder design pattern");

            builderPatternDialog = new BuilderPatternDialog(path, project);
            builderPatternDialog.showAndGet();
        }
        else if(userSelectedValue.equals("Chain of Responsibility")){
            logger().debug("Clicked on COR design pattern");

            corPatternDialog = new CORPatternDialog(path, project);
            corPatternDialog.showAndGet();
        }
        else if(userSelectedValue.equals("Facade")){
            logger().debug("Clicked on Facade design pattern");

            facadePatternDialog = new FacadePatternDialog(path, project);
            facadePatternDialog.showAndGet();
        }
        else if(userSelectedValue.equals("Factory")){
            logger().debug("Clicked on Factory design pattern");

            factoryPatternDialog = new FactoryPatternDialog(path, project);
            factoryPatternDialog.showAndGet();
        }
        else if(userSelectedValue.equals("Mediator")){
            logger().debug("Clicked on Mediator design pattern");

            mediatorPatternDialog = new MediatorPatternDialog(path, project);
            mediatorPatternDialog.showAndGet();
        }
        else if(userSelectedValue.equals("Template")){
            logger().debug("Clicked on Template design pattern");

            templatePatternDialog = new TemplatePatternDialog(path, project);
            templatePatternDialog.showAndGet();
        }
        else{
            logger().debug("Clicked on Visitor design pattern");

            visitorPatternDialog = new VisitorPatternDialog(path, project);
            visitorPatternDialog.showAndGet();
        }
    }
}

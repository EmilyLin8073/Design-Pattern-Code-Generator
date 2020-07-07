import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class MyToolWindowFactory implements ToolWindowFactory {
    public String path;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull com.intellij.openapi.wm.ToolWindow toolWindow) {
        //Get the base path for the user's project
        path = project.getBasePath();

        //Put files under src folder
        path += "/src";

        DePaCoGeToolWindow window = new DePaCoGeToolWindow(path, project);
        toolWindow.getContentManager().addContent(ContentFactory.SERVICE.getInstance().createContent(window, "", false));
    }
}

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ProjectFileStorage implements Logs{
    //Variable for PsiFile, Project, VirtualFile
    public PsiFile psiFile;
    public Project project;
    public VirtualFile virtualFile;

    //Variable for storing the class name in the user project
    public List<String> fileNameList = new ArrayList<String>();
//    public Hashtable<String, String> fileHashTable = new Hashtable<>();

    //Variable for storing file name and package name
    public String fileName;
    public String filePackage;

    //Variable for iterator through the list
    public Iterator iterator;


    protected  ProjectFileStorage(Project project){
        this.project = project;
        init();
    }

    public void init(){
        //Iterator through the project to get the psiFle
        ProjectFileIndex.SERVICE.getInstance(project).iterateContent(new ContentIterator() {
            @Override
            public boolean processFile(@NotNull VirtualFile fileInProject) {
                //fileInProject = LocalFileSystem.getInstance().findFileByPath(filePath);

                //Get the PsiFile
                psiFile = PsiManager.getInstance(project).findFile(fileInProject);

                //If psiFile is found, and if the file is .java, put the file in the list
                if(psiFile != null){
                    //Check if the file type is java
                    if(psiFile instanceof PsiJavaFile) {
                        //Get the virtual file from psiFile
                        virtualFile = psiFile.getVirtualFile();

                        //Get the file name
                        fileName = virtualFile.getNameWithoutExtension();

                        //Get the file package
                        filePackage = ((PsiJavaFile) psiFile).getPackageName();

                        //Put the file in the hashtable
                        //fileHashTable.put(fileName, filePackage);

                        //Put the name of the file in the ArrayList
                        fileNameList.add(virtualFile.getNameWithoutExtension());
                    }
                }
                return true;
            }});

        //Log the size of the hashtable
        logger().info("Size of the file name ArrayList : " + fileNameList.size());

        iterator = fileNameList.iterator();
        //Loop through the list
        while(iterator.hasNext()){
            logger().debug("The value in the list : " + iterator.next());
        }
        /*
        //Loop through hashtable to check the value
        for(Map.Entry i : fileHashTable.entrySet()){
            logger().info(i.getKey() + " " + i.getValue());
        }
         */
    }
}

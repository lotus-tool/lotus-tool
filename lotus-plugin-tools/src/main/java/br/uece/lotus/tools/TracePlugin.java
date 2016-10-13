package br.uece.lotus.tools;

/**
 * Created by lucas on 02/03/15.
 */
import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.project.ProjectDialogsHelper;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.project.ProjectSerializer;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;



public class TracePlugin  extends Plugin {

    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;
    private ProjectDialogsHelper mProjectDialogsHelper;
    private Project p ;
    private Component c;
    private ProjectSerializer mTraceSerializer = new TraceSerializer();

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = (UserInterface) extensionManager.get(UserInterface.class);
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);
        mProjectDialogsHelper = extensionManager.get(ProjectDialogsHelper.class);
        mUserInterface.getMainMenu().
        newItem("Model/Model from Trace")
                    .setWeight(Integer.MIN_VALUE)
                    .setAction(mImportTrace)
                    .create();

    }

        final Runnable mImportTrace = new Runnable() {
            @Override
            public void run() {
                Project p = mProjectDialogsHelper.open(mTraceSerializer, "Import Trace", "Trace Files (*.csv)", "*.csv");
                String nameFile = mProjectDialogsHelper.getTraceFile().getName();
                String nameView = nameFile.substring(0, nameFile.length()-4);  // remove a extensao do nome do projeto no Substring
                if (p != null) {
                    p.setName("Model "+ nameView);
                    p.getComponent(0).setName(nameView);
                    mProjectExplorer.open(p);

                }
            }
        };



}

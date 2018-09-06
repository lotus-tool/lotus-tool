//package br.uece.lotus.uml.app.runtime;
//
//import br.uece.lotus.uml.api.ds.StandardModeling;
//import br.uece.lotus.uml.app.project.ProjectExplorerPluginDS;
//import br.uece.seed.ext.ExtensionManager;
//import br.uece.seed.ext.Plugin;
//
//public class RuntimePlugin extends Plugin {
//
//    private ProjectExplorerPluginDS projectExplorerDS;
//    private StandardModeling standardModeling;
//
//    @Override
//    public void onStart(ExtensionManager extensionManager) throws Exception {
//        projectExplorerDS = extensionManager.get(ProjectExplorerPluginDS.class);
//         standardModeling = projectExplorerDS.getSelectedComponentBuildDS();
//
//    }
//
//    @Override
//    public void onStop(ExtensionManager extensionManager) throws Exception {
//        super.onStop(extensionManager);
//    }
//
//    public StandardModeling getParallelComponent() {
//        return standardModeling;
//    }
//}
